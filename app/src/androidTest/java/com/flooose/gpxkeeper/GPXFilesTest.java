package com.flooose.gpxkeeper;

import android.os.Environment;
import android.test.AndroidTestCase;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by chris on 05.04.15.
 */
public class GPXFilesTest extends AndroidTestCase {
    private File gpxFile;
    private File nonGPXFile;
    private File downloadDir = new File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .getPath());

    @Override
    public void setUp(){
        try {
            gpxFile = File.createTempFile("foo", ".gpx", downloadDir);
            nonGPXFile = File.createTempFile("foo", ".bar", downloadDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gpxFile.deleteOnExit();
        nonGPXFile.deleteOnExit();
    }

    @Override
    public void tearDown(){

    }

    public void testReturnsGPXFilesInTheDownloadDirectory(){
        GPXFiles gpxFiles = new GPXFiles(downloadDir);

        assertTrue(Arrays.asList(gpxFiles.files()).contains(gpxFile));
        assertFalse(Arrays.asList(gpxFiles.files()).contains(nonGPXFile));
    }
}
