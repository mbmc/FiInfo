package com.mbmc.fiinfo.util;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtil {

    public static String getDate(long ms, String timeZone) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                DateFormat.MEDIUM, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateFormat.format(ms);
    }

}
