package com.example.final_project.objects;

public class Run {
    private long startTime = 0;
    private long duration = 0;
    private double distance = 0.0;
    private double averageSpeed = 0.0;
    private double maxSpeed = 0.0;
    private double calories = 0.0;

    public Run() {}

    public Run(long startTime, long duration, double distance, double averageSpeed, double maxSpeed, double calories) {
        this.startTime = startTime;
        this.duration = duration;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.calories = calories;
    }

    public long getStartTime() {
        return startTime;
    }

    public Run setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public Run setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public long getEndTime() {
        return this.startTime + this.duration;
    }

    public double getDistance() {
        return distance;
    }

    public Run setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public Run setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
        return this;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public Run setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public double getCalories() {
        return calories;
    }

    public Run setCalories(double calories) {
        this.calories = calories;
        return this;
    }
}
