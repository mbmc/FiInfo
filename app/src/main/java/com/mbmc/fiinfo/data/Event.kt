package com.mbmc.fiinfo.data

import android.os.Parcelable
import androidx.room.TypeConverters
import com.mbmc.fiinfo.database.Converters
import kotlinx.parcelize.Parcelize
import java.util.*

@TypeConverters(Converters::class)
@Parcelize
data class Event(
    override val type: Type,
    val timestamp: String = "",
    val timezone: String = TimeZone.getDefault().id,
    override var ssid: String? = null,
    var frequency: String? = null,
    override var mccmnc: String? = null,
    var operator: String? = null,
    override var speed: Int? = null,
) : Record, Parcelable {

    // Don't account for timestamp
    fun isEqualTo(event: Event): Boolean =
        type == event.type
                && timezone == event.timezone
                && ssid == event.ssid
                && frequency == event.frequency
                && mccmnc == event.mccmnc
                && operator == event.operator
                && speed == event.speed

    fun copy() =
        Event(
            type = this.type,
            timestamp = this.timestamp,
            timezone = this.timezone,
            ssid = this.ssid,
            frequency = this.frequency,
            mccmnc = this.mccmnc,
            operator = this.operator,
            speed = this.speed
        )
}