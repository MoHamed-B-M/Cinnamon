package com.sosauce.cinnamon.presentation.screens.messages

enum class CallState {
    RINGING, // When we're receiving a call
    DIALING, // When we're calling
    ONGOING,
    ENDED
}