package com.agiledge.keocometemployee.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Booking extends AppCompatActivity {
    public static EditText et1, et2;
    public static Button submit;
    private TransparentProgressDialog pd;
    private String[] branchsitearr;
   public String[] sitearr;
    private String[] brancharr;
    private String[] siteidarr;
    private String[] branchidarr;
  static public boolean setdate=false;
    private String selectedbranchid;
    private String selectedsiteid;



    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public void open() {
            //onCreateDialog();


        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

        /*
            - Does not work correctly with those Theme
            THEME_DEVICE_DEFAULT_LIGHT
            THEME_DEVICE_DEFAULT_DARK

            -Works fine with those Theme
            THEME_HOLO_LIGHT
            THEME_HOLO_DARK
            THEME_TRADITIONAL
         */

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_HOLO_DARK,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_TRADITIONAL,this,year, month, day);

            //Get the DatePicker instance from DatePickerDialog
            DatePicker dp = dpd.getDatePicker();
            //Set the DatePicker minimum date selection to current date
            dp.setMinDate(c.getTimeInMillis()-1000);//get the current day
            //dp.setMinDate(System.currentTimeMillis() - 1000);// Alternate way to get the current day
            dpd.setTitle("Choose a Date");
            //Add 6 days with current date
            c.add(Calendar.DAY_OF_MONTH,60);

            //Set the maximum date to select from DatePickerDialog
            dp.setMaxDate(c.getTimeInMillis());
            //Now DatePickerDialog have 7 days range to get selection any one from those dates
            return dpd; //Return the DatePickerDialog in app window
        }


        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            String smonth = "" + month, sday = "" + day;
            if (month < 10) {
                smonth = "0" + month;
            }
            if (day < 10) {
                sday = "0" + day;
            }
            if(setdate) {
                    et1.setText(sday + "/" + smonth + "/" + year);
            }
            else{
                    et2.setText(sday + "/" + smonth + "/" + year);
            }



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Spinner location=(Spinner) findViewById(R.id.service11);
        final Spinner sitespnr=(Spinner) findViewById(R.id.frequency1);
        Spinner logoutspinner = (Spinner) findViewById(R.id.pro_typ);
        AppController.getInstance().trackScreenView("Booking Activity");// for Google analytics data
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        et1 = (EditText) findViewById(R.id.selected_fromdate);
        et2 = (EditText) findViewById(R.id.selected_todate);
        submit = (Button) findViewById(R.id.submit_book);
        fillvalues();
        pd = new TransparentProgressDialog(this, R.drawable.loading);
        pd.show();

        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                setdate=true;


            }
        });
        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                setdate=false;


            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                book();



            }
        });



location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        selectedbranchid=branchidarr[position];
        sitespnr.setAdapter(null);
        ArrayList<String> sitelist = new ArrayList<>();
        for (int i = 0; i < sitearr.length; i++) {
            if(branchidarr[position].equalsIgnoreCase(branchsitearr[i]))
                sitelist.add(sitearr[i]);
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sitelist);
        adp.setDropDownViewResource(R.layout.my_spinner);
        sitespnr.setAdapter(adp);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // sometimes you need nothing here
    }});

        sitespnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getChildAt(0)!=null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedsiteid=siteidarr[position];

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }});
        logoutspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getChildAt(0)!=null)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }});

    }



    public void fillvalues() {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "GET_LOGS");
            final Spinner sitespinner = (Spinner) findViewById(R.id.frequency1);
            final Spinner logoutspinner = (Spinner) findViewById(R.id.pro_typ);
            final Spinner branchspinner = (Spinner) findViewById(R.id.service11);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
                            JSONArray logins = response.getJSONArray("LOG_IN");
                            JSONArray logouts = response.getJSONArray("LOG_OUT");
                            JSONArray branchids = response.getJSONArray("BRANCH_ID");
                            JSONArray branch = response.getJSONArray("BRANCH");
                            JSONArray siteids = response.getJSONArray("SITE_ID");
                            JSONArray site = response.getJSONArray("SITE");
                            JSONArray jbranchsite=response.getJSONArray("SITEBRANCH");
                            branchsitearr=new String[jbranchsite.length()];
                            for(int i=0;i<jbranchsite.length();i++){
                            branchsitearr[i]=jbranchsite.getString(i);
                            }
                            sitearr=new String[site.length()];
                            for(int i=0;i<site.length();i++){
                                sitearr[i]=site.getString(i);
                            }
                            siteidarr=new String[siteids.length()];
                            for(int i=0;i<siteids.length();i++){
                                siteidarr[i]=siteids.getString(i);
                            }
                            brancharr=new String[branch.length()];
                            for(int i=0;i<branch.length();i++){
                                brancharr[i]=branch.getString(i);
                            }
                            branchidarr=new String[branchids.length()];
                            for(int i=0;i<branchids.length();i++){
                                branchidarr[i]=branchids.getString(i);
                            }
                            ArrayList<String> branchlist = new ArrayList<>();
                            for (int i = 0; i < branch.length(); i++) {
                                branchlist.add(branch.getString(i));
                            }
                            ArrayAdapter<String> branchadp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, branchlist);
                            branchadp.setDropDownViewResource(R.layout.my_spinner);
                            branchspinner.setAdapter(branchadp);
                            ArrayList<String> sitelist = new ArrayList<>();
                            for (int i = 0; i < site.length(); i++) {
                                if(branchidarr[0].equalsIgnoreCase(branchsitearr[i]))
                                sitelist.add(site.getString(i));
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sitelist);
                            adp.setDropDownViewResource(R.layout.my_spinner);
                            sitespinner.setAdapter(adp);
