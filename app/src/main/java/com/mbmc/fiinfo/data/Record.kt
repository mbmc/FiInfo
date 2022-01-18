package com.mbmc.fiinfo.data

import com.mbmc.fiinfo.util.getSpeedDescription

interface Record {
    val type: Type
    val ssid: String?
    val mccmnc: String?
    val speed: Int?

    fun getWifiCarrierDetails() =
        "$ssid / ${getSpeedDescription(speed)}"
}