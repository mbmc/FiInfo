package com.mbmc.fiinfo.data;

import android.content.Context;

import com.mbmc.fiinfo.R;

import java.util.Arrays;
import java.util.List;

public enum MobileCarrier {

    SPRINT (R.string.carrier_sprint, "3102", "310120", "312530", "316010",
            "312190, 311880", "311870", "311490"),
    T_MOBILE(R.string.carrier_t_mobile, "31026", "31031", "310160", "310200", "310210",
            "310220", "310230", "310240", "310250", "310260", "310270", "310310", "310490",
            "310800", "310660", "310300", "310280", "310330"),
    THREE_UK(R.string.carrier_three_uk, "23420", "23454", "27205", "23205"),
    US_CELLULAR(R.string.carrier_us_cellular, "310066", "310730", "311220", "311580");

    public int labelId;
    public List<String> mccmnc;

    MobileCarrier(int labelId, String... mccmnc) {
        this.labelId = labelId;
        this.mccmnc = Arrays.asList(mccmnc);
    }

    public static String getName(Context context, String sim, String simName) {
        for (MobileCarrier mobileCarrier : MobileCarrier.values()) {
            if (mobileCarrier.mccmnc.contains(sim)) {
                return context.getString(mobileCarrier.labelId);
            }
        }
        return simName;
    }
}
