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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;


public class GPXDetailDialog extends DialogFragment {
    public File gpxFile;
    public GPXDetailDialog(File file) throws FileNotFoundException {
        this.gpxFile = file;
    }
    GPXDetailDialogListener mListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (GPXDetailDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.delete_message)
                .setPositiveButton(R.string.delete_file, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(GPXDetailDialog.this);
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

    public interface GPXDetailDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        //public void onDialogNegativeClick(DialogFragment dialog);
    }
}
