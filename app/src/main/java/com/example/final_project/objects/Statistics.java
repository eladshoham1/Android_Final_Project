package com.example.final_project.objects;

public class Statistics {
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
    private int numOfRuns = 0;

    public Statistics() { }

    public Statistics(long averageTime, long maxTime, long totalTime, double averageDistance, double maxDistance, double totalDistance, double averageSpeed, double maxSpeed, double totalAverageSpeed, int averageCalories, int maxCalories, int totalCalories) {
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

    public void addRun(Run run) {
        this.numOfRuns++;
        updateStatistics(run);
    }

    public void deleteRun(Run run) {
        this.numOfRuns--;
        updateStatistics(run);
    }

    private void updateStatistics(Run run) {
        updateTime(run.getDuration());
        updateSpeed(run.getAverageSpeed(), run.getMaxSpeed());
        updateDistance(run.getDistance());
        updateCalories(run.getCalories());
    }

    private void updateTime(long time) {
        this.totalTime += time;
        this.averageTime = this.totalTime / this.numOfRuns;

        if (time > this.maxTime) {
            this.maxTime = time;
        }
    }

    private void updateSpeed(double averageSpeed, double maxSpeed) {
        this.averageSpeed *= (numOfRuns - 1);
        this.averageSpeed += averageSpeed;
        this.averageSpeed /= numOfRuns;

        if (maxSpeed > this.maxSpeed) {
            this.maxSpeed = maxSpeed;
        }
    }

    private void updateDistance(double distance) {
        this.totalDistance += distance;
        this.averageDistance = this.totalDistance / this.numOfRuns;

        if (distance > this.maxDistance) {
            this.maxDistance = distance;
        }
    }

    private void updateCalories(int calories) {
        this.totalCalories += calories;
        this.averageCalories = this.totalCalories / this.numOfRuns;

        if (calories > this.maxCalories) {
            this.maxCalories = calories;
        }
    }

}
