package com.flooose.gpxkeeper;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by chris on 08.05.16.
 */
public class GPXKeeperLocationListener implements LocationListener {
    private ArrayList<GeoPoint> mWaypoints;
    int blub = 1;

    public GPXKeeperLocationListener() {
        mWaypoints = new ArrayList<>();
        //waypoints.add(startPoint);
        //GeoPoint endPoint = new GeoPoint(48.4, -1.9);
        //mWaypoints.add(endPoint);
    }

    public ArrayList<GeoPoint> getWaypoints() {
        return mWaypoints;
    }

    @Override
    public void onLocationChanged(Location location) {
        mWaypoints.add(new GeoPoint(location.getLatitude(), location.getLongitude()));
        blub += 1;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        int blub = 1;
    }

    @Override
    public void onProviderEnabled(String s) {
        int blub = 1;
    }

    @Override
    public void onProviderDisabled(String s) {
        int blub = 1;
    }

}