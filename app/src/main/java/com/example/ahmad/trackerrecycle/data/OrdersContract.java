package com.example.ahmad.trackerrecycle.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * The Orders Data Contract
 */
public class OrdersContract {

    /**
     * The Content Authority
     */
    public static final String AUTHORITY = "com.example.ahmad.trackerrecycle";

    /**
     * The Base Content Authority
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * The Orders Path
     */
    public static final String PATH_ORDERS = "orders";

    /**
     * The Orders Table Definition
     */
    public static final class Orders implements BaseColumns {

         /**
         * The Orders Content URI
         */
        public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ORDERS)
                .build();

        /**
         * The Orders Table Name
         */
        public static final String TABLE_NAME = "orders";

        /**
         * The Title Column Name
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * The Instructions Column Name
         */
        public static final String COLUMN_INSTRUCTIONS = "instructions";



    }

}
