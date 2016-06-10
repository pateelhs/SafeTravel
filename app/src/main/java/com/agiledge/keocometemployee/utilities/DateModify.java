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
    public int year, month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.datemodify);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 7);
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(DateModify.this, result, Toast.LENGTH_LONG).show();
//            }
//        });


//        List<String> tmp = new ArrayList<>();
//        tmp.add("2015-7-1");
//        tmp.add("2015-7-8");
//        tmp.add("2015-7-16");
//        DPCManager.getInstance().setDecorBG(tmp);
//
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 7);
//        picker.setDPDecor(new DPDecor() {
//            @Override
//            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.RED);
//                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
//            }
//        });
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(DateModify.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

        // Example of custom date's foreground decor
//        List<String> tmpTL = new ArrayList<>();
//        tmpTL.add("2015-10-5");
//        tmpTL.add("2016-6-9");
//        tmpTL.add("2015-10-6");
//        tmpTL.add("2015-10-7");
//        tmpTL.add("2015-10-8");
//        tmpTL.add("2015-10-9");
//        tmpTL.add("2015-10-10");
//        tmpTL.add("2015-10-11");
//        DPCManager.getInstance().setDecorTL(tmpTL);
//
//        List<String> tmpTR = new ArrayList<>();
//        tmpTR.add("2015-10-10");
//        tmpTR.add("2015-10-11");
//        tmpTR.add("2015-10-12");
//        tmpTR.add("2015-10-13");
//        tmpTR.add("2015-10-14");
//        tmpTR.add("2015-10-15");
//        tmpTR.add("2015-10-16");

        getschedules();

        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
        Calendar now = Calendar.getInstance();
         year = now.get(Calendar.YEAR);
         month = now.get(Calendar.MONTH) + 1;
        picker.setDate(year, month);

        picker.setFestivalDisplay(false);
        picker.setTodayDisplay(true);
        picker.setHolidayDisplay(true);
        picker.setDeferredDisplay(false);
        picker.setMode(DPMode.SINGLE);

//        picker.setDPDecor(new DPDecor() {
//            @Override
//            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint, String data) {
//                super.drawDecorTL(canvas, rect, paint, data);
//                switch (data) {
//                    case "2016-6-5":
//                    case "2016-6-7":
//                    case "2016-6-9":
//                    case "2016-6-11":
//                        paint.setColor(Color.GREEN);
//                        canvas.drawRect(rect, paint);
//                        break;
//                    default:
//                        paint.setColor(Color.RED);
//                        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
//                        break;
//                }
//            }
//
//            @Override
//            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint, String data) {
//                super.drawDecorTR(canvas, rect, paint, data);
//                switch (data) {
//                    case "2015-10-10":
//                    case "2015-10-11":
//                    case "2015-10-12":
//                        paint.setColor(Color.BLUE);
//                        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
//                        break;
//                    default:
//                        paint.setColor(Color.YELLOW);
//                        canvas.drawRect(rect, paint);
//                        break;
//                }
//            }
//        });
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

        // DatePicker Example in dialog
        //Commented by Pateel to hide date picker button functionality
//        Button btnPick = (Button) findViewById(R.id.main_btn);
//        btnPick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog dialog = new AlertDialog.Builder(DateModify.this).create();
//                dialog.show();
//                DatePicker picker = new DatePicker(DateModify.this);
//                picker.setDate(year, month);
//                picker.setMode(DPMode.SINGLE);
//                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
//                    @Override
//                    public void onDatePicked(String date) {
//                        Toast.makeText(DateModify.this, date, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setContentView(picker, params);
//                dialog.getWindow().setGravity(Gravity.CENTER);
//            }
//        });
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
                            DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
                            picker.setDate(year, month);
                            DPCManager.getInstance().setDecorTR(calscheduleddates);
                           // Toast.makeText(getApplicationContext(), calscheduleddates.size(), Toast.LENGTH_LONG).show();
                            picker.setDPDecor(new DPDecor() {
                                @Override
                                public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
                                    paint.setColor(Color.RED);
                                    canvas.drawRect(rect, paint);
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();


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

    public void fillschedules(){
        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
        DPCManager.getInstance().setDecorTR(calscheduleddates);

        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.RED);
                canvas.drawRect(rect, paint);
            }
        });
    }
}
