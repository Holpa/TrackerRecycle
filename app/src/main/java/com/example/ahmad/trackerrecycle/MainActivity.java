package com.example.ahmad.trackerrecycle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.ahmad.trackerrecycle.data.OrdersContract;

import java.util.List;

/**
 * The Tracker Recycle Main Activity
 *
 * MBJ: 2017-Oct-14
 * This activity presents a list of all Orders (Update this with description, most of my understanding
 * of this app is influenced by what I see.. =))
 *
 * MBJ: 2017-Oct-14
 * I've switched this to load from an Sqlite data source for several reasons that we can discuss,
 * if need be, we can move back to an Xml Source, I expect there might be a disconnected player (
 * like a service or something) that this is displaying the data for.  If thats the case, we might be
 * better off building a worker that imports the XML source into the Sqlite database.  Again
 * I can discuss why that might be a better option.
 *
 *
 */
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OrderAdapter.OrderClickListener {

    /**
     * Class Tag for Logging Purposes
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The Order Loader Identifier
     */
    private static final int ORDER_LOADER_ID = 0;

    /**
     * Extra OrderID (used as a key for passing bundle information as part of an intent)
     */
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    /**
     * The Order Adapter
     */
    private OrderAdapter _orderAdapter;

    /**
     * The Recycler View (For displaying orders)
     */
    RecyclerView _recyclerView;

    /**
     * Fired when Activity is first created
     * @param savedInstanceState The Saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOrders);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));

        _orderAdapter = new OrderAdapter(this, this);
        _recyclerView.setAdapter(_orderAdapter);


        /**
            MBJ: 2017-oct-14
            I've added an ItemTouchHelper, I think you'll find this candy pretty awesome.
            What it allows us to do is work with our data in a natural way.  For example,
            swiping a row out of the list to delete it.  Which is how I will wire this up for now,
            we can discuss later how else you can use it.
         */
//        new ItemTouchHelper(
//                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                        //This would be used to move things around in the view
//                        return false;
//                    }
//
//                    /**
//                     * Fires on Swipe Action, we are wiring this up to delete the record form the content provider
//                     * @param viewHolder The ViewHolder (The View holding our record)
//                     * @param direction The Direction Swiped
//                     */
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        /**
//                         * MBJ: 2017-Oct-14
//                         * If you look through the adapter code, you will see that when we bind the
//                         * order to a fragment view, we are pulling the records unique identifier from
//                         * the database and stashing it into a tag.  The tag is a property
//                         * available on every view, which is intended for non-visual use.
//                         *
//                         * In this case, we are using that in order to pass to our content provider
//                         * to uniquely identify a single record, to mark it for deletion.
//                         *
//                         * We can also use it for other things, such as clicking on an order to
//                         * bring up a detail screen, or maybe to modify a piece of data on it.
//                         */
//                        int id = (int) viewHolder.itemView.getTag();
//                        String stringId = Integer.toString(id);
//
//                        /**
//                         * In order to delete, we need to create a content uri,
//                         * this content uri will uniquely identify a single order.
//                         * content://com.example.ahmad.tracerrecycle/orders/[ordernumber]
//                         */
//                        Uri uri = OrdersContract.Orders.CONTENT_URI;
//                        uri = uri.buildUpon().appendEncodedPath(stringId).build();
//
//                        /**
//                         * Because we are resolving the ID as part of the URI, no need to
//                         * specify a custom selection expression and provide the ID that way.
//                         * We could though, if we added a match for the table as a whole,
//                         * though it would implicitly be more dangerous to do so, only because
//                         * failure to pass in a selection expression, or by passing in something
//                         * too broad, you could delete far too much data. (just my opinion though)
//                         */
//                        getContentResolver().delete(uri, null, null);
//
//                        /**
//                         * Restart the loader, to properly re-render (we've deleted some data right?)
//                         */
//                        getSupportLoaderManager().restartLoader(ORDER_LOADER_ID, null, MainActivity.this);
//
//                    }
//
//            }).attachToRecyclerView(_recyclerView);

        //Delete all the data
        getContentResolver().delete(OrdersContract.Orders.CONTENT_URI, null, null);
        TextFilter tf = new TextFilter();
        List<String> orders = tf.getPostOrders("<Title_E>amh123456789amh123456789amh123456789amh123456789amh123456789amh123456789amh123456789</Title_E> <Instructions_E> jump </Instructions_E><Title_E>Mike</Title_E> <Instructions_E> BEER </Instructions_E><Title_E>CAM</Title_E> <Instructions_E> WTH </Instructions_E>");
        for (int i = 0; i < orders.size() / 2; i++) {
            ContentValues cv = new ContentValues();
            cv.put(OrdersContract.Orders.COLUMN_TITLE, orders.get(i));
            cv.put(OrdersContract.Orders.COLUMN_INSTRUCTIONS, orders.get((orders.size() / 2) + i));

            getContentResolver().insert(OrdersContract.Orders.CONTENT_URI, cv);
        }


        /**
         * MBJ: 2017-Oct-14
         * We are going to load all data off the UI thread.  This ensures smooth UI animations etc,
         * for example if the task were more onerous, the user would be none the aware.
         * It's also best practice, and allows the device to more intelligently schedule things it
         * needs to get done. (battery life FTW!)
         */
        getSupportLoaderManager().initLoader(ORDER_LOADER_ID, null, this);

        /**
         * MBJ: 2017-Oct-14
         * Wiring up a click handler for our add record/order button,
         * this will just bring up an activity we can use to add orders.  Not sure if this
         * is in the requirements, but will make it easy to add testing data.
         *
         * We can also make a method that inserts data, similar to how the original source worked as well.
         */
        FloatingActionButton addOrderButton = (FloatingActionButton) findViewById(R.id.fabAdd);
        addOrderButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Fires when user taps the add button (the floating +)
             * @param view The View
             */
            @Override
            public void onClick(View view) {
                Intent addOrderIntent = new Intent(MainActivity.this, AddOrderActivity.class);
                startActivity(addOrderIntent);
            }
        });

        /**
        MBJ: 2017-Oct-14
        I've commented this out, in order to wire-up a recycler adapter.
         The reason I've done this for a few reasons.
         -It will allow implied contextual interaction with individual orders (items displayed
         -It allows the device to allocate enough visual controls that can be seen by the device,
            and then re-binds those existing controls with data as the user scrolls through them)
            This is significantly better for device resources, battery life etc, and plays well
            with the Activity Lifecycle, eg, can be created when the activity is created,
            and when adding a new item, or viewing ane existing one, and then returning, will
            refresh the views with up to date data without having to reconstruct them again.

        GUIMakerControl guiMakerControl = new GUIMakerControl(this);
        guiMakerControl.makeTablePostOrders(this,new TextFilter().getPostOrders("<Title_E>amh</Title_E> <Instructions_E> jump </Instructions_E><Title_E>Mike</Title_E> <Instructions_E> BEER </Instructions_E><Title_E>CAM</Title_E> <Instructions_E> WTH </Instructions_E>"));
        */


    }

    /**
     * Fires after activity has been paused or restarted.
     * This will most often occur when you add a new order, but could also be fired
     * by changing the device orientation, or switching to another app and back
     */
    @Override
    protected void onResume() {
       super.onResume();

        getSupportLoaderManager().restartLoader(ORDER_LOADER_ID, null, this);
    }

    /**
     * MBJ: 2017-Oct-14
     * because the UI has been changed to isolate everything into respective activities, I've removed
     * the on click handler from this activity.
     * The click function will now be the responsibility of the order view.  Which is probably
     * more desirable anyways, as it greatly simplifies your code
     */
