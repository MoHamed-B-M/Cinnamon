package com.sosauce.cinnamon.features.messaging.data.local.conversationSettings

sealed class ConversationSettingActions {
    data class UpsertConversationSettings(val conversationSettingsEntity: ConversationSettingsEntity) :
        ConversationSettingActions()
}