package com.agiledge.keocometemployee.activities;

/**
 * Created by Pateel on 6/14/2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.adapter.CustomAndroidGridViewAdapter;
import com.agiledge.keocometemployee.navdrawermenu.AboutActivity;
import com.agiledge.keocometemployee.navdrawermenu.EmergencyContactActivity;
import com.agiledge.keocometemployee.navdrawermenu.FeedBackActivity;
import com.agiledge.keocometemployee.navdrawermenu.ManageBookingActivity;
import com.agiledge.keocometemployee.navdrawermenu.TripDetailsActivity;
import com.agiledge.keocometemployee.utilities.PromptDialog;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    Context context;
    ArrayList arrayList;
    String android_id="";
    ImageView profile;
    boolean startapp=false;
    private static final int REQUEST_CODE=0;
    String displayname="Hi ",empid="",gender="",user_type="";
    public static String[] gridViewStrings = {
            "Track Cab",
            "Book Cab",
            "Trip Details",
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

        profile=(ImageView) findViewById(R.id.profile_image);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            displayname+=extras.getString("displayname");
            Log.d("name",displayname);
            empid=extras.getString("empid");
            gender=extras.getString("gender");
            user_type=extras.getString("user_type");
        }
        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new CustomAndroidGridViewAdapter(Home_Activity.this, gridViewStrings, gridViewImages));

        initInstances();
        setprofile();
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                switch (position) {
                    case 0:
                        Intent map = new Intent(Home_Activity.this, TrackMyCabActivity.class);

                        boolean isEnabled = isGPSenabled();
                        if (isEnabled) {
                            map.putExtra("empid", empid);
                            map.putExtra("user_type", user_type);
                            startActivityForResult(map, 0);
                        }



                        else {
                            buildAlertMessageNoGps();



                        }
                        break;
                    case 1:
                        Intent book = new Intent(Home_Activity.this, ManageBookingActivity.class);
                        book.putExtra("user_type",user_type);
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
                        Intent about = new Intent(Home_Activity.this, AboutActivity.class);
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
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onBackPressed()
    {
        {
            new PromptDialog.Builder(Home_Activity.this)
                    .setTitle("Exit Application")

                    .setTitleColor(Color.WHITE)
                    .setCanceledOnTouchOutside(false)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setButton1TextColor(R.color.md_blue_400)
                    .setButton2TextColor(R.color.md_blue_400)

                    .setMessage("Are you sure want to Exit?")
                    .setButton1("OK", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            finish();
                            System.exit(0);
                        }

                    })
                    .setButton2("Cancel", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }

                    })
                    .show();
        }
//        super.onBackPressed();
//        System.exit(0);

    }
   private void setprofile(){

           if(gender.equalsIgnoreCase("F")){
          profile.setImageResource(R.drawable.avatar_female);
       }
       else
               profile.setImageResource(R.drawable.avatar_male);

   }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


    }


    private boolean isGPSenabled()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        final Dialog dialog = new Dialog(Home_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gpspopup);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                launchGPSOptions();



            }
        });
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();

//                Intent back = new Intent(Home_Activity.this, Home_Activity.class);
//                startActivity(back);
            }
        });
        dialog.show();
    }
    private void launchGPSOptions()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_CODE);

    }
}