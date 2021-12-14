package com.example.kitchen.medicinetracker;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ReportList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<String> dayList=new ArrayList<String>();
        dayList.add("Sunday");
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thurdsay");
        dayList.add("Friday");
        dayList.add("Saturday");
        DayAdapter dayAdapter=new DayAdapter(this,dayList);
        ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(dayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ReportList.this,ViewReport.class);
                intent.putExtra("Day",position);
                startActivity(intent);
            }
        });
    }
}
