package com.mbmc.fiinfo.data;

import com.mbmc.fiinfo.R;


public enum Filter {

    ALL(R.string.filter_all, ""),
    CONNECTIVITY(R.string.filter_connectivity,
            "type = " + Event.MOBILE.ordinal() + " or type = " + Event.WIFI.ordinal());

    public int stringId;
    public String selection;


    Filter(int stringId, String selection) {
        this.stringId = stringId;
        this.selection = selection;
    }

}
