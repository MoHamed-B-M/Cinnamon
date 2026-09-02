@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.cinnamon.features.phone.presentation.call.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sosauce.cinnamon.R
import com.sosauce.cinnamon.core.ui.CinnamonTheme
import com.sosauce.nekobites.animations.bouncySpec
import com.sosauce.cinnamon.features.phone.presentation.call.CallAction
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun IncomingBottomBar(
    onCallAction: (CallAction) -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val animatable = remember { Animatable(0f) }
    // Wider drag for expressive motion — 8dp system aligned 140dp
    val maxDrag = with(density) { 140.dp.toPx() }
    val dragState = rememberDraggableState { dragAmount ->
        scope.launch {
            val newValue = (animatable.value + dragAmount).coerceIn(-maxDrag, maxDrag)
            animatable.snapTo(newValue)
        }
    }

    // Normalized -1..1
    val dragProgress by remember {
        derivedStateOf { (animatable.value / maxDrag).coerceIn(-1f, 1f) }
    }
    val declineProgress = (-dragProgress).coerceIn(0f, 1f)
    val answerProgress = dragProgress.coerceIn(0f, 1f)

    // Expressive thumb color — tonal morph: neutral -> error (decline) / primary (answer)
    val neutralThumb = MaterialTheme.colorScheme.surfaceContainerHighest
    val errorColor = MaterialTheme.colorScheme.error
    val answerColor = MaterialTheme.colorScheme.primary
    val thumbColor by animateColorAsState(
        targetValue = when {
            dragProgress < -0.08f -> lerp(neutralThumb, errorColor, (declineProgress * 1.2f).coerceIn(0f, 1f))
            dragProgress > 0.08f -> lerp(neutralThumb, answerColor, (answerProgress * 1.2f).coerceIn(0f, 1f))
            else -> neutralThumb
        },
        animationSpec = bouncySpec(),
        label = "thumbColor"
    )

    // Background pill tonal shift — subtle expressive feedback
    val bgColor by animateColorAsState(
        targetValue = when {
            dragProgress < -0.15f -> lerp(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f), declineProgress * 0.6f)
            dragProgress > 0.15f -> lerp(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f), answerProgress * 0.6f)
            else -> MaterialTheme.colorScheme.surfaceContainer
        },
        animationSpec = bouncySpec(),
        label = "bgColor"
    )

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hint label — expressive typography
        Text(
            text = if (dragProgress < -0.2f) "Release to decline" else if (dragProgress > 0.2f) "Release to answer" else "Swipe to answer or decline",
            style = MaterialTheme.typography.labelLargeEmphasized.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // Expressive swipe container — pill with tonal elevation and motion
        Surface(
            shape = RoundedCornerShape(50), // Full pill expressive token
            color = bgColor,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .clip(RoundedCornerShape(50))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                // Side labels with expressive fading based on drag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decline side — error tonal
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.scale(1f + declineProgress * 0.06f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = lerp(
                                MaterialTheme.colorScheme.surfaceContainerHighest,
                                MaterialTheme.colorScheme.errorContainer,
                                declineProgress
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(R.drawable.phone_filled),
                                    contentDescription = null,
                                    tint = lerp(
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                        MaterialTheme.colorScheme.onErrorContainer,
                                        declineProgress
                                    ),
                                    modifier = Modifier
                                        .size(20.dp)
                                        .graphicsLayer { rotationZ = 135f }
                                )
                            }
                        }
                        Text(
                            text = "Decline",
                            style = MaterialTheme.typography.titleSmallEmphasized.copy(
                                fontWeight = FontWeight.Bold,
                                color = lerp(
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                    MaterialTheme.colorScheme.onErrorContainer,
                                    declineProgress * 0.7f
                                )
                            )
                        )
                    }

                    // Answer side — primary tonal
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.scale(1f + answerProgress * 0.06f)
                    ) {
                        Text(
                            text = "Answer",
                            style = MaterialTheme.typography.titleSmallEmphasized.copy(
                                fontWeight = FontWeight.Bold,
                                color = lerp(
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                    answerProgress * 0.7f
                                )
                            )
                        )
                        Surface(
                            shape = CircleShape,
                            color = lerp(
                                MaterialTheme.colorScheme.surfaceContainerHighest,
                                MaterialTheme.colorScheme.primaryContainer,
                                answerProgress
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(R.drawable.phone_filled),
                                    contentDescription = null,
                                    tint = lerp(
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        answerProgress
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Expressive draggable thumb — squircle morph, spring, and haptics
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = animatable.value.roundToInt(),
                                y = 0
                            )
                        }
                        .size(width = 96.dp, height = 64.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            color = thumbColor,
                            shape = RoundedCornerShape(50)
                        )
                        .draggable(
                            state = dragState,
                            orientation = Orientation.Horizontal,
                            onDragStopped = {
                                val threshold = maxDrag * 0.5f
                                val goto = when {
                                    animatable.value >= threshold -> {
                                        haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                                        onCallAction(CallAction.AnswerCall)
                                        maxDrag
                                    }
                                    animatable.value <= -threshold -> {
                                        haptics.performHapticFeedback(HapticFeedbackType.Reject)
                                        onCallAction(CallAction.DeclineCall)
                                        -maxDrag
                                    }
                                    else -> {
                                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        0f
                                    }
                                }
                                scope.launch { animatable.animateTo(goto, bouncySpec()) }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.phone_filled),
                        contentDescription = "Swipe",
                        tint = when {
                            dragProgress < -0.08f -> MaterialTheme.colorScheme.onError
                            dragProgress > 0.08f -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier
                            .size(26.dp)
                            .graphicsLayer {
                                // Expressive rotation morph: 0 -> 135 for decline
                                if (animatable.value < 0f) {
                                    rotationZ = 135f * (-animatable.value / maxDrag).coerceIn(0f, 1f)
                                } else {
                                    rotationZ = 0f
                                }
                                scaleX = 1f + (kotlin.math.abs(dragProgress) * 0.08f)
                                scaleY = 1f + (kotlin.math.abs(dragProgress) * 0.08f)
                            }
                    )
                }
            }
        }

        // Fallback expressive tap buttons — accessibility + alternative to swipe
        // 8dp spacing system, full pill shapes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Decline — error expressive button
            androidx.compose.material3.Button(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.Reject)
                    onCallAction(CallAction.DeclineCall)
                },
                shapes = ButtonDefaults.shapes(
                    shape = RoundedCornerShape(50),
                    pressedShape = RoundedCornerShape(20.dp)
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.phone_filled),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer { rotationZ = 135f }
                )
                Text(
                    text = "Decline",
                    style = MaterialTheme.typography.labelLargeEmphasized.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Answer — primary expressive button (tonal primaryContainer for high emphasis)
            androidx.compose.material3.Button(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                    onCallAction(CallAction.AnswerCall)
                },
                shapes = ButtonDefaults.shapes(
                    shape = RoundedCornerShape(50),
                    pressedShape = RoundedCornerShape(20.dp)
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.phone_filled),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Answer",
                    style = MaterialTheme.typography.labelLargeEmphasized.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
)
@Composable
private fun SwipeToAnswerPreview() {
    CinnamonTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            IncomingBottomBar(onCallAction = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeToAnswerPreviewLight() {
    CinnamonTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            IncomingBottomBar(onCallAction = {})
        }
    }
}
