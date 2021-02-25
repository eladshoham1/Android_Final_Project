package com.example.final_project.objects;

public class User {
    private String uid = "";
    private String pictureUrl = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private double height = 0.0;
    private double weight = 0.0;
    private BirthDate birthDate = null;
    private Settings settings = null;

    public User() { }

    public User(String uid, String pictureUrl, String firstName, String lastName, String phone, double height, double weight, BirthDate birthDate, Settings settings) {
        this.uid = uid;
        this.pictureUrl = pictureUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
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

        if (height != 0) {
            bmi = weight / Math.pow((height / 100), 2);
        }

        return bmi;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public User setBirthDate(BirthDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Settings getSettings() {
        return settings;
    }

    public User setSettings(Settings settings) {
        this.settings = settings;
        return this;
    }

}
