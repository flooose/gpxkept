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
