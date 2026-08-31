package com.sosauce.cinnamon.features.messaging.data.model

import com.sosauce.cinnamon.features.messaging.domain.Participant

data class CuteConversationEntity(
    val threadId: Long = 0,
    val participants: List<Participant> = emptyList(),
    val snippet: String = "",
    val date: Long = 0,
    val read: Boolean = true
)
