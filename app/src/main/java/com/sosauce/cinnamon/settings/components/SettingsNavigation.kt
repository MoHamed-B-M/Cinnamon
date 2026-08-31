package com.sosauce.cinnamon.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sosauce.cinnamon.R
import com.sosauce.cinnamon.core.datastore.rememberInitialScreen
import com.sosauce.cinnamon.core.utils.DefaultTabOption
import com.sosauce.nekobites.components.LazyRowWithScrollButton

@Composable
fun SettingsNavigation() {

    var initialScreen by rememberInitialScreen()

    val screenItems = listOf(
        ScreenItem(
            onClick = { initialScreen = DefaultTabOption.MESSAGES },
            icon = R.drawable.messages_filled,
            text = R.string.messages,
            isSelected = initialScreen == DefaultTabOption.MESSAGES
        ),
        ScreenItem(
            onClick = { initialScreen = DefaultTabOption.CONTACTS },
            icon = R.drawable.contacts_filled,
            text = R.string.contacts,
            isSelected = initialScreen == DefaultTabOption.CONTACTS
        ),
        ScreenItem(
            onClick = { initialScreen = DefaultTabOption.DIALER },
            icon = R.drawable.phone_filled,
            text = R.string.dialer,
            isSelected = initialScreen == DefaultTabOption.DIALER
        ),
        ScreenItem(
            onClick = { initialScreen = DefaultTabOption.DIALPAD },
            icon = R.drawable.dialpad,
            text = R.string.dialpad,
            isSelected = initialScreen == DefaultTabOption.DIALPAD
        )
    )

    Column {
        SettingsWithTitle(
            title = R.string.default_launch_screen
        ) {
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column {
                    LazyRowWithScrollButton(
                        items = screenItems
                    ) { screen ->
                        SettingsSelector(
                            onClick = screen.onClick,
                            icon = screen.icon,
                            text = screen.text,
                            isSelected = screen.isSelected
                        )
                    }
                }
            }
        }
    }

}

data class ScreenItem(
    val onClick: () -> Unit,
    val icon: Int,
    val text: Int,
    val isSelected: Boolean
)