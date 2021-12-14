package com.example.kitchen.medicinetracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry;
import com.example.kitchen.medicinetracker.data.AlarmContract;

/**
 * Created by Shreyas on 20-10-2017.
 */

public class AlarmProvider extends ContentProvider{
    public static final String LOG_TAG = AlarmProvider.class.getSimpleName();

    private static final int ALARMS = 100;
    private static final int ALARM_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(AlarmContract.CONTENT_AUTHORITY, AlarmContract.PATH_ALARMS, ALARMS);

        sUriMatcher.addURI(AlarmContract.CONTENT_AUTHORITY, AlarmContract.PATH_ALARMS + "/#", ALARM_ID);
    }


    private AlarmDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AlarmDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();


        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:

                cursor = database.query(AlarmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ALARM_ID:
                selection = AlarmEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(AlarmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                return insertAlarm(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertAlarm(Uri uri, ContentValues values) {

        Integer hours = values.getAsInteger(AlarmEntry.COLUMN_ALARM_HOURS);
        if (hours == null) {
            throw new IllegalArgumentException("Reminder requires hours");
        }

        Integer minutes = values.getAsInteger(AlarmEntry.COLUMN_ALARM_MINUTES);
        if (minutes == null) {
            throw new IllegalArgumentException("Reminder requires minutes");
        }


        String status = values.getAsString(AlarmEntry.COLUMN_ALARM_LABEL);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(AlarmEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                return updateAlarm(uri, contentValues, selection, selectionArgs);
            case ALARM_ID:

                selection = AlarmEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateAlarm(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateAlarm(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(AlarmEntry.COLUMN_ALARM_HOURS)) {
            Integer hours = values.getAsInteger(AlarmEntry.COLUMN_ALARM_HOURS);
            if (hours == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if (values.containsKey(AlarmEntry.COLUMN_ALARM_MINUTES)) {
            Integer gender = values.getAsInteger(AlarmEntry.COLUMN_ALARM_MINUTES);
            if (gender == null) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        if (values.containsKey(AlarmEntry.COLUMN_ALARM_LABEL)) {
            // Check that the weight is greater than or equal to 0 kg
            String status = values.getAsString(AlarmEntry.COLUMN_ALARM_LABEL);
            if (status== null) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(AlarmEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:

                rowsDeleted = database.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);

                break;
            case ALARM_ID:

                selection = AlarmEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALARMS:
                return AlarmEntry.CONTENT_LIST_TYPE;
            case ALARM_ID:
                return AlarmEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

