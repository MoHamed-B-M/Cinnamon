package com.sosauce.cinnamon.features.contacts.data.local.contactSettings

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactSettingsDao {

    @Upsert
    suspend fun upsertContact(contactSettingsEntity: ContactSettingsEntity)

    @Query("SELECT * FROM contactsettingsentity WHERE contactId = :contactId LIMIT 1")
    fun getContactSettings(contactId: Long): Flow<ContactSettingsEntity?>

    @Query("SELECT poster FROM contactsettingsentity WHERE contactId = :contactId LIMIT 1")
    fun getContactPoster(contactId: Long): String?

    @Query("DELETE FROM contactsettingsentity WHERE contactId IN (:contactIds)")
    suspend fun deleteContactsSettings(contactIds: List<Long>)

    @Delete
    suspend fun deleteContactSettings(contactSettingsEntity: ContactSettingsEntity)

}