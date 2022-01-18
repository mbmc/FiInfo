package com.mbmc.fiinfo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.helper.EventManager;

public class WifiChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getExtras().getInt(WifiManager.EXTRA_WIFI_STATE);
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            EventManager.getInstance().log(context, Event.WIFI_ON);
        } else if (state == WifiManager.WIFI_STATE_DISABLED) {
            EventManager.getInstance().log(context, Event.WIFI_OFF);
        }
    }
}
