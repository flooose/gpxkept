package com.flooose.gpxkeeper;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.flooose.gpxkeeper.TrackedActivity.AsyncRoadRetriever;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class RunningActivity extends FragmentActivity {

    private boolean recording = false;
    private GPXKeeperLocationListener ll = new GPXKeeperLocationListener();
    private LocationManager mLocationManager;
    private MapView mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        // Must be done in order to avoid mLocationManater#getLastKnownLocation returning null
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, ll, null);
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
        recording = !recording;
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        //LocationRequest mLocationRequest = new LocationRequest();

        if (recording) {
            button.setText(getString(R.string.stop));
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, ll);
//            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            button.setText(getString(R.string.start));

            new AsyncRoadRetriever(this).execute(ll.getWaypoints(), null, null);

            lm.removeUpdates(ll);
        }
    }

    public void setActivityOnMap(Road road) {
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
        mMap.getOverlays().add(roadOverlay);
        mMap.invalidate();
    }
}
