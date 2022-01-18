package com.mbmc.fiinfo.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mbmc.fiinfo.R

enum class WifiCarrier(
    @StringRes override val descriptionRes: Int,
    @DrawableRes override val drawableRes: Int
) : Icon {
    WIFI_OTHER(R.string.connected_wifi_carrier, R.drawable.ic_wifi_carrier),
    WIFI_SPRINT(R.string.connected_wifi_sprint, R.drawable.ic_wifi_sprint),
    WIFI_T_MOBILE(R.string.connected_wifi_t_mobile, R.drawable.ic_wifi_t_mobile),
    WIFI_THREE(R.string.connected_wifi_three, R.drawable.ic_wifi_three),
    WIFI_US_CELLULAR(R.string.connected_wifi_us_cellular, R.drawable.ic_wifi_us_cellular);
}