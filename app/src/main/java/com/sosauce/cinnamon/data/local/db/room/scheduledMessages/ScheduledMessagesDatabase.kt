package com.sosauce.cinnamon.data.local.db.room.scheduledMessages

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScheduledMessage::class],
    version = 1
)
abstract class ScheduledMessagesDatabase : RoomDatabase() {
    abstract val dao: ScheduledMessagesDao
}