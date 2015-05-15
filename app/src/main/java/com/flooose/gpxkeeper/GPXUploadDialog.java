package com.flooose.gpxkeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class GPXUploadDialog extends DialogFragment {
    private GPXFile gpxFile;

    public GPXUploadDialog(File file) throws FileNotFoundException {
        this.gpxFile = new GPXFile(file);
    }

    private String getOauthToken(){
        return PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString("oauth_token", "");
    }

    private GPXFile getUploadFile() {
        return gpxFile;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.post_activity)
                .setSingleChoiceItems(GPXFile.SupportedActivities, 0, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i){
                        getUploadFile().setActivityType(i);
                    }
                })
                .setPositiveButton(R.string.post_activity, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RunKeeperRequest runKeeperRequest = new RunKeeperRequest(getOauthToken(),
                                getActivity().getApplicationContext());

                        try {
                            String gpxJSON = getUploadFile().toJSON();
                            runKeeperRequest.send(getActivity(), gpxJSON);
                        } catch (OAuthSystemException e) {
                            e.printStackTrace();
                        } catch (OAuthProblemException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
