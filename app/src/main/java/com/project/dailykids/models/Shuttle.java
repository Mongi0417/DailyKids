package com.project.dailykids.models;

public class Shuttle {
    private double latitude;
    private double longitude;
    private String runState;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRunState() {
        return runState;
    }

    public Shuttle(double latitude, double longitude, String run_state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.runState = run_state;
    }

    public Shuttle() {
        this.latitude = 0;
        this.longitude = 0;
        this.runState = "운행 중지";
    }
}
