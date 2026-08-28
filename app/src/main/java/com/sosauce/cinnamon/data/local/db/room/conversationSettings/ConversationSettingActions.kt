package com.sosauce.cinnamon.data.local.db.room.conversationSettings

import com.sosauce.cinnamon.domain.model.ConversationSettings

sealed class ConversationSettingActions {
    data class UpsertConversationSettings(val conversationSettings: ConversationSettings) :
        ConversationSettingActions()
}