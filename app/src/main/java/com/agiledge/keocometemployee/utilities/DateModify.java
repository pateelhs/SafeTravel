package com.agiledge.keocometemployee.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.navdrawermenu.AdocDetails;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

/**

 *
 * @author Arshad and Pateel
 */
public class DateModify extends AppCompatActivity {

    List<String> scheduleddates=new ArrayList<String>();
    List<String> calscheduleddates=new ArrayList<String>();
    DatePicker picker ;
    public int year, month;
    private TransparentProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.datemodify);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pd = new TransparentProgressDialog(this, R.drawable.loading);
        pd.show();
       picker=(DatePicker) findViewById(R.id.main_dp);
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH)+1;
        picker.setDate(2016, 1);
       // picker.setFestivalDisplay(false);
        //picker.setTodayDisplay(false);
        //picker.setHolidayDisplay(true);
        //picker.setDeferredDisplay(false);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                try {
                    int month=Integer.parseInt(date.split("-")[1]);
                    int day=Integer.parseInt(date.split("-")[2]);
                    String mnth,dte;
                    if(month<10){
                        mnth="0"+month;
                    }
                    else
                        mnth=""+month;
                    if(day<10){
                        dte="0"+day;
                    }
                    else
                        dte=""+day;
                    date=dte+"/"+mnth+"/"+date.split("-")[0];
                    boolean flag=false;


                    if(scheduleddates.contains(date)) {
                        Intent k = new Intent(DateModify.this, AdocDetails.class);
                        k.putExtra("CLICKDATE", date);
                        startActivity(k);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });

        getschedules();



    }
    public void getschedules() {

        try {
            JSONObject jobj = new JSONObject();
            WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            String macAddress = wimanager.getConnectionInfo().getMacAddress();
            jobj.put("ACTION", "GET_SCHEDULES");
            jobj.put("IMEI", macAddress);
            JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
                            JSONArray dates=response.getJSONArray("DATES");
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");
                            for(int i=0;i<dates.length();i++)
                            {
                                calscheduleddates.add(dates.get(i).toString());
                                Date d=sdf.parse(dates.get(i).toString());
                                int month=Integer.parseInt(dates.get(i).toString().split("-")[1]);
                                int day=Integer.parseInt(dates.get(i).toString().split("-")[2]);
                                String mnth,dte;
                                if(month<10){
                                    mnth="0"+month;
                                }
                                else
                                    mnth=""+month;
                                if(day<10){
                                    dte="0"+day;
                                }
                                else
                                    dte=""+day;
                                String date=dte+"/"+mnth+"/"+dates.get(i).toString().split("-")[0];

                                scheduleddates.add(date);
                            }

                            showschedules();

                        } else {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception e) {
                        pd.dismiss();


                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Error while communicating" , Toast.LENGTH_LONG).show();


                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }
        catch (Exception e){
            e.printStackTrace();
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



    }
public void showschedules(){

List<String> calsch=new ArrayList<String>();
    for(int i=0;i<calscheduleddates.size();i++) {
        calsch.add(calscheduleddates.get(i));
    }
    DPCManager.getInstance().setDecorBG(calsch);
    picker.setDPDecor(new DPDecor() {
        @Override
        public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
            paint.setColor(Color.RED);
            canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
        }
    });
    picker.setDate(year,month);
    pd.dismiss();
}



}
