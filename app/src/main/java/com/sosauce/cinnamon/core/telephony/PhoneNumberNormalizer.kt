package com.sosauce.cinnamon.core.telephony

import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import java.util.Locale

class PhoneNumberNormalizer(
    private val telephonyManager: TelephonyManager,
    private val locale: Locale = Locale.getDefault()
) {

    /**
     * Should ALWAYS be used before sending a message/calling, it formats the desired number to E164, along-side using the network's ISO
     * (important when traveling abroad (learned that the hard way in Portugal)), if unavailable, fall back to the device's region.
     */
    fun formatToE164(number: String): String {
        return PhoneNumberUtils.formatNumberToE164(
            number,
            getIso()
        ) ?: number
    }


    fun getIso(): String {
        val networkIso = telephonyManager.networkCountryIso
        val simIso = telephonyManager.simCountryIso

        return when {
            networkIso.isNotEmpty() -> networkIso.uppercase()
            simIso.isNotEmpty() -> simIso.uppercase()
            else -> locale.country.uppercase()
        }
    }
}