package com.sosauce.cinnamon.features.messaging.data.local.conversationSettings

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationSettingsDao {

    @Upsert
    suspend fun upsertConversation(conversationSettingsEntity: ConversationSettingsEntity)

    @Query("SELECT * FROM conversationsettingsentity WHERE threadId = :threadId LIMIT 1")
    fun getConversationSettings(threadId: Long): Flow<ConversationSettingsEntity?>

    @Query("SELECT threadId, draft FROM conversationsettingsentity")
    fun getAllDrafts(): Flow<Map<@MapColumn(columnName = "threadId") Long, @MapColumn(columnName = "draft") String>>

    @Query("SELECT draft FROM conversationsettingsentity WHERE :threadId = threadId LIMIT 1")
    fun getDraftForThread(threadId: Long): Flow<String?>

}