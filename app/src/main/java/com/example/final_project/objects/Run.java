package com.example.final_project.objects;

public class Run {
    private long startTime = 0;
    private long endTime = 0;
    private double distance = 0;
    private double maxSpeed = 0;
    private double averageSpeed = 0;

    public Run() {}

    public Run(long startTime, long endTime, double distance, double maxSpeed, double averageSpeed) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.averageSpeed = averageSpeed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }
}
