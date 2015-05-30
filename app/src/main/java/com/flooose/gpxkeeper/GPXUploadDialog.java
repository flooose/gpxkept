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
