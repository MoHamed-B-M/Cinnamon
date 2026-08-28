package com.sosauce.cinnamon.presentation.screens.messages

import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.net.toUri
import kotlinx.serialization.Serializable

data class CuteConversationUI(
    val threadId: Long,
    val participants: List<Participant>,
    val snippet: String,
    val date: String,
    val read: Boolean,
    val draft: String
) {
    val isGroupChat = participants.size > 1

    val name = buildString {
        participants.fastForEachIndexed { index, participant ->
            append(participant.displayName)
            if (index != participants.lastIndex) {
                append(", ")
            }
        }
    }

    val isAnyBlocked = participants.fastAny { it.isBlocked }

    /**
     * For non-group chats
     */
    val photo = participants.firstOrNull()?.photoUri
}

data class Participant(
    val rawNumber: String,
    val displayName: String,
    val photoUriString: String?,
    val isBlocked: Boolean
) {
    val photoUri
        get() = photoUriString?.toUri()
}