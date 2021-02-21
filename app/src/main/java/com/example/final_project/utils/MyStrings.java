package com.example.final_project.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class MyStrings {

    public static final String TWO_POINT_FORMAT = "##.##";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static String twoDigitsAfterPoint(double num) {
        DecimalFormat decimalFormat = new DecimalFormat(TWO_POINT_FORMAT);

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
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        sb.append(hours < 10 ? "0" : "").append(hours).append(":");
        sb.append(minutes < 10 ? "0" : "").append(minutes).append(":");
        sb.append(seconds < 10 ? "0" : "").append(seconds);

        return sb.toString();
    }

}
