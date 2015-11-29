package com.mbmc.fiinfo.helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.ui.activity.MainActivity;
import com.mbmc.fiinfo.util.StringUtil;


public class NotificationManager {

    private static final int ID = 0;


    public static void show(Context context, ConnectivityEvent connectivityEvent) {
        // Notifications disabled, nothing to do
        if (!PreferencesManager.getInstance(context).getBoolean(Preferences.NOTIFICATION_ENABLE)) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(connectivityEvent.event.iconId);
        int defaults = 0;
        if (PreferencesManager.getInstance(context).getBoolean(Preferences.NOTIFICATION_SOUND)) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (PreferencesManager.getInstance(context).getBoolean(Preferences.NOTIFICATION_VIBRATE)) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        builder.setDefaults(defaults);
        builder.setAutoCancel(true);
        builder.setContentTitle(context.getString(R.string.app_name));

        String string = StringUtil.getConnectionName(context, connectivityEvent);
        if (connectivityEvent.event == Event.DISCONNECT) {
            builder.setSmallIcon(Event.DISCONNECT.iconId);
        } else if (connectivityEvent.event == Event.MOBILE) {
            builder.setSmallIcon(Event.getMobileIcon(connectivityEvent.name));
        } else if (connectivityEvent.event == Event.WIFI_MOBILE) {
            string += "\n" + context.getString(R.string.state_mobile, connectivityEvent.mobile,
                    connectivityEvent.speed);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(string));
            builder.setSmallIcon(Event.getWifiMobileIcon(connectivityEvent.mobile));
        }
        builder.setContentText(string);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, builder.build());
    }

    public static void cancel(Context context) {
        ((android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ID);
    }

}
