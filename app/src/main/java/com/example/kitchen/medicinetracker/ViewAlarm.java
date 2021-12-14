package com.example.kitchen.medicinetracker;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry;


public class ViewAlarm extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER = 0;


    AlarmCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alarm);
        getSupportActionBar().setTitle("Your Reminders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView petListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);


        mCursorAdapter = new AlarmCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);


        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                 Intent intent = new Intent(ViewAlarm.this, EditAlarm.class);
                 Uri currentAlarmUri = ContentUris.withAppendedId(AlarmEntry.CONTENT_URI, id);
                 intent.setData(currentAlarmUri);
                 startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                AlarmEntry._ID,
                AlarmEntry.COLUMN_ALARM_HOURS,
                AlarmEntry.COLUMN_ALARM_MINUTES,
        AlarmEntry.COLUMN_ALARM_LABEL};

        return new CursorLoader(this,   // Parent activity context
                AlarmEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                AlarmEntry.COLUMN_ALARM_HOURS+" ASC, "+AlarmEntry.COLUMN_ALARM_MINUTES+" ASC");
                                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
