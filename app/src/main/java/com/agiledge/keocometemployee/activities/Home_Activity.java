package com.agiledge.keocometemployee.activities;

/**
 * Created by Pateel on 6/14/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.adapter.CustomAndroidGridViewAdapter;
import com.agiledge.keocometemployee.navdrawermenu.EmergencyContactActivity;
import com.agiledge.keocometemployee.navdrawermenu.FeedBackActivity;
import com.agiledge.keocometemployee.navdrawermenu.ManageBookingActivity;
import com.agiledge.keocometemployee.navdrawermenu.TripDetailsActivity;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    Context context;
    ArrayList arrayList;
    String displayname="Hi ",empid="";
    public static String[] gridViewStrings = {
            "Track My Cab",
            "Book Cab",
            "My Trip Details",
            "Emergency Contacts",
            "Feedback",
            "About",


    };
    public static int[] gridViewImages = {
            R.drawable.menu_tracking,
            R.drawable.menu_booking,
            R.drawable.menu_details,
            R.drawable.warning,
            R.drawable.menu_feedback,
            R.drawable.menu_about
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
       toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            displayname+=extras.getString("displayname");
            empid=extras.getString("empid");
        }
        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new CustomAndroidGridViewAdapter(Home_Activity.this, gridViewStrings, gridViewImages));

        initInstances();
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                switch (position) {
                    case 0:
                        Intent map = new Intent(Home_Activity.this, MapClass.class);
                        startActivityForResult(map, 0);
                        break;
                    case 1:
                        Intent book = new Intent(Home_Activity.this, ManageBookingActivity.class);
                        startActivityForResult(book, 0);
                        break;
                    case 2:
                        Intent tripdetails = new Intent(Home_Activity.this, TripDetailsActivity.class);
                        startActivityForResult(tripdetails, 0);
                        break;
                    case 3:
                        Intent emergency = new Intent(Home_Activity.this, EmergencyContactActivity.class);
                        emergency.putExtra("empid",empid);
                        startActivityForResult(emergency, 0);
                        break;
                    case 4:
                        Intent feed = new Intent(Home_Activity.this, FeedBackActivity.class);
                        startActivityForResult(feed, 0);
                        break;
                    case 5:
                        Intent about = new Intent(Home_Activity.this, TrackMyCabActivity.class);
                        startActivityForResult(about, 0);
                        break;

                }
            }
        });


    }

    private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout) findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_android_layout);
        collapsingToolbarLayoutAndroid.setTitle(displayname);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        System.exit(0);

    }
}