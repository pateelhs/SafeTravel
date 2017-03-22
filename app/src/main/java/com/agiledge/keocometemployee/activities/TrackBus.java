package com.agiledge.keocometemployee.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pateel on 10/13/2016.
 */

public class TrackBus extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private CoordinatorLayout coordinatorLayout;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private int numMessagesOne = 0;
    private int notificationIdOne = 111;
    private NotificationManager myNotificationManager;
   // private TransparentProgressDialog pd;
    static int alertval = 0, escval = 0, tripstatusval = 0;
    String tripid="",tripdate="",triptime="";
    boolean tripstarted=false,panicactive=false,showadmincabs=true,distalert=true,cabalert=true,resetmap=false,mytripsecurity=false;
    int refreshinterval=30000,total;
    FloatingActionButton legendfab;
    FloatingActionButton fab;
    FloatingActionButton infofab;
    String currentcabaddress="",distance="",eta="",mycabregno="";
    Handler mHandler;
    double longitude,latitude,mylatitude,mylongitude;
    //double last_lat=0.0,last_long=0.0;
    int empcount=0;
    List<String> mytripempnames=new ArrayList<String>();
    List<String> mytripempids=new ArrayList<String>();
    List<String> mytripempgenders=new ArrayList<String>();
    boolean isRunFirst=true;

    boolean isMarkerRotating=false;
    boolean slidestatus=false;
    boolean isRunning1st=true;
    //ClusterManager<MyItem> mClusterManager;
    String android_id="",usertype="";
    TextView total_trips;
    private static final String TAG = TrackBus.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_bus);
        android_id= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        total_trips=(TextView) findViewById(R.id.UpdateTrips);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.top_band));
        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Live_tracking) + "</font>")));
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("Track Bus");
        SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




       // pd = new TransparentProgressDialog(this, R.drawable.loading);
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
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
                mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
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
                android.Manifest.permission.ACCESS_FINE_LOCATION)
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
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
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











    private final Runnable m_Runnable = new Runnable() {

        public void run()


        {

            try {

                if(resetmap)
                    clearAllMarkers();
                else
                    resetmap=true;

                if(showadmincabs){
                    updateAdminCab();
                }

                TrackBus.this.mHandler.postDelayed(m_Runnable, refreshinterval);

            } catch (Exception e) {
                e.printStackTrace();
                //AppController.getInstance().trackException(e);
            }


        }

    };

    @Override
    protected void onStart() {
        super.onStart();
//Toast.makeText(getApplicationContext(),"here start",Toast.LENGTH_LONG).show();
        this.mHandler = new Handler();
        m_Runnable.run();
    }
    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(getApplicationContext(),"here resume",Toast.LENGTH_LONG).show();
        this.mHandler = new Handler();
        m_Runnable.run();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showadmincabs=false;
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
        showadmincabs=false;
        finish();



    }

    @Override
    public void onDestroy()
    {   showadmincabs=false;
        super.onDestroy();
        this.mHandler.removeCallbacks(m_Runnable);
        //  pd.cancel();
    }

    public void updateAdminCab() {
        try{
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "admin_track");
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println(response.toString());
                        if (response.getString("result").equalsIgnoreCase("true")) {
                            JSONArray LAT = response.getJSONArray("LAT");
                            JSONArray LONG = response.getJSONArray("LONG");
                            JSONArray REGNO = response.getJSONArray("REGNO");
                            JSONArray TRIPID = response
                                    .getJSONArray("TRIPID");
                            total= TRIPID.length();

                            total_trips.setText("Trips running: "+total);
                            //System.out.println(total);
                            //Toast.makeText(getApplicationContext(),total,Toast.LENGTH_LONG).show();
                            total_trips.setTextColor(getResources().getColor(R.color.md_red_600));

                            if( LAT.length()== LONG.length())
                            {
                                for (int i = 0; i < LAT.length(); i++) {
                                    double offset = i / 60d;
                                    longitude = Double.parseDouble(LONG.get(i).toString());
                                    latitude = Double.parseDouble(LAT.get(i).toString());
                                    MarkerOptions marker = new MarkerOptions()
                                            .position(
                                                    new LatLng(latitude,
                                                            longitude))
                                            .title(REGNO.get(i).toString()
                                                    + ","
                                                    + "   "
                                                    + "TRIPID"
                                                    + " "
                                                    + TRIPID.get(i)
                                                    .toString());
                                    //  	  Toast.makeText(getApplicationContext(), REGNO.get(i).toString()+"valli"+TRIPID.get(i).toString(), Toast.LENGTH_LONG).show();





                                   // mGoogleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),REGNO.get(i).toString()));
                                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon));
                                    mGoogleMap.addMarker(marker);
                                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                                        @Override
                                        public boolean onMarkerClick(Marker arg0) {
                                            try {
                                                arg0.showInfoWindow();

                                            }catch(Exception e){e.printStackTrace();}
                                            return true;
                                        }

                                    });
                                }
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(latitude,longitude)).zoom(10).build();


                                mGoogleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));


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
            int socketTimeout = 8000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }catch(Exception e){e.printStackTrace();}

    }

    public void clearAllMarkers(){
        try {
            mGoogleMap.clear();
        }catch (Exception e){e.printStackTrace();}
    }










    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}