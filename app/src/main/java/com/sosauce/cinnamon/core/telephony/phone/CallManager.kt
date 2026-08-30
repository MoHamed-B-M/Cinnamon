package com.sosauce.cinnamon.core.telephony.phone

import android.content.Context
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.core.net.toUri
import com.sosauce.cinnamon.core.datastore.UserPreferences
import com.sosauce.cinnamon.core.telephony.PhoneNumberNormalizer
import com.sosauce.cinnamon.features.phone.domain.AudioRoute
import com.sosauce.cinnamon.features.phone.domain.CuteSimCard
import com.sosauce.cinnamon.features.phone.presentation.call.CallState
import com.sosauce.cinnamon.features.phone.presentation.call.CallingState
import com.sosauce.cinnamon.core.utils.beautifyNumber
import com.sosauce.cinnamon.core.utils.getContactNameOrNothing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

// Inspired by Fossify's call manager!

/**
 * A bridge between an InCallService (CallService) and the ViewModel.
 */
class CallManager(
    private val context: Context,
    private val telecomManager: TelecomManager,
    private val userPreferences: UserPreferences,
    private val phoneNumberNormalizer: PhoneNumberNormalizer
) {

    private var callServiceCallback: CallServiceCallback? = null
    private var androidCallCallback: AndroidCallCallback? = null


    val _callingState = MutableStateFlow(CallingState())
    val callingState = _callingState.asStateFlow()


    fun registerCallServiceCallback(cb: CallServiceCallback) {
        callServiceCallback = cb
    }

    fun registerAndroidCallCallback(cb: AndroidCallCallback) {
        androidCallCallback = cb
    }

    fun unregisterCallServiceCallback() {
        callServiceCallback = null
    }

    fun unregisterAndroidCallCallback() {
        androidCallCallback = null
    }

    fun answerCall() = androidCallCallback?.answerCall()

    fun declineCall() = androidCallCallback?.declineCall()

    /**
     * @return Whether the call was successfully placed or not
     */
    fun startCall(number: String): Boolean {

        val phoneHandle = runBlocking { userPreferences.getDefaultPhoneHandle().first() }

        val bundle = Bundle().apply {
            putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneHandle)
        }

        val normalizedNumber = phoneNumberNormalizer.formatToE164(number)
        val numberUri = "tel:$normalizedNumber".toUri()

        return try {
            telecomManager.placeCall(numberUri, bundle)
            true
        } catch (_: SecurityException) {
            false
        }
    }


    fun hangupOngoingCall() = androidCallCallback?.hangupOngoingCall()

    fun toggleMute(mute: Boolean) = callServiceCallback?.toggleMute(mute)

    fun startTone(char: Char) = androidCallCallback?.startTone(char)

    fun toggleHold() = androidCallCallback?.toggleHold()

    fun switchAudioRoute(route: AudioRoute) = callServiceCallback?.switchAudioRoute(route)

    fun updateAvailableAudioRoutes(routes: List<AudioRoute>) {
        _callingState.update {
            it.copy(availableAudioRoutes = routes)
        }
    }

    fun updateCurrentAudioRoute(route: AudioRoute) {
        _callingState.update {
            it.copy(currentAudioRoute = route)
        }
    }

    fun updateIsMuted(isMuted: Boolean) {
        _callingState.update {
            it.copy(isMuted = isMuted)
        }
    }

    fun updateIsHolding(isHolding: Boolean) {
        _callingState.update {
            it.copy(isHolding = isHolding)
        }
    }

    fun updateCallState(callState: CallState) {
        _callingState.update {
            it.copy(callState = callState)
        }
    }

    fun updateTimeSpent(time: Long) {
        _callingState.update {
            it.copy(timeSpentInCall = time)
        }
    }

    fun updateNumber(number: String) {
        _callingState.update {
            it.copy(
                number = number,
                displayName = number.getContactNameOrNothing(context).beautifyNumber()
            )
        }
    }

    fun updateActiveSim(sim: CuteSimCard) {
        _callingState.update {
            it.copy(activeSim = sim)
        }
    }

    fun isInCall(): Boolean {
        return try {
            telecomManager.isInCall
        } catch (_: SecurityException) {
            false
        }
    }
}


interface AndroidCallCallback {
    fun answerCall()
    fun declineCall()
    fun hangupOngoingCall()
    fun startTone(char: Char)
    fun toggleHold()
}

interface CallServiceCallback {
    fun toggleMute(mute: Boolean)
    fun switchAudioRoute(route: AudioRoute)
}