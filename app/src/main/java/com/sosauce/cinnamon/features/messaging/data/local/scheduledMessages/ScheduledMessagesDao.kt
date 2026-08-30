package com.sosauce.cinnamon.features.messaging.data.local.scheduledMessages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledMessagesDao {


    @Upsert
    fun upsertScheduledMessage(scheduledMessageEntity: ScheduledMessageEntity): Long

    @Query("SELECT * FROM scheduledmessageentity WHERE :threadId = threadId")
    fun getScheduledMessagesForThread(threadId: Long): Flow<List<ScheduledMessageEntity>>

    @Query("SELECT * FROM scheduledmessageentity WHERE :id = id")
    fun getScheduledMessageById(id: Long): ScheduledMessageEntity

    @Delete
    fun deleteScheduledMessage(scheduledMessageEntity: ScheduledMessageEntity)
}