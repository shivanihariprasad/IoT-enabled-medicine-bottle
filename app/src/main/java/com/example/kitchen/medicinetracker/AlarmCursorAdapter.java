package com.example.kitchen.medicinetracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.kitchen.medicinetracker.data.AlarmContract.AlarmEntry;

/**
 * Created by Shreyas on 20-10-2017.
 */

public class AlarmCursorAdapter extends CursorAdapter {
    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.time);

            TextView amPmTextView = (TextView) view.findViewById(R.id.am_pm);
        TextView label=(TextView)view.findViewById(R.id.label2);

        int hoursColumnIndex = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_HOURS);
        int minutesColumnIndex = cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_MINUTES);
        int labelColumnIndex=cursor.getColumnIndex(AlarmEntry.COLUMN_ALARM_LABEL);

        int hours = cursor.getInt(hoursColumnIndex);
        int minutes = cursor.getInt(minutesColumnIndex);
        String labelText=cursor.getString(labelColumnIndex);

        String am_pm="AM";
        String min_string;

        if(hours==12)
        {am_pm="PM";}
        if(hours==0){
            hours=12;
        }
        if(hours>12) {
            hours -= 12;
            am_pm = "PM";
        }
        if(minutes<10)
        {min_string="0"+minutes;}
        else {
            min_string = minutes + "";
        }


        nameTextView.setText(hours+" : "+min_string+" ");
        amPmTextView.setText(am_pm);
        label.setText(labelText);

    }
}
