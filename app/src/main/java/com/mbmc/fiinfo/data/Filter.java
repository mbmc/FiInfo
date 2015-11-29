package com.mbmc.fiinfo.data;

import com.mbmc.fiinfo.R;


public enum Filter {

    ALL(R.string.filter_all, ""),
    CONNECTIVITY(R.string.filter_connectivity,
            "type = " + Event.MOBILE.ordinal() + " OR type = " + Event.WIFI.ordinal()
                    + " OR type = " + Event.WIFI_MOBILE.ordinal()),
    WIFI(R.string.wifi, "type = " + Event.WIFI.ordinal()
            + " OR type = " + Event.WIFI_MOBILE.ordinal()),
    SPRINT(R.string.carrier_sprint, "name LIKE '%Sprint%'"
            + " OR mobile LIKE '%Sprint%'"),
    T_MOBILE(R.string.carrier_t_mobile, "name LIKE '%T-Mobile%'"
            + " OR mobile LIKE '%T-Mobile%'");

    public int stringId;
    public String selection;


    Filter(int stringId, String selection) {
        this.stringId = stringId;
        this.selection = selection;
    }

}
