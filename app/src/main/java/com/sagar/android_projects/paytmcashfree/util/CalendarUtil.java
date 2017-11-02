package com.sagar.android_projects.paytmcashfree.util;

import java.util.Calendar;

/**
 * Created by SAGAR on 11/2/2017.
 */

public class CalendarUtil {
    public static String getToday() {
        return String.valueOf(
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        + "/"
                        + (Calendar.getInstance().get(Calendar.MONTH) + 1)
                        + "/"
                        + Calendar.getInstance().get(Calendar.YEAR)
        );
    }
}
