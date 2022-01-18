package com.mbmc.fiinfo.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiInfo.FREQUENCY_UNITS
import android.telephony.TelephonyDisplayInfo.*
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import com.mbmc.fiinfo.data.Carrier
import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.data.Type
import com.mbmc.fiinfo.data.WifiCarrier

@SuppressLint("MissingPermission")
fun TelephonyManager.toEvent(): Event =
    Event(
        type = Type.CARRIER,
        mccmnc = simOperator,
        operator = if (simOperator == networkOperator) null else networkOperator,
        speed = dataNetworkType
    )

fun WifiInfo.getDetails(): String =
    "$frequency$FREQUENCY_UNITS"

fun getCarrier(mccmnc: String?): Carrier =
    mccmnc?.let {
        FI_MCC_MNC_MAP[it]
    } ?: Carrier.OTHER

fun getCarrierName(context: Context, mccmnc: String?): String? {
    val carrier = getCarrier(mccmnc)
    return if (carrier == Carrier.OTHER) {
        mccmnc
    } else {
        context.getString(carrier.nameRes)
    }
}

fun getWifiCarrier(mccmnc: String?): WifiCarrier =
    getCarrier(mccmnc).toWifiCarrier()

fun getOperator(mccmnc: String): String =
    MCC_MNC_MAP[mccmnc] ?: mccmnc

fun fixSsid(ssid: String?): String? =
    ssid?.let {
        if (it.isNotEmpty() && it == UNKNOWN_SSID) {
            return "n/a"
        }
        return it
    }

// TelephonyManager.java
fun getSpeedDescription(type: Int?): String =
    when (type) {
        // 2G
        NETWORK_TYPE_GPRS -> "2G (GPRS)" // 1
        NETWORK_TYPE_EDGE -> "2G (EDGE)" // 2
        NETWORK_TYPE_CDMA -> "2G (CDMA)" // 4
        NETWORK_TYPE_1xRTT -> "2G (1xRTT)" // 7
        NETWORK_TYPE_IDEN -> "2G (IDEN)" // 11
        NETWORK_TYPE_GSM -> "2G (GSM)" // 16
        // 3G
        NETWORK_TYPE_UMTS -> "3G (UMTS)" // 3
        NETWORK_TYPE_EVDO_0 -> "3G (EVDO_0)" // 5
        NETWORK_TYPE_EVDO_A -> "3G (EVDO_A)" // 6
        NETWORK_TYPE_HSDPA -> "3G (HSDPA)" // 8
        NETWORK_TYPE_HSUPA -> "3G (HSUPA)" // 9
        NETWORK_TYPE_HSPA -> "3G (HSPA)" // 10
        NETWORK_TYPE_EVDO_B -> "3G (EVDO_B)" // 12
        NETWORK_TYPE_EHRPD -> "3G (EHRPD)" // 14
        NETWORK_TYPE_HSPAP -> "3G (HSPAP)" // 15
        NETWORK_TYPE_TD_SCDMA -> " 3G (TD_SCDMA)" // 17
        // 4G
        NETWORK_TYPE_LTE -> "4G (LTE)" // 13
        19 -> "4G (LTE_CA)" // 19 NETWORK_TYPE_LTE_CA
        // 5G
        NETWORK_TYPE_NR -> "5G (NR)" // 20
        // iWLAN
        NETWORK_TYPE_IWLAN -> "IWLAN" // 18

        // TelephonyDisplayInfo.java
        // Override, + 100 not to collide with above
        OVERRIDE_NETWORK_TYPE_LTE_CA + OVERRIDE_SHIFT -> "4G (LTE_CA)" // 1
        OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO + OVERRIDE_SHIFT -> " 4G (LTE_ADV)" // 2
        OVERRIDE_NETWORK_TYPE_NR_NSA + OVERRIDE_SHIFT -> "5G (NR_NSA)" // 3
        OVERRIDE_NETWORK_TYPE_NR_ADVANCED + OVERRIDE_SHIFT -> "5G (NR_ADV)" // 5

        // else
        else -> UNKNOWN // 0
    }