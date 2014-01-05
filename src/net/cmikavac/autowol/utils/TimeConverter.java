package net.cmikavac.autowol.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;

public class TimeConverter {
    public static Long getTimeInMilliseconds(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis(); 
    }

    public static String getFormatedTime(Long milliSeconds, Context context) {
        DateFormat formatter = getFormatter(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    
    private static DateFormat getFormatter(Context context) {
        String format = android.text.format.DateFormat.is24HourFormat(context) ? "HH:mm" : "hh:mm aa";
        return new SimpleDateFormat(format, Locale.getDefault());
    }

    public static int getHourFromMilliseconds(Long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteFromMilliseconds(Long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.get(Calendar.MINUTE);
    }
}