package com.example.kitchen.medicinetracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.kitchen.medicinetracker.data.ReportContract;

/**
 * Created by Shreyas on 23-10-2017.
 */

public class ReportCursorAdapter extends CursorAdapter{
    public ReportCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.report_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.report_time);

        TextView amPmTextView = (TextView) view.findViewById(R.id.am_pm_report);
        TextView takenTextView=(TextView)view.findViewById(R.id.Taken);
        TextView labelTextView=(TextView)view.findViewById(R.id.label3);

        int hoursColumnIndex = cursor.getColumnIndex(ReportContract.ReportEntry.COLUMN_ALARM_HOURS);
        int minutesColumnIndex = cursor.getColumnIndex(ReportContract.ReportEntry.COLUMN_ALARM_MINUTES);
        int takenColumnIndex=cursor.getColumnIndex(ReportContract.ReportEntry.COLUMN_ALARM_TAKEN);
        //int labelColumnIndex=cursor.getColumnIndex(ReportContract.ReportEntry.COLUMN_ALARM_LABEL);
    //cursor.moveToFirst();
        int hours = cursor.getInt(hoursColumnIndex);
        int minutes = cursor.getInt(minutesColumnIndex);
        String takenText=cursor.getString(takenColumnIndex);
        //String label=cursor.getString(labelColumnIndex);
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
        takenTextView.setText(takenText);
        //labelTextView.setText(label);
    }


}
