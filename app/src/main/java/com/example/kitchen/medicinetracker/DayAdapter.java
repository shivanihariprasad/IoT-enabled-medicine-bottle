package com.example.kitchen.medicinetracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shreyas on 22-10-2017.
 */

public class DayAdapter extends ArrayAdapter<String> {
    public DayAdapter(Context context, ArrayList<String> list) {
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.report_list, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        String currentDay="";
        switch (position)
        {
            case 0:currentDay="Sunday";
                break;
            case 1:currentDay="Monday";
                break;
            case 3:currentDay="Wednesday";
                break;
            case 4:currentDay="Thursday";
                break;
            case 5:currentDay="Friday";
                break;
            case 6:currentDay="Saturday";
                break;
            case 2:currentDay="Tuesday";
                break;
        }

        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView dayTextView = (TextView) listItemView.findViewById(R.id.day);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
        dayTextView.setText(currentDay);


        return listItemView;







    }
}
