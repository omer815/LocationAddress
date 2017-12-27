package com.google.android.gms.location.sample.locationaddress.data;

/**
 * Created by omer on 12/20/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.location.sample.locationaddress.data.AlarmClockDbConfiguration.*;

public class AlarmClockDbConnection extends SQLiteOpenHelper{
    // The database name
    private static final String DATABASE_NAME = "alarm555.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public AlarmClockDbConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_WAITLIST_TABLE1 = "CREATE TABLE " + alarmclockEntry.TABLE_NAME + " (" +
                alarmclockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                alarmclockEntry.COLUMN_RANGE_SIZE + " REAL NOT NULL, " +
                alarmclockEntry.COLUMN_LOCATION_ADDRESS + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + alarmclockEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
