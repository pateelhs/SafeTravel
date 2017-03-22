package com.agiledge.keocometemployee.activities;

/**
 * Created by Pateel on 6/14/2016.
 */

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
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

import com.agiledge.keocometemployee.GCM.QuickstartPreferences;
import com.agiledge.keocometemployee.GCM.RegistrationIntentService;
import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.adapter.CustomAndroidGridViewAdapter;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.navdrawermenu.EmergencyContactActivity;
import com.agiledge.keocometemployee.navdrawermenu.FeedBackActivity;
import com.agiledge.keocometemployee.navdrawermenu.ManageBookingActivity;
import com.agiledge.keocometemployee.navdrawermenu.TripDetailsActivity;
import com.agiledge.keocometemployee.utilities.PromptDialog;
import com.eggheadgames.siren.ISirenListener;
import com.eggheadgames.siren.Siren;
import com.eggheadgames.siren.SirenAlertType;
import com.eggheadgames.siren.SirenVersionCheckType;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home_Activity extends AppCompatActivity {
    private static final String TAG = "Home_Activity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    Context context;
    ArrayList arrayList;
    String android_id="";
    ImageView profile;
    Typeface type;
    boolean startapp=false;
    private static final int REQUEST_CODE=0;
    String displayname="Hi ",empid="",gender="",user_type="";
    public static String[] gridViewStrings = {
            "Track Bus",
            "Book Cab",
            "Trip Details",
            "Emergency Contacts",
            "Feedback",
            "Late Night Track cab"


    };
    public static int[] gridViewImages = {
            R.drawable.menu_tracking,
            R.drawable.menu_booking,
            R.drawable.menu_details,
            R.drawable.warning,
            R.drawable.menu_feedback,
            R.drawable.late

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
       toolbar = (Toolbar) findViewById(R.id.toolbar_home);
    type = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
        try {
            setSupportActionBar(toolbar);
            //gcm
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        //mInformationTextView.setText(getString(R.string.server_registering));
                    } else {
                        //mInformationTextView.setText(getString(R.string.gcm_error));
                    }
                }
            };
            //mInformationTextView = (TextView) findViewById(R.id.informationTextView);

            // Registering BroadcastReceiver
            registerReceiver();
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }catch (Exception e){e.printStackTrace();}
try {
    checkCurrentAppVersion();
}catch (Exception e)
{e.printStackTrace();}
        profile=(ImageView) findViewById(R.id.profile_image);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        android_id=Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println(android_id);
            displayname+= CommenSettings.displayname;
            Log.d("name",displayname);
            empid=CommenSettings.empid;
            gender=CommenSettings.gender;
            user_type=CommenSettings.user_type;
        final PromptDialog promptDialog=new PromptDialog.Builder(Home_Activity.this)
                .setTitle("No Internet")
                .setCanceledOnTouchOutside(false)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setButton1TextColor(R.color.md_blue_400)

                .setMessage("No Internet Connection")

                .setButton1("OK", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
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

                            if (isConnected()) {
                                Intent map = new Intent(Home_Activity.this, TrackMyCabActivity.class);

                                boolean isEnabled = isGPSenabled();
                                if (isEnabled|| !isEnabled) {
                                    map.putExtra("empid", empid);
                                    map.putExtra("user_type", user_type);
                                    startActivityForResult(map, 0);
                                } else {
                                    buildAlertMessageNoGps();
                                }


                            } else {
                                promptDialog.show();
                            }

                        break;
                    case 1:
                        try {
                            if (CommenSettings.user_type.equalsIgnoreCase("admin")||CommenSettings.gender.equalsIgnoreCase("F")) {
                                if (isConnected()) {
                                    Intent book = new Intent(Home_Activity.this, ManageBookingActivity.class);
                                    book.putExtra("user_type", user_type);
                                    startActivityForResult(book, 0);
                                } else {
                                    promptDialog.show();
                                }
                            } else {


                                alert();
                            }
                        }
                        catch (Exception e){e.printStackTrace();}
                        break;
                    case 2:
                        if (isConnected()) {
                            Intent tripdetails = new Intent(Home_Activity.this, TripDetailsActivity.class);
                            startActivityForResult(tripdetails, 0);

                        } else {
                            promptDialog.show();
                        }
                        break;
                    case 3:
                        if (isConnected()) {
                            Intent emergency = new Intent(Home_Activity.this, EmergencyContactActivity.class);
                            emergency.putExtra("empid", empid);
                            startActivityForResult(emergency, 0);
                        } else {
                            promptDialog.show();
                        }
                        break;
                    case 4:
                        Intent feed = new Intent(Home_Activity.this, FeedBackActivity.class);
                        startActivityForResult(feed, 0);
                        break;
                    case 5:
                        try {
                            if (CommenSettings.user_type.equalsIgnoreCase("admin") || CommenSettings.gender.equalsIgnoreCase("F")) {
                                if (isConnected()) {
                                    Intent map = new Intent(Home_Activity.this, LateNightCabTrack.class);

                                    boolean isEnabled = isGPSenabled();
                                    if (isEnabled) {
                                        map.putExtra("empid", empid);
                                        map.putExtra("user_type", user_type);
                                        startActivityForResult(map, 0);
                                    } else {
                                        buildAlertMessageNoGps();
                                    }


                                } else {
                                    promptDialog.show();
                                }
                            } else {
                                alert();
                            }
                        }catch (Exception e){e.printStackTrace();}
                        break;

                }
            }
        });


    }

    private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout) findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_android_layout);
        collapsingToolbarLayoutAndroid.setTitle(displayname);
        collapsingToolbarLayoutAndroid.setCollapsedTitleTypeface(type);

    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }
    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        try{
            checkCurrentAppVersion();}catch (Exception e){e.printStackTrace();}
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

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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

    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public  void alert(){
       // pd.dismiss();
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("No access!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();


    }

    private void checkCurrentAppVersion() {
     try{
        Log.d("check version","here");
        Siren siren = Siren.getInstance(getApplicationContext());
        siren.setSirenListener(sirenListener);
        siren.setMajorUpdateAlertType(SirenAlertType.FORCE);
        siren.setMinorUpdateAlertType(SirenAlertType.FORCE);
        siren.setPatchUpdateAlertType(SirenAlertType.FORCE);
        siren.setRevisionUpdateAlertType(SirenAlertType.FORCE);
        siren.setVersionCodeUpdateAlertType(SirenAlertType.FORCE);

        siren.checkVersion(this, SirenVersionCheckType.IMMEDIATELY, CommenSettings.SIREN_JSON_URL);
     }
     catch(Exception e) {
         e.printStackTrace();
     }
    }

    ISirenListener sirenListener = new ISirenListener() {
        @Override
        public void onShowUpdateDialog() {
            Log.d(TAG, "onShowUpdateDialog");
        }

        @Override
        public void onLaunchGooglePlay() {
            Log.d(TAG, "onLaunchGooglePlay");
        }

        @Override
        public void onSkipVersion() {
            Log.d(TAG, "onSkipVersion");
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }

        @Override
        public void onDetectNewVersionWithoutAlert(String message) {
            Log.d(TAG, "onDetectNewVersionWithoutAlert: " + message);
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "onError");
            e.printStackTrace();
        }
    };
}