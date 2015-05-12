package com.flooose.gpxkeeper;

import android.test.AndroidTestCase;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import static android.os.Environment.*;


/**
 * Created by chris on 05.04.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GPXFilesTest extends AndroidTestCase {
    private File gpxFile;
    private File anotherGPXFile = null;

    private File nonGPXFile;
    private File downloadDir = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);

    @Override
    public void setUp(){
        try {
            gpxFile = File.createTempFile("foo", ".gpx", downloadDir);
            anotherGPXFile = File.createTempFile("bar", ".gpx", downloadDir);
            nonGPXFile = File.createTempFile("foo", ".bar", downloadDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tearDown(){
        gpxFile.delete();
        nonGPXFile.delete();
        anotherGPXFile.delete();
    }

    public void testReturnsGPXFilesInTheDownloadDirectory(){
        GPXFiles gpxFiles = new GPXFiles(downloadDir);

        assertTrue(Arrays.asList(gpxFiles.files()).contains(gpxFile));
        assertFalse(Arrays.asList(gpxFiles.files()).contains(nonGPXFile));
    }

    public void testReturnsGPXFilesOrderedFromNewestToOldest(){
        System.setProperty("dexmaker.dexcache",
                "/data/data/" + BuildConfig.APPLICATION_ID + "/cache");

        File gpxFile = mock(File.class);
        when(gpxFile.toString()).thenReturn("foo.gpx");

        File anotherGPXFile = mock(File.class);
        when(anotherGPXFile.toString()).thenReturn("bar.gpx");
        when(gpxFile.lastModified()).thenReturn(new Long(10));

        File downloadDir = mock(File.class);
        when(downloadDir.listFiles(any(FileFilter.class))).thenReturn(new File[]{gpxFile, anotherGPXFile});

        GPXFiles gpxFiles = new GPXFiles(downloadDir);

        assertTrue(gpxFiles.files()[0].equals(gpxFile));

        when(anotherGPXFile.lastModified()).thenReturn(new Long(20));
        assertTrue(gpxFiles.files()[0].equals(anotherGPXFile));
    }
}
