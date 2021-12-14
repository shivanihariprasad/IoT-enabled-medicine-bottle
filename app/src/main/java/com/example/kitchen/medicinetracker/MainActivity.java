package com.example.kitchen.medicinetracker;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.app.NotificationCompat;
//import android.view.View;
//import android.widget.Button;
//
//public class MainActivity extends AppCompatActivity {
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////        Button button=(Button)findViewById(R.id.button);
////        startService(new Intent(this,Notificationservice.class));
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this);
////                builder.setSmallIcon(R.drawable.cupcakes);
////                builder.setContentTitle("notify");
////                builder.setContentText("This is my first notofication");
////                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
////                TaskStackBuilder stackBuilder=TaskStackBuilder.create(MainActivity.this);
////                stackBuilder.addParentStack(Main2Activity.class);
////                stackBuilder.addNextIntent(intent);
////                PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
////                builder.setContentIntent(pendingIntent);
////                NotificationManager NM= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                NM.notify(0,builder.build());
////            }
////        });
////    }
//}
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitchen.medicinetracker.data.AlarmContract;
import com.example.kitchen.medicinetracker.data.HeightDbHelper;
import com.example.kitchen.medicinetracker.data.ReportContract;
import com.example.kitchen.medicinetracker.data.ReportDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private HeightDbHelper heightDbHelper=new HeightDbHelper(this);
    private Button button;
    private int pStatus = 0;
    private final float MAX_HEIGHT=7.1f;
    TextView tv;
    float height=0;
    Handler handler=new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
       // Log.e("ji",calendar.get(Calendar.DAY_OF_WEEK)+"");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAlarm.class);
                startActivity(intent);
            }
        });
//        ReportDbHelper reportDbHelper=new ReportDbHelper(this);
//        SQLiteDatabase liteDatabase=reportDbHelper.getWritableDatabase();
//        String[] string={"6"};
//        liteDatabase.delete(ReportContract.ReportEntry.TABLE_NAME,ReportContract.ReportEntry.COLUMN_ALARM_DAY+"=?",string);
        NavigationView nv = (NavigationView)findViewById(R.id.nv1);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case(R.id.home):
                        //Toast.makeText(MainActivity.this,"abcde",Toast.LENGTH_SHORT).show();
                        return true;
                    case(R.id.view_alamrs):
                        Intent intent=new Intent(MainActivity.this,ViewAlarm.class);
                        startActivity(intent);
                        return true;
                    case (R.id.view_reports):
                        Intent intent_second=new Intent(MainActivity.this,ReportList.class);
                        startActivity(intent_second);
                        return true;
                }
                return false;
            }
        });

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button=(Button)findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();

            }
        });
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("bottle1").child("height");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                height = dataSnapshot.getValue(Float.class);
//                SQLiteDatabase db2 = heightDbHelper.getReadableDatabase();
//                Cursor cursor = db2.rawQuery("SELECT * FROM " + AlarmContract.AlarmEntry.SECOND_TABLE, null);
//                cursor.moveToNext();
//                int ColumnIndex = cursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_HEIGHT);
//                float previousHeight = cursor.getFloat(ColumnIndex);
//                Log.e("prev",previousHeight+"");
                setUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
                                                 }


    void setUI()
    {   pStatus=0;
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
       // mProgress.setProgress(0);   // Main Progress
        //mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

      /*  ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", 0, 100);
        animation.setDuration(50000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();*/
        final float percentage=(100*(MAX_HEIGHT-height)/MAX_HEIGHT);
        tv = (TextView) findViewById(R.id.tv);
        //tv.setText(percentage+"");
        //mProgress.setProgress((int)percentage);
        //mProgress.setSecondaryProgress(100-(int)percentage);
        new Thread(new Runnable() {

            @Override
            public void run() {

                while (pStatus <percentage) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            mProgress.setProgress(pStatus);
                            tv.setText(pStatus + "%");

                        }
                    });
                    try {

                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        TextView alert=(TextView)findViewById(R.id.alert);
        if(percentage<=15.0)
        {   if(percentage==0)
        {alert.setText("There are no more pills");}
            alert.setVisibility(View.VISIBLE);
        }
        else {
        alert.setVisibility(View.INVISIBLE);
        }
    }
void refresh()
{
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("bottle1").child("height");
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            height=dataSnapshot.getValue(Float.class);
            Log.e("Quantity",height+"");
//            Resources res = getResources();
//            Drawable drawable = res.getDrawable(R.drawable.circular);
//            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
//            // mProgress.setProgress(0);   // Main Progress
//            //mProgress.setSecondaryProgress(100); // Secondary Progress
//            mProgress.setMax(100); // Maximum Progress
//            mProgress.setProgressDrawable(drawable);
//
//            mProgress.setProgress(0);
//            tv = (TextView) findViewById(R.id.tv);
//            tv.setText("0%");
            setUI();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
@Override
    public boolean onOptionsItemSelected(MenuItem item)
{
    if(mToggle.onOptionsItemSelected(item)) {return true;}


    return super.onOptionsItemSelected(item);

}
}


