package com.oneone.utils;

import android.content.Context;

import com.oneone.R;
import com.oneone.framework.ui.utils.Res;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by here on 18/4/23.
 */

public class TimeUtil {
    public static final long DAY_TIME = 86400000;

    public static String secToMinute(int sec) {
        return (int) (sec / 60) + "";
    }

    public static String getNotifyListTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String monthStr = (calendar.get(Calendar.MONTH) + 1) < 10 ? ("0" + (calendar.get(Calendar.MONTH) + 1)) : ((calendar.get(Calendar.MONTH) + 1) + "");
        String dayStr = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + calendar.get(Calendar.DAY_OF_MONTH)) : (calendar.get(Calendar.DAY_OF_MONTH) + "");
        String hourStr = calendar.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + calendar.get(Calendar.HOUR_OF_DAY)) : (calendar.get(Calendar.HOUR_OF_DAY) + "");
        String minStr = calendar.get(Calendar.MINUTE) < 10 ? ("0" + calendar.get(Calendar.MINUTE)) : (calendar.get(Calendar.MINUTE) + "");
        return monthStr + "-" + dayStr + " " + hourStr + ":" + minStr;
    }

    public static String getContactTime(long time, Context context) {
        long curTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curTime);
        int todayHour = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.setTimeInMillis(time);
        String timeStr = "";
        long beforeTime = curTime - time;
        float preDay = beforeTime / DAY_TIME;
        System.out.println("pre day : " + preDay);
        if (preDay < 1) {
            int timeHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (todayHour < timeHour)
                timeStr = context.getResources().getString(R.string.str_contact_time_yesterday);
            else
                timeStr = timeHour + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));

        } else if (preDay < 2) {
            timeStr = context.getResources().getString(R.string.str_contact_time_yesterday);
        } else if (preDay < 7) {
            String[] weekArr = {
                    context.getResources().getString(R.string.str_sunday)
                    , context.getResources().getString(R.string.str_monday)
                    , context.getResources().getString(R.string.str_tuesday)
                    , context.getResources().getString(R.string.str_wednesday)
                    , context.getResources().getString(R.string.str_thursday)
                    , context.getResources().getString(R.string.str_friday)
                    , context.getResources().getString(R.string.str_saturday)
            };
            timeStr = weekArr[calendar.get(calendar.DAY_OF_WEEK) - 1];
        } else {
            timeStr = ((int) preDay) + context.getResources().getString(R.string.str_contact_time_one_week_before);
        }

        return timeStr;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    public static String getTimeIntervalOf24Hour() {
        String s = null;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (0 <= hour && hour <= 4) {
            s = Res.getString(R.string.str_time_night);
        }
        if (5 <= hour && hour <= 8) {
            s = Res.getString(R.string.str_time_morning);
        }
        if (8 <= hour && hour <= 12) {
            s = Res.getString(R.string.str_time_forenoon);
        }
        if (12 <= hour && hour <= 13) {
            s = Res.getString(R.string.str_time_noon);
        }
        if (13 <= hour && hour <= 18) {
            s = Res.getString(R.string.str_time_afternoon);
        }
        if (19 <= hour && hour <= 23) {
            s = Res.getString(R.string.str_time_night);
        }
        return s + Res.getString(R.string.str_good);
    }
}
