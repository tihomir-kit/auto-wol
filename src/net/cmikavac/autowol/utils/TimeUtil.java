package net.cmikavac.autowol.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;

public class TimeUtil {
    /**
     * Gets a timestamp in milliseconds from hour and minute of day. 
     * @param hour      Hour of day.
     * @param minute    Minute of day.
     * @return          Timestamp in milliseconds.
     */
    public static Long getTimeInMilliseconds(int hour, int minute) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        return time.getTimeInMillis(); 
    }

    /**
     * Formats time in milliseconds based on users AndroidOS preferences (AM/PM vs 24-hour).
     * @param milliSeconds      Timestamp in milliseconds.
     * @param context           Context entity.
     * @return                  Human-readable formatted time string.
     */
    public static String getFormatedTime(Long milliSeconds, Context context) {
        DateFormat formatter = getFormatter(context);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliSeconds);
        return formatter.format(time.getTime());
    }

    /**
     * Gets android formatter based on current Context and users AndroidOS preferences (AM/PM vs 24-hour).
     * @param context       Context entity.
     * @return              DateFormat entity with formatting set.
     */
    private static DateFormat getFormatter(Context context) {
        String format = android.text.format.DateFormat.is24HourFormat(context) ? "HH:mm" : "hh:mmaa";
        return new SimpleDateFormat(format, Locale.getDefault());
    }

    /**
     * Gets hour of day from milliseconds.
     * @param milliSeconds      Timestamp in milliseconds.
     * @return                  Hour of day.
     */
    public static int getHourFromMilliseconds(Long milliSeconds) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliSeconds);
        return time.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Gets minutes value from milliseconds.
     * @param milliSeconds      Timestamp in milliseconds.
     * @return                  Minutes.
     */
    public static int getMinuteFromMilliseconds(Long milliSeconds) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliSeconds);
        return time.get(Calendar.MINUTE);
    }

    /**
     * Checks if time now is between quiet hours "from" and "to" values. Creates 3 new 
     * new timestamps and sets hours and minutes for quiet hours "from" and "to" 
     * and then compares the timestamps in milliseconds.
     * @param quietFrom     Quiet hours from timestamp in milliseconds.
     * @param quietTo       Quiet hours to timestamp in milliseconds.
     * @return              Is time now between quiet hours "from" and "to" values?
     */
    public static Boolean isNowBetweenQuietHours(Long quietFrom, Long quietTo) {
        Calendar timeNow = Calendar.getInstance();

        Calendar timeFrom = Calendar.getInstance();
        timeFrom.set(Calendar.HOUR_OF_DAY, getHourFromMilliseconds(quietFrom));
        timeFrom.set(Calendar.MINUTE, getMinuteFromMilliseconds(quietFrom));

        Calendar timeTo = Calendar.getInstance();
        timeTo.set(Calendar.HOUR_OF_DAY, getHourFromMilliseconds(quietTo));
        timeTo.set(Calendar.MINUTE, getMinuteFromMilliseconds(quietTo));

        if (timeTo.before(timeFrom)) {
            timeTo.add(Calendar.DATE, 1);
        }

        return timeNow.after(timeFrom) && timeNow.before(timeTo) ? true : false;
    }

    /**
     * Checks if idleTime amount of minutes has passed since last network disconnection.
     * @param idleTime              Number of minutes to check if they passed since last DC.
     * @param lastDisconnected      Last time disconnected timestamp in milliseconds.
     * @return                      Has idle time passed?
     */
    public static Boolean hasIdleTimePassed(Integer idleTime, Long lastDisconnected) {
        if (lastDisconnected.equals(null))
            return true;

        Calendar timeIdle = Calendar.getInstance();
        timeIdle.add(Calendar.MINUTE, -1 * idleTime);

        Calendar timeDisconnected = Calendar.getInstance();
        timeDisconnected.setTimeInMillis(lastDisconnected);

        return timeDisconnected.before(timeIdle);
    }
}
