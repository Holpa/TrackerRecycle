package com.example.ahmad.trackerrecycle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The orders Database Helper
 */
public class OrdersDbHelper extends SQLiteOpenHelper {

    /**
     * The Database Filename
     */
    private static final String DATABASE_NAME = "orders.db";

    /**
     * The Database Version
     */
    private static final int VERSION = 2;

    /**
     * Initialize the Orders Database Helper
     * @param context
     */
    OrdersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Fires when the database needs to be created
     * @param db The SQLite Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ORDERS_TABLE = " CREATE TABLE "  + OrdersContract.Orders.TABLE_NAME + " ( " +
                OrdersContract.Orders._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OrdersContract.Orders.COLUMN_TITLE + " TEXT NOT NULL, " +
                OrdersContract.Orders.COLUMN_INSTRUCTIONS + " TEXT NOT NULL " +
                " ); ";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    /**
     * Fires when the database needs to be upgraded (a newer version, eg columns, tables added or removed)
     *
     * MBJ: 2017-Oct-14
     * I should note, that we arent really using this, so if the schema changes, just delete the existing tables,
     * and create them again.
     *
     * @param db The SQLite Database
     * @param oldVersion The current version of the database
     * @param newVersion The new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OrdersContract.Orders.TABLE_NAME);
        onCreate(db);
    }
}
