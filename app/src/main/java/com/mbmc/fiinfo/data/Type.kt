package com.mbmc.fiinfo.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mbmc.fiinfo.R

enum class Type(
    @StringRes override val descriptionRes: Int,
    @DrawableRes override val drawableRes: Int
) : Icon {
    UNKNOWN(R.string.unknown, 0), // 0
    AIRPLANE_OFF(R.string.airplane_off, R.drawable.ic_airplane_off), // 1
    AIRPLANE_ON(R.string.airplane_on, R.drawable.ic_airplane_on), // 2
    CARRIER(0, 0), // 3
    CELLULAR_OFF(R.string.cellular_off, R.drawable.ic_cellular_off), // 4
    CELLULAR_ON(R.string.cellular_on, R.drawable.ic_cellular_on), // 5
    DISCONNECTED(R.string.disconnected, R.drawable.ic_disconnected), // 6
    PHONE_OFF(R.string.phone_off, R.drawable.ic_phone_off), // 7
    PHONE_ON(R.string.phone_on, R.drawable.ic_phone_on), // 8
    WIFI(R.string.connected_wifi, R.drawable.ic_wifi), // 9
    WIFI_CARRIER(0, 0), // 10
    WIFI_OFF(R.string.wifi_off, R.drawable.ic_wifi_off), // 11
    WIFI_ON(R.string.wifi_on, R.drawable.ic_wifi_on); // 12
}