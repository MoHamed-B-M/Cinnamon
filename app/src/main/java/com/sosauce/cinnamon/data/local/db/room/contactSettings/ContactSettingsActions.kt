package com.sosauce.cinnamon.data.local.db.room.contactSettings

sealed class ContactSettingsActions {
    data class UpsertContactSettings(val contactSettings: ContactSettings) :
        ContactSettingsActions()
}