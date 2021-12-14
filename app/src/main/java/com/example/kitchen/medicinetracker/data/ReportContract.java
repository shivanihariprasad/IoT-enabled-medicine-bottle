package com.example.kitchen.medicinetracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Shreyas on 23-10-2017.
 */

public class ReportContract {

    private ReportContract() {}


    public static final String CONTENT_AUTHORITY = "com.example.medicinetracker";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_REPORTS = "reports";


    public static final class ReportEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REPORTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REPORTS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REPORTS;


        public final static String TABLE_NAME = "reports";

        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_ALARM_HOURS ="hours";


        public final static String  COLUMN_ALARM_MINUTES = "minutes";

        public static final String COLUMN_ALARM_DAY="day";
        public final static String COLUMN_ALARM_TAKEN = "taken";
        public final static String COLUMN_ALARM_LABEL="label";

    }
}
