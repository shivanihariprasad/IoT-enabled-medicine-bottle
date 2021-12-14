package com.example.kitchen.medicinetracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry;

import java.util.Calendar;

import static com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry.CONTENT_URI;
import static com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry._ID;

public class EditAlarm extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private Uri mCurrentAlarmUri;
    private Button deleteButton;
    private Button editButton;
    private PendingIntent pendingIntent;
    private PendingIntent pendingIntent2;
    private PendingIntent pendingIntent3;
    TimePicker picker;
    AlarmManager alarmManager;
    AlarmManager manager;
    AlarmManager manager_third;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        setupUI(findViewById(R.id.parent_view));
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toptoolbar2);
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Reminder");
        Intent intent = getIntent();
        mCurrentAlarmUri = intent.getData();
        getLoaderManager().initLoader(0, null, this);
        deleteButton = (Button) findViewById(R.id.delete);
        editButton=(Button)findViewById(R.id.save);
        editText=(EditText)findViewById(R.id.edit_label);
        String path = mCurrentAlarmUri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        final int id = Integer.parseInt(idStr);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm(id);
                Toast.makeText(EditAlarm.this,"Alarm deleted",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm(id);
                addAlarm();

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                _ID,
                AlarmEntry.COLUMN_ALARM_HOURS,
                AlarmEntry.COLUMN_ALARM_MINUTES,
                AlarmEntry.COLUMN_ALARM_LABEL,
                 };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentAlarmUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        picker=(TimePicker)findViewById(R.id.editTimePicker);
        if(cursor.moveToFirst()) {
            int hourColumnIndex = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_HOURS);
            int minuteColumnIndex = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_MINUTES);
            int statusColumnIndex = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_LABEL);


            // Extract out the value from the Cursor for the given column index
            int hour = cursor.getInt(hourColumnIndex);
            int minute = cursor.getInt(minuteColumnIndex);
            String label = cursor.getString(statusColumnIndex);

            picker.setHour(hour);
            picker.setMinute(minute);
            editText.setText(label);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void deleteAlarm(int id)
    {   alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager_third=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
        Log.e("abc",id+"");

        alarmManager.cancel(pendingIntent);

        Intent intent2=new Intent(getApplicationContext(),AlarmReceiver2.class);
        pendingIntent2=PendingIntent.getBroadcast(getApplicationContext(),id+1000,intent2,0);
        manager.cancel(pendingIntent2);

        Intent intent3=new Intent(getApplicationContext(),AlarmReceiver3.class);
        pendingIntent3=PendingIntent.getBroadcast(getApplicationContext(),id+2000,intent3,0);
        manager_third.cancel(pendingIntent3);
        Log.e("uri",mCurrentAlarmUri.toString());
       int rowsDeleted =getContentResolver().delete(mCurrentAlarmUri,null,null);
        Log.e("tag",""+rowsDeleted);



    }
    void addAlarm() {
        long time;
        picker=(TimePicker)findViewById(R.id.editTimePicker);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager_third=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int hour = picker.getHour();
        int min = picker.getMinute();
        int pendingIntentId=saveAlarm(hour, min);
        Calendar calendar = Calendar.getInstance();
        Log.e("pending intent id",pendingIntentId+"");
        calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
        calendar.set(Calendar.MINUTE, picker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), pendingIntentId, intent, 0);
        Intent intent2 = new Intent(getApplicationContext(), AlarmReceiver2.class);
        pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), pendingIntentId+1000, intent2, 0);
        Intent intent3 = new Intent(getApplicationContext(), AlarmReceiver3.class);
        pendingIntent3 = PendingIntent.getBroadcast(getApplicationContext(), pendingIntentId+2000, intent3, 0);
        Log.e("cal", calendar.getTimeInMillis() + "");
        Log.e("sys", System.currentTimeMillis() + "");

        time = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > time) {
            Log.e("Check", "true");
            time = time + (1000 * 60 * 60 * 24);

            // time = time + (1000*60*60*24);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, time+1000*60*1, AlarmManager.INTERVAL_DAY, pendingIntent2);
        manager_third.setRepeating(AlarmManager.RTC_WAKEUP,time+1000*60*2,AlarmManager.INTERVAL_DAY,pendingIntent3);
        String am_pm = "AM";
        String minute;
        if (hour == 12) {
            am_pm = "PM";
        }
        if (hour == 0) {
            hour = 12;
        }

        if (hour > 12) {
            hour = hour - 12;
            am_pm = "PM";
        }
        minute = picker.getMinute() + "";
        if (picker.getMinute() < 10) {
            minute = "0" + minute;
        }
        Toast.makeText(EditAlarm.this, "Reminder modified to ring at " + hour + ":" + minute + " " + am_pm + " everyday", Toast.LENGTH_SHORT).show();
        finish();
        Intent back = new Intent(EditAlarm.this, ViewAlarm.class);
        startActivity(back);
    }

    int saveAlarm(int hour, int minute) {

        ContentValues values = new ContentValues();
        values.put(AlarmEntry.COLUMN_ALARM_HOURS, hour);
        values.put(AlarmEntry.COLUMN_ALARM_MINUTES, minute);
        String label=editText.getText().toString().trim();
        if(!TextUtils.isEmpty(label))
        {values.put(AlarmEntry.COLUMN_ALARM_LABEL, label);}
        Uri newUri = getContentResolver().insert(AlarmEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
//        if (newUri == null) {
//            // If the new content URI is null, then there was an error with insertion.
//            Toast.makeText(this, "Failed",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            // Otherwise, the insertion was successful and we can display a toast.
//            Toast.makeText(this, "Success",
//                    Toast.LENGTH_SHORT).show();
//        }

        String path = newUri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        int id = Integer.parseInt(idStr);
        Log.e("abc",id+"");
        return id;}

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(EditAlarm.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}