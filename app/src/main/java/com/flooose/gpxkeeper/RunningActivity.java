package com.flooose.gpxkeeper;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.flooose.gpxkeeper.TrackedActivity.AsyncRoadRetriever;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class RunningActivity extends FragmentActivity {

    private boolean mRecording = false;
    private GPXKeeperLocationListener gpxKeeperLocationListener;
    private LocationManager mLocationManager;
    private MapView mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        // Must be done in order to avoid mLocationManater#getLastKnownLocation returning null
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, gpxKeeperLocationListener, null);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mMap = (MapView) findViewById(R.id.map);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        //map.setBuiltInZoomControls(true);
        mMap.setMultiTouchControls(true);

        IMapController mapController = mMap.getController();
        mapController.setZoom(14);
        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(mMap);
        startMarker.setPosition(startPoint);
        //startMarker.setIcon(getcontex(R.drawable.ic_menu_mylocation));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMap.getOverlays().add(startMarker);
    }


    public void startNewTrack(View view) {
        Button button = (Button) view;
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        //LocationRequest mLocationRequest = new LocationRequest();

        if (mRecording) {
            mRecording = false;
            button.setText(getString(R.string.start));

            new AsyncRoadRetriever(this).execute(gpxKeeperLocationListener.getWaypoints(), null, null);

            locationManager.removeUpdates(gpxKeeperLocationListener);
        } else {
            mRecording = true;
            gpxKeeperLocationListener = new GPXKeeperLocationListener();
            button.setText(getString(R.string.stop));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, gpxKeeperLocationListener);
//            Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public void setActivityOnMap(Road road) {
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
        mMap.getOverlays().add(roadOverlay);
        mMap.invalidate();
    }
}
