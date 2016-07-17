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

    public GPXKeeperLocationListener() {
        mWaypoints = new ArrayList<>();
    }

    public ArrayList<GeoPoint> getWaypoints() {
        return mWaypoints;
    }

    @Override
    public void onLocationChanged(Location location) {
        mWaypoints.add(new GeoPoint(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

}
