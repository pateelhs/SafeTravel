package com.agiledge.keocometemployee.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.agiledge.keocometemployee.utilities.MyLocation;
import com.agiledge.keocometemployee.utilities.PopupAdapter;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.agiledge.keocometemployee.constants.CommenSettings.user_type;
import static java.lang.Double.parseDouble;



public class TrackMyCabActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,ResultCallback<LocationSettingsResult> {
    private CoordinatorLayout coordinatorLayout;
    int REQUEST_CHECK_SETTINGS = 100;
    boolean isEnabled;
    private static final String TAG1 = TrackMyCabActivity.class.getSimpleName();
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private int numMessagesOne = 0,total;
    private int notificationIdOne = 111;
    private NotificationManager myNotificationManager;
    private TransparentProgressDialog pd;
    static int alertval = 0, escval = 0, tripstatusval = 0;
    String tripid="",tripdate="",triptime="";
    boolean tripstarted=false,panicactive=false,showadmincabs=true,distalert=true,cabalert=true,resetmap=false,mytripsecurity=false,boarded=false;
    int refreshinterval=10000;
    FloatingActionButton legendfab;
    FloatingActionButton fab;
    FrameLayout infolayout;
    FloatingActionButton infofab,busfab;
    String currentcabaddress="",distance="",eta="",mycabregno="";
    Handler mHandler;
    double longitude,latitude,mylatitude,mylongitude,picklatitude,pickuplong,droplatitude,droplongitude,shuttlelat,shuttlelong;
    //double last_lat=0.0,last_long=0.0;
    int empcount=0;
    List<String> mytripempnames=new ArrayList<String>();
    List<String> mytripempids=new ArrayList<String>();
    List<String> mytripempgenders=new ArrayList<String>();
    boolean isRunFirst=true;
    TextView total_trips;
    boolean isMarkerRotating=false;
    boolean slidestatus=false;
    boolean isRunning1st=true;
    //ClusterManager<MyItem> mClusterManager;
    String android_id="",usertype="";
    //for shuttle track
    boolean shuttlestarted=false,nearby=false,passed=false;
    String shuttlevehicleid="",title="Bus Track",logtype="";
    Marker myshuttle;
    private static final String TAG = TrackMyCabActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_my_cab);
        try {
            android_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        }
        catch (Exception e){
            android_id=CommenSettings.android_id;
            e.printStackTrace();}
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
     isEnabled= isGPSenabled();
        if (!isEnabled) {
            displayLocationSettingsRequest(this);
        }
        infolayout=(FrameLayout) findViewById(R.id.infolayout);
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder(title);
        SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_band));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_yellow_800)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(boarded){
                    if(panicactive){
                        Toast.makeText(getApplicationContext(),"Already panic activated!",Toast.LENGTH_LONG).show();
                    }else
                        panicalert();
                }
                else{
                    alert();
                }
            }

        });
        legendfab = (FloatingActionButton) findViewById(R.id.legendfab);
        busfab=(FloatingActionButton) findViewById(R.id.busfab);
        legendfab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_green_400)));
        legendfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                legendAlert();
            }
        });
        busfab=(FloatingActionButton) findViewById(R.id.busfab);
        total_trips=(TextView) findViewById(R.id.UpdateTrips);
        if(user_type.equalsIgnoreCase("admin")){
            total_trips.setVisibility(View.VISIBLE);
            busfab.setVisibility(View.GONE);
            legendfab.setVisibility(View.INVISIBLE);
        }
        else{
            try {
                busfab.setVisibility(View.GONE);
            }catch (Exception e){e.printStackTrace();}
        }
        busfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bus=new Intent(TrackMyCabActivity.this,TrackBus.class);
                startActivity(bus);

            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //Log.d("extras","extras not null");

            usertype=extras.getString("user_type");
            // Log.d(TAG,usertype);

            //System.out.println(usertype);
        }
        else{usertype= CommenSettings.user_type.toString();}

        if(usertype.equalsIgnoreCase("emp")) {
            legendfab.hide();
            refreshinterval=7000;
            showadmincabs=false;
        }
        infofab=(FloatingActionButton) findViewById(R.id.infofab);
        //infofab.hide();
        infolayout.setVisibility(View.INVISIBLE);
        infofab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_blue_500)));
        infofab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setETA(shuttlelat,shuttlelong,CommenSettings.pickuplat,CommenSettings.pickuplong);
            }
        });



        pd = new TransparentProgressDialog(this, R.drawable.loading);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
                mGoogleMap.getUiSettings().isMapToolbarEnabled();
                mGoogleMap.getUiSettings().setCompassEnabled(true);
                mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
                mGoogleMap.getUiSettings().setMapToolbarEnabled(true);


            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        }
        //setUpClusterer();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mGoogleMap.setTrafficEnabled(true);
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    }

                } else {
                    checkLocationPermission();
                    LayoutInflater inflater = getLayoutInflater();
                    View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    TextView msg=(TextView) toastLayout.findViewById(R.id.custom_toast_message);
                    msg.setText("Kindly allow RideIT to access location");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, "App needs location permission to work ", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        this.mHandler.removeCallbacks(m_Runnable);



    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.mHandler.removeCallbacks(m_Runnable);
        pd.cancel();
    }

    protected void displayNotificationOne() {

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("CAB PROXIMITY ALERT");
        mBuilder.setContentText("Your cab is arriving");
        mBuilder.setTicker("Your cab Arriving");
        mBuilder.setSmallIcon(R.drawable.agile);
        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(++numMessagesOne);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setColor(getResources().getColor(R.color.md_red_400));
        mBuilder.setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("notificationId", notificationIdOne);

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(TrackMyCabActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT //can only be used once
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // pass the Notification object to the system
        myNotificationManager.notify(notificationIdOne, mBuilder.build());
        distalert=false;
    }
    public void getTripDetails(){
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "TRIP_DETAILS");
            jobj.put("IMEI_NUMBER",android_id);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {



                        Log.d("getTripdetails bus",""+response.toString());
                        if (response.getString("result").equalsIgnoreCase("TRUE")) {
                            if(!response.getString("TRIP_ID").equalsIgnoreCase("")){
                                Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
                                TextView date = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
                                TextView time = (TextView) toolbarTop.findViewById(R.id.toolbar_title2);
                                tripid=response.getString("TRIP_ID");
                                tripdate=response.getString("TRIP_DATE");
                                triptime=response.getString("TRIP_TIME");
                                mycabregno=response.getString("REG_NO");
                                date.setText("date:"+tripdate);
                                time.setText("time:"+triptime);
                                tripstarted=true;
                                //infolayout.setVisibility(View.VISIBLE);
                               // infofab.show();
                                mytripempnames=new ArrayList<String>();
                                mytripempids=new ArrayList<String>();
                                mytripempgenders=new ArrayList<String>();
                                for(int i=1;i<=response.getInt("EMPS_COUNT");i++){
                                    mytripempnames.add(response.getString("EMP_NAME"+i));
                                    mytripempids.add(response.getString("EMP_ID"+i));
                                    mytripempgenders.add(response.getString("GENDER"+i));
                                    if(response.getString("SECURITY").equalsIgnoreCase("YES")){
                                        mytripsecurity=true;

                                    }
                                    buildAlertMessageNoEscort();
                                }
                                Toolbar tool=(Toolbar) findViewById(R.id.toolbar_top) ;
                                tool.setVisibility(View.VISIBLE);
                                updateMyCabposition();

                            }
                            else{
                                fab.hide();
                                if(cabalert)
                                    buildAlertMessageTRIPSTATUS();
                            }



                        } else {
                            //buildAlertMessageTRIPSTATUS();
                            //Toast.makeText(getApplicationContext(), "Trip has not started!!", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low" , Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getTripDetails();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.BLUE);
                    textView.setMaxLines(3);
                    pd.hide();
                    snackbar.show();

                }
            });
            int socketTimeout = 15000;//5 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  AppController.getInstance().trackScreenView("Login");// for Google analytics data

    }
    public void buildAlertMessageTRIPSTATUS() {
        final Dialog dialog = new Dialog(TrackMyCabActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tripstatuspopup);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button Ok = (Button) dialog.findViewById(R.id.tripok);
        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        dialog.show();
        cabalert=false;

    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist;
    }

    public void alertdist(double dist) {
        float indist = (float) dist;
        BigDecimal bd = new BigDecimal(Float.toString(indist));
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("VEHICLE PROXIMITY ALERT");
        builder.setMessage("Your vehicle Is "+bd+" KM From Your Pickup Location");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int id)
            {
                dialog.cancel();
            }
        });
        final Dialog dialog = new Dialog(TrackMyCabActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cabalertpopup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button Ok = (Button) dialog.findViewById(R.id.cabOK);

        TextView distxt = (TextView) dialog.findViewById(R.id.disttitle1);
        distxt.setText("Your vehicle Is " + bd + " KM From Your Location");
        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void panicalert() {
        final Dialog dialog = new Dialog(TrackMyCabActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.panicpopup);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView yes = (ImageView) dialog.findViewById(R.id.yespanic);
        Button no = (Button) dialog.findViewById(R.id.nopanic);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                try {

                    JSONObject jobj = new JSONObject();
                    jobj.put("ACTION", "PANIC_ACTIVATED");
                    MyLocation myLocation;
                    myLocation = new MyLocation(TrackMyCabActivity.this);
                    double latitude = myLocation.getLatitude();
                    double longitude = myLocation.getLongitude();
                    jobj.put("EMP_ID", CommenSettings.empid);
                    jobj.put("IMEI_NUMBER",android_id );
                    jobj.put("TRIP_ID", tripid);
                    jobj.put("LAT", latitude + "");
                    jobj.put("LONG", longitude + "");
                    JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("result").equalsIgnoreCase("TRUE")) {
                                    if (response != null
                                            && response.getString("result")
                                            .equalsIgnoreCase("true")) {
                                        if (response.getString("PANIC_STATUS")
                                                .equalsIgnoreCase("ACTIVATED")) {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Panic Activated!",
                                                    Toast.LENGTH_LONG).show();
                                            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                            panicactive=true;



                                        }
                                    } else if (response != null
                                            && response.getString("result")
                                            .equalsIgnoreCase("false"))

                                    {
                                        Toast.makeText(getApplicationContext(),
                                                "Opertaion Failed!",
                                                Toast.LENGTH_LONG).show();
                                    }



                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();



                                }
                            } catch (Exception e) {


                                e.printStackTrace();
                                AppController.getInstance().trackException(e);
                            }
                        }
                    }
                            ,new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse (VolleyError error){
                            Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low", Toast.LENGTH_LONG).show();
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            panicalert();
                                        }
                                    });

                            // Changing message text color
                            snackbar.setActionTextColor(Color.RED);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            textView.setMaxLines(3);
                            pd.hide();
                            snackbar.show();

                        }
                    });

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(req);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppController.getInstance().trackException(e);
                }

            }

        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.show();
    }

    public void legendAlert() {
        final Dialog dialog = new Dialog(TrackMyCabActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.legentpopup);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button ok = (Button) dialog.findViewById(R.id.legentok);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });

        dialog.show();

    }

    private final Runnable m_Runnable = new Runnable() {

        public void run()


        {

            try {
//                if (resetmap)
//                    clearAllMarkers();
//
//                else
//                    resetmap = true;
//                if (tripstarted) {
//                    isRunFirst = true;
//                    updateMyCabposition();
//                } else {
//                    getTripDetails();
//                    // getEMPDetails();
//                }
//                if (showadmincabs) {
//                    updateAdminCab();
//                }
                if(shuttlestarted){
                    checkshuttletrip();
                    refreshinterval=5000;
                }else{
                    plotMystop();
                    plotMyPickUpDrop();


                }
                TrackMyCabActivity.this.mHandler.postDelayed(m_Runnable, refreshinterval);

            } catch (Exception e) {
                e.printStackTrace();
                AppController.getInstance().trackException(e);
            }


        }

    };

    public void updateMyCabposition(){
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "VEHICLE_LOCATION");
            jobj.put("TRIP_ID", tripid);
            jobj.put("IMEI_NUMBER", android_id);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("result").equalsIgnoreCase("TRUE")) {
                            if (response.getString("LAT") != null && !response.getString("LAT").equalsIgnoreCase("") && response.getString("LONG") != null && !response.getString("LONG").equalsIgnoreCase("")) {
                                longitude = parseDouble(response.getString("LONG"));
                                latitude = parseDouble(response.getString("LAT"));
                                //last_lat=Double.parseDouble(response.getString("LAST_LAT"));
                                //last_long=Double.parseDouble(response.getString("LAST_LONG"));
                                try {
                                    mylatitude = mLastLocation.getLatitude();
                                    mylongitude = mLastLocation.getLongitude();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                double dist = distance(latitude, longitude, mylatitude, mylongitude);
                                if (dist < 2 && distalert) {
                                    displayNotificationOne();
                                    alertdist(dist);
                                }
                                LatLng latLng = new LatLng(latitude, longitude);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title("My Cab:"+mycabregno);
                                String line = "";
                                List<String> boardids = new ArrayList<String>();
                                String escortalert = "NO";
                                for (int i = 1; i <= response.getInt("COUNT"); i++) {
                                    boardids.add(response.getString("BOARD_ID" + i));
                                }
                                if (mytripsecurity) {
                                    if (response.getString("ESCORT_STATUS").equalsIgnoreCase("SHOW")) {
                                        line = "\n\tEscort\t\t\u2714" + "\n\n";
                                    } else {
                                        line = "\n\tEscort" + "\n\n";
                                        escortalert = "YES";
                                    }
                                }
                                int i=0;
                                for(String empid:mytripempids){
                                    String tick = "";
                                    if (boardids.contains(empid)){
                                        tick = "\u2714";
                                    }
                                    line += "\t" + (i+1) + ")" +mytripempnames.get(i)  + " " + mytripempgenders.get(i) + " " + tick + " " + " " + "\n";
                                    i++;
                                }
                                if(boardids.contains(CommenSettings.empid))
                                {
                                    fab.setVisibility(View.VISIBLE);
                                    boarded=true;
                                }
                                else{
                                    fab.hide();
                                }
                                mGoogleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(), line,mycabregno));
                                //LatLng lastltng=new LatLng(last_lat,last_long);
                                LatLng currlatlng=new LatLng(latitude,longitude);
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.safebus_ride_icon));
                                mCurrLocationMarker=mGoogleMap.addMarker(markerOptions);
                                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                                //animateMarker(markerOptions,lastltng,currlatlng);


                            } else {
                                Toast.makeText(getApplicationContext(), "Updation failed", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Updation failed", Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse (VolleyError error){
                    //Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low", Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateMyCabposition();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    // pd.hide();
                    textView.setMaxLines(3);
                    snackbar.show();

                }
            });
            int socketTimeout = 15000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(req);
        }catch(Exception e){e.printStackTrace();}

    }



    public void updateAdminCab() {
        try{
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "admin_track");
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("result").equalsIgnoreCase("TRUE")) {
                            JSONArray LAT = response.getJSONArray("LAT");
                            JSONArray LONG = response.getJSONArray("LONG");
                            JSONArray REGNO = response.getJSONArray("REGNO");
                            JSONArray TRIPCODE = response
                                    .getJSONArray("TRIPCODE");
                            JSONArray LOGSTATUS = response
                                    .getJSONArray("LOGSTATUS");
                            JSONArray LADYCOUNT = response
                                    .getJSONArray("LADYCOUNT");
                            JSONArray EMPINCOUNT = response
                                    .getJSONArray("EMPINCOUNT");
                            final JSONArray TRIPIDS=response.getJSONArray("TRIPID");
                            JSONArray ESCORT = response.getJSONArray("ESCORT");



                            total= TRIPIDS.length();

                            total_trips.setText("Trips running: "+total);
                            total_trips.setTextColor(getResources().getColor(R.color.md_red_600));

                            if (LAT.length() == LONG.length()) {
                                for (int i = 0; i < LAT.length(); i++) {
                                    double offset = i / 60d;
                                    longitude = parseDouble(LONG.get(i).toString());
                                    latitude = parseDouble(LAT.get(i).toString());
                                    MarkerOptions marker = new MarkerOptions()
                                            .position(
                                                    new LatLng(latitude,
                                                            longitude))
                                            .title(REGNO.get(i).toString()
                                                    + ","
                                                    + "   "
                                                    + "TRIPCODE"
                                                    + " "
                                                    + TRIPCODE.get(i)
                                                    .toString());

                                    if (LOGSTATUS.get(i).toString()
                                            .equalsIgnoreCase("danger")) {
                                        marker.icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.pan_car));


                                    } else {

                                        if (Integer.parseInt(EMPINCOUNT
                                                .get(i).toString()) == 0) {
                                            marker.icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.safebus_ride_icon));
                                        } else if (Integer
                                                .parseInt(LADYCOUNT.get(i)
                                                        .toString()) < 1) {
                                            marker.icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.safebus_ride_icon));
                                        } else if (Integer
                                                .parseInt(LADYCOUNT.get(i)
                                                        .toString()) > 0
                                                && Integer
                                                .parseInt(EMPINCOUNT
                                                        .get(i)
                                                        .toString()) > 1) {
                                            marker.icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.safebus_ride_icon));
                                        } else if (Integer
                                                .parseInt(LADYCOUNT.get(i)
                                                        .toString()) == 1
                                                && Integer
                                                .parseInt(EMPINCOUNT
                                                        .get(i)
                                                        .toString()) == 1
                                                && ESCORT
                                                .get(i)
                                                .toString()
                                                .equalsIgnoreCase(
                                                        "NO")) {
                                            marker.icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.safebus_ride_icon));
                                        } else if (Integer
                                                .parseInt(LADYCOUNT.get(i)
                                                        .toString()) == 1
                                                && Integer
                                                .parseInt(EMPINCOUNT
                                                        .get(i)
                                                        .toString()) == 1
                                                && ESCORT
                                                .get(i)
                                                .toString()
                                                .equalsIgnoreCase(
                                                        "YES")) {
                                            marker.icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.safebus_ride_icon));
                                        }
                                        //MyItem offsetItem = new MyItem(latitude, longitude);
                                        // mClusterManager.addItem(offsetItem);


                                    }
                                    //  mGoogleMap.clear();
