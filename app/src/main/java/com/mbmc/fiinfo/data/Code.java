package com.mbmc.fiinfo.data;

import com.mbmc.fiinfo.R;

public enum Code {

    NONE(0, 0),
    AUTO(342886, R.string.carrier_auto),
    INFO(344636, 0),
    NEXT(346398, R.string.carrier_next),
    QR(359948434, 0),
    REPAIR(34963, R.string.carrier_repair),
    SPRINT(34777, R.string.carrier_sprint),
    T_MOBILE(34866, R.string.carrier_t_mobile),
    THREE_UK(3474666, R.string.carrier_three_uk),
    US_CELLULAR(34326, R.string.carrier_us_cellular); // 34872

    public int code;
    public int labelId;

    Code(int code, int labelId) {
        this.code = code;
        this.labelId = labelId;
    }

    public static Code get(String name) {
        return valueOf(name);
    }
}
