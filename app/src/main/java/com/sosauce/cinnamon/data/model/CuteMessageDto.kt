package com.sosauce.cinnamon.data.model

data class CuteMessageDto(
    val id: Long,
    val threadId: Long,
    val recipients: List<String>,
    val body: String?,
    val date: Long,
    val type: Int,
    val read: Int,
    val status: Int,
    val isMms: Boolean
) {
    val isGroupChat
        get() = recipients.size > 1
}