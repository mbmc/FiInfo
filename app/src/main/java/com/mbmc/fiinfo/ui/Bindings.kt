package com.mbmc.fiinfo.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mbmc.fiinfo.data.*
import com.mbmc.fiinfo.util.*

@BindingAdapter("android:icon")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
    imageView.setIconTint()
}

@BindingAdapter("android:icon")
fun setImageViewResource(imageView: ImageView, record: Record) {
    setImageViewResource(
        imageView,
        when (record.type) {
            Type.CARRIER -> getCarrier(record.mccmnc).drawableRes
            Type.WIFI_CARRIER -> getWifiCarrier(record.mccmnc).drawableRes
            else -> record.type.drawableRes
        }
    )
}

@BindingAdapter("android:date")
fun setDate(textView: TextView, event: Event) {
    // TODO: check why there's an NPE, sometimes
    if (textView != null && event != null) {
        textView.text = getDate(event.timestamp.toLong(), event.timezone)
    }
}

@BindingAdapter("android:details")
fun setDetails(textView: TextView, record: Record) {
    textView.text =
        when (record.type) {
            Type.CARRIER -> getSpeedDescription(record.speed)
            Type.WIFI -> record.ssid
            Type.WIFI_CARRIER -> record.getWifiCarrierDetails()
            else -> null
        }
}