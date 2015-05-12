package com.flooose.gpxkeeper;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;

/**
 * Created by chris on 05.04.15.
 */
public class GPXFiles {
    private static String GPX_EXTENSION = "gpx";
    private File folder;

    @Inject
    public GPXFiles(File folder){
        this.folder = folder;
    }

    @Inject
    public File[] files(){
        FileFilter gpxFilter = new FileFilter(){
            public boolean accept(File pathname) {
                String[] components = pathname.toString().split("\\.");
                return components[components.length -1].equals(GPXFiles.GPX_EXTENSION);
            }
        };
        File[] files = folder.listFiles(gpxFilter);
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });
        return files;
    }
}

