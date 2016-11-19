package com.mbmc.fiinfo.data;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.constant.Constants;


public enum Event {

    NONE(R.string.event_unknown, 0),
    AIRPLANE_ON(R.string.event_airplane_on, R.drawable.ic_airplane_on),
    AIRPLANE_OFF(R.string.event_airplane_off, R.drawable.ic_airplane_off),
    BOOT(R.string.event_boot, R.drawable.ic_phone_on),
    DISCONNECT(R.string.event_disconnect, R.drawable.ic_disconnect),
    MOBILE(R.string.event_mobile, R.drawable.ic_mobile),
    MOBILE_OFF(R.string.event_mobile_off, R.drawable.ic_mobile_off),
    SHUTDOWN(R.string.event_shutdown, R.drawable.ic_phone_off),
    WIFI(R.string.event_wifi, R.drawable.ic_wifi),
    WIFI_MOBILE(R.string.event_wifi_mobile, R.drawable.ic_wifi_mobile),
    WIFI_ON(R.string.event_wifi_on, R.drawable.ic_wifi_on),
    WIFI_OFF(R.string.event_wifi_off, R.drawable.ic_wifi_off);

    public int labelId, iconId;


    Event(int labelId, int iconId) {
        this.labelId = labelId;
        this.iconId = iconId;
    }

    public static Event get(int type) {
        try {
            return Event.values()[type];
        } catch (Exception exception) {
            return NONE;
        }
    }

    public static int getMobileIcon(String string) {
        if (string.contains(Constants.SPRINT)) {
            return R.drawable.ic_sprint;
        } else if (string.contains(Constants.T_MOBILE)) {
            return R.drawable.ic_t_mobile;
        } else if (string.contains(Constants.US_CELLULAR)) {
            return R.drawable.ic_us_cellular;
        }
        return R.drawable.ic_mobile;
    }

    public static int getWifiMobileIcon(String string) {
        if (string.contains(Constants.SPRINT)) {
            return R.drawable.ic_wifi_sprint;
        } else if (string.contains(Constants.T_MOBILE)) {
            return R.drawable.ic_wifi_t_mobile;
        } else if (string.contains(Constants.US_CELLULAR)) {
            return R.drawable.ic_wifi_us_cellular;
        }
        return R.drawable.ic_wifi_mobile;
    }

}