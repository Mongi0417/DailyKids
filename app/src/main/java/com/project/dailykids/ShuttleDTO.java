package com.project.dailykids;

public class ShuttleDTO {
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

    public ShuttleDTO(double latitude, double longitude, String run_state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.runState = run_state;
    }
}
