package com.sosauce.cinnamon.features.phone.presentation.call

enum class CallState {
    RINGING, // When we're receiving a call
    DIALING, // When we're calling
    ONGOING,
    ENDED
}