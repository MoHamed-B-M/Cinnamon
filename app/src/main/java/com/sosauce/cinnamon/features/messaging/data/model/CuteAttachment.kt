package com.sosauce.cinnamon.features.messaging.data.model

import android.net.Uri

data class CuteAttachment(
    val id: Long = 0,
    val body: String = "",
    val attachmentDetails: List<AttachmentDetails> = emptyList()
) {
    data class AttachmentDetails(
        val id: Long,
        val uri: Uri,
        val filename: String,
        val attachmentType: AttachmentType,
        val size: Long
    )
}


enum class AttachmentType {
    IMAGE,
    VIDEO,
    AUDIO,
    VCARD,
    OTHER
}