//    @Override
//    public void onClick(View view) {
//        ( (ViewFlipper) findViewById(R.id.Main_Activity_View_Flipper)).setDisplayedChild(0);
//
//    }


    /**
     * Creates our AsyncTaskLoader, with a given ID
     * This loader will return orders data as a cursor (which our data adapter binds to)
     * or a null if an error occurs.
     * @param id The Loader Identifier (The constant at the top of the class)
     * @param loaderArgs The Loader Arguments
     * @return The Loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<Cursor>(this) {

            /**
             * The Order Cursor (data)
             */
            Cursor _orderData;

            /**
             * Fires when the loader first starts
             */
            @Override
            protected void onStartLoading() {
                //If we have a cursor, deliver it
                if (_orderData != null) {
                    deliverResult(_orderData);
                } else {
                    //Otherwise force load
                    forceLoad();
                }
            }

            /**
             * This is the asynchronous task, that runs in the background
             * This would be the "heavy" operation that queries the database
             *
             * But the conceptual reason for the isolation isn't just that, it's also because
             * the operation has nothing to do with user interaction.
             * @return The Cursor (The Data Returned)
             */
            @Override
            public Cursor loadInBackground() {
                try {
                    //We are doing a straight, pull everything, so no need to specify projection or selection style arguments
                    return getContentResolver().query(OrdersContract.Orders.CONTENT_URI,
                            null,
                            null,
                            null,
                            OrdersContract.Orders._ID);
                } catch (Exception caught) {
                    Log.e(TAG, "Failed to Load Data");
                    caught.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load, to the registered listener
             * @param data The Data (Result)
             */
            public void deliverResult(Cursor data) {
                _orderData = data;
                super.deliverResult(data);
            }

        };
    }

    /**
     * Fires when the loader has finished its task
     * @param loader The Loader
     * @param data The data generated by the loader
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //pass the new data to the adapter (which will cause the activity to generate a list)
        _orderAdapter.swapCursor(data);
    }

    /**
     * Fires when the loader has been reset, which makes the data go away.
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //remove the data from the adapter, as there is nothing to display
        _orderAdapter.swapCursor(null);
    }

    /**
     * Fires when an order is clicked
     * @param orderId The Order ID
     */
    @Override
    public void oOrderClick(int orderId) {
        Intent detailIntent = new Intent(MainActivity.this, OrderDetailsActivity.class);
        detailIntent.putExtra(MainActivity.EXTRA_ORDER_ID, orderId);
        startActivity(detailIntent);
    }
}
