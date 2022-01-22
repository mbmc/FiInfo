package com.mbmc.fiinfo.helper

import com.mbmc.fiinfo.data.Event
import javax.inject.Inject

class BroadcastManager @Inject constructor(
    private val notificationManager: NotificationManager,
    private val widgetManager: WidgetManager
) {

    fun update(event: Event) {
        notificationManager.update(event)
        widgetManager.update(event)
    }
}