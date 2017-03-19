package com.mbmc.fiinfo.data;

import android.content.Context;

import com.mbmc.fiinfo.R;


public enum WiFiMobileEvent {

    SPRINT(R.string.event_wifi_sprint, R.drawable.ic_wifi_sprint, R.string.carrier_sprint),
    T_MOBILE(R.string.event_wifi_t_mobile, R.drawable.ic_wifi_t_mobile, R.string.carrier_t_mobile),
    THREE_UK(R.string.event_wifi_three_uk, R.drawable.ic_wifi_three_uk, R.string.carrier_three_uk),
    US_CELLULAR(R.string.event_wifi_us_cellular, R.drawable.ic_wifi_us_cellular, R.string.carrier_us_cellular);

    public int labelId, iconId, nameId;


    WiFiMobileEvent(int labelId, int iconId, int nameId) {
        this.labelId = labelId;
        this.iconId = iconId;
        this.nameId = nameId;
    }

    public static int getIcon(Context context, String string) {
        for (WiFiMobileEvent wiFiMobileEvent : WiFiMobileEvent.values()) {
            if (string.contains(context.getString(wiFiMobileEvent.nameId))) {
                return wiFiMobileEvent.iconId;
            }
        }
        return R.drawable.ic_wifi_mobile;
    }

    public static String getInfo(Context context, String mobile, String info, String speed) {
        for (WiFiMobileEvent wiFiMobileEvent : WiFiMobileEvent.values()) {
            if (mobile.contains(context.getString(wiFiMobileEvent.nameId))) {
                return info + " / " + speed;
            }
        }
        return  info + " / " + mobile + " [" + speed + "]";
    }

}
