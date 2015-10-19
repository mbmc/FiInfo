package com.mbmc.fiinfo.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateUtil {

    private static final int ONE_SECOND = 1;


    public static String getDate(long ms, String timeZone) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                DateFormat.MEDIUM, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateFormat.format(ms);
    }

    public static boolean isLessThanOneSecond(long ms) {
        return ONE_SECOND <=
                TimeUnit.MILLISECONDS.convert(new Date(System.currentTimeMillis()).getTime()
                        - new Date(ms).getTime(),
                        TimeUnit.SECONDS);
    }

}
