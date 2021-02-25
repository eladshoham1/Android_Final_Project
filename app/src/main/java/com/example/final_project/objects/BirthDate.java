package com.example.final_project.objects;

import java.util.Calendar;

public class BirthDate {
    private int day = 0;
    private int month = 0;
    private int year = 0;

    public BirthDate() { }

    public BirthDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public BirthDate setDay(int day) {
        this.day = day;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public BirthDate setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getYear() {
        return year;
    }

    public BirthDate setYear(int year) {
        this.year = year;
        return this;
    }

    public int getAge() {
        Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = calenderToday.get(Calendar.MONTH) + 1;
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);
        int age = currentYear - year;

        if (month > currentMonth) {
            age--;
        } else if (month == currentMonth) {
            if (day > todayDay) {
                age--;
            }
        }

        return age;
    }

}
