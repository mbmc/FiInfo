package com.mbmc.fiinfo.helper;

import android.content.ContentValues;
import android.content.Context;

import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.provider.EventProvider;

import java.util.Locale;
import java.util.TimeZone;


public class EventManager {

    private static EventManager instance;


    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void log(Context context, Event event) {
        log(context, event.ordinal(), "", "");
    }

    public void log(Context context, ConnectivityEvent connectivityEvent) {
        log(context, connectivityEvent.event.ordinal(),
                connectivityEvent.name, connectivityEvent.speed);
    }


    private EventManager() {

    }

    private void log(Context context, int type, String name, String speed) {
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_TYPE, type);
        values.put(Database.COLUMN_DATE, System.currentTimeMillis()/1000);
        values.put(Database.COLUMN_TIME_ZONE, TimeZone.getDefault().getID());
        values.put(Database.COLUMN_COUNTRY, Locale.getDefault().getDisplayName());
        values.put(Database.COLUMN_NAME, name);
        values.put(Database.COLUMN_SPEED, speed);
        context.getContentResolver().insert(EventProvider.URI, values);
    }

}
