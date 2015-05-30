/*
    Copyright 2015-2080 christopher floess

    This file is part of gpxkept.

    gpxkept is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    gpxkept is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with gpxkept.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.flooose.gpxkeeper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
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
    public java.util.ArrayList<File> files(){
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

        return new ArrayList<File>(Arrays.asList(files));
    }
}

