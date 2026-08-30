package com.sosauce.cinnamon.features.messaging.data.local.conversationSettings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConversationSettingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val threadId: Long = 0,
    val draft: String = "",
    val wallpaper: String = "",
    val wallpaperBlurIntensity: Int = 0,
    val color: Int = -1
)