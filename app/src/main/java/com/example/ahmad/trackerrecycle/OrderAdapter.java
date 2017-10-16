package com.example.ahmad.trackerrecycle;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmad.trackerrecycle.data.OrdersContract;

/**
 * Created by mike on 2017-10-14.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    /**
     * The Data Cursor
     */
    private Cursor _cursor;

    /**
     * The Activity Context
     */
    private Context _context;

    final private OrderClickListener _onClickListener;

    /**
     * Initialize the Order Adapter
     * @param context The Activity Context
     * @param clickListener The Click Listener
     */
    public OrderAdapter(Context context, OrderClickListener clickListener) {
        this._context = context;
        this._onClickListener = clickListener;
    }

    /**
     * Called when ViewHolder is created to fill the recycler viewer
     * @param parent The Parent ViewGroup
     * @param viewType The View Type
     * @return A new OrderViewHolder, which references the view for a given item
     */
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context)
                .inflate(R.layout.order_layout, parent, false);

        return new OrderViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the cursor
     * @param holder Te ViewHolder to bind the cursor data to
     * @param position the position of the data in the cursor
     */
    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

        /**
         * MBJ: 2017-Oct-14
         * Each column, or piece of data in a record is considered a column.  you can think of the
         * results as a whole, like a spreadsheet.
         * So every row is a record, and every column is a part of that record.
         * What we need to do is determine which column belongs to which piece of data.
         * Since we can change the schema (or layout) of the database in future versions,
         * it would be a bad idea to use numbers, because then we would have to regression
         * test everything that works with the data,
         */
        int idIndex = _cursor.getColumnIndex(OrdersContract.Orders._ID);
        int titleIndex = _cursor.getColumnIndex(OrdersContract.Orders.COLUMN_TITLE);
        int instructionsIndex = _cursor.getColumnIndex(OrdersContract.Orders.COLUMN_INSTRUCTIONS);

        //This is how we extract the data we want from the record.  Now the data is stored in
        //standard variables, so you can do whatever you want with it
        _cursor.moveToPosition(position);
        final int id = _cursor.getInt(idIndex);
        String title = _cursor.getString(titleIndex);
        String instructions = _cursor.getString(instructionsIndex);

        //We set the record identifier to the view's tag, so we can easily access it from the
        //swipe action, so we can issue a delete
        holder.itemView.setTag(id);

        /**
         * MBJ: 2017-Oct-14
         * Now we can bind the UI elements for the record display view, in any way we want.
         *
         * It's important to note, now that the display of each record is isolated to it's own
         * layout fragment, you can add as many widgets as you want, such as pictures, numbers,
         * change colors etc, and all that logic is done here in the adapter.
         *
         * So making such changes won't complicate the rest of the application =)
         *
         */
        holder.titleTextView.setText(title);

    }

    /**
     * Returns the number of items to display
     * @return the number of items to display
     */
    @Override
    public int getItemCount() {
        if (_cursor == null) {
            return 0;
        }
        return _cursor.getCount();
    }

    /**
     * When we re-query data, such as when the data changes (add a record, delete a record etc)
     * we can use this method to change the adapter bound cursor with a new one.  This will
     * allow it to rebind existing views to the new data.  This is significantly easier on the device.
     * @param cursor The new Cursor
     * @return The cursor bound to the adapter
     */
    public Cursor swapCursor(Cursor cursor) {
        //We are checking if the cursor handed in is already bound,
        //if it is, no point in doing anything
        if (_cursor == cursor) {
            //Return null, indicating that nothing has changed
            return null;
        }

        //Assign the new cursor
        Cursor temp = _cursor;
        this._cursor = cursor;

        //If the cursor is valid, notify the activity that the data has changed, so it can re-render
        if (_cursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    /**
     * Interface that receives onClick messages
     */
    public interface OrderClickListener {

        /**
         * Fires on List Item Click
         * @param orderId The Order ID
         */
        void oOrderClick(int orderId);
    }

    /**
     * The Order ViewHolder
     */
    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * The Title TextView
         */
        TextView titleTextView;

        /**
         * Initialize the Order ViewHolder
         * @param itemView The view inflated in onCreateViewHolder
         */
        public OrderViewHolder(View itemView) {
            super(itemView);


            titleTextView = itemView.findViewById(R.id.tv_order_title);

            //Wire-up the click listener when creating the view holder
            itemView.setOnClickListener(this);

        }

        /**
         * This fires when the user clicks an item
         * @param view The View Clicked
         */
        @Override
        public void onClick(View view) {
            //This just routes the index of the clicked item, so we can handle that code in our main activity
            //int clickedPosition = getAdapterPosition();
            _onClickListener.oOrderClick((int)view.getTag());
        }
    }

}
