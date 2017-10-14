package com.example.ahmad.trackerrecycle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * Created by Ahmad on 10/13/2017.
 */

public class GUIMakerControl implements View.OnClickListener {
    public Activity activity;
    public List<String> pO; // Post Orders List
    public GUIMakerControl(Activity _activity){
        this.activity = _activity;
    }


    //                <View
//    android:id="@+id/line1"
//    android:layout_width="match_parent"
//    android:layout_height="10dip"
//    android:layout_weight="1"
//    android:background="#000000"
//    android:padding="2dip" />
    public void makeTablePostOrders(Context ct, List<String> postOrders)
    {
        pO= postOrders;
        //Post order TITLE
        for(int i = 0; i < postOrders.size()/2 ; i++) {
            //make View
            View divider = new View(ct);
            divider.setBackgroundColor(Color.BLACK);
            divider.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,1));
            divider.setBottom(1);
            //make tableRow
            TableRow tableRow = new TableRow(ct);
//            tableRow.setClickable(true);
//            tableRow.setOnClickListener(this);
            TableRow tableDivider = new TableRow(ct);
            // make LinearLayout
            //make TextView
            TextView textView_Title = new TextView(ct);
            textView_Title.setText(postOrders.get(i));
            textView_Title.setTextColor(Color.BLACK);
            textView_Title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1f));
            // make button
            Button bt_View = new Button(ct);
            bt_View.setTag("PostOrder_btn_" + Integer.toString(i));
            bt_View.setId(i+postOrders.size()/2);
            bt_View.setText("Select");
            bt_View.setOnClickListener(this);
            //adding views
            tableRow.addView(textView_Title);
            tableRow.addView(bt_View);
//            tableDivider.addView(divider);

            ((TableLayout) this.activity.findViewById(R.id.Main_Activity_PostOrders_Table)).addView(tableRow, i + 1);
            ((TableLayout) this.activity.findViewById(R.id.Main_Activity_PostOrders_Table)).addView(tableDivider, i + 2);

        }
    }



    @Override
    public void onClick(View view) {
        ((TextView) this.activity.
                findViewById(R.id.Main_Activity_PostOrders_Description_Text_View)).
                setText(pO.get(view.getId()));
        ( (ViewFlipper) this.activity.findViewById(R.id.Main_Activity_View_Flipper)).setDisplayedChild(1);

    }
}
