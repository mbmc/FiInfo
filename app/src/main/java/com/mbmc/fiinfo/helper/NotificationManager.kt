package com.mbmc.fiinfo.helper

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.util.getPendingIntent
import com.mbmc.fiinfo.util.toNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

import android.app.NotificationManager as AndroidNotificationManager

@Singleton
class NotificationManager @Inject constructor(@ApplicationContext private val context: Context) {

    private lateinit var builder: Notification.Builder
    private lateinit var notificationManager: AndroidNotificationManager

    fun provide(): Notification {
        builder = Notification.Builder(context, createChannel())
            .setContentTitle(context.getString(R.string.connection_status))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(
                Notification.BigTextStyle()
                    .bigText(context.getString(R.string.waiting))
            )
            .setContentIntent(context.getPendingIntent())

        return builder.build()
    }

    fun update(event: Event) {
        event.toNotification(context)?.let {
            builder.style = Notification.BigTextStyle().bigText(it.content)
            builder.setSmallIcon(it.drawableRes)
            notificationManager.notify(ID, builder.build())
        }
    }

    private fun createChannel(): String {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.connection_status),
            AndroidNotificationManager.IMPORTANCE_NONE
        )
        channel.description = context.getString(R.string.connection_status_description)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
        notificationManager.createNotificationChannel(channel)
        return CHANNEL_ID
    }

    companion object {
        private const val CHANNEL_ID = "1000"
        const val ID = 1000
    }
}