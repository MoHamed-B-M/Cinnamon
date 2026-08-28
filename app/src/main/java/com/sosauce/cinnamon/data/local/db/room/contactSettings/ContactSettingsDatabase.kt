package com.sosauce.cinnamon.data.local.db.room.contactSettings

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ContactSettings::class],
    version = 1
)
abstract class ContactSettingsDatabase : RoomDatabase() {
    abstract val dao: ContactSettingsDao
}