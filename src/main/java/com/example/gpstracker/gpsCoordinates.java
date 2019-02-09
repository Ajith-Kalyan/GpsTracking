package com.example.gpstracker;

public class gpsCoordinates {

    private String coordinates, timestamp;

    public gpsCoordinates(String coordinates, String timestamp) {
        this.coordinates = coordinates;
        this.timestamp = timestamp;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
