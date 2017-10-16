package com.example.ahmad.trackerrecycle;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmad.trackerrecycle.data.OrdersContract;

public class AddOrderActivity extends AppCompatActivity {

    /**
     * The Title EditText Field
     */
    EditText titleEditText;

    /**
     * The Instructions EditText Field
     */
    EditText instructionsEditText;

    /**
     * Fires when created Activity
     * @param savedInstanceState The Saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        titleEditText = (EditText) findViewById(R.id.et_order_title);
        instructionsEditText = (EditText) findViewById(R.id.et_order_instructions);

    }

    /**
     * This is wired up to the Create Button, you can re-assign it in the layout markup
     * This method has a few purposes,
     * First of all, it exists to allow the user to save data entered into our SQLite database,
     * Second of all, I've put in some very sparse validation, because I've assumed both
     * fields are required, this will present a helpful "toast" message, to tell the user the
     * fields must be entered before attempting to save/
     * @param view The View
     */
    public void onClickedCreateOrder(View view) {

        //First grab the user entered values, and validate them.  In this case, they must have data
        String titleInput = titleEditText.getText().toString();
        String instructionsInput = instructionsEditText.getText().toString();
        if (titleInput.length() == 0) {
            Toast.makeText(getBaseContext(), "Title must be entered", Toast.LENGTH_LONG).show();
            return;
        }
        if (instructionsInput.length() == 0) {
            Toast.makeText(getBaseContext(), "Instructions must be entered", Toast.LENGTH_LONG).show();
            return;
        }

        //If we've gotten this far, the data entered is "sane", so pack them in a content values set
        ContentValues values = new ContentValues();
        values.put(OrdersContract.Orders.COLUMN_TITLE, titleInput);
        values.put(OrdersContract.Orders.COLUMN_INSTRUCTIONS, instructionsInput);

        Uri newOrderUri = getContentResolver().insert(OrdersContract.Orders.CONTENT_URI, values);
        if (newOrderUri != null) {
            Toast.makeText(getBaseContext(),
                    "Saved new Order: " + newOrderUri.toString(),
                    Toast.LENGTH_LONG);
        }

        //This activity is done, call finish to clean it up, we should be back in our main UI, which
        //will now show our new order.
        finish();
    }

}
