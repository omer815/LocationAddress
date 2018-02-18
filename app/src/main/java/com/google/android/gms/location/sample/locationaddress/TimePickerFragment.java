package com.google.android.gms.location.sample.locationaddress;

/**
 * Created by omer on 12/25/2017.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import android.view.View;
import android.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        TextView tv = (TextView) getActivity().findViewById(R.id.time_choosen);
        TextView hour1 = (TextView) getActivity().findViewById(R.id.hour);
        TextView minute1 = (TextView) getActivity().findViewById(R.id.minute);

        hour1.setText(String.valueOf(hourOfDay));

        minute1.setText(String.valueOf(minute));

        tv.setText( convertDate(hourOfDay)+":"+
                 convertDate(minute) );
    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

}
