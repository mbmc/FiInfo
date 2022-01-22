package com.mbmc.fiinfo.helper

import com.mbmc.fiinfo.data.Event
import android.appwidget.AppWidgetManager
import android.content.Context

import android.content.Intent
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.widget.WidgetProvider
import com.mbmc.fiinfo.util.EVENT_PARCEL
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WidgetManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun update(event: Event) {
        val intent = Intent(context, WidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(EVENT_PARCEL, event)
        val ids = intArrayOf(R.layout.widget)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}