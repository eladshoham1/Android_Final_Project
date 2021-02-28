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

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
