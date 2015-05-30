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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chris on 13.05.15.
 *
 * Copied and adapted from https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class GPXFilesAdapter extends ArrayAdapter<File> {
    public GPXFilesAdapter(Context context, ArrayList<File> files) {
        super(context, 0, files);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        File file = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gpx_file_entry_layout, parent, false);
        }

        // Lookup view for data population
        TextView gpxFileName = (TextView) convertView.findViewById(R.id.gpx_file_name);
        // Populate the data into the template view using the data object
        gpxFileName.setText(file.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
