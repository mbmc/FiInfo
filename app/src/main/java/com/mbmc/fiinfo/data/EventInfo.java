package com.mbmc.fiinfo.data;

import android.content.Context;
import android.database.Cursor;

import com.mbmc.fiinfo.helper.Database;

public class EventInfo {

    public int iconId;
    public String info;

    public static EventInfo get(Context context, Event event, Cursor cursor) {
        EventInfo eventInfo = new EventInfo();

        int iconId = event.iconId;
        String info = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_NAME));

        switch (event) {
            case MOBILE:
                iconId = MobileEvent.getIcon(context, info);
                info = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED));
                break;

            case WIFI_MOBILE:
                String mobile =
                        cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_MOBILE));
                iconId = WiFiMobileEvent.getIcon(context, mobile);
                info = WiFiMobileEvent.getInfo(context, mobile, info,
                        cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SPEED)));
                break;
        }
        eventInfo.iconId = iconId;
        eventInfo.info = info;
        return eventInfo;
    }
}
