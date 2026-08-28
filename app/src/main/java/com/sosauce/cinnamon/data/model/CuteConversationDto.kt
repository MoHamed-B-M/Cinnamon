package com.sosauce.cinnamon.data.model

import com.sosauce.cinnamon.presentation.screens.messages.Participant

data class CuteConversationDto(
    val threadId: Long,
    val participants: List<Participant>,
    val snippet: String,
    val date: Long,
    val read: Boolean
)
