package com.mbmc.fiinfo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.helper.EventManager;


public class AirplaneModeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EventManager.getInstance().log(context,
                intent.getExtras().getBoolean("state")
                        ? Event.AIRPLANE_ON
                        : Event.AIRPLANE_OFF);
    }

}
