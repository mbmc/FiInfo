package com.mbmc.fiinfo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.ServiceState;

import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.helper.EventManager;
import com.mbmc.fiinfo.util.ConnectivityUtil;


public class ServiceChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        int dataState = bundle.getInt("dataRegState");
        int voiceState = bundle.getInt("voiceRegState");
        int type = bundle.getInt("dataRadioTechnology");

        if (dataState == ServiceState.STATE_POWER_OFF && voiceState == ServiceState.STATE_POWER_OFF) {
            EventManager.getInstance().log(context, Event.MOBILE_OFF);
        } else if (dataState == ServiceState.STATE_IN_SERVICE && voiceState == ServiceState.STATE_IN_SERVICE) {
            ConnectivityEvent connectivityEvent;
            String mobile = ConnectivityUtil.getMobileName(context);
            String speed = ConnectivityUtil.getSpeedFromService(type);
            if (ConnectivityUtil.isConnectedToWifi(context)) {
                String wifi = ConnectivityUtil.getWifiName(context);
                connectivityEvent = new ConnectivityEvent(Event.WIFI_MOBILE, wifi, mobile, speed);
            } else {
                connectivityEvent = new ConnectivityEvent(Event.MOBILE, mobile, speed);
            }
            EventManager.getInstance().log(context, connectivityEvent);
        }
    }

}
