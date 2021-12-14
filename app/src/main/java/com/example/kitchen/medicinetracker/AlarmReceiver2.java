package com.example.kitchen.medicinetracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;

import com.example.kitchen.medicinetracker.data.AlarmContract;
import com.example.kitchen.medicinetracker.data.HeightDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Shreyas on 22-10-2017.
 */

public class AlarmReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        final HeightDbHelper heightDbHelper=new HeightDbHelper(context);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("bottle1");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int closed=dataSnapshot.child("closed").getValue(Integer.class);
                if(closed==1)
                {
                    displayNotification(context);
                }
                if(closed==0)
                {
                    SQLiteDatabase db=heightDbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM " + AlarmContract.AlarmEntry.SECOND_TABLE, null);
                    cursor.moveToNext();
                    int ColumnIndex = cursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_HEIGHT);
                    float previousHeight=cursor.getFloat(ColumnIndex);
                    float height=dataSnapshot.child("height").getValue(Float.class);
                    //if(height<=previousHeight)
                        if (height < previousHeight + 0.2 && height > previousHeight - 0.2) {
                            displayNotification(context);
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    void displayNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        long when = System.currentTimeMillis();

        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //if we want ring on notifcation then uncomment below line//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                setSmallIcon(R.drawable.pill_image).
                setContentIntent(pendingIntent).
                setContentText("Seems like you still haven't taken your pills!").
                setContentTitle("Pill Reminder")
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        ;
//                setSound(alarmSound).

        notificationManager.notify(100,builder.build());
    }
}
