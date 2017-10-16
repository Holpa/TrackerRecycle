package com.example.ahmad.trackerrecycle;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.ahmad.trackerrecycle.data.OrdersContract;

import org.w3c.dom.Text;

/**
 * The Order Details Activity
 *
 * For now, just setting this up to show the order record (all fields, not just those shown in list)
 */
public class OrderDetailsActivity extends AppCompatActivity {

    /**
     * Tag for Logging Purposes
     */
    public static final String TAG = OrderDetailsActivity.class.getSimpleName();

    /**
     * The Title Text View
     */
    TextView titleTextView;

    /**
     * The Instructions TextView
     */
    TextView instructionsTextView;

    /**
     * Fires on Activity Create
     * @param savedInstanceState The Saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * MBJ: 2017-Oct-14
         * I obviously just sped though this, without following any sort of convention to get some
         * data up on the screen.  So Much work here I guess...
         *
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        titleTextView = (TextView) findViewById(R.id.tv_order_title);
        instructionsTextView = (TextView) findViewById(R.id.tv_order_instructions);

        Intent intent = getIntent();
        int orderId = intent.getIntExtra(MainActivity.EXTRA_ORDER_ID, 0);

        Uri uri = OrdersContract.Orders.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(orderId)).build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, OrdersContract.Orders._ID);

        try {
            cursor.moveToFirst();
            int titleIndex = cursor.getColumnIndex(OrdersContract.Orders.COLUMN_TITLE);
            int instructionsIndex = cursor.getColumnIndex(OrdersContract.Orders.COLUMN_INSTRUCTIONS);
            titleTextView.setText(cursor.getString(titleIndex));
            instructionsTextView.setText(cursor.getString(instructionsIndex));
        } catch (Exception caught) {
            caught.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        return true;
    }
}
