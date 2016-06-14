package com.agiledge.keocometemployee.activities;

/**
 * Created by Pateel on 6/14/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.adapter.CustomAndroidGridViewAdapter;
import com.agiledge.keocometemployee.navdrawermenu.AboutActivity;
import com.agiledge.keocometemployee.navdrawermenu.EmergencyContactActivity;
import com.agiledge.keocometemployee.navdrawermenu.FeedBackActivity;
import com.agiledge.keocometemployee.navdrawermenu.ManageBookingActivity;
import com.agiledge.keocometemployee.navdrawermenu.TripDetailsActivity;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    Context context;
    ArrayList arrayList;

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
            R.drawable.menu_emergency,
            R.drawable.menu_feedback,
            R.drawable.menu_about
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        final SharedPreferences sharedpref=Home_Activity.this.getPreferences(Context.MODE_PRIVATE);
        String username=sharedpref.getString("APP_USERNAME","NOT_FOUND");
        Log.d("check username",username);
        Toast.makeText(getApplicationContext(),""+username,Toast.LENGTH_LONG).show();
     //   toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
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
                        startActivityForResult(emergency, 0);
                        break;
                    case 4:
                        Intent feed = new Intent(Home_Activity.this, FeedBackActivity.class);
                        startActivityForResult(feed, 0);
                        break;
                    case 5:
                        Intent about = new Intent(Home_Activity.this, AboutActivity.class);
                        startActivityForResult(about, 0);
                        break;

                }
            }
        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                if (position == 0) {
//                    Intent myIntent = new Intent(view.getContext(), MapClass.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 1) {
//                    Intent myIntent = new Intent(view.getContext(), ManageBookingActivity.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 2) {
//                    Intent myIntent = new Intent(view.getContext(), TripDetailsActivity.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 3) {
//                    Intent myIntent = new Intent(view.getContext(), EmergencyContactActivity.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 4) {
//                    Intent myIntent = new Intent(view.getContext(), FeedBackActivity.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 5) {
//                    Intent myIntent = new Intent(view.getContext(), AboutActivity.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 6) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 7) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                    startActivityForResult(myIntent, 0);
//                }
//            }
//        });

    }

    private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout) findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_android_layout);
        collapsingToolbarLayoutAndroid.setTitle("RideIT");
    }

}