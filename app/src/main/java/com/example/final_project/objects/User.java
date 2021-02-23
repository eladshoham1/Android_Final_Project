package com.example.final_project.objects;

import java.util.Calendar;

public class User {
    private String uid = "";
    private String pictureUrl = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private double height = 0.0;
    private double weight = 0.0;
    private int birthYear = 0;
    private int birthMonth = 0;
    private int birthDay = 0;
    private Settings settings;

    public User() { }

    public User(String uid, String pictureUrl, String firstName, String lastName, String phone, double height, double weight, int birthYear, int birthMonth, int birthDay, Settings settings) {
        this.uid = uid;
        this.pictureUrl = pictureUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.height = height;
        this.weight = weight;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.settings = settings;
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public User setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public User setHeight(double height) {
        this.height = height;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public User setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public double getBMI() {
        double bmi = 0.0;

        if (height != 0)
            bmi = weight / Math.pow((height / 100), 2);

        return bmi;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public User setBirthYear(int birthYear) {
        this.birthYear = birthYear;
        return this;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public User setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
        return this;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public User setBirthDay(int birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public Settings getSettings() {
        return settings;
    }

    public User setSettings(Settings settings) {
        this.settings = settings;
        return this;
    }

    public int getAge() {
        Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = calenderToday.get(Calendar.MONTH) + 1;
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);
        int age = currentYear - birthYear;

        if (birthMonth > currentMonth) {
            age--;
        } else if (birthMonth == currentMonth) {
            if (birthDay > todayDay){
                age--;
            }
        }

        return age;
    }

}
