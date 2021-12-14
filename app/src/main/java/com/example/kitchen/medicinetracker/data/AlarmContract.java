package com.example.kitchen.medicinetracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.provider.BaseColumns;

/**
 * Created by Shreyas on 19-10-2017.
 */

public class AlarmContract {
    private AlarmContract() {}


    public static final String CONTENT_AUTHORITY = "com.example.kitchen.medicinetracker";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_ALARMS = "alarms";


    public static final class AlarmEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ALARMS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALARMS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALARMS;


        public final static String TABLE_NAME = "alarms";

        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_ALARM_HOURS ="hours";


        public final static String  COLUMN_ALARM_MINUTES = "minutes";


        public final static String COLUMN_ALARM_LABEL = "label";
        public final static String SECOND_TABLE="lastHeight";
        public final static String COLUMN_HEIGHT="height";

    }
}


