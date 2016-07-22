package com.flooose.gpxkeeper.TrackedActivity.models;

/**
 * Created by chris on 17.07.16.
 */

public class RunKeeperGPXPoint {
    private int timestamp;
    private String latitude;
    private String longitude;
    private String altitude;
    private String type = "gps";

    public RunKeeperGPXPoint(int timestamp, String latitude, String longitude, String altitude) {
        timestamp = timestamp;
        latitude = latitude;
        longitude = longitude;
        altitude = altitude;
    }
}
