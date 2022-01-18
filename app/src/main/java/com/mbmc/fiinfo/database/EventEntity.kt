package com.mbmc.fiinfo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int? = null,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "timestamp") val timestamp: String,
    @ColumnInfo(name = "timezone") val timezone: String,
    @ColumnInfo(name = "ssid") val ssid: String? = null,
    @ColumnInfo(name = "frequency") val frequency: String? = null,
    @ColumnInfo(name = "mccmnc") val mccmnc: String? = null,
    @ColumnInfo(name = "operator") val operator: String? = null,
    @ColumnInfo(name = "speed") val speed: Int? = null,
)