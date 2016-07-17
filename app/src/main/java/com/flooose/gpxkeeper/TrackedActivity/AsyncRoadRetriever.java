package com.flooose.gpxkeeper.TrackedActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.flooose.gpxkeeper.MainActivity;
import com.flooose.gpxkeeper.RunningActivity;

import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chris on 16.07.16.
 */

public class AsyncRoadRetriever  extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {

    private Activity mActivity;

    public AsyncRoadRetriever(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    protected Road doInBackground(ArrayList<GeoPoint>... wayPoints) {
        RoadManager roadManager = new MapQuestRoadManager("YKfqq3zkvgk8iOfvm1hzLoAjZMy0yE1g");
        roadManager.addRequestOption("routeType=bicycle");
        //RoadManager roadManager = new OSRMRoadManager(mActivity);
        return roadManager.getRoad(wayPoints[0]);
    }

    @Override
    protected void onPostExecute(Road road) {
        ((RunningActivity)mActivity).setActivityOnMap(road);
    }
}
