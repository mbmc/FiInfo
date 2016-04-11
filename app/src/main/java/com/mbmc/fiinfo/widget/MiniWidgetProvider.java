package com.mbmc.fiinfo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.helper.WidgetManager;
import com.mbmc.fiinfo.ui.activity.MainActivity;
import com.mbmc.fiinfo.util.ConnectivityUtil;

import org.parceler.Parcels;


public class MiniWidgetProvider extends AppWidgetProvider {

    private ConnectivityEvent connectivityEvent;


    @Override
    public void onReceive(Context context, Intent intent) {
        connectivityEvent = Parcels.unwrap(intent.getParcelableExtra(WidgetManager.EVENT));
        super.onReceive(context, intent); // Has to be called after
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        if (connectivityEvent == null) {
            connectivityEvent = ConnectivityUtil.getConnectivityEvent(context);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_1x1);
        remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        WidgetManager.update(context, this.getClass(), remoteViews, connectivityEvent);
    }

}
