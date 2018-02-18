package com.google.android.gms.location.sample.locationaddress.alarmmanager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by omer on 2/18/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent)
    {
        Toast.makeText(context,"Running",Toast.LENGTH_LONG).show();
    }
}
