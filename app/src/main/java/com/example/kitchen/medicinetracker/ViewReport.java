package com.example.kitchen.medicinetracker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kitchen.medicinetracker.data.AlarmContract;
import com.example.kitchen.medicinetracker.data.ReportContract;

public class ViewReport extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int REPORT_LOADER = 0;
    int day;


    ReportCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Intent intent=getIntent();
        day=intent.getIntExtra("Day",1);
        Log.e("Day",day+"");
        getSupportActionBar().setTitle("Day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView reportListView = (ListView) findViewById(R.id.report_list);




        mCursorAdapter = new ReportCursorAdapter(this, null);
        reportListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(REPORT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ReportContract.ReportEntry._ID,
                ReportContract.ReportEntry.COLUMN_ALARM_HOURS,
                ReportContract.ReportEntry.COLUMN_ALARM_MINUTES,
                ReportContract.ReportEntry.COLUMN_ALARM_DAY,
                ReportContract.ReportEntry.COLUMN_ALARM_TAKEN};
        String[] selectionArgs={day+""};
        return new CursorLoader(this,   // Parent activity context
                ReportContract.ReportEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                ReportContract.ReportEntry.COLUMN_ALARM_DAY+" = ?",                   // No selection clause
                selectionArgs,                   // No selection arguments

                ReportContract.ReportEntry.COLUMN_ALARM_HOURS+" ASC, "+ ReportContract.ReportEntry.COLUMN_ALARM_MINUTES+" ASC");
        // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}