//                                LatLng newlat=new LatLng(latitude,longitude);
//                                LatLng road=new LatLng(12.959869773633582,77.64835351807531);
//                                LatLng lat2=new LatLng(12.959791489282114,77.64889220777359);
//                                double brng=bearingBetweenLocations(road,lat2);
//                                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(road).icon(BitmapDescriptorFactory.fromResource(R.drawable.uber_car90)).title("Test marker"));
                                    //   marker.setRotation(Float.parseFloat(""+brng));
                                    // animateMarker(marker,road,lat2);
                                    Marker currMarker=mGoogleMap.addMarker(marker);
                                    if (LOGSTATUS.get(i).toString()
                                            .equalsIgnoreCase("danger")) {
                                        currMarker.setTitle("Danger"+TRIPIDS.get(i).toString());
                                        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                                            @Override
                                            public boolean onMarkerClick(Marker arg0) {
                                                try {
                                                    if(arg0.getTitle().contains("Danger")){
                                                        Intent panicstop = new Intent(TrackMyCabActivity.this, PanicAdminBus.class);
                                                        Log.d("panic",arg0.getTitle());
                                                        panicstop.putExtra("TRIP_ID", arg0.getTitle().replace("Danger",""));
                                                        panicstop.putExtra("EMP_ID",CommenSettings.empid);
                                                        startActivity(panicstop);}
                                                    else{
                                                        arg0.showInfoWindow();
                                                    }
                                                }catch(Exception e){e.printStackTrace();}
                                                return true;
                                            }

                                        });
                                    }
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude)).zoom(10)
                                            .build();
                                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                                    mGoogleMap.animateCamera(CameraUpdateFactory
                                            .newCameraPosition(cameraPosition));
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

                            }
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low" , Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateAdminCab();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    textView.setMaxLines(3);
                    // pd.hide();
                    snackbar.show();


                }
            });
            int socketTimeout = 15000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }catch(Exception e){e.printStackTrace();}

    }

    public void clearAllMarkers(){
        mGoogleMap.clear();
    }

    public void setETA(double cablat,double cablng,double emplat,double emplng){
        String gurl="https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+cablat+","+cablng+"&destinations="+emplat+","+emplng+"&key=AIzaSyC_6Qst7HGlBpciyZccp6WcIqga8WJOdIU";
        JsonObjectRequest req = new JsonObjectRequest(gurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equalsIgnoreCase("OK")) {
                        currentcabaddress="Current Location-"+response.getJSONArray("origin_addresses").getString(0);
                        distance="Distance-"+response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
                        eta= "ETA-"+response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text")+" more.";
                        showinfo(eta,distance,currentcabaddress);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse (VolleyError error){
                Toast.makeText(getApplicationContext(), "Server took too long to respond or internet connectivity is low", Toast.LENGTH_LONG).show();


            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

    public void showinfo(String eta,String distance,String currentcabaddress) {
        final Dialog dialog = new Dialog(TrackMyCabActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cab_info);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button Ok = (Button) dialog.findViewById(R.id.cabOK);

        TextView distxt = (TextView) dialog.findViewById(R.id.disttitle1);
        TextView distxt2 = (TextView) dialog.findViewById(R.id.disttitle2);
        TextView distxt3 = (TextView) dialog.findViewById(R.id.disttitle3);
        distxt.setText(eta);
        distxt2.setText(distance);
        distxt3.setText(currentcabaddress);
        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        dialog.show();

    }
    @Override
    protected void onStart() {
        super.onStart();
       // getTripDetails();
        this.mHandler = new Handler();
        m_Runnable.run();
    }

    private void rotateMarker(final Marker marker,final LatLng startltlng,final LatLng endltlng ,final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;
            long elapsed = SystemClock.uptimeMillis() - start;

            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    float rot = t * toRotation + (1 - t) * startRotation;
                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }
    public void animateMarker(final MarkerOptions markerOptions,final LatLng startltlng,final LatLng endltlng){

        final Interpolator interpolator = new LinearInterpolator();
        final long start = SystemClock.uptimeMillis();
        final long duration = 5000;
        mCurrLocationMarker.setVisible(false);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                final long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lat = t * endltlng.latitude + (1 - t) * startltlng.latitude;
                double lng = t * endltlng.longitude + (1 - t) * startltlng.longitude;
                LatLng intermediatePosition = new LatLng(lat, lng);
                double brng=bearingBetweenLocations(startltlng,intermediatePosition);
                mCurrLocationMarker.setPosition(intermediatePosition);
                mCurrLocationMarker.setRotation(Float.parseFloat(""+brng));
                if(isRunFirst) {
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    mCurrLocationMarker.setVisible(true);

                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                    isRunFirst=false;
                }
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(intermediatePosition));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }
    //    private void setUpClusterer() {
//
//
//        // Position the map.
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.9716,77.5946), 10));
//
//        // Initialize the manager with the context and the map.
//        // (Activity extends context, so we can pass 'this' in the constructor.)
//        mClusterManager = new ClusterManager<MyItem>(this, mGoogleMap);
//        // Point the map's listeners at the listeners implemented by the cluster
//        // manager.
//       mGoogleMap.setOnCameraChangeListener(mClusterManager);
//        mGoogleMap.setOnMarkerClickListener(mClusterManager);
//
//
//    }
    public void buildAlertMessageNoEscort() {
        // final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        // builder.setTitle("No Escort");
        // builder.setMessage("Escort Not Boarded The Cab!");
        // builder.setCancelable(false);
        //
        // builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        // {
        // public void onClick(final DialogInterface dialog, final int id)
        // {
        // dialog.cancel();
        // }
        // });
        // if(escval==0)
        // {
        // builder.create().show();
        // escval=1;

        // }
        final Dialog dialog = new Dialog(TrackMyCabActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.escortstatus);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button Ok = (Button) dialog.findViewById(R.id.escortok);
        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        if (escval == 0) {
            dialog.show();
            escval = 1;
        }
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
        pd.dismiss();
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("You have not boarded cab!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                .show();


    }


    public void plotMyPickUpDrop(){
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "GET_PICKUPDROP");
            jobj.put("IMEI_NUMBER",android_id);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("PLOT MY PICKUP",""+response.toString());
                        if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
                            clearAllMarkers();
                            String lat = response.optString("PICKUP_LAT");
                            String lon = response.getString("PICKUP_LONG");
                            String dlat = response.optString("DROP_LAT");
                            String dlon = response.getString("DROP_LONG");
                            picklatitude=parseDouble(lat);
                            pickuplong=parseDouble(lon);
                            droplatitude=parseDouble(dlat);
                            droplongitude=parseDouble(dlon);
                            CommenSettings.pickuplat=picklatitude;
                            CommenSettings.pickuplong=pickuplong;
                            CommenSettings.droplat=droplatitude;
                            CommenSettings.droplong=droplongitude;
                            longitude = parseDouble(lon);
                            latitude = parseDouble(lat);



                            boolean samePickUpAndDrop = false;


                            if(!lat.isEmpty() && !lon.isEmpty()) {

                                LatLng latLng = new LatLng(parseDouble(lat), parseDouble(lon));
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title("PICKUP LOCATION");

                                if(!dlat.isEmpty() && !dlon.isEmpty()) {

                                    //if both pickup and drop are very close

                                    double differenceInKm = distance(Double.parseDouble(lat),Double.parseDouble(lon),Double.parseDouble(dlat),Double.parseDouble(dlon));
                                    double differenceInMeter = differenceInKm * 1000 ;

                                    if(differenceInMeter <= 100)
                                    {


                                        markerOptions.title("PICKUP & DROP LOCATION");
                                        samePickUpAndDrop= true;
                                    }


                                }

                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.add_marker));
                                    mGoogleMap.addMarker(markerOptions);


                            }
                            if(!dlat.isEmpty() && !dlon.isEmpty() && !samePickUpAndDrop ) {


                                LatLng latLng2 = new LatLng(parseDouble(dlat), parseDouble(dlon));

                                MarkerOptions markerOptions2 = new MarkerOptions();
                                markerOptions2.position(latLng2);
                                markerOptions2.title("DROP LOCATION");
                                markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.add_marker));
                                mGoogleMap.addMarker(markerOptions2);
                            }
                            /*shuttlestarted=true;
                            shuttlevehicleid=response.getString("VEHICLEID");
                            longitude = latLng.latitude;
                            latitude = latLng.longitude;*/
                        }else{
                            /*shuttlestarted=false;
                            shuttlevehicleid="";*/
                            clearAllMarkers();
                        }

                    } catch (Exception e) {
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low" , Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    plotMyPickUpDrop();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.BLUE);
                    textView.setMaxLines(3);
                    pd.hide();
                    snackbar.show();

                }
            });
            int socketTimeout = 15000;//5 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  AppController.getInstance().trackScreenView("Login");// for Google analytics data

    }

    public void plotMystop(){
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "GET_MYSTOP");
            jobj.put("IMEI_NUMBER",android_id);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("PLOT MY STOP",""+response.toString());
                        if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
                            clearAllMarkers();
                            logtype=response.getString("LOGTYPE");
                            LatLng latLng = new LatLng(parseDouble(response.getString("LAT")), parseDouble(response.getString("LNG")));
                            /*MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("Scheduled Time-"+response.getString("TIME"));
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.add_marker));
                            mGoogleMap.addMarker(markerOptions);*/

                            if (response.getString("LAT") != null && !response.getString("LAT").equalsIgnoreCase("") && response.getString("LNG") != null && !response.getString("LNG").equalsIgnoreCase("")){
                            shuttlestarted=true;
                            }
                            shuttlevehicleid=response.getString("VEHICLEID");
                            if(shuttlestarted && logtype.equalsIgnoreCase("IN")&&!nearby){
                                infolayout.setVisibility(View.VISIBLE);
                                //infofab.hide();
                            }
                            else if(nearby)
                            {  infolayout.setVisibility(View.INVISIBLE);}
                            else
                            {  infolayout.setVisibility(View.INVISIBLE);}
                            /*longitude = latLng.latitude;
                            latitude = latLng.longitude;*/
                        }else{
                            shuttlestarted=false;
                            shuttlevehicleid="";
                            clearAllMarkers();
                        }

                    } catch (Exception e) {
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low" , Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    plotMystop();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.BLUE);
                    textView.setMaxLines(3);
                    pd.hide();
                    snackbar.show();

                }
            });
            int socketTimeout = 15000;//5 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  AppController.getInstance().trackScreenView("Login");// for Google analytics data

    }

    public void checkshuttletrip(){
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "GET_SHUTTLEPOSITION");
            Log.d(" shuttlevehicleid ",shuttlevehicleid);
            jobj.put("VEHICLE_ID",shuttlevehicleid);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("checkshuttletrip bus",""+response.toString());
                        if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
                            LatLng latLng = new LatLng(parseDouble(response.getString("LAT")), parseDouble(response.getString("LNG")));

                            shuttlelat=parseDouble(response.getString("LAT"));
                            shuttlelong=parseDouble(response.getString("LNG"));
                            LatLng shuttleposition= new LatLng(shuttlelat,shuttlelong);
                            if(myshuttle==null) {
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title("My Vehicle " + response.optString("VEHICLE") );
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_car));
                                myshuttle = mGoogleMap.addMarker(markerOptions);
                                Log.d("",""+latitude +" "+ longitude +" "+  latLng.latitude+ " " +latLng.longitude);
                                if(shuttleposition!=null) {
                                    CameraUpdate center = CameraUpdateFactory.newLatLng(shuttleposition);
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                                    mGoogleMap.moveCamera(center);
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    mGoogleMap.animateCamera(zoom);
                                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
                                    mGoogleMap.setBuildingsEnabled(true);
                                }
                                double dist = distance(latitude, longitude,latLng.latitude, latLng.longitude);
                                Log.d("","distance=========================== "+ dist);
                                if(dist<0.1)
                                {

                                    nearby=true;
                                System.out.println("NEARBY"+nearby);

                                }

                                if (dist < 2 && distalert) {
                                    Log.d("","dist     =======");
                                    displayNotificationOne();
                                    alertdist(dist);
                                    Log.d("","dist     =======");
                                }
                            }else{
                                myshuttle.setPosition(latLng);
                            }


                        }

                    } catch (Exception e) {
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low" , Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    checkshuttletrip();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.BLUE);
                    textView.setMaxLines(3);
                    pd.hide();
                    snackbar.show();

                }
            });
            int socketTimeout = 15000;//5 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  AppController.getInstance().trackScreenView("Login");// for Google analytics data

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(4000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(TrackMyCabActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    private boolean isGPSenabled()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacks(m_Runnable);
        //stop location updates when Activity is no longer active
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public void onResume(){
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        this.mHandler = new Handler();
        m_Runnable.run();


    }
}
