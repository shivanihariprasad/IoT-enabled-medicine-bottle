package com.example.kitchen.medicinetracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.kitchen.medicinetracker.data.AlarmContract;
import com.example.kitchen.medicinetracker.data.HeightDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Shreyas on 19-10-2017.
 */

public class AlarmReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        long when = System.currentTimeMillis();

        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //if we want ring on notifcation then uncomment below line//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                setSmallIcon(R.drawable.pill_image).
                setContentIntent(pendingIntent).
                setContentText("Time to take your pills").
                setContentTitle("Pill Reminder")
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        ;
//                setSound(alarmSound).

        notificationManager.notify(100,builder.build());
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bottle1");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float height=dataSnapshot.child("height").getValue(Float.class);
                HeightDbHelper heightDbHelper=new HeightDbHelper(context);

                SQLiteDatabase db=heightDbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(AlarmContract.AlarmEntry.COLUMN_HEIGHT,height);
                db.delete(AlarmContract.AlarmEntry.SECOND_TABLE,null,null);
                db.insert(AlarmContract.AlarmEntry.SECOND_TABLE,null,values);
                SQLiteDatabase db2 = heightDbHelper.getReadableDatabase();
                Cursor cursor = db2.rawQuery("SELECT * FROM " + AlarmContract.AlarmEntry.SECOND_TABLE, null);
                cursor.moveToNext();
                int ColumnIndex = cursor.getColumnIndex(AlarmContract.AlarmEntry.COLUMN_HEIGHT);
                float previousHeight = cursor.getFloat(ColumnIndex);
               // reference.child("prev").setValue(previousHeight);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
