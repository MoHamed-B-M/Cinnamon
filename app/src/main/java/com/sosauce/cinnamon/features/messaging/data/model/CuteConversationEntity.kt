package com.sosauce.cinnamon.features.messaging.data.model

import com.sosauce.cinnamon.features.messaging.domain.Participant

data class CuteConversationEntity(
    val threadId: Long,
    val participants: List<Participant>,
    val snippet: String,
    val date: Long,
    val read: Boolean
)
