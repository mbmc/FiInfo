package com.mbmc.fiinfo.helper;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.widget.MiniWidgetProvider;
import com.mbmc.fiinfo.widget.WidgetProvider;

import org.parceler.Parcels;


public class WidgetManager {

    public static final String EVENT = "parcelable_event";


    public static void update(Context context) {
        update(context, null);
    }

    public static void update(Context context, ConnectivityEvent connectivityEvent) {
        // 1x1
        Intent intent = new Intent(context, MiniWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        if (connectivityEvent != null) {
            intent.putExtra(EVENT, Parcels.wrap(connectivityEvent));
        }
        int[] ids = { R.layout.layout_widget_1x1 };
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);

        // 4x1
        intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        if (connectivityEvent != null) {
            intent.putExtra(EVENT, Parcels.wrap(connectivityEvent));
        }
        ids = new int[] { R.layout.layout_widget_1x1 };
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    public static void update(Context context, Class widget, RemoteViews remoteViews, ConnectivityEvent connectivityEvent) {
        ComponentName componentName = new ComponentName(context, widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String name = connectivityEvent.name;
        if (TextUtils.isEmpty(name)) {
            name = context.getString(connectivityEvent.event.labelId);
        }
        remoteViews.setTextViewText(R.id.widget_connected, name);

        int iconId = connectivityEvent.event.iconId;
        switch (connectivityEvent.event) {
            case MOBILE:
                iconId = Event.getMobileIcon(name);
                break;

            case WIFI_MOBILE:
                iconId = Event.getWifiMobileIcon(connectivityEvent.mobile);
                break;
        }
        remoteViews.setImageViewResource(R.id.widget_icon, iconId);
        remoteViews.setTextViewText(R.id.widget_mobile, connectivityEvent.speed);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

}
