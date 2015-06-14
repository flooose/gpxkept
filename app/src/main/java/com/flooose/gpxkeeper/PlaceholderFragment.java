package com.flooose.gpxkeeper;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by chris on 13.06.15.
 */
public class PlaceholderFragment extends Fragment {
    public ListView fileListView;
    private GPXFiles gpxFiles = new GPXFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));


    public PlaceholderFragment() {        }

    public GPXFilesAdapter getGPXFilesAdapter(){
        return new GPXFilesAdapter(getActivity(), gpxFiles.files());
    }

    public void setFileArrayAdapter(){
        if (oAuthenticated()){
            fileListView.setAdapter(getGPXFilesAdapter());
        }
    }

    private void setButtonText(View rootView){
        Button button = (Button) rootView.findViewById(R.id.oAuthButton);
        if(oAuthenticated()){
            button.setText(R.string.deauthenticate);
        } else {
            button.setText(R.string.setup);
        }
    }

    public boolean oAuthenticated() {
        return ((MainActivity)getActivity()).getOAuthToken() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //setButtonText(rootView);

        fileListView = (ListView) rootView.findViewById(R.id.gpx_file_list_view);

        if(oAuthenticated()) {
            fileListView.setAdapter(getGPXFilesAdapter());
            fileListView.setOnItemClickListener(new ListView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    GPXUploadDialog gpxUploadDialog = null;
                    try {
                        gpxUploadDialog = new GPXUploadDialog((File) adapterView.getItemAtPosition(i));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    gpxUploadDialog.show(getActivity().getFragmentManager(), "hello");
                }
            });
        }
        return rootView;
    }

}
