package com.agiledge.keocometemployee.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.agiledge.keocometemployee.Model.Trips;
import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.adapter.CustomListAdapter;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.navdrawermenu.FeedBackActivity;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.agiledge.keocometemployee.utilities.OtherFunctions;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TripHistoryList extends AppCompatActivity {
    // Log tag
    private static final String TAG = TripHistoryList.class.getSimpleName();


    // json url
    private static final String url = CommenSettings.serverAddress_wemp;
    private ProgressDialog pDialog;
    private List<Trips> tripList = new ArrayList<Trips>();
    private ListView listView;
    private CustomListAdapter adapter;
    String android_id="";

   // final ListView lv1 = (ListView) findViewById(R.id.list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppController.getInstance().trackScreenView("Trip list Screen");// for Google analytics data
        super.onCreate(savedInstanceState);

        setContentView(R.layout.triphistorylist);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.top_band));
        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Live_tracking) + "</font>")));
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("Trip History");
        SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.list);

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        adapter = new CustomListAdapter(this, tripList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                AppController.getInstance().trackEvent("Job Deails ", "Details of job", "Job Details Event");
                Trips job = (Trips) listView.getItemAtPosition(position);

       Intent i = new Intent(getApplicationContext(), FeedBackActivity.class);
        i.putExtra("CUSTOMER_NAME", job.getCustomerName());
                i.putExtra("TIME",job.getTime());
                i.putExtra("PRIORITY",job.getPriority());
                i.putExtra("ISSUE",job.getIssue());
                i.putExtra("JOBID",job.getJobid());
                i.putExtra("STATUS",job.getStatus());


        startActivity(i);
            }
        });
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        try {
            WifiManager wifiMan = (WifiManager) this.getSystemService(
                    Context.WIFI_SERVICE);
            WifiInfo wifiInf = wifiMan.getConnectionInfo();


           String macAddress = wifiInf.getMacAddress();
        JSONObject jobj=new JSONObject();
        jobj.put("ACTION", "GET_JOBCARDS");
        jobj.put("MAC_ADDRESS", android_id);
        JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp,jobj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                        // Parsing json
                            try {
                                if(response.getString("RESULT").equalsIgnoreCase("TRUE")) {
                                    String customernames[] = new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("CUSTOMER_NAMES"));
                                    String time_date[] = new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("TIME_DATE"));
                                    String jobids[] = new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("JOB_IDS"));
                                    String priority[] = new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("PRIORITY"));
                                    String issues[] = new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("ISSUES"));
                                    String image[] = new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("IMAGE"));
                                    String status[]=new OtherFunctions().JSONArrayToStringArray(response.getJSONArray("STATUS"));

                                    Timestamp timestamp;
                                    Date date;
                                    SimpleDateFormat sd=new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                    for (int i = 0; i < customernames.length; i++) {
                                        Trips jobs = new Trips();
                                        timestamp=Timestamp.valueOf(time_date[i]);
                                        sd.format(timestamp);
                                        jobs.setTitle(customernames[i]);
                                        jobs.setThumbnailUrl("http://api.androidhive.info/json/movies/1.jpg");
                                        jobs.setIssue(issues[i]);
                                        jobs.setTime(sd.format(timestamp));
                                        jobs.setPriority(priority[i]);
                                        jobs.setJobid(jobids[i]);
                                        jobs.setStatus(status[i]);

                                        tripList.add(jobs);
                                        hidePDialog();
                                    }
                                }
                                else{
                                    hidePDialog();
                                    Toast.makeText(getApplicationContext(),response.getString("MESSAGE"),Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
               hidePDialog();

            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
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

}