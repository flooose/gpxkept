package com.flooose.gpxkeeper;

import android.util.Xml;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by chris on 11.04.15.
 */
public class GPXFile {
    private static String ELEVATION = "altitude";
    private static String LONGITUDE = "longitude";
    private static String LATITUDE = "latitude";
    private static String TIMESTAMP = "timestamp";
    private static String TRACKING_POINT_TYPE = "type";
    private static String GPS_ACTIVITY_TYPE = "type";
    private static String START_TIME = "start_time";
    private static String GPS_PATH = "path";
    private static String  DATE_FORMAT_STRING = "EEE, d MMM yyyy H:mm:ss";

    private static String TRACK_POINT = "trkpt";

    private static String GPX_DATE_FORMAT_STRING = "yyyy-MM-dd'T'H:m:s'Z'";

    private Date startTime = null;
    private FileInputStream gpxFileInputStream = null;
    private XmlPullParser parser = Xml.newPullParser();
    private JSONArray gpsPath = new JSONArray();
    private JSONObject gpsActivity = new JSONObject(); // actually a RunkeeperActivity

    public GPXFile(String path) throws FileNotFoundException {
        File gpxFile = new File(path);
        gpxFileInputStream = new FileInputStream(gpxFile);
    }

    public String toJSON() throws IOException, XmlPullParserException {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(gpxFileInputStream, null);

        putJSONPayloadItem(GPXFile.TRACKING_POINT_TYPE, "Running");

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (trackPointStart()) {
                    saveTrackingPointAsJSON();
                }

                parser.nextTag();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        putJSONPayloadItem(GPS_PATH, gpsPath);

        return gpsActivity.toString();
    }

    private void putJSONPayloadItem(String payloadKey, JSONArray payloadValue) {
        try {
            gpsActivity.put(payloadKey, payloadValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void putJSONPayloadItem(String payloadKey, String payloadValue) {
        try {
            gpsActivity.put(payloadKey, payloadValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveTrackingPointAsJSON(){
        Date timestamp = null;

        JSONObject trackingPoint = new JSONObject();
        if(startTime == null){

        }

        for (int i = 0; i < parser.getAttributeCount(); i++) {
            try {
                if("lon".equals(parser.getAttributeName(i))){
                    trackingPoint.put(LONGITUDE, parser.getAttributeValue(i));

                } else if ("lat".equals(parser.getAttributeName(i))){
                    trackingPoint.put(LATITUDE, parser.getAttributeValue(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            while(!trackPointEnd()){
                if("ele".equals(parser.getName()) && parser.getEventType() != parser.END_TAG){
                    while (parser.getEventType() != parser.TEXT) parser.next();
                    trackingPoint.put(GPXFile.ELEVATION, parser.getText());
                } else if ("time".equals(parser.getName()) && parser.getEventType() != parser.END_TAG){
                    while (parser.getEventType() != parser.TEXT) parser.next();
                    try {
                        timestamp = new SimpleDateFormat(GPX_DATE_FORMAT_STRING).parse(parser.getText());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(startTime == null){
                        startTime = timestamp;
                    }
                    gpsActivity.put(GPXFile.START_TIME, new SimpleDateFormat(DATE_FORMAT_STRING).format(startTime));
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            trackingPoint.put(GPXFile.TIMESTAMP, (timestamp.getTime() - startTime.getTime())/1000); // there should be a constant defined somewhere. Find it.
            trackingPoint.put(GPXFile.TRACKING_POINT_TYPE, "gps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gpsPath.put(trackingPoint);
    }

    private boolean trackPointStart(){
        try {
            if (parser.getName() == null){
                return false;
            } else {
                return parser.getName().equals(GPXFile.TRACK_POINT) && parser.getEventType() == XmlPullParser.START_TAG;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean trackPointEnd() {
        try {
            if (parser.getName() == null){
                return false;
            } else {
                return parser.getName().equals(GPXFile.TRACK_POINT) && parser.getEventType() == XmlPullParser.END_TAG;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        }
    }
}
