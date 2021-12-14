package com.example.kitchen.medicinetracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shreyas on 23-10-2017.
 */

public class ReportDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = ReportDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "reports.db";


    private static final int DATABASE_VERSION = 3;


    public ReportDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
    //db.execSQL("DROP TABLE "+ ReportContract.ReportEntry.TABLE_NAME);
        String SQL_CREATE_REPORTS_TABLE = "CREATE TABLE " + ReportContract.ReportEntry.TABLE_NAME + " ("
                + ReportContract.ReportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReportContract.ReportEntry.COLUMN_ALARM_HOURS + " INTEGER NOT NULL, "
                + ReportContract.ReportEntry.COLUMN_ALARM_MINUTES + " INTEGER NOT NULL, "
                + ReportContract.ReportEntry.COLUMN_ALARM_DAY+" INTEGER NOT NULL, "
                + ReportContract.ReportEntry.COLUMN_ALARM_TAKEN + " TEXT NOT NULL, "
                + ReportContract.ReportEntry.COLUMN_ALARM_LABEL+ " TEXT NOT NULL);";
                //+"FOREIGN KEY ("+ ReportContract.ReportEntry.COLUMN_ALARM_HOURS+") REFERENCES "+ AlarmContract.AlarmEntry.TABLE_NAME+"("+ AlarmContract.AlarmEntry.COLUMN_ALARM_HOURS+"), "
                //+""


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_REPORTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        if (newVersion > oldVersion) {
            //db.execSQL("ALTER TABLE "+ ReportContract.ReportEntry.TABLE_NAME+" ADD COLUMN "+ ReportContract.ReportEntry.COLUMN_ALARM_LABEL+" TEXT");
    }
}}