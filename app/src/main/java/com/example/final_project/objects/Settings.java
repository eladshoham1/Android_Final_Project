package com.example.final_project.objects;

public class Settings {
    private boolean picture = true;
    private boolean age = true;
    private boolean bmi = true;
    private boolean height = true;
    private boolean weight = true;

    public Settings() { }

    public Settings(boolean picture, boolean age, boolean bmi, boolean height, boolean weight) {
        this.picture = picture;
        this.age = age;
        this.bmi = bmi;
        this.height = height;
        this.weight = weight;
    }

    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }

    public boolean isAge() {
        return age;
    }

    public void setAge(boolean age) {
        this.age = age;
    }

    public boolean isBmi() {
        return bmi;
    }

    public void setBmi(boolean bmi) {
        this.bmi = bmi;
    }

    public boolean isHeight() {
        return height;
    }

    public void setHeight(boolean height) {
        this.height = height;
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

}
