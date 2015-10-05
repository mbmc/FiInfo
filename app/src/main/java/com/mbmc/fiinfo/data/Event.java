package com.mbmc.fiinfo.data;

import com.mbmc.fiinfo.R;


public enum Event {

    NONE(R.string.event_unknown),
    BOOT(R.string.event_boot),
    DISCONNECT(R.string.event_disconnect),
    MOBILE(R.string.event_mobile),
    SHUTDOWN(R.string.event_shutdown),
    WIFI(R.string.event_wifi);

    public int stringId;


    Event(int stringId) {
        this.stringId = stringId;
    }

    public static Event get(int type) {
        switch (type){
            case 1: return BOOT;
            case 2: return DISCONNECT;
            case 3: return MOBILE;
            case 4: return SHUTDOWN;
            case 5: return WIFI;
        }
        return NONE;
    }

}