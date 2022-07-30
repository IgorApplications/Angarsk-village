package com.iapp.angara.util;

public class TimeUtil {

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
