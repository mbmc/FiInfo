package com.mbmc.fiinfo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.event.Listener;
import com.mbmc.fiinfo.helper.EventManager;
import com.mbmc.fiinfo.helper.NotificationManager;
import com.mbmc.fiinfo.util.ConnectivityUtil;
import com.mbmc.fiinfo.util.DateUtil;

import de.greenrobot.event.EventBus;


public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private static final Listener.Connectivity connectivity = new Listener.Connectivity();

    private static Event previousEvent = Event.NONE;
    private static long timestamp = System.currentTimeMillis();


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityEvent connectivityEvent = ConnectivityUtil.getConnectivityEvent(context);
        if (connectivityEvent == null) {
            return;
        }

        if (connectivityEvent.event == previousEvent && DateUtil.isLessThanOneSecond(timestamp)) {
            // Log.d();
        } else {
            EventManager.getInstance().log(context, connectivityEvent);
        }
        previousEvent = connectivityEvent.event;
        timestamp = System.currentTimeMillis();

        Event event = connectivityEvent.event;
        if (EventBus.getDefault().hasSubscriberForEvent(Listener.Connectivity.class)) {
            EventBus.getDefault().post(connectivity);
        } else if (event == Event.MOBILE || event == Event.WIFI) {
            NotificationManager.showNotification(context, connectivityEvent);
        }
    }

}