package com.mbmc.fiinfo.data

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.room.TypeConverters
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.database.Converters
import com.mbmc.fiinfo.database.EventEntity
import com.mbmc.fiinfo.util.*
import java.util.*

@TypeConverters(Converters::class)
data class Event(
    override val type: Type,
    val timestamp: String = "",
    val timezone: String = TimeZone.getDefault().id,
    override var ssid: String? = null,
    var frequency: String? = null,
    override var mccmnc: String? = null,
    var operator: String? = null,
    override var speed: Int? = null,
) : Record {

    // Don't account for timestamp
    fun isEqualTo(event: Event): Boolean =
        type == event.type
                && timezone == event.timezone
                && ssid == event.ssid
                && frequency == event.frequency
                && mccmnc == event.mccmnc
                && operator == event.operator
                && speed == event.speed

    fun toEntity(): EventEntity =
        EventEntity(
            type = type.ordinal,
            timestamp = "${System.currentTimeMillis()}",
            timezone = TimeZone.getDefault().id,
            ssid = fixSsid(ssid),
            frequency = frequency,
            mccmnc = mccmnc,
            operator = operator,
            speed = speed
        )

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

    fun toNotification(context: Context): Notification? {
        val content: String
        @DrawableRes val drawableRes: Int

        when (type) {
            Type.CARRIER -> {
                content = getCarrierDetails(context)
                drawableRes = getCarrier(mccmnc).drawableRes
            }

            Type.DISCONNECTED -> {
                content = context.getString(type.descriptionRes)
                drawableRes = type.drawableRes
            }

            Type.WIFI -> {
                content = getWifiDetails(context)
                drawableRes = type.drawableRes
            }

            Type.WIFI_CARRIER -> {
                content = context.getString(
                    R.string.combine,
                    getWifiDetails(context),
                    getCarrierDetails(context)
                )
                drawableRes = getWifiCarrier(mccmnc).drawableRes
            }

            else -> return null
        }
        return Notification(content, drawableRes)
    }

    private fun getWifiDetails(context: Context) =
        context.getString(R.string.wifi, fixSsid(ssid), frequency)

    private fun getCarrierDetails(context: Context): String {
        val carrier = getCarrierName(context, mccmnc)
        val speed = getSpeedDescription(speed)

        return operator?.let {
            context.getString(R.string.carrier_operator, carrier, speed, getOperator(it))
        } ?: context.getString(R.string.carrier, carrier, speed)
    }
}