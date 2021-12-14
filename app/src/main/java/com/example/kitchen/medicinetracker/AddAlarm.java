package com.example.kitchen.medicinetracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ToggleButton;

import com.example.kitchen.medicinetracker.data.AlarmContract;
import com.example.kitchen.medicinetracker.data.ReportContract;

import java.util.Calendar;

    public class AddAlarm extends AppCompatActivity {
        TimePicker alarmTimePicker;
        PendingIntent pendingIntent;
        PendingIntent pendingIntent2;
        PendingIntent pendingIntent3;
        AlarmManager alarmManager;
        AlarmManager manager;
        AlarmManager manager2;
        Button button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_alarm);
            setupUI(findViewById(R.id.parentView));
            Toolbar topToolBar = (Toolbar) findViewById(R.id.toptoolbar);
            setSupportActionBar(topToolBar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Add Reminder");
            alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            button = (Button) findViewById(R.id.add);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAlarm();
                }
            });
        }

        void addAlarm() {
            long time;
            int hour = alarmTimePicker.getHour();
            int min = alarmTimePicker.getMinute();
            int pendingIntentId=saveAlarm(hour, min);
            Calendar calendar = Calendar.getInstance();
            Log.e("pending intent id",pendingIntentId+"");
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), pendingIntentId, intent, 0);
            Intent intent2 = new Intent(getApplicationContext(), AlarmReceiver2.class);
            pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), pendingIntentId+1000, intent2, 0);
            Intent intent3 = new Intent(getApplicationContext(), AlarmReceiver3.class);
            intent3.putExtra("hour",hour);
            intent3.putExtra("minute",min);
            EditText editText=(EditText)findViewById(R.id.label);
            String label=editText.getText().toString().trim();
            if(!TextUtils.isEmpty(label))
            {intent3.putExtra("label",label);}
            else {
                intent3.putExtra("label","Alarm");
            }
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
            manager2.setRepeating(AlarmManager.RTC_WAKEUP,time+1000*60*2,AlarmManager.INTERVAL_DAY,pendingIntent3);
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
            minute = alarmTimePicker.getMinute() + "";
            if (alarmTimePicker.getMinute() < 10) {
                minute = "0" + minute;
            }
            Toast.makeText(AddAlarm.this, "Reminder set to ring " + hour + ":" + minute + " " + am_pm + " everyday", Toast.LENGTH_SHORT).show();
            finish();
            Intent back = new Intent(AddAlarm.this, MainActivity.class);
            startActivity(back);
        }

        int saveAlarm(int hour, int minute) {

            ContentValues values = new ContentValues();
            values.put(AlarmEntry.COLUMN_ALARM_HOURS, hour);
            values.put(AlarmEntry.COLUMN_ALARM_MINUTES, minute);
            EditText editText=(EditText)findViewById(R.id.label);
            String label=editText.getText().toString().trim();
            if(!TextUtils.isEmpty(label))
            {values.put(AlarmEntry.COLUMN_ALARM_LABEL, label);}
            Uri newUri = getContentResolver().insert(AlarmEntry.CONTENT_URI, values);
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
                        hideSoftKeyboard(AddAlarm.this);
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
//        public void OnToggleClicked(View vi/ew)
//        {
//            long time;
//            if (((ToggleButton) view).isChecked())
//            {
//                Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
//                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
//                Intent intent = new Intent(this, AlarmReceiver.class);
//                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//                time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
//                if(System.currentTimeMillis()>time)
//                {
//                    if (calendar.AM_PM == 0)
//                        time = time + (1000*60*60*12);
//                    else
//                        time = time + (1000*60*60*24);
//                }
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
//            }
//            else
//            {
//                alarmManager.cancel(pendingIntent);
//                Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
//            }
//        }