//                            sitespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                TextView tv=(TextView)findViewById(R.id.spintext);
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                                    tv.setTextColor(Color.BLACK);
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//                                    //Another interface callback
//                                }
//                            });

                            ArrayList<String> logoutslist = new ArrayList<>();
                            for (int i = 0; i < logouts.length(); i++) {
                                logoutslist.add(logouts.getString(i));
                            }
                            ArrayAdapter<String> logoutsadp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, logoutslist);
                            logoutsadp.setDropDownViewResource(R.layout.my_spinner);
                            logoutspinner.setAdapter(logoutsadp);

                            pd.dismiss();

                        } else {

                            Toast.makeText(getApplicationContext(), "Communication failure!", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();


                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  AppController.getInstance().trackScreenView("Login");// for Google analytics data

    }

    public void book() {
        try{
        WifiManager wifiMan = (WifiManager) this.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
            EditText fromdate=(EditText) findViewById(R.id.selected_fromdate);
            EditText todate=(EditText) findViewById(R.id.selected_todate);
            Spinner time=(Spinner) findViewById(R.id.pro_typ);

            boolean valid=true;
        String macAddress = wifiInf.getMacAddress();
            if(fromdate.getText().toString().equalsIgnoreCase("")){
                fromdate.setError("Please select date!");
                valid=false;

            }
            if(todate.getText().toString().equalsIgnoreCase("")){
                todate.setError("Please select date!");
                valid=false;
            }
            if(valid) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fd = sdf.parse(fromdate.getText().toString());
                Date td=sdf.parse(todate.getText().toString());
                if(fd.after(td)){
                    fromdate.setError("To date should be after from date!");
                    valid=false;
                }

            }



            Spinner sitespinner = (Spinner) findViewById(R.id.frequency1);
              if(sitespinner.getSelectedItem()==null){
                valid=false;
                Toast.makeText(getApplicationContext(),"Please select site!",Toast.LENGTH_LONG).show();
            }

            if(valid) {
                Date fd=new Date();
                Date td=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                fd=sdf.parse(fromdate.getText().toString());
                td=sdf.parse(todate.getText().toString());
                if(fd.after(td)){
                    valid=false;
                    todate.setError("Please select after from date!");
                    Toast.makeText(getApplicationContext(),"To date should be after from date!",Toast.LENGTH_LONG).show();
                }
                JSONObject jobj = new JSONObject();
                jobj.put("ACTION", "BOOKING");
                jobj.put("IMEI", macAddress);
                jobj.put("FROM_DATE", fromdate.getText().toString());
                jobj.put("TO_DATE", todate.getText().toString());
                jobj.put("LOG_IN", "WEEKLY OFF");
                jobj.put("LOG_OUT", time.getSelectedItem().toString());
                jobj.put("SITE_ID",selectedsiteid);

                JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("result").equalsIgnoreCase("TRUE")) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), ""+response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), ""+response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
                                finish();
                            }


                        } catch (Exception e) {
                            AppController.getInstance().trackException(e);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
                        pd.dismiss();


                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(req);
            }else{
                pd.dismiss();
            }
    } catch (Exception e) {
        e.printStackTrace();
            pd.dismiss();
    }

    }


        @Override
        public void onBackPressed()
        {

            	super.onBackPressed();



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