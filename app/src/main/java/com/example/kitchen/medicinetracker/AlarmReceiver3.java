package com.example.kitchen.medicinetracker;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.kitchen.medicinetracker.data.AlarmContract;
import com.example.kitchen.medicinetracker.data.HeightDbHelper;
import com.example.kitchen.medicinetracker.data.ReportContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Shreyas on 23-10-2017.
 */

public class AlarmReceiver3 extends BroadcastReceiver {
    String taken;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        /* check it pill taken or not
        */
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bottle1");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HeightDbHelper heightDbHelper = new HeightDbHelper(context);
                int closed = dataSnapshot.child("closed").getValue(Integer.class);
                if (closed == 1) {
                    taken = "No";
                }
                if (closed == 0) {
                    SQLiteDatabase db = heightDbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM " + AlarmContract.AlarmEntry.SECOND_TABLE, null);
                    cursor.moveToNext();
                    int ColumnIndex = cursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_HEIGHT);
                    float previousHeight = cursor.getFloat(ColumnIndex);
                    float height = dataSnapshot.child("height").getValue(Float.class);
                    if (height < previousHeight + 0.2 && height > previousHeight - 0.2) {
                        taken = "No";
                    }
                    if(height>previousHeight+0.2)
                    {
                        taken="Yes";
                    }
                }
                addToTable(intent,context);
                reference.child("closed").setValue(1);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //reference.child("closed").setValue(1);
            }


        });
    }
    void addToTable(Intent intent,Context context)
    {int hour=intent.getIntExtra("hour",0);
        int minute=intent.getIntExtra("minute",0);
        String label=intent.getStringExtra("label");
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()-1000*60*2);
        int day=calendar.get(Calendar.DAY_OF_WEEK)-1;
        ContentValues values = new ContentValues();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bottle1");
        values.put(ReportContract.ReportEntry.COLUMN_ALARM_HOURS, hour);
        values.put(ReportContract.ReportEntry.COLUMN_ALARM_MINUTES, minute);
        values.put(ReportContract.ReportEntry.COLUMN_ALARM_DAY, day);
        values.put(ReportContract.ReportEntry.COLUMN_ALARM_TAKEN,taken);
        values.put(ReportContract.ReportEntry.COLUMN_ALARM_LABEL,label);
        Uri newUri = context.getContentResolver().insert(ReportContract.ReportEntry.CONTENT_URI, values);



    }
}
