package com.example.final_project.objects;

import android.location.Address;
import android.util.Log;
import android.view.MenuItem;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class User {
    private String uid = "";
    private long picture = 0;
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private long dateOfBirth = 0;
    private double height = 0.0;
    private double weight = 0.0;
    private Address address = null;
    private ArrayList<String> allFriends = null;
    private ArrayList<String> allRuns = null;

    public User() { }

    public User(String uid, long picture, String firstName, String lastName, String phone, long dateOfBirth, double height, double weight, Address address, ArrayList<String> allFriends, ArrayList<String> allRuns) {
        this.uid = uid;
        this.picture = picture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.allFriends = allFriends;
        this.allRuns = allRuns;
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public long getPicture() {
        return picture;
    }

    public User setPicture(long picture) {
        this.picture = picture;
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

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public User setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public long getAge() {
        double age = (System.currentTimeMillis() - dateOfBirth) / 1000;
        age /= 60 * 60 * 24 * 365;
        return (int)age;
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

    public double getBmi() {
        double bmi = 0.0;

        if (height != 0)
            bmi = weight / Math.pow((height / 100), 2);

        return bmi;
    }

    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return this;
    }

    public ArrayList<String> getAllFriends() {
        return allFriends;
    }

    public User setAllFriends(ArrayList<String> allFriends) {
        this.allFriends = allFriends;
        return this;
    }

    public ArrayList<String> getAllRuns() {
        return allRuns;
    }

    public User setAllRuns(ArrayList<String> allRuns) {
        this.allRuns = allRuns;
        return this;
    }

}
