package com.example.kitchen.medicinetracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shreyas on 27-10-2017.
 */

public class HeightDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = AlarmDbHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "heights.db";


    private static final int DATABASE_VERSION = 1;


    public HeightDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + AlarmContract.AlarmEntry.SECOND_TABLE + " ("
                + AlarmContract.AlarmEntry.COLUMN_HEIGHT + " FLOAT NOT NULL);";



       db.execSQL(SQL_CREATE_PETS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
