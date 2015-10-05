package com.mbmc.fiinfo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.mbmc.fiinfo.event.TelephonyStateListener;


public class TelephonyService extends Service {

    private TelephonyStateListener telephonyStateListener;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setupListener();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(telephonyStateListener, PhoneStateListener.LISTEN_NONE);
        }
        telephonyStateListener = null;
        super.onDestroy();
    }

    private void setupListener() {
        if (telephonyStateListener != null) {
            return;
        }

        telephonyStateListener = new TelephonyStateListener(this);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            stopSelf();
            return;
        }

        telephonyManager.listen(telephonyStateListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }

}
