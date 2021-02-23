package com.example.final_project.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class MyStrings {

    public static final String TWO_POINT_FORMAT = "##.##";
    public static final String THREE_POINT_FORMAT = "##.###";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String ZERO = "0";
    public static final int MILLISECONDS_IN_SECOND = 1000;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int ONE_DIGIT = 10;

    public static String twoDigitsAfterPoint(double num) {
        DecimalFormat decimalFormat = new DecimalFormat(TWO_POINT_FORMAT);

        return decimalFormat.format(num);
    }

    public static String threeDigitsAfterPoint(double num) {
        DecimalFormat decimalFormat = new DecimalFormat(THREE_POINT_FORMAT);

        return decimalFormat.format(num);
    }

    public static String makeDateString(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        return simpleDateFormat.format(date);
    }

    public static String makeTimeString(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);

        return simpleDateFormat.format(time);
    }

    public static String makeDurationString(long duration) {
        StringBuilder sb = new StringBuilder();
        long seconds = duration / MILLISECONDS_IN_SECOND;
        long minutes = seconds / SECONDS_IN_MINUTE;
        long hours = minutes / MINUTES_IN_HOUR;

        seconds %= SECONDS_IN_MINUTE;
        minutes %= MINUTES_IN_HOUR;
        hours %= HOURS_IN_DAY;

        sb.append(hours < ONE_DIGIT ? ZERO : "").append(hours).append(":");
        sb.append(minutes < ONE_DIGIT ? ZERO : "").append(minutes).append(":");
        sb.append(seconds < ONE_DIGIT ? ZERO : "").append(seconds);

        return sb.toString();
    }

}
