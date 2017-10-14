package com.example.ahmad.trackerrecycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GUIMakerControl guiMakerControl = new GUIMakerControl(this);
        guiMakerControl.makeTablePostOrders(this,new TextFilter().getPostOrders("<Title_E>amh</Title_E> <Instructions_E> jump </Instructions_E><Title_E>Mike</Title_E> <Instructions_E> BEER </Instructions_E>"));
    }

    @Override
    public void onClick(View view) {
        ( (ViewFlipper) findViewById(R.id.Main_Activity_View_Flipper)).setDisplayedChild(0);

    }

}
