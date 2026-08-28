@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sosauce.cinnamon.presentation.screens.archived

import android.provider.Telephony
import androidx.compose.ui.util.fastMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosauce.cinnamon.data.local.db.datastore.UserPreferences
import com.sosauce.cinnamon.data.model.toCuteConversation
import com.sosauce.cinnamon.data.repository.MessagesRepository
import com.sosauce.cinnamon.presentation.screens.messages.ConversationsAction
import com.sosauce.cinnamon.presentation.screens.messages.CuteConversationUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArchivedViewModel(
    private val userPreferences: UserPreferences,
    private val messagesRepository: MessagesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ArchivedState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            // TODO: REFACTOR
            userPreferences.archivedConversations
                .flatMapLatest { archived ->
                    if (archived.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        val placeholders = archived.joinToString(", ") { "?" }
                        messagesRepository.fetchLatestConversations(
                            extraSelection = "${Telephony.Threads._ID} IN ($placeholders)",
                            extraSelectionArgs = archived.toTypedArray()
                        )
                    }

                }.collectLatest { threads ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            threads = threads.fastMap { it.toCuteConversation("") }
                        )
                    }
                }
        }
    }

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

data class ArchivedState(
    val isLoading: Boolean = false,
    val threads: List<CuteConversationUI> = emptyList()
)