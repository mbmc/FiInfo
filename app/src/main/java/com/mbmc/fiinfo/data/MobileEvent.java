package com.mbmc.fiinfo.data;

import android.content.Context;

import com.mbmc.fiinfo.R;


public enum MobileEvent {

    SPRINT(R.string.event_mobile_sprint, R.drawable.ic_sprint, R.string.carrier_sprint),
    T_MOBILE(R.string.event_mobile_t_mobile, R.drawable.ic_t_mobile, R.string.carrier_t_mobile),
    THREE_UK(R.string.event_mobile_three_uk, R.drawable.ic_three_uk, R.string.carrier_three_uk),
    US_CELLULAR(R.string.event_mobile_us_cellular, R.drawable.ic_us_cellular, R.string.carrier_us_cellular);

    public int labelId, iconId, nameId;


    MobileEvent(int labelId, int iconId, int nameId) {
        this.labelId = labelId;
        this.iconId = iconId;
        this.nameId = nameId;
    }

    public static int getIcon(Context context, String string) {
        for (MobileEvent mobileEvent : MobileEvent.values()) {
            if (string.contains(context.getString(mobileEvent.nameId))) {
                return mobileEvent.iconId;
            }
        }
        return R.drawable.ic_mobile;
    }

}
