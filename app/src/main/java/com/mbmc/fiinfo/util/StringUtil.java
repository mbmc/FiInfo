package com.mbmc.fiinfo.util;

import android.content.Context;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.ConnectivityEvent;


public class StringUtil {

    public static String getConnectionName(Context context, ConnectivityEvent connectivityEvent) {
        String string = "";
        switch (connectivityEvent.event) {
            case AIRPLANE_ON:
            case DISCONNECT:
                string = context.getString(R.string.state_disconnected);
                break;

            case MOBILE:
                string = context.getString(R.string.state_connected, connectivityEvent.name, connectivityEvent.speed);
                break;

            case WIFI:
            case WIFI_MOBILE:
                string = context.getString(R.string.state_connected, connectivityEvent.name,
                        context.getString(R.string.wifi));
                break;
        }
        return string;
    }

}
