package com.mbmc.fiinfo;

import android.content.Intent;

import com.mbmc.fiinfo.service.TelephonyService;


public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, TelephonyService.class));
    }

}
