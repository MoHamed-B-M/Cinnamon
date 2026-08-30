package com.sosauce.cinnamon.features.messaging.presentation.customization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosauce.cinnamon.features.messaging.data.local.conversationSettings.ConversationSettingActions
import com.sosauce.cinnamon.features.messaging.data.local.conversationSettings.ConversationSettingsDao
import com.sosauce.cinnamon.features.messaging.data.local.conversationSettings.ConversationSettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ThemingViewModel(
    private val threadId: Long,
    private val conversationSettingsDao: ConversationSettingsDao
) : ViewModel() {

    private val _state = MutableStateFlow(ThemingState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            conversationSettingsDao.getConversationSettings(threadId).collectLatest { settings ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        settings = settings ?: ConversationSettingsEntity(threadId)
                    )
                }
            }
        }
    }

    fun handleConversationSettingsActions(action: ConversationSettingActions) {
        when (action) {
            is ConversationSettingActions.UpsertConversationSettings -> {
                viewModelScope.launch(Dispatchers.IO) {
                    conversationSettingsDao.upsertConversation(action.conversationSettingsEntity)
                }
            }
        }
    }


}

data class ThemingState(
    val isLoading: Boolean = false,
    val settings: ConversationSettingsEntity = ConversationSettingsEntity()
)