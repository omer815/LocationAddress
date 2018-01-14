package com.google.android.gms.location.sample.locationaddress;

/**
 * Created by omer on 1/14/2018.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import android.view.View;
import android.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class pickDayDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.title_day)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.days, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        TextView repet = (TextView) getActivity().findViewById(R.id.repetchosen);

                        StringBuilder days = new StringBuilder();
                        String day = null;
                        for (int loc : mSelectedItems) {
                            switch (loc) {
                                case 0:
                                    day = "א";
                                    break;

                                case 1:
                                    day = "ב";
                                    break;
                                case 2:
                                    day = "ג";
                                    break;
                                case 3:
                                    day = "ד";
                                    break;
                                case 4:
                                    day = "ה";
                                    break;
                                case 5:
                                    day = "ו";
                                    break;
                                case 6:
                                    day = "ש";
                                    break;
                                default:
                                    day = "Invalid month";
                                    break;

                            }
                            days.append(day+" ,");
                        }
                        repet.setText(days);
                    }
                })
                .setNegativeButton(R.string.cencel1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
