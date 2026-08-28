@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.cinnamon.presentation.screens.archived

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sosauce.cinnamon.R
import com.sosauce.cinnamon.presentation.components.ConversationsSelectedBar
import com.sosauce.cinnamon.presentation.components.buttons.CuteNavigationButtonSurface
import com.sosauce.cinnamon.presentation.components.searchbars.CuteSearchbar
import com.sosauce.cinnamon.presentation.navigation.Screen
import com.sosauce.cinnamon.presentation.screens.messages.ConversationsAction
import com.sosauce.cinnamon.presentation.screens.messages.CuteConversationUI
import com.sosauce.cinnamon.presentation.screens.messages.components.dialogs.DeleteConversationsDialog
import com.sosauce.cinnamon.presentation.screens.messages.threadsList
import com.sosauce.cinnamon.utils.selfAlignHorizontally
import com.sosauce.nekobites.components.NoXFound
import com.sosauce.sweetselect.rememberSweetSelectState

@Composable
fun SharedTransitionScope.ArchivedThreads(
    state: ArchivedState,
    onNavigate: (Screen) -> Unit,
    onNavigateUp: () -> Unit,
    onHandleThreadsAction: (ConversationsAction) -> Unit
) {

    val sweetSelectState = rememberSweetSelectState<CuteConversationUI>()
    var showDeleteConversationsDialog by remember { mutableStateOf(false) }

    if (showDeleteConversationsDialog) {
        DeleteConversationsDialog(
            onDismissRequest = { showDeleteConversationsDialog = false },
            onDelete = {
                val threadIds = sweetSelectState.selectedItems.map { it.threadId }
                onHandleThreadsAction(ConversationsAction.DeleteConversations(threadIds))

                showDeleteConversationsDialog = false
            },
            numberOfConversations = sweetSelectState.selectedItems.size
        )
    }



    Scaffold(
        bottomBar = {
            AnimatedContent(
                targetState = sweetSelectState.isInSelectionMode,
            ) {
                if (!it) {
                    CuteSearchbar(
                        modifier = Modifier.selfAlignHorizontally(),
                        onNavigate = onNavigate,
                        navigationIcon = { CuteNavigationButtonSurface(onNavigateUp = onNavigateUp) }
                    )
                } else {
                    ConversationsSelectedBar(
                        modifier = Modifier.selfAlignHorizontally(),
                        items = state.threads,
                        multiSelectState = sweetSelectState,
                        onDeleteConversations = {
                            val threadIds = sweetSelectState.selectedItems.map { it.threadId }
                            onHandleThreadsAction(ConversationsAction.DeleteConversations(threadIds))
                            sweetSelectState.clearSelected()
                        },
                        onArchiveThreads = {
                            val threadIds = sweetSelectState.selectedItems.map { it.threadId }
                            onHandleThreadsAction(ConversationsAction.ArchiveConversations(threadIds))
                            sweetSelectState.clearSelected()
                        },
                        onPinThreads = {
                            val threadIds = sweetSelectState.selectedItems.map { it.threadId }
                            onHandleThreadsAction(ConversationsAction.PinConversations(threadIds))
                            sweetSelectState.clearSelected()
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            threadsList(
                pinnedConversations = emptyList(),
                conversations = state.threads,
                sweetSelectState = sweetSelectState,
                onNavigate = onNavigate,
                sharedTransitionScope = this@ArchivedThreads,
                emptyState = {
                    NoXFound(
                        headlineText = R.string.no_archived_convos,
                        bodyText = R.string.no_archived_convos_desc,
                        icon = R.drawable.archived_outlined
                    )
                },
                onHandleConversationsAction = onHandleThreadsAction
            )

        }

    }
}