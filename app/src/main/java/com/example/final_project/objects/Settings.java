package com.example.final_project.objects;

public class Settings {
    private boolean sound = true;
    private boolean picture = true;
    private boolean age = true;
    private boolean bmi = true;
    private boolean height = true;
    private boolean weight = true;

    public Settings() { }

    public Settings(boolean sound, boolean picture, boolean age, boolean bmi, boolean height, boolean weight) {
        this.sound = sound;
        this.picture = picture;
        this.age = age;
        this.bmi = bmi;
        this.height = height;
        this.weight = weight;
    }

    public boolean isSound() {
        return sound;
    }

    public Settings setSound(boolean sound) {
        this.sound = sound;
        return this;
    }

    public boolean isPicture() {
        return picture;
    }

    public Settings setPicture(boolean picture) {
        this.picture = picture;
        return this;
    }

    public boolean isAge() {
        return age;
    }

    public Settings setAge(boolean age) {
        this.age = age;
        return this;
    }

    public boolean isBmi() {
        return bmi;
    }

    public Settings setBmi(boolean bmi) {
        this.bmi = bmi;
        return this;
    }

    public boolean isHeight() {
        return height;
    }

    public Settings setHeight(boolean height) {
        this.height = height;
        return this;
    }

    public boolean isWeight() {
        return weight;
    }

    public Settings setWeight(boolean weight) {
        this.weight = weight;
        return this;
    }

}
