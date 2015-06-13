# Introduction

This is an App I use to post GPX files to Runkeeper because, one, I
can't use the official app from Runkeeper because I don't have Google
services installed on my [cyanogenmod](http://www.cyanogenmod.org/),
and two, I wanted to see if I could do it.

All the application does is post the contents of GPX files created
with [OSMTracker](https://code.google.com/p/osmtracker-android/) to
Runkeeper via the Runkeeper API.

If you're interested in using this App (I can't imagine why). Install
the app (see below), open it and click on the "Setup" button. After
that, you have to setup your OSMTracker to export its GPX files to
your Android™ device's `Download`s folder. After that, all `*.gpx`
files will be listed when you open gpxkeeper.

## Development

If you're interested in developing this further (which would be even
more surprising), follow the usual fork, patch, pull request
procedure. See notes below. If you're really feeling inspired, you
could even swing by the
[project's trello page](https://trello.com/b/SBmci3pO/gpxkept) and
pick one of those tasks to finish :)

### `GPXKeeperOAuthData.java.example` ###

Before this project can be compiled, `GPXKeeperOAuthData.java.example`
has to be copied to `GPXKeeperOAuthData.java` and then add your
application's client id and client secret to the file.

### gpx Sample ###

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

with a date format string `yyyy-MM-ddTk:m:sZ` used in `GPX_DATE_FORMAT_STRING`.

### JSON structure ###

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

Date format string: `EEE, d MMM yyyy H:m:s` used in `DATE_FORMAT_STRING`.

### Valid sample ####

    {
        type: "Running",
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
            ]
    }

The `timestamp` field must be number of seconds `start_time`.

## Installation

Contact me if you need an APK, otherwise see "Setup" above.
