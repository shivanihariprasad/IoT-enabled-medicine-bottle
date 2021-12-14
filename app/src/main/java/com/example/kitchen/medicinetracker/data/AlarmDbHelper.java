package com.example.kitchen.medicinetracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry;
/**
 * Created by Shreyas on 20-10-2017.
 */

public class AlarmDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = AlarmDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "alarms.db";


    private static final int DATABASE_VERSION = 1;


    public AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + AlarmEntry.TABLE_NAME + " ("
                + AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AlarmEntry.COLUMN_ALARM_HOURS + " INTEGER NOT NULL, "
                + AlarmEntry.COLUMN_ALARM_MINUTES + " INTEGER NOT NULL, "
                + AlarmEntry.COLUMN_ALARM_LABEL + " TEXT DEFAULT \"Alarm\");";


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }




}
