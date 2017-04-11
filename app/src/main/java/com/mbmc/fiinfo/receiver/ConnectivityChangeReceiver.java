package com.mbmc.fiinfo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.helper.EventManager;
import com.mbmc.fiinfo.util.ConnectivityUtil;


public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private static Event previousEvent = Event.NONE;


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityEvent connectivityEvent = ConnectivityUtil.getConnectivityEvent(context);
        Event event = connectivityEvent.event;
        if (event != previousEvent && event != Event.DISCONNECT) {
            EventManager.getInstance().log(context, connectivityEvent);
        }
        previousEvent = event;
    }

}
