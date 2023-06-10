package com.mbmc.fiinfo.util

import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.ui.MainActivity

@Suppress("DEPRECATION")
fun <T> Context.isServiceForegrounded(service: Class<T>): Boolean =
    (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
        ?.getRunningServices(Integer.MAX_VALUE)
        ?.find { it.service.className == service.name }
        ?.foreground == true

fun Context.isLocationServiceEnabled(): Boolean {
    return (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        .isLocationEnabled
}

fun Fragment.openBrowserIfInstalled(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val resolve = intent.resolveActivity(requireContext().packageManager)
    resolve?.let {
        startActivity(intent)
    } ?: run {
        Toast.makeText(requireContext(), R.string.no_browser, Toast.LENGTH_SHORT).show()
    }
}

fun Context.getPendingIntent(): PendingIntent =
    Intent(this, MainActivity::class.java).let { notificationIntent ->
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

fun Intent.settings(context: Context): Intent =
    apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", context.packageName, null)
    }

fun ImageView.setIconTint() {
    setColorFilter(MaterialColors.getColor(this, R.attr.iconColor))
}