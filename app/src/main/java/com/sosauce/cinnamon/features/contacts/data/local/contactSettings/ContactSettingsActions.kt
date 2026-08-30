package com.sosauce.cinnamon.features.contacts.data.local.contactSettings

sealed class ContactSettingsActions {
    data class UpsertContactSettings(val contactSettingsEntity: ContactSettingsEntity) :
        ContactSettingsActions()
}