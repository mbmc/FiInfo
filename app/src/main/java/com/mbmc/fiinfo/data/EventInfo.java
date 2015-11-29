package com.mbmc.fiinfo.data;

import android.database.Cursor;

import com.mbmc.fiinfo.constant.Constants;
import com.mbmc.fiinfo.helper.Database;


public class EventInfo {

    public int iconId;
    public String info;


    public static EventInfo get(Event event, Cursor cursor) {
        EventInfo eventInfo = new EventInfo();

        int iconId = event.iconId;
        String info = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME));

        switch (event) {
            case MOBILE:
                iconId = Event.getMobileIcon(info);
                info = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED));
                break;

            case WIFI_MOBILE:
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_MOBILE));
                iconId = Event.getWifiMobileIcon(mobile);
                if (mobile.contains(Constants.SPRINT ) || mobile.contains(Constants.T_MOBILE)) {
                    info = info + " / " + cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED));
                } else {
                    info = info + " / " + mobile + " [" + cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED)) + "]";
                }
                break;
        }
        eventInfo.iconId = iconId;
        eventInfo.info = info;
        return eventInfo;
    }

}
