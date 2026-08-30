package com.sosauce.cinnamon.features.messaging.domain

import com.sosauce.cinnamon.features.messaging.data.model.CuteAttachment

data class CuteMessage(
    val id: Long,
    val body: String,
    val type: MessageType,
    val date: String,
    val time: String,
    val timestamp: Long,
    val threadId: Long,
    val read: Boolean,
    val isMms: Boolean,
    val attachment: CuteAttachment? = null,
    val isScheduled: Boolean,
    val delivered: Boolean
)
enum class MessageType {
    SENT,
    RECEIVED,
    FAILED,
    SENDING
}