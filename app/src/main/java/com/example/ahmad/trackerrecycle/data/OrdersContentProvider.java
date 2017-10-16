package com.example.ahmad.trackerrecycle.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * The Orders Content Provider
 */
public class OrdersContentProvider extends ContentProvider {

    /**
     * The Orders URI Match Identifier
     */
    public static final int ORDERS = 100;

    /**
     * The Order with ID URI Match Identifier
     */
    public static final int ORDER_WITH_ID = 101;

    /**
     * The UriMatcher
     */
    private static final UriMatcher _uriMatcher = buildUriMatcher();

    /**
     * The Orders Database Helper
     */
    private OrdersDbHelper _ordersDbHelper;

    /**
     * Build URI Matcher
     *
     * The URI Matcher is used to identify data, it works similarly to URLs you might see on the internet.
     *
     * For example: content://com.example.ahmad.trackerrecycle/orders  would indicate the orders table
     *
     * Whereas: content://com.example.ahmad.trackerrecycle/order/14 would indicate the 14th record, or
     *  the record with an id of 14.  This can be further customized if needed.
     *
     * @return The URI Matcher
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(OrdersContract.AUTHORITY, OrdersContract.PATH_ORDERS, ORDERS);
        uriMatcher.addURI(OrdersContract.AUTHORITY, OrdersContract.PATH_ORDERS + "/#", ORDER_WITH_ID);
        return uriMatcher;
    }

    /**
     * Fires when instance created
     * @return true if content provider created properly
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        _ordersDbHelper = new OrdersDbHelper(context);
        return true;
    }

    /**
     * Query Orders
     * @param uri The Content URI
     * @param projection The Projection, which is the set of columns you want to pull, you should
     *                   always narrow down to exactly what you need.  this is significantly easier
     *                   on the limited device resources we have, and generally makes things perform
     *                   as fast as they can
     * @param selection The selection is a set of conditions, or predicates that narrow down which
     *                  data we want to receive.  for example, filter to orders for today
     * @param selectionArguments    The selection arguments are intended to match the selection expression.
     *                              for example date=? as an expression, the argument should be a date.any time
     * @param sortOrder The sort order is used to determine the order in which records should be
     *                  presented.  A date makes a good choice, as it would for example allow records
     *                  to be displayed in the order they were entered.  If a priority column were
     *                  created, this could be used to display highest priority items first.  Its value
     *                  is just the name of the column.
     * @return The Cursor, the cursor is the object we use to work with the data returned.  This is
     *          a really handy way of looking at the data, because it presents it one record at a time,
     *          which as it turns out works very well with our recycler view used to present the data.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArguments, @Nullable String sortOrder) {

        //Here we are getting a readable version of the database, because we will not be modifying any data
        final SQLiteDatabase db = _ordersDbHelper.getReadableDatabase();
        //We match the uri with the matcher, to determine how we want to look at the data, based on the URI handed in
        int uriMatch = _uriMatcher.match(uri);
        Cursor retCursor;

        switch (uriMatch) {
            case ORDERS:
                retCursor = db.query(OrdersContract.Orders.TABLE_NAME,
                        projection,
                        selection,
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            case ORDER_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = OrdersContract.Orders._ID + "=?";
                selectionArguments = new String[] { id };
                retCursor = db.query(OrdersContract.Orders.TABLE_NAME,
                        projection,
                        selection,
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retCursor;
    }

    /**
     * Insert New Order
     * @param uri The Content URI
     * @param values The Content/Record Values (The Values being inserted)
     * @return The URI of the newly Created Order
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //Here we are getting a writable version of the database, we need to insert a new record
        final SQLiteDatabase db = _ordersDbHelper.getWritableDatabase();
        int uriMatch = _uriMatcher.match(uri);
        Uri retUri;

        switch (uriMatch) {
            case ORDERS:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(OrdersContract.Orders.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    retUri = ContentUris.withAppendedId(OrdersContract.Orders.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retUri;
    }

    /**
     * Delete an Order from the Database
     * @param uri The Content URI
     * @param selection The Selection
     *                  Much like querying for data, you need to focus records to delete.
     *                  If you failed to filter out any records, every record would be deleted.
     * @param selectionArgs The Selection Arguments
     *                      Just as with the querying of data, you must provide arguments to match
     *                      your selection expression.
     * @return The Number of Rows Affected.
     *          You can use the number of rows affected to determine if anything was deleted.
     *
     *          Note, that I am for now, only wiring up a single record delete,
     *          which requires the user to pass in the ID of the order, with the URI.
     *
     *          We can look at more expressive deletes, such as selecting more than one, and then
     *          deleting them in one action a bit later.  Not sure that will be required.
     *
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //Here we are getting a writable version of the database, we need to delete an existing record
        final SQLiteDatabase db = _ordersDbHelper.getWritableDatabase();
        int uriMatch = _uriMatcher.match(uri);
        int ordersDeleted;

        switch (uriMatch) {
            case ORDERS:
                ordersDeleted = db.delete(OrdersContract.Orders.TABLE_NAME, null, null);
                break;
            case ORDER_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                ordersDeleted = db.delete(OrdersContract.Orders.TABLE_NAME,
                        OrdersContract.Orders._ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //Notify the resolver of a data change (we want the UI to react right?
        // This way it wont show data that doesn't still exist...)
        if (ordersDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ordersDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    /**
     * Returns a MIME Type, which will not be needed for this application
     * @param uri The Content Uri
     * @return The MIME Type
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not Implemented");
    }

}
