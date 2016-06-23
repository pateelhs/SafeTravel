package com.agiledge.keocometemployee.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.MyLocation;
import com.agiledge.keocometemployee.utilities.PopupAdapter;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TrackMyCabActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

        GoogleMap mGoogleMap;
        SupportMapFragment mapFrag;
        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;
        Location mLastLocation;
        Marker mCurrLocationMarker;
        private int numMessagesOne = 0;
        private int notificationIdOne = 111;
        private NotificationManager myNotificationManager;
        private TransparentProgressDialog pd;
        public static String macAddress;
        String tripid="",tripdate="",triptime="",empid="",user_type="";
        boolean tripstarted=false,panicactive=false,showadmincabs=true,distalert=true,cabalert=true,resetmap=false;
        int refreshinterval=20000;
        FloatingActionButton legendfab;
        FloatingActionButton fab;
        FloatingActionButton infofab;
        String currentcabaddress="",distance="",eta="";
        Handler mHandler;
        double longitude,latitude,mylatitude,mylongitude;
        int empcount=0;
        List<String> allempids=new ArrayList<String>();
        boolean isMarkerRotating=false;
        boolean slidestatus=false;



        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_track_my_cab);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            Bundle extras=getIntent().getExtras();
            if(extras!=null){
                empid=extras.getString("empid");
                user_type=extras.getString("user_type");
            }
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(panicactive){
                        Toast.makeText(getApplicationContext(),"Already panic activated!",Toast.LENGTH_LONG).show();
                    }else
                    panicalert();
                }
            });
            legendfab = (FloatingActionButton) findViewById(R.id.legendfab);
            legendfab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_green_400)));
            legendfab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    legendAlert();
                }
            });
            if(user_type.equalsIgnoreCase("emp")) {
                legendfab.hide();
                refreshinterval=6000;
                showadmincabs=false;
            }
            infofab=(FloatingActionButton) findViewById(R.id.infofab);
            infofab.hide();
            infofab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_blue_500)));
            infofab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setETA(latitude,longitude,mylatitude,mylongitude);
                }
            });

            WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            macAddress = wimanager.getConnectionInfo().getMacAddress();
            pd = new TransparentProgressDialog(this, R.drawable.loading);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

            mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            this.mHandler.removeCallbacks(m_Runnable);
            //stop location updates when Activity is no longer active
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
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
                }
            }
            else {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
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
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("My Location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
                        }

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
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
    }

    protected void displayNotificationOne() {

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("CAB PROXIMITY ALERT");
        mBuilder.setContentText("Your cab is arriving");
        mBuilder.setTicker("Your cab Arriving");
        mBuilder.setSmallIcon(R.drawable.location_icon);
        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(++numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, TrackMyCabActivity.class);
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
        jobj.put("IMEI_NUMBER",macAddress);
        JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getString("result").equalsIgnoreCase("TRUE")) {
                        if(!response.getString("TRIP_ID").equalsIgnoreCase("")){
                            Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
                            TextView date = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
                            TextView time = (TextView) toolbarTop.findViewById(R.id.toolbar_title2);
                            tripid=response.getString("TRIP_ID");
                            tripdate=response.getString("TRIP_DATE");
                            triptime=response.getString("TRIP_TIME");
                            date.setText("date:"+tripdate);
                            time.setText("time:"+triptime);
                            tripstarted=true;
                            infofab.show();
                            updateMyCabposition();

                        }
                        else{
                            fab.hide();
                            if(cabalert)
                            buildAlertMessageTRIPSTATUS();
                        }



                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    AppController.getInstance().trackException(e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error while communicating" , Toast.LENGTH_LONG).show();


            }
        });

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

        builder.setTitle("CAB ALERT");
        builder.setMessage("Cab Is "+bd+" KM From Your Location");
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
        distxt.setText("Cab is " + bd + " KM From Your Location");
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
                    jobj.put("EMP_ID", empid);
                    jobj.put("IMEI_NUMBER",macAddress );
                    jobj.put("TRIP_ID", tripid);
                    jobj.put("LAT", latitude + "");
                    jobj.put("LONG", longitude + "");
                    JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
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
                                            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_yellow_500)));
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
                            Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();


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
                if(resetmap)
                   clearAllMarkers();
                else
                resetmap=true;
                    getTripDetails();
                    if(showadmincabs){
                        updateAdminCab();
                    }
                    TrackMyCabActivity.this.mHandler.postDelayed(m_Runnable, refreshinterval);
                   // Toast.makeText(getApplicationContext(),"running",Toast.LENGTH_LONG).show();

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
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("result").equalsIgnoreCase("TRUE")) {
                            if (response.getString("LAT") != null && !response.getString("LAT").equalsIgnoreCase("") && response.getString("LONG") != null && !response.getString("LONG").equalsIgnoreCase("")) {
                                longitude = Double.parseDouble(response.getString("LONG"));
                                latitude = Double.parseDouble(response.getString("LAT"));
                                mylatitude = mLastLocation.getLatitude();
                                mylongitude = mLastLocation.getLongitude();
                                double dist = distance(latitude, longitude, mylatitude, mylongitude);
                                if (dist < 2 && distalert) {
                                    displayNotificationOne();
                                }
                                LatLng latLng = new LatLng(latitude, longitude);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title("My Cab");
                                String line = "";
//                                List<String> boardids = new ArrayList<String>();
//                                for (int i = 1; i <= Integer.parseInt(response.getString("COUNT")); i++) {
//                                    boardids.add(response.getString("BOARD_ID" + i));
//                                }
//                                if (extras.getString("SECURITY").equalsIgnoreCase("YES")) {
//                                    if (response.getString("ESCORT_STATUS").equalsIgnoreCase("SHOW")) {
//                                        line = "\n\tEscort\t\t\u2714"
//                                                + "\n\n";
//                                    } else {
//                                        line = "\n\tEscort" + "\n\n";
//                                    }
//
//                                }
//                                if (empcount>0) {
//                                    for(String empid:allempids){
//                                        String tick = "";
//                                            if (boardids.contains(empid))
//                                                tick = "\u2714";
//                                            if (boardids.contains(empid)) {
//                                                distalert = false;
//                                            } else {
//                                                panicstatus = "no";
//                                            }
//
//                                        }
//                                    }
//                                        line += "\t" + i + ")" + extras.getString("EMP_NAME" + i) + " " + extras.getString("GENDER" + i) + " " + tick + " " + " " + "\n";


                                    mGoogleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(), line));
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_car));
                                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                                //move map camera
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


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
                    Toast.makeText(getApplicationContext(), "Error while communicating", Toast.LENGTH_LONG).show();


                }
                    });

                    AppController.getInstance().addToRequestQueue(req);
        }catch(Exception e){e.printStackTrace();}

    }

    public void updateAdminCab() {
        try{
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "admin_track");
        JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
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
                        JSONArray ESCORT = response.getJSONArray("ESCORT");

                        if (LAT.length() == LONG.length()) {
                            for (int i = 0; i < LAT.length(); i++) {
                                longitude = Double.parseDouble(LONG.get(i).toString());
                                latitude = Double.parseDouble(LAT.get(i).toString());
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
                                                .fromResource(R.drawable.only_driver));
                                    } else if (Integer
                                            .parseInt(LADYCOUNT.get(i)
                                                    .toString()) < 1) {
                                        marker.icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.only_guys));
                                    } else if (Integer
                                            .parseInt(LADYCOUNT.get(i)
                                                    .toString()) > 0
                                            && Integer
                                            .parseInt(EMPINCOUNT
                                                    .get(i)
                                                    .toString()) > 1) {
                                        marker.icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.atleast_onelady));
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
                                                .fromResource(R.drawable.singlelady_noesc));
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
                                                .fromResource(R.drawable.singlelady_withesc));
                                    }

                                }

                                LatLng lat1=new LatLng(latitude,longitude);
                                LatLng road=new LatLng(12.959869773633582,77.64835351807531);
                                LatLng lat2=new LatLng(12.959791489282114,77.64889220777359);
                                double brng=bearingBetweenLocations(road,lat2);
                                Marker marker1 = mGoogleMap.addMarker(new MarkerOptions()
                                        .position(road)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.uber_car90))
                                        .rotation(Float.parseFloat(brng+"")).title("Test marker"));
                                animateMarker(marker1,road,lat2);
                                //rotateMarker(marker1,road,lat2,Float.parseFloat(brng+""));
                                mGoogleMap.addMarker(marker);

                            }
                          /*  CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(latitude,
                                            longitude)).zoom(11)
                                    .build();

                            mGoogleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));*/

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
                Toast.makeText(getApplicationContext(), "Error while communicating" , Toast.LENGTH_LONG).show();


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }catch(Exception e){e.printStackTrace();}

    }

    public void clearAllMarkers(){
       // mGoogleMap.clear();
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
                Toast.makeText(getApplicationContext(), "Error while communicating", Toast.LENGTH_LONG).show();


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
    public void animateMarker(final Marker marker,final LatLng startltlng,final LatLng endltlng){
        final Interpolator interpolator = new LinearInterpolator();
        final long start = SystemClock.uptimeMillis();
        final long duration = 3000;

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                final long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lat = t * endltlng.latitude + (1 - t) * startltlng.latitude;
                double lng = t * endltlng.longitude + (1 - t) * startltlng.longitude;
                LatLng intermediatePosition = new LatLng(lat, lng);
                marker.setPosition(intermediatePosition);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 1000);
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
}
