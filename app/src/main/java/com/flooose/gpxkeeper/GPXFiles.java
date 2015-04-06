package com.flooose.gpxkeeper;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by chris on 05.04.15.
 */
public class GPXFiles {
    private static String GPX_EXTENSION = "gpx";
    private File folder;

    public GPXFiles(File folder){
        this.folder = folder;
    }

    public File[] files(){
        FileFilter gpxFilter = new FileFilter(){
            public boolean accept(File pathname) {
                String[] components = pathname.toString().split("\\.");
                return components[components.length -1].equals(GPXFiles.GPX_EXTENSION);
            }
        };
        return folder.listFiles(gpxFilter);
    }
}

