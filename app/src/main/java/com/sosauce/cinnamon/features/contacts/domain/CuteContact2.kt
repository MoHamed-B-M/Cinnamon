package com.sosauce.cinnamon.features.contacts.domain

import android.net.Uri

data class CuteContact2(
    val id: Long,
    val displayName: String,
    val thumbnail: Uri?,
    val isFavorite: String,
    val accountName: String
)

data class ContactPhone(
    val number: String,
    val type: Int,
    val isDefault: Boolean,
    val isBlocked: Boolean
)
