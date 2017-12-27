package com.google.android.gms.location.sample.locationaddress.data;

import android.provider.BaseColumns;

/**
 * Created by omer on 12/20/2017.
 */

public class AlarmClockDbConfiguration {

    public static final class alarmclockEntry implements BaseColumns {

        public static final String TABLE_NAME = "alarmClock";
        public static final String COLUMN_RANGE_SIZE = "Radius";
        public static final String COLUMN_LOCATION_ADDRESS = "fullAdress";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUTE = "longitude";
        public static final String COLUMN_ALARM_ON_OFF = "alarmOn/Off";
        public static final String COLUMN_DAY_1 = "sunday";
        public static final String COLUMN_DAY_2 = "monday";
        public static final String COLUMN_DAY_3 = "tuesday";
        public static final String COLUMN_DAY_4 = "wednesday";
        public static final String COLUMN_DAY_5 = "thursday";
        public static final String COLUMN_DAY_6 = "friday";
        public static final String COLUMN_DAY_7 = "saturday";
        public static final String COLUMN_RINGNAME = "ringtoneName";
        public static final String COLUMN_VIBRATE = "vibrateOn/Off";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_HOUR = "hour";

    }
}
