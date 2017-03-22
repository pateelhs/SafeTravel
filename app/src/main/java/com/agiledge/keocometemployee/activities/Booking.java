package com.agiledge.keocometemployee.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.constants.GetMacAddress;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Booking extends AppCompatActivity {
    public static EditText et1, et2;
    private CoordinatorLayout coordinatorLayout;
    public static Button submit;
    private TransparentProgressDialog pd;
    private String[] branchsitearr;
    public String[] sitearr;
    private String[] brancharr;
    private String[] siteidarr;
    private String[] branchidarr;
    static public boolean setdate = false;
    private String selectedbranchid,msg = "", msg2 = "";
    private String selectedsiteid,serch = "";
    ToggleButton toggleButton;
    //String android_id="";
    TextView bookfor;
    AutoCompleteTextView ACTV;
    ArrayAdapter<String> adapter;
    boolean searchstatus=true,isSearchselected=false,issearchChecked=false;
    ArrayList<String> searchempids=new ArrayList<String>();
    String searchselectedempid="";
    String macaddress= GetMacAddress.getMacAddr();
   public String user_type, URL="http://180.179.227.73/atomKeo/employeeappservlet";
    SweetAlertDialog pDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public void open() {
            //onCreateDialog();


        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
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

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_HOLO_DARK,this,year, month, day);
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_TRADITIONAL,this,year, month, day);

            //Get the DatePicker instance from DatePickerDialog
            DatePicker dp = dpd.getDatePicker();
            //Set the DatePicker minimum date selection to current date
            dp.setMinDate(c.getTimeInMillis() - 1000);//get the current day
            //dp.setMinDate(System.currentTimeMillis() - 1000);// Alternate way to get the current day
            dpd.setTitle("Choose a Date");
            //Add 6 days with current date
            c.add(Calendar.DAY_OF_MONTH, 60);

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
            if (setdate) {
                et1.setText(sday + "/" + smonth + "/" + year);
            } else {
                et2.setText(sday + "/" + smonth + "/" + year);
            }


        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Spinner location = (Spinner) findViewById(R.id.service11);
        final Spinner sitespnr = (Spinner) findViewById(R.id.frequency1);
        Spinner logoutspinner = (Spinner) findViewById(R.id.pro_typ);
        AppController.getInstance().trackScreenView("Booking Activity");// for Google analytics data

        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.top_band));
        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Live_tracking) + "</font>")));
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("Booking");
        SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

