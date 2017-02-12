package com.app.infideap.postingapp.util;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.app.infideap.notificationapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shiburagi on 28/01/2017.
 */
public class Common {

    public static Date parseDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String getUserFriendlyDurationString(Context context, Calendar calendar) {

        Calendar currentCalendar = Calendar.getInstance();
//        currentCalendar.set(Calendar.MILLISECOND, 0);
//        currentCalendar.set(Calendar.SECOND, 0);
//        currentCalendar.set(Calendar.MINUTE, 0);
//        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);

        String displayTime;
        long diff = currentCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        if (TimeUnit.MILLISECONDS.toDays(diff) == 0) {
            if (TimeUnit.MILLISECONDS.toHours(diff) == 0) {
                if (TimeUnit.MILLISECONDS.toMinutes(diff) == 0) {
                    if (TimeUnit.MILLISECONDS.toSeconds(diff) <= 1)
                        displayTime = context.getResources().getString(R.string.asecondago);
                    else
                        displayTime = String.format(Locale.getDefault(),
                                context.getResources().getString(R.string.secondsago),
                                TimeUnit.MILLISECONDS.toSeconds(diff));
                } else if (TimeUnit.MILLISECONDS.toMinutes(diff) == 1) {
                    displayTime = context.getResources().getString(R.string.aminuteago);
                } else
                    displayTime = String.format(Locale.getDefault(),
                            context.getResources().getString(R.string.minutesago),
                            TimeUnit.MILLISECONDS.toMinutes(diff));
            } else if (TimeUnit.MILLISECONDS.toHours(diff) == 1) {
                displayTime = context.getResources().getString(R.string.anhourago);
            } else
                displayTime = String.format(Locale.getDefault(),
                        context.getResources().getString(R.string.hoursago),
                        TimeUnit.MILLISECONDS.toHours(diff));
        } else if (TimeUnit.MILLISECONDS.toDays(diff) == 1) {
            displayTime = context.getResources().getString(R.string.yesterday);
        } else
            displayTime = String.format(Locale.getDefault(),
                    context.getResources().getString(R.string.daysago),
                    TimeUnit.MILLISECONDS.toDays(diff));
        return displayTime;
    }
    public static String getDateTimeString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        return getDateTimeString(calendar);
    }

    public static String getDateTimeString(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return getDateTimeString(calendar);
    }

    public static String getDateTimeString(TimePicker timePicker) {

        int hour;
        int minute;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, minute);

        return getDateTimeString(calendar);
    }

    public static String getDateTimeString(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return getDateTimeString(calendar);
    }


    public static String getDateTimeString(Calendar calendar) {

        return getDateTimeString(calendar.getTime());
    }

    public static String getDateTimeString(Date calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        return simpleDateFormat.format(calendar);
    }
}
