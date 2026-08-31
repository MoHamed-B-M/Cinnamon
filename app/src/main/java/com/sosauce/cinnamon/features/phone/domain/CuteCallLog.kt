package com.sosauce.cinnamon.features.phone.domain

import android.net.Uri
data class CuteCallLog2(
    val id: Long,
    val number: String,
    val displayName: String,
    val callType: CallType,
    val date: String,
    val time: String,
    val duration: String?,
    val location: String?,
    val presentation: CallPresentation,
    val photo: Uri?
)


enum class CallPresentation {
    ALLOWED,
    UNAVAILABLE,
    UNKNOWN,
    RESTRICTED,
    PAYPHONE
}

enum class CallType {
    INCOMING,
    OUTGOING,
    MISSED,
    REJECTED
}