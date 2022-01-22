package com.mbmc.fiinfo.util

import android.content.Context
import androidx.annotation.DrawableRes
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.data.Notification
import com.mbmc.fiinfo.data.Type
import com.mbmc.fiinfo.data.Widget
import com.mbmc.fiinfo.database.EventEntity
import java.util.*

fun Event.toEntity(): EventEntity =
    EventEntity(
        type = type.ordinal,
        timestamp = "${System.currentTimeMillis()}",
        timezone = TimeZone.getDefault().id,
        ssid = ssid,
        frequency = frequency,
        mccmnc = mccmnc,
        operator = operator,
        speed = speed
    )

fun Event.toNotification(context: Context): Notification? {
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

fun Event.toWidget(context: Context): Widget? {
    val connection: String
    @DrawableRes val drawableRes: Int
    var carrier: String? = null

    when (type) {
        Type.CARRIER -> {
            connection = getSpeedDescriptionAndOperator(context)
            drawableRes = getCarrier(mccmnc).drawableRes
        }

        Type.DISCONNECTED -> {
            connection = context.getString(type.descriptionRes)
            drawableRes = type.drawableRes
        }

        Type.WIFI -> {
            connection = getSsidAndFrequency(context)
            drawableRes = type.drawableRes
        }

        Type.WIFI_CARRIER -> {
            connection = getSsidAndFrequency(context)
            drawableRes = getWifiCarrier(mccmnc).drawableRes
            carrier = getSpeedDescriptionAndOperator(context)
        }

        else -> return null
    }

    return Widget(connection, drawableRes, carrier)
}

private fun Event.getWifiDetails(context: Context) =
    context.getString(R.string.wifi_details, getSsidAndFrequency(context))

private fun Event.getSsidAndFrequency(context: Context) =
    context.getString(R.string.wifi, ssid, frequency)

private fun Event.getCarrierDetails(context: Context): String =
    context.getString(
        R.string.carrier_details,
        getCarrierName(context, mccmnc),
        getSpeedDescriptionAndOperator(context)
    )

private fun Event.getSpeedDescriptionAndOperator(context: Context): String {
    val speed = getSpeedDescription(speed)

    return operator?.let {
        context.getString(R.string.speed_operator, speed, getOperator(it))
    } ?: context.getString(R.string.speed, speed)
}