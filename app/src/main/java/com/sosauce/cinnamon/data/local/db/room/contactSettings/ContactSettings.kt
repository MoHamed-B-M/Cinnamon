package com.sosauce.cinnamon.data.local.db.room.contactSettings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val contactId: Long = 0,
    val poster: String = ""
)