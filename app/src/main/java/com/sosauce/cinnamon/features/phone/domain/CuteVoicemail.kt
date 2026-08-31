package com.sosauce.cinnamon.features.phone.domain

import android.net.Uri

data class CuteVoicemail(
    val id: Long,
    val number: String,
    val displayName: String,
    val photo: Uri?,
    val date: String,
    val time: String,
    val duration: String,
    val voicemail: Uri,
)
