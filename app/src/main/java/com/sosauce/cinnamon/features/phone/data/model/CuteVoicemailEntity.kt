package com.sosauce.cinnamon.features.phone.data.model

data class CuteVoicemailEntity(
    val id: Long,
    val name: String?,
    val photo: String?,
    val number: String,
    val date: Long,
    val duration: Long,
    val voicemail: String
)
