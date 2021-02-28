package com.example.final_project.objects;

import com.example.final_project.utils.Constants;

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
    private int averageCalories = 0;
    private int maxCalories = 0;
    private int totalCalories = 0;

    public Statistics() { }

    public Statistics(int numOfRuns, long averageTime, long maxTime, long totalTime, double averageDistance, double maxDistance, double totalDistance, double averageSpeed, double maxSpeed, int averageCalories, int maxCalories, int totalCalories) {
        this.numOfRuns = numOfRuns;
        this.averageTime = averageTime;
        this.maxTime = maxTime;
        this.totalTime = totalTime;
        this.averageDistance = averageDistance;
        this.maxDistance = maxDistance;
        this.totalDistance = totalDistance;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.averageCalories = averageCalories;
        this.maxCalories = maxCalories;
        this.totalCalories = totalCalories;
    }

    public int getNumOfRuns() {
        return numOfRuns;
    }

    public void setNumOfRuns(int numOfRuns) {
        this.numOfRuns = numOfRuns;
    }

    public long getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(long averageTime) {
        this.averageTime = averageTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(double averageDistance) {
        this.averageDistance = averageDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getAverageCalories() {
        return averageCalories;
    }

    public void setAverageCalories(int averageCalories) {
        this.averageCalories = averageCalories;
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
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
        setAverageSpeed(this.totalDistance / (this.totalTime / Constants.MILLISECOND_TO_HOURS));
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
