package com.mbmc.fiinfo.data

import androidx.annotation.DrawableRes

data class Widget(
    val connection: String,
    @DrawableRes val drawableRes: Int,
    val carrier: String? = null,
)