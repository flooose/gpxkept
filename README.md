## `GPXKeeperOAuthData.java.example`

Before this project can be compiled, `GPXKeeperOAuthData.java.example` has to be copied to `GPXKeeperOAuthData.java`
and then add your applications client id from Runkeeper.

## gpx Structure

    <?xml version="1.0" encoding="UTF-8" ?>
    <gpx xmlns="http://www.topografix.com/GPX/1/1" version="1.1" creator="OSMTracker for Android™ - http://osmtracker-android.googlecode.com/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd ">
        <trk>
            <name><![CDATA[Tracked with OSMTracker for Android™]]></name>
            <trkseg>
                <trkpt lat="53.5383126" lon="10.10585945">
                    <ele>99.0</ele>
                    <time>2015-03-28T11:07:02Z</time>
                    <extensions>
                        <speed>0.0</speed>
                    </extensions>
                </trkpt>
            </trkseg>
        </trk>
    </gpx>

with a date format string `yyyy-MM-ddTk:m:sZ`

## JSON structure

See [here](http://developer.runkeeper.com/healthgraph/fitness-activities#newly-completed-activities)

    type: "Running",
    start_time: "Sat, 1 Jan 2011 00:00:00",
    path: [
        {
            timestamp: 0,
            latitude: "53.5383126",
            longitude: "10.10585945",
            altitude: "99.0",
            type: "gps"
        },
        ...
    ]

Date format string:

    "EEE, d MMM yyyy H:m:s"

### Valid sample

    {type: "Running",
    start_time: "Wed, 28 Mar 2015 11:07:02",
    path: [
            {
                timestamp: 0,
                latitude: "53.5383126",
                longitude: "10.10585945",
                altitude: "99.0",
                type: "gps"
            },
         {
                timestamp: 2,
                latitude: "53.5383126",
                longitude: "10.10585945",
                altitude: "99.0",
                type: "gps"
            }
        ]}

Time stamp must be number of seconds after start time `start_time`
