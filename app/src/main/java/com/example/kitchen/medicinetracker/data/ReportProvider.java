package com.example.kitchen.medicinetracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Shreyas on 23-10-2017.
 */

public class ReportProvider extends ContentProvider{
    public static final String LOG_TAG = ReportProvider.class.getSimpleName();

    private static final int REPORTS = 100;
    private static final int REPORT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(ReportContract.CONTENT_AUTHORITY, ReportContract.PATH_REPORTS, REPORTS);

        sUriMatcher.addURI(ReportContract.CONTENT_AUTHORITY, ReportContract.PATH_REPORTS + "/#", REPORT_ID);
    }


    private ReportDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ReportDbHelper(getContext());
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
            case REPORTS:

                cursor = database.query(ReportContract.ReportEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case REPORT_ID:
                selection = ReportContract.ReportEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ReportContract.ReportEntry.TABLE_NAME, projection, selection, selectionArgs,
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
            case REPORTS:
                return insertReport(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertReport(Uri uri, ContentValues values) {

        Integer hours = values.getAsInteger(ReportContract.ReportEntry.COLUMN_ALARM_HOURS);
        if (hours == null) {
            throw new IllegalArgumentException("Report requires hours");
        }

        Integer minutes = values.getAsInteger(ReportContract.ReportEntry.COLUMN_ALARM_MINUTES);
        if (minutes == null) {
            throw new IllegalArgumentException("Report requires minutes");
        }

        Integer day = values.getAsInteger(ReportContract.ReportEntry.COLUMN_ALARM_DAY);
        if (day == null) {
            throw new IllegalArgumentException("Report requires day");
        }


        String status = values.getAsString(ReportContract.ReportEntry.COLUMN_ALARM_TAKEN);
        String label=values.getAsString(ReportContract.ReportEntry.COLUMN_ALARM_LABEL);
        if(label==null)
        {
            throw new IllegalArgumentException("Report requires label");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ReportContract.ReportEntry.TABLE_NAME, null, values);

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
            case REPORTS:
                return updateReport(uri, contentValues, selection, selectionArgs);
            case REPORT_ID:

                selection = ReportContract.ReportEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReport(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateReport(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ReportContract.ReportEntry.COLUMN_ALARM_HOURS)) {
            Integer hours = values.getAsInteger(ReportContract.ReportEntry.COLUMN_ALARM_HOURS);
            if (hours == null) {
                throw new IllegalArgumentException("requires hours");
            }
        }

        if (values.containsKey(ReportContract.ReportEntry.COLUMN_ALARM_MINUTES)) {
            Integer gender = values.getAsInteger(ReportContract.ReportEntry.COLUMN_ALARM_MINUTES);
            if (gender == null) {
                throw new IllegalArgumentException("requires minutes");
            }
        }

        if (values.containsKey(ReportContract.ReportEntry.COLUMN_ALARM_DAY)) {
            Integer day = values.getAsInteger(ReportContract.ReportEntry.COLUMN_ALARM_DAY);
            if (day == null) {
                throw new IllegalArgumentException("requires day");
            }
        }

        if (values.containsKey(ReportContract.ReportEntry.COLUMN_ALARM_TAKEN)) {

            String status = values.getAsString(ReportContract.ReportEntry.COLUMN_ALARM_TAKEN);
            if (status== null) {
                throw new IllegalArgumentException("requires status");
            }
        }
        if (values.containsKey(ReportContract.ReportEntry.COLUMN_ALARM_LABEL)) {

            String label = values.getAsString(ReportContract.ReportEntry.COLUMN_ALARM_LABEL);
            if (label== null) {
                throw new IllegalArgumentException("requires label");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(ReportContract.ReportEntry.TABLE_NAME, values, selection, selectionArgs);

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
            case REPORTS:

                rowsDeleted = database.delete(ReportContract.ReportEntry.TABLE_NAME, selection, selectionArgs);

                break;
            case REPORT_ID:

                selection = ReportContract.ReportEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ReportContract.ReportEntry.TABLE_NAME, selection, selectionArgs);
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
            case REPORTS:
                return ReportContract.ReportEntry.CONTENT_LIST_TYPE;
            case REPORT_ID:
                return ReportContract.ReportEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
