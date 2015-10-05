package com.mbmc.fiinfo.event;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.helper.EventManager;
import com.mbmc.fiinfo.helper.NotificationManager;
import com.mbmc.fiinfo.util.ConnectivityUtil;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;


public class TelephonyStateListener extends PhoneStateListener {

    private static final Listener.Connectivity connectivity = new Listener.Connectivity();

    private WeakReference<Context> context;
    private int previousState = TelephonyManager.DATA_DISCONNECTED;
    private int previousType = TelephonyManager.NETWORK_TYPE_UNKNOWN;


    public TelephonyStateListener(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
        super.onDataConnectionStateChanged(state, networkType);

        // Notify speed change
        if (previousType != networkType) {
            EventBus.getDefault().post(connectivity);
            // Log speed change if connected
            if (state == TelephonyManager.DATA_CONNECTED
                    && previousState == TelephonyManager.DATA_CONNECTED
                    && networkType != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                ConnectivityEvent connectivityEvent = new ConnectivityEvent(Event.MOBILE,
                        ConnectivityUtil.getMobileName(context.get()),
                        ConnectivityUtil.getSpeed(networkType));

                EventManager.getInstance().log(context.get(), connectivityEvent);
                if (!EventBus.getDefault().hasSubscriberForEvent(Listener.Connectivity.class)) {
                    NotificationManager.showNotification(context.get(), connectivityEvent);
                }
            }
        }

        previousState = state;
        previousType = networkType;
    }

}
