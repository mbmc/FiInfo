package com.mbmc.fiinfo.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mbmc.fiinfo.R

enum class Carrier(
    @StringRes val nameRes: Int,
    @StringRes override val descriptionRes: Int,
    @DrawableRes override val drawableRes: Int
) : Icon {
    OTHER(0, R.string.connected_carrier, R.drawable.ic_carrier),
    SPRINT(R.string.sprint, R.string.connected_sprint, R.drawable.ic_sprint), // 310120
    T_MOBILE(R.string.t_mobile, R.string.connected_t_mobile, R.drawable.ic_t_mobile), // 310260
    THREE_AT(R.string.three_at, R.string.connected_three, R.drawable.ic_three), // 23210
    THREE_UK(R.string.three_uk, R.string.connected_three, R.drawable.ic_three), // 23420
    US_CELLULAR(R.string.us_cellular, R.string.connected_us_cellular, R.drawable.ic_us_cellular); // 311580

    fun toWifiCarrier(): WifiCarrier =
        when (this) {
            SPRINT -> WifiCarrier.WIFI_SPRINT
            T_MOBILE -> WifiCarrier.WIFI_T_MOBILE
            THREE_AT, THREE_UK -> WifiCarrier.WIFI_THREE
            US_CELLULAR -> WifiCarrier.WIFI_US_CELLULAR
            else -> WifiCarrier.WIFI_OTHER
        }
}