package com.agiledge.keocometemployee.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.agiledge.keocometemployee.utilities.EmplistPanic;
import com.agiledge.keocometemployee.utilities.PromptDialog;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


public class PanicAdminActivity extends AppCompatActivity {

    private Spinner panicSpin;
    public EditText comment;
    private Button stop,reset;
    public String reason="",tripid="";
    private TransparentProgressDialog pd;
    String android_id="",empid="",SURL="";
    String[] spinnerItems = new String[]{
            "False Alarm",
            "Accident",
            "Rash Driving",
            "Driver Issue",
            "Vehicle Breakdown",

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panic_admin);
       stop=(Button) findViewById(R.id.approve) ;
        reset=(Button) findViewById(R.id.reset);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.top_band));
        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Live_tracking) + "</font>")));
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("Panic Action");
        SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SURL=CommenSettings.serverAddress_wemp;
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            tripid=extras.getString("TRIP_ID");

        }
       // Toast.makeText(getApplicationContext(),tripid,Toast.LENGTH_LONG).show();
        panicdetails();
        panicSpin = (Spinner) findViewById(R.id.panicspin);
        comment= (EditText) findViewById(R.id.input_comment1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_textview,spinnerItems );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textview);
        panicSpin.setAdapter(spinnerArrayAdapter);
        reason=comment.getText().toString();
        Log.d("comment***",""+reason);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stoppanic();


            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();



            }
        });

    }
    public void panicdetails() {
        try {
            final JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "PANICTRIPDETAILS");
            jobj.put("TRIPID", tripid);

                JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("Panic acdmin response",""+response.toString());
                            if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {

                                TextView vehnum = (TextView) findViewById(R.id.vehnumber);
                                TextView drivname = (TextView) findViewById(R.id.drivname);
                                TextView drivcontact = (TextView) findViewById(R.id.drivcontact);
                                TextView escname = (TextView) findViewById(R.id.escname);
                                TextView esccontact = (TextView) findViewById(R.id.esccontact);
                                TextView vendorcontact=(TextView) findViewById(R.id.vendorcontact);
                                TextView vendorname=(TextView) findViewById(R.id.vendorname);


                                vehnum.setText(response.getString("VEHICLENUMBER"));
                                drivname.setText(response.getString("DRIVERNAME"));
                                drivcontact.setText(response.getString("DRIVERNUMBER"));
                                escname.setText(response.getString("ESCORTNAME"));
                                esccontact.setText(response.getString("ESCORTNUMBER"));
                                vendorname.setText(response.getString("VENDORNAME"));

                                vendorcontact.setText(response.getString("VENDORCONTACT"));


                            } else {

                                Toast.makeText(getApplicationContext(), "Contact Details not found", Toast.LENGTH_LONG).show();


                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }
                        ,new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){

                        Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low", Toast.LENGTH_LONG).show();


                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(req);


            }

         catch (Exception e) {
            e.printStackTrace();
        }
    }
public void stoppanic()
{
    try{
        boolean valid = true;
        if (comment.getText().toString().equalsIgnoreCase("")) {
            comment.setError("Please input primary action");
            valid = false;

        }
        if (valid) {
            final JSONObject jobj = new JSONObject();
            jobj.put("ACTION", "PANICSTOP");
            jobj.put("EMPID", CommenSettings.empid);
            jobj.put("TRIPID", tripid);
            jobj.put("ALARMCAUSE", panicSpin.getSelectedItem().toString());
            jobj.put("PRIMARYACTION", comment.getText().toString());
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("PANICSTOP", "" + jobj.toString());
                        Log.d("RESPONSE PANIC", "" + response.toString());
                        if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {


                            {
                                new PromptDialog.Builder(PanicAdminActivity.this)
                                        .setTitle("Panic Stop")
                                        .setCanceledOnTouchOutside(false)
                                        .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR)
                                        .setButton1TextColor(R.color.md_blue_400)

                                        .setMessage("" + response.getString("STATUS"))
                                        .setButton1("OK", new PromptDialog.OnClickListener() {

                                            @Override
                                            public void onClick(Dialog dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                            // Toast.makeText(getApplicationContext(), "" + response.getString("STATUS"), Toast.LENGTH_LONG).show();
                            // finish();

                        } else {

                            new PromptDialog.Builder(PanicAdminActivity.this)
                                    .setTitle("Panic Stop")
                                    .setCanceledOnTouchOutside(false)
                                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR)
                                    .setButton1TextColor(R.color.md_blue_400)

                                    .setMessage("" + response.getString("STATUS"))
                                    .setButton1("OK", new PromptDialog.OnClickListener() {

                                        @Override
                                        public void onClick(Dialog dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .show();
//                                Toast.makeText(getApplicationContext(), "" + response.getString("STATUS"), Toast.LENGTH_LONG).show();
//                                finish();
                        }


                    } catch (Exception e) {
                        AppController.getInstance().trackException(e);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Server took too long to respond", Toast.LENGTH_LONG).show();
//                        pd.dismiss();


                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);

        }


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.panic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if(id==R.id.action_user)
        {
            Toast.makeText(getApplicationContext(),"USER ON BOARD",Toast.LENGTH_LONG).show();
            //emplist(JSONObject);
            final Intent intent = new Intent(this, EmplistPanic.class);
            intent.putExtra("TRIPID",tripid);
            intent.putExtra("URL",SURL);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    private void emplist(JSONObject jsonObject){
        String names[] ={"A","B","C","D","E","F","G","H","I","J","K","L"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PanicAdminActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.emplist_panic, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Employees on board");
        ListView lv = (ListView) convertView.findViewById(R.id.paniclistview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        alertDialog.show();
    }
}

