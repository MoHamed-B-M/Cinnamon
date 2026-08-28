package com.sosauce.cinnamon.data.model

import com.sosauce.cinnamon.presentation.screens.messages.CuteConversationUI
import com.sosauce.cinnamon.utils.toDate

fun CuteConversationDto.toCuteConversation(draft: String): CuteConversationUI {
    return CuteConversationUI(
        threadId = threadId,
        participants = participants,
        snippet = snippet,
        date = date.toDate(),
        read = read,
        draft = draft
    )
}