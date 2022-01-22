package com.mbmc.fiinfo.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.util.DEBUG_TAG
import com.mbmc.fiinfo.util.EVENT_PARCEL
import android.content.ComponentName
import com.mbmc.fiinfo.util.toWidget

import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.data.Widget
import com.mbmc.fiinfo.util.getPendingIntent

class WidgetProvider : AppWidgetProvider() {

    private var event: Event? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            event = intent?.getParcelableExtra(EVENT_PARCEL)!!
        } catch (exception: Exception) {
            Log.d(DEBUG_TAG, "Can't get event")
        }
        super.onReceive(context, intent) // Needs to be called last
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        event?.toWidget(context)?.let {
            appWidgetManager.updateAppWidget(
                ComponentName(context, WidgetProvider::class.java),
                getRemoteViews(context, it))
        }
    }

    private fun getRemoteViews(context: Context, widget: Widget) =
        RemoteViews(context.packageName, R.layout.widget).apply {
            setTextViewText(R.id.connection, widget.connection)
            setImageViewResource(R.id.icon, widget.drawableRes)
            setTextViewText(R.id.carrier, widget.carrier)
            setOnClickPendingIntent(R.id.widget_container, context.getPendingIntent())
        }
}