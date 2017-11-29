package com.mbmc.fiinfo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.event.Listener;
import com.mbmc.fiinfo.provider.EventProvider;
import com.mbmc.fiinfo.util.ConnectivityUtil;

import java.util.Locale;
import java.util.TimeZone;

import de.greenrobot.event.EventBus;


public class EventManager {

    private static final Listener.Connectivity connectivity = new Listener.Connectivity();

    private static EventManager instance;

    private int type;
    private Integer previousType = null;
    private String name, previousName = "", mobile, previousMobile = "", speed, previousSpeed = "";


    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void log(Context context, Event event) {
        log(context, new ConnectivityEvent(event));
    }

    public synchronized void log(Context context, ConnectivityEvent connectivityEvent) {
        if (previousType == null) {
            previousType = Event.NONE.ordinal();
            Cursor cursor = context.getContentResolver().query(EventProvider.URI_LAST,
                    null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToPosition(0);
                previousType = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_TYPE));
                previousName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME));
                previousMobile =
                        cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_MOBILE));
                previousSpeed =
                        cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED));
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        type = connectivityEvent.event.ordinal();
        name = connectivityEvent.name;
        mobile = connectivityEvent.mobile;
        speed = connectivityEvent.speed;
        if (type == previousType
                && name.equals(previousName)
                && mobile.equals(previousMobile)
                && speed.equals(previousSpeed)) {
            return;
        }

        sendUpdate(context, connectivityEvent);

        log(context, type, name, mobile, speed);

        previousType = type;
        previousName = name;
        previousMobile = mobile;
        previousSpeed = speed;
    }


    private EventManager() {

    }

    private void log(Context context, int type, String name, String mobile, String speed) {
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_TYPE, type);
        values.put(Database.COLUMN_DATE, String.valueOf(System.currentTimeMillis()));
        values.put(Database.COLUMN_TIME_ZONE, TimeZone.getDefault().getID());
        values.put(Database.COLUMN_COUNTRY, Locale.getDefault().getDisplayName());
        values.put(Database.COLUMN_NAME, name);
        values.put(Database.COLUMN_MOBILE, mobile);
        values.put(Database.COLUMN_SPEED, speed);
        context.getContentResolver().insert(EventProvider.URI, values);
    }

    private void sendUpdate(Context context, ConnectivityEvent connectivityEvent) {
        Event event = connectivityEvent.event;
        boolean subscribers = false;
        if (EventBus.getDefault().hasSubscriberForEvent(Listener.Connectivity.class)) {
            EventBus.getDefault().post(connectivity);
            subscribers = true;
        }

        // Update Widget and Notification only if WiFi, Mobile or Disconnect event
        if (event == Event.AIRPLANE_ON || event == Event.WIFI_OFF || event == Event.MOBILE_OFF) {
            ConnectivityEvent disconnectedEvent = ConnectivityUtil.getConnectivityEvent(context);
            if (disconnectedEvent.event == Event.DISCONNECT) {
                connectivityEvent = disconnectedEvent;
                event = connectivityEvent.event;
            }
        }

        if (event == Event.DISCONNECT || event == Event.MOBILE || event == Event.WIFI
                || event == Event.WIFI_MOBILE) {
            WidgetManager.update(context, connectivityEvent);
            if (!subscribers) {
                NotificationManager.show(context, connectivityEvent);
            }
        }
    }

}
