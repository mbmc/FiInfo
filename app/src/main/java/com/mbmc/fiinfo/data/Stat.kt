package com.mbmc.fiinfo.data

import androidx.room.TypeConverters
import com.mbmc.fiinfo.database.Converters

@TypeConverters(Converters::class)
data class Stat(
    val count: Int,
    override val type: Type,
    override val ssid: String? = null,
    override val mccmnc: String? = null,
    override val speed: Int? = null
) : Record