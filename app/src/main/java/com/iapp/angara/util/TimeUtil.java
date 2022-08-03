package com.iapp.angara.util;

import android.content.Context;
import android.text.format.DateFormat;

import com.iapp.angara.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// TODO
public class TimeUtil {

    public static String defineTimeView(Context context, long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(new Date(time));
        calendar.setTimeZone(TimeZone.getDefault());

        long passed = new Date().getTime() - calendar.getTime().getTime();

        String res;
        if (TimeUtil.getSeconds(passed) < 60) {
            res = context.getString(R.string.less_minute);
        } else if (TimeUtil.getMinutes(passed) < 60) {
            res = DateFormat.format("mm ", new Date(passed)) + context.getString(R.string.minutes_ago);
        } else if (TimeUtil.getHours(passed) < 24){
            res = String.valueOf(DateFormat.format("HH:mm", new Date(time)));
        } else if (TimeUtil.getDays(passed) < 365) {
            res = String.valueOf(DateFormat.format("dd MMMM", new Date(time)));
        } else {
            res = String.valueOf(DateFormat.format("dd MMMM yyyy", new Date(time)));
        }
        res = res.replaceAll("^0", "");

        return res;
    }

    public static int getSeconds(long time) {
        return Math.round(time / 1000);
    }

    public static int getMinutes(long time) {
        return Math.round(getSeconds(time) / 60);
    }

    public static int getHours(long time) {
        return Math.round(getMinutes(time) / 60);
    }

    public static int getDays(long time) {
        return Math.round(getHours(time) / 24);
    }

    public static int getYears(long time) {
        return Math.round(getDays(time) / 365);
    }
}
