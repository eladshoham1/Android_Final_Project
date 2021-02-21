package com.example.final_project.objects;

import java.util.ArrayList;

public class Statistics {
    private int numOfRuns = 0;
    private long averageTime = 0L;
    private long maxTime = 0L;
    private long totalTime = 0L;
    private double averageDistance = 0.0;
    private double maxDistance = 0.0;
    private double totalDistance = 0.0;
    private double averageSpeed = 0.0;
    private double maxSpeed = 0.0;
    private double totalAverageSpeed = 0;
    private int averageCalories = 0;
    private int maxCalories = 0;
    private int totalCalories = 0;

    public Statistics() { }

    public Statistics(int numOfRuns, long averageTime, long maxTime, long totalTime, double averageDistance, double maxDistance, double totalDistance, double averageSpeed, double maxSpeed, double totalAverageSpeed, int averageCalories, int maxCalories, int totalCalories) {
        this.numOfRuns = numOfRuns;
        this.averageTime = averageTime;
        this.maxTime = maxTime;
        this.totalTime = totalTime;
        this.averageDistance = averageDistance;
        this.maxDistance = maxDistance;
        this.totalDistance = totalDistance;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.totalAverageSpeed = totalAverageSpeed;
        this.averageCalories = averageCalories;
        this.maxCalories = maxCalories;
        this.totalCalories = totalCalories;
    }

    public int getNumOfRuns() {
        return numOfRuns;
    }

    public Statistics setNumOfRuns(int numOfRuns) {
        this.numOfRuns = numOfRuns;
        return this;
    }

    public long getAverageTime() {
        return averageTime;
    }

    public Statistics setAverageTime(long averageTime) {
        this.averageTime = averageTime;
        return this;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public Statistics setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public Statistics setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public Statistics setAverageDistance(double averageDistance) {
        this.averageDistance = averageDistance;
        return this;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public Statistics setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public Statistics setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
        return this;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public Statistics setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
        return this;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public Statistics setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public double getTotalAverageSpeed() {
        return totalAverageSpeed;
    }

    public Statistics setTotalAverageSpeed(double totalAverageSpeed) {
        this.totalAverageSpeed = totalAverageSpeed;
        return this;
    }

    public int getAverageCalories() {
        return averageCalories;
    }

    public Statistics setAverageCalories(int averageCalories) {
        this.averageCalories = averageCalories;
        return this;
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public Statistics setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
        return this;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public Statistics setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
        return this;
    }

    public void calculateStatistics(ArrayList<Run> allRuns) {
        long time;
        double speed, distance;
        int calories;
        setNumOfRuns(allRuns.size());

        if (this.numOfRuns == 0) {
            return;
        }

        for (Run run : allRuns) {
            time = run.getDuration();
            speed = run.getMaxSpeed();
            distance = run.getDistance();
            calories = run.getCalories();

            this.totalTime += time;
            this.totalDistance += distance;
            this.totalCalories += calories;

            updateMaxTime(time);
            updateMaxSpeed(speed);
            updateMaxDistance(distance);
            updateMaxCalories(calories);
        }

        setAverageTime(this.totalTime / this.numOfRuns);
        setAverageSpeed(this.totalDistance / (this.totalTime / (1000.0 * 60.0 * 60.0)));
        setAverageDistance(this.totalDistance / this.numOfRuns);
        setAverageCalories(this.totalCalories / this.numOfRuns);
    }

    private void updateMaxTime(long time) {
        if (time > this.maxTime)
            setMaxTime(time);
    }

    private void updateMaxSpeed(double speed) {
        if (speed > this.maxSpeed)
            setMaxSpeed(speed);
    }

    private void updateMaxDistance(double distance) {
        if (distance > this.maxDistance)
            setMaxDistance(distance);
    }

    private void updateMaxCalories(int calories) {
        if (calories > this.maxCalories)
            setMaxCalories(calories);
    }

}
