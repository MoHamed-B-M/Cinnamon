@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.sosauce.cinnamon.presentation.screens.messages

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosauce.cinnamon.data.local.db.datastore.UserPreferences
import com.sosauce.cinnamon.data.local.db.room.conversationSettings.ConversationSettingsDao
import com.sosauce.cinnamon.data.model.toCuteConversation
import com.sosauce.cinnamon.data.repository.MessagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ConversationsViewModel(
    private val messagesRepository: MessagesRepository,
    private val userPreferences: UserPreferences,
    private val conversationSettingsDao: ConversationSettingsDao
) : ViewModel() {

    val state = combine(
        messagesRepository.fetchLatestConversations(),
        userPreferences.pinnedConversations,
        conversationSettingsDao.getAllDrafts(),
        userPreferences.archivedConversations,
        snapshotFlow { textFieldState.text }.debounce(250.milliseconds)
    ) { cleanConversations, pinned, allDrafts, archived, searchQuery ->
        val (pinnedThreads, unpinnedThreads) = cleanConversations
            .fastFilter { it.threadId.toString() !in archived }
            .fastFilter {
                it.snippet.contains(searchQuery, true) ||
                        it.participants.fastAny { it.displayName.contains(searchQuery, true) }
            }
            .fastMap {
                val draft = allDrafts[it.threadId] ?: ""
                it.toCuteConversation(draft)
            }
            .partition { it.threadId.toString() in pinned }

        ConversationsState(
            isLoading = false,
            conversations = unpinnedThreads,
            pinnedConversations = pinnedThreads,
            hasArchivedThreads = archived.isNotEmpty()
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ConversationsState(isLoading = true)
    )

    val textFieldState = TextFieldState()

    fun handleThreadsAction(action: ConversationsAction) {
        when (action) {
            is ConversationsAction.ArchiveConversations -> {
                viewModelScope.launch {
                    userPreferences.toggleArchiveThreads(action.threadIds)
                }
            }

            is ConversationsAction.PinConversations -> {
                viewModelScope.launch {
                    userPreferences.pinThreads(action.threadIds)
                }
            }

            is ConversationsAction.DeleteConversations -> {
                viewModelScope.launch {
                    messagesRepository.deleteThreads(action.threadIds)
                }
            }
        }

    }


}

data class ConversationsState(
    val isLoading: Boolean = false,
    val hasArchivedThreads: Boolean = false,
    val conversations: List<CuteConversationUI> = emptyList(),
    val pinnedConversations: List<CuteConversationUI> = emptyList()
)

sealed interface ConversationsAction {
    data class ArchiveConversations(val threadIds: List<Long>) : ConversationsAction
    data class PinConversations(val threadIds: List<Long>) : ConversationsAction
    data class DeleteConversations(val threadIds: List<Long>) : ConversationsAction
}
