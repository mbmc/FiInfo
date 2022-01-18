package com.mbmc.fiinfo.database

import androidx.room.TypeConverter
import com.mbmc.fiinfo.data.Type

object Converters {
    @TypeConverter
    fun toType(type: Int): Type = Type.values()[type]

    @TypeConverter
    fun fromType(type: Type): Int = type.ordinal
}