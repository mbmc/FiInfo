package com.mbmc.fiinfo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Constants;
import com.mbmc.fiinfo.constant.Preferences;
import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.helper.CodeManager;
import com.mbmc.fiinfo.helper.PreferencesManager;
import com.mbmc.fiinfo.helper.WidgetManager;
import com.mbmc.fiinfo.ui.activity.MainActivity;
import com.mbmc.fiinfo.util.ConnectivityUtil;

import org.parceler.Parcels;


public class WidgetProvider extends AppWidgetProvider {

    private Code code1, code2, code3;
    private ConnectivityEvent connectivityEvent;


    @Override
    public void onReceive(Context context, Intent intent) {
        getCode(context);
        connectivityEvent = Parcels.unwrap(intent.getParcelableExtra(WidgetManager.EVENT));

        switch (intent.getAction()) {
            case Preferences.ACTION_1:
                CodeManager.openDialer(context, code1.code);
                break;

            case Preferences.ACTION_2:
                CodeManager.openDialer(context, code2.code);
                break;

            case Preferences.ACTION_3:
                CodeManager.openDialer(context, code3.code);
                break;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        getCode(context);
        if (connectivityEvent == null) {
            connectivityEvent = ConnectivityUtil.getConnectivityEvent(context);
        }

        // Click
        Intent intent = new Intent(context, MainActivity.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_4x1);
        remoteViews.setOnClickPendingIntent(R.id.widget_container, PendingIntent.getActivity(context, 0, intent, 0));

        // Action 1
        setAction(context, remoteViews, Preferences.ACTION_1, R.id.widget_action_1, code1);

        // Action 2
        setAction(context, remoteViews, Preferences.ACTION_2, R.id.widget_action_2, code2);

        // Action 3
        setAction(context, remoteViews, Preferences.ACTION_3, R.id.widget_action_3, code3);

        WidgetManager.update(context, this.getClass(), remoteViews, connectivityEvent);
    }


    private void getCode(Context context) {
        code1 = getCode(context, Preferences.ACTION_1, Constants.CODE_1);
        code2 = getCode(context, Preferences.ACTION_2, Constants.CODE_2);
        code3 = getCode(context, Preferences.ACTION_3, Constants.CODE_3);
    }

    private Code getCode(Context context, String preference, Code defaultCode) {
        Code code = Code.get(PreferencesManager.getInstance(context).getString(preference, defaultCode.name()));
        if (code == Code.NONE) {
            code = defaultCode;
        }
        return code;
    }

    private void setAction(Context context, RemoteViews remoteViews, String action, int viewId, Code code) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(action);
        remoteViews.setTextViewText(viewId, context.getString(code.labelId));
        remoteViews.setOnClickPendingIntent(viewId, PendingIntent.getBroadcast(context, 0, intent, 0));
    }

}