//        android_id = Settings.Secure.getString(this.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        et1 = (EditText) findViewById(R.id.selected_fromdate);
        et2 = (EditText) findViewById(R.id.selected_todate);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/AvantGarde Md BT.ttf");
        et1.setTypeface(type);
        et2.setTypeface(type);
        submit = (Button) findViewById(R.id.submit_book);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        ACTV = (AutoCompleteTextView) findViewById(R.id.auto);
        Bundle extras=getIntent().getExtras();
        //Toast.makeText(getApplicationContext(),""+CommenSettings.user_type,Toast.LENGTH_LONG).show();
        SharedPreferences sharedpref=getPreferences(Context.MODE_PRIVATE);
        LinearLayout one = (LinearLayout) findViewById(R.id.lin);
        one.setVisibility(View.GONE);

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);

        if(CommenSettings.user_type.equalsIgnoreCase("admin"))
        {
           // Toast.makeText(getApplicationContext(),""+CommenSettings.user_type,Toast.LENGTH_LONG).show();
            one.setVisibility(View.VISIBLE);}

        bookfor = (TextView) findViewById(R.id.bookfor);
        fillvalues();
        pd = new TransparentProgressDialog(this, R.drawable.loading);
        pDialog.show();
        ACTV.setThreshold(2);
        ACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"on click actv",Toast.LENGTH_LONG).show();
                serch = ACTV.getText().toString();
                if (serch.length() > 2 && searchstatus && !isSearchselected) {
                    searchfill(serch);
                }
            }
        });

        ACTV.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>2&&searchstatus&&!isSearchselected) {
                    searchfill(s.toString());
                }
                if(count<3)
                    isSearchselected=false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                searchselectedempid=searchempids.get(position);
                isSearchselected=true;
                ACTV.dismissDropDown();

            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked == true) {
                    bookfor.setText("Others ");
                    issearchChecked=true;

                    ACTV.setVisibility(View.VISIBLE);
                } else {
                    bookfor.setText("Self  ");

                    ACTV.setVisibility(View.INVISIBLE);
                    issearchChecked=false;

                }
            }
        });
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                setdate = true;


            }
        });
        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                setdate = false;


            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                book();


            }
        });


        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                selectedbranchid = branchidarr[position];
                sitespnr.setAdapter(null);
                ArrayList<String> sitelist = new ArrayList<>();
                for (int i = 0; i < sitearr.length; i++) {
                    if (branchidarr[position].equalsIgnoreCase(branchsitearr[i]))
                        sitelist.add(sitearr[i]);
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sitelist);
                adp.setDropDownViewResource(R.layout.my_spinner);
                sitespnr.setAdapter(adp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        sitespnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedsiteid = siteidarr[position];

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
        logoutspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getChildAt(0) != null)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }


    public void searchfill(final String searchkey)
    {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "SEARCH_EMPLOYEE");
            jobj.put("SEARCHKEY",searchkey);
            searchstatus=false;
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String data[];
                        searchempids=new ArrayList<String>();
                    if(response.getString("RESULT").equalsIgnoreCase("TRUE")){
                        JSONArray namearr=response.getJSONArray("NAMES");
                        JSONArray idarr=response.getJSONArray("IDS");
                        data=new String[namearr.length()];
                        for(int i=0;i<namearr.length();i++){
                            data[i]=namearr.get(i).toString();
                            searchempids.add(idarr.get(i).toString());
                        }
                    }else{
                        data=new String[1];
                        data[0]=response.getString("MESSAGE");
                    }
                        adapter= new ArrayAdapter<String>(Booking.this,android.R.layout.select_dialog_item,data);
                        ACTV.setAdapter(adapter);
                        adapter.setNotifyOnChange(true);
                        ACTV.showDropDown();
                        searchstatus=true;
                    }catch(Exception e){e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    searchfill(searchkey);
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                   // pd.hide();
                    snackbar.show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }catch (Exception e){e.printStackTrace();}
    }
    public void fillvalues() {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "GET_LOGS");
            final Spinner sitespinner = (Spinner) findViewById(R.id.frequency1);
            final Spinner logoutspinner = (Spinner) findViewById(R.id.pro_typ);
            final Spinner branchspinner = (Spinner) findViewById(R.id.service11);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
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
                            JSONArray jbranchsite = response.getJSONArray("SITEBRANCH");
                            branchsitearr = new String[jbranchsite.length()];
                            for (int i = 0; i < jbranchsite.length(); i++) {
                                branchsitearr[i] = jbranchsite.getString(i);
                            }
                            sitearr = new String[site.length()];
                            for (int i = 0; i < site.length(); i++) {
                                sitearr[i] = site.getString(i);
                            }
                            siteidarr = new String[siteids.length()];
                            for (int i = 0; i < siteids.length(); i++) {
                                siteidarr[i] = siteids.getString(i);
                            }
                            brancharr = new String[branch.length()];
                            for (int i = 0; i < branch.length(); i++) {
                                brancharr[i] = branch.getString(i);
                            }
                            branchidarr = new String[branchids.length()];
                            for (int i = 0; i < branchids.length(); i++) {
                                branchidarr[i] = branchids.getString(i);
                            }
                            ArrayList<String> branchlist = new ArrayList<>();
                            for (int i = 0; i < branch.length(); i++) {
                                branchlist.add(branch.getString(i));
                            }
                            ArrayAdapter<String> branchadp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, branchlist);
                            branchadp.setDropDownViewResource(R.layout.spinner_textview);
                            branchspinner.setAdapter(branchadp);
                            ArrayList<String> sitelist = new ArrayList<>();
                            for (int i = 0; i < site.length(); i++) {
                                if (branchidarr[0].equalsIgnoreCase(branchsitearr[i]))
                                    sitelist.add(site.getString(i));
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, sitelist);
                            adp.setDropDownViewResource(R.layout.spinner_textview);
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
                            ArrayAdapter<String> logoutsadp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, logoutslist);
                            logoutsadp.setDropDownViewResource(R.layout.spinner_textview);
                            logoutspinner.setAdapter(logoutsadp);

                            pDialog.dismiss();

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        pDialog.dismiss();
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {    pDialog.dismiss();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fillvalues();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    pDialog.dismiss();
                    snackbar.show();


                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {    pDialog.dismiss();
            e.printStackTrace();
        }
        //  AppController.getInstance().trackScreenView("Login");// for Google analytics data

    }

    public void book() {
        try {


            EditText fromdate = (EditText) findViewById(R.id.selected_fromdate);
            EditText todate = (EditText) findViewById(R.id.selected_todate);
            Spinner time = (Spinner) findViewById(R.id.pro_typ);

            boolean valid = true;

            if (fromdate.getText().toString().equalsIgnoreCase("")) {
                fromdate.setError("Please select date!");
                valid = false;

            }
            if (todate.getText().toString().equalsIgnoreCase("")) {
                todate.setError("Please select date!");
                valid = false;
            }
            if (valid) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fd = sdf.parse(fromdate.getText().toString());
                Date td = sdf.parse(todate.getText().toString());
                if (fd.after(td)) {
                    fromdate.setError("To date should be after from date!");
                    valid = false;
                }

            }


            Spinner sitespinner = (Spinner) findViewById(R.id.frequency1);
            if (sitespinner.getSelectedItem() == null) {
                valid = false;
                Toast.makeText(getApplicationContext(), "Please select site!", Toast.LENGTH_LONG).show();
            }

            if (valid) {
                Date fd = new Date();
                Date td = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                fd = sdf.parse(fromdate.getText().toString());
                td = sdf.parse(todate.getText().toString());
                if (fd.after(td)) {
                    valid = false;
                    todate.setError("Please select after from date!");
                    Toast.makeText(getApplicationContext(), "To date should be after from date!", Toast.LENGTH_LONG).show();
                }
                final JSONObject jobj = new JSONObject();
                jobj.put("ACTION", "BOOKING");
                jobj.put("IMEI", CommenSettings.android_id);
                jobj.put("FROM_DATE", fromdate.getText().toString());
                jobj.put("TO_DATE", todate.getText().toString());
                jobj.put("LOG_IN", "WEEKLY OFF");
                jobj.put("LOG_OUT", time.getSelectedItem().toString());
                jobj.put("SITE_ID", selectedsiteid);
                if(issearchChecked)
                jobj.put("BOOKING_FOR",searchselectedempid);
                else
                    jobj.put("BOOKING_FOR","SELF");
                Log.d("booking request",jobj.toString());
                JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(getApplicationContext(),""+jobj.toString(),Toast.LENGTH_LONG).show();
                            Log.d("booking request",jobj.toString());
                            Log.d("Booking RESPONSE*****",""+response.toString());
                            msg = response.getString("MESSAGE");
                            if (response.getString("result").equalsIgnoreCase("TRUE")) {
                                pDialog.dismiss();
                                {
//                                    new PromptDialog.Builder(Booking.this)
//                                            .setTitle("Booking")
//                                            .setCanceledOnTouchOutside(false)
//                                            .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
//                                            .setButton1TextColor(R.color.md_blue_400)
//
//                                            .setMessage(""+response.getString("MESSAGE"))
//                                            .setButton1("OK", new PromptDialog.OnClickListener() {
//
//                                                @Override
//                                                public void onClick(Dialog dialog, int which) {
//                                                    dialog.dismiss();
//                                                    finish();
//                                                }
//                                            })
//                                            .show();
                                    success();
                                }
//                                pd.dismiss();
//                                Toast.makeText(getApplicationContext(), "" + response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
//                                finish();

                            } else {
                                msg2 = response.getString("MESSAGE");
                                pDialog.dismiss();
                                {
//                                    new PromptDialog.Builder(Booking.this)
//                                            .setTitle("Booking")
//                                            .setCanceledOnTouchOutside(false)
//                                            .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
//                                            .setButton1TextColor(R.color.md_blue_400)
//
//                                            .setMessage(""+response.getString("MESSAGE"))
//                                            .setButton1("OK", new PromptDialog.OnClickListener() {
//
//                                                @Override
//                                                public void onClick(Dialog dialog, int which) {
//                                                    dialog.dismiss();
//                                                    finish();
//                                                }
//                                            })
//                                            .show();
                                    alert();
                                }
//                                pd.dismiss();
//                                Toast.makeText(getApplicationContext(), "" + response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
//                                finish();
                            }


                        } catch (Exception e) {
                            pDialog.dismiss();
                            AppController.getInstance().trackException(e);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        book();
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        pDialog.dismiss();
                        snackbar.show();

                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(req);
            } else {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismiss();
        }

    }


    @Override
    public void onBackPressed() {

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
    public void alert() {
        //pd.dismiss();
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(msg2)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                .show();
        pDialog.dismiss();


    }

    public void success() {
        pDialog.dismiss();
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success :)")
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                .show();
       // pDialog.dismiss();


    }
    @Override
    public void onPause() {

        super.onPause();
        if(pDialog!= null){
            pDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
        if(pDialog!= null){
            pDialog.dismiss();
        }
    }
}