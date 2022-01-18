package com.mbmc.fiinfo.data

import androidx.annotation.DrawableRes

data class Notification(
    val content: String,
    @DrawableRes val drawableRes: Int
)