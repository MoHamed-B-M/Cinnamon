package com.sosauce.cinnamon.features.contacts.data.local.contactSettings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactSettingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val contactId: Long = 0,
    val poster: String = ""
)