package com.example.final_project.utils;

import java.text.DecimalFormat;

public class MyStrings {

    public static final String PATTERN = "##.##";

    public static String twoNumbersAfterPoint(double num) {
        DecimalFormat decimalFormat = new DecimalFormat(PATTERN);

        return decimalFormat.format(num);
    }

    public static String makeDateString(long date) {
        StringBuilder sb = new StringBuilder();
        long seconds = date / 1000;
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
