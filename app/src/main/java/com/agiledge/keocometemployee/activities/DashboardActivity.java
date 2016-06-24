//package com.agiledge.keocometemployee.activities;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AppCompatActivity;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.RelativeSizeSpan;
//import android.text.style.StyleSpan;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.agiledge.keocometemployee.constants.CommenSettings;
//import com.agiledge.keocometemployee.R;
//import com.agiledge.keocometemployee.app.AppController;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.github.mikephil.charting.animation.Easing;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.Legend.LegendPosition;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.formatter.PercentFormatter;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//
//
//
//public class DashboardActivity extends AppCompatActivity {
//    private TransparentProgressDialog pd;
//    private CommenSettings server;
//    private PieChart mChart;
//    String completed = "0", pending = "0", onhold = "0";
//    private Typeface tf;
//    public static TextView SelectedDateView;
//    public static EditText et1;
//
//    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//        public void open() {
//            //onCreateDialog();
//
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            month=month+1;
//            String smonth=""+month,sday=""+day;
//            if(month<10){
//                smonth="0"+month;
//            }
//            if(day<10){
//                sday="0"+day;
//            }
//           et1.setText(year + "-" + smonth + "-" + sday);
//
//
//        }
//    }
//
//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
//        AppController.getInstance().trackScreenView("View DashBoard Screen");// for Google analytics data
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
//        Button et=(Button) findViewById(R.id.txtdate);
//         et1=(EditText) findViewById(R.id.selected_date);
//        //SelectedDateView=(TextView) findViewById(R.id.txtdate);
//        pd = new TransparentProgressDialog(this, R.drawable.loading);
//        et1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(), "datePicker");
//
//
//            }
//        });
//        //pd.show();
//        et.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setData();
//            }
//        });
//
//
//       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        mChart = (PieChart) findViewById(R.id.chart1);
//        mChart.setUsePercentValues(true);
//        mChart.setDescription("");
//        mChart.setExtraOffsets(5, 10, 5, 5);
//
//        mChart.setDragDecelerationFrictionCoef(0.95f);
//
//        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//
//        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
//        mChart.setCenterText(generateCenterSpannableText());
//
//        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleColorTransparent(true);
//
//        mChart.setTransparentCircleColor(Color.BLACK);
//        mChart.setTransparentCircleAlpha(110);
//
//        mChart.setHoleRadius(48f);
//        mChart.setTransparentCircleRadius(48f);
//
//        mChart.setDrawCenterText(true);
//
//        mChart.setRotationAngle(0);
//        // enable rotation of the chart by touch
//        mChart.setRotationEnabled(true);
//        mChart.setHighlightPerTapEnabled(true);
//
//        // mChart.setUnit(" €");
//        // mChart.setDrawUnitsInChart(true);
//
//        // add a selection listener
//        //  mChart.setOnChartValueSelectedListener(this);
//
//
//
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        // mChart.spin(2000, 0, 360);
//
//        Legend l = mChart.getLegend();
//        l.setPosition(LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //getMenuInflater().inflate(R.menu.pie, menu);
//        return true;
//    }
//    private class TransparentProgressDialog extends Dialog {
//
//        private ImageView iv;
//
//        public TransparentProgressDialog(Context context, int resourceIdOfImage) {
//            super(context, R.style.TransparentProgressDialog);
//            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
//            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
//            getWindow().setAttributes(wlmp);
//            setTitle(null);
//            setCancelable(false);
//            setOnCancelListener(null);
//            LinearLayout layout = new LinearLayout(context);
//            layout.setOrientation(LinearLayout.VERTICAL);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            iv = new ImageView(context);
//            iv.setImageResource(resourceIdOfImage);
//            layout.addView(iv, params);
//            addContentView(layout, params);
//        }
//
//        @Override
//        public void show() {
//            super.show();
//            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
//            anim.setInterpolator(new LinearInterpolator());
//            anim.setRepeatCount(Animation.INFINITE);
//            anim.setDuration(1000);
//            iv.setAnimation(anim);
//            iv.startAnimation(anim);
//        }
//    }
//
//
//
//
//
//    public void setData() {
//
//
//       final ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//       final ArrayList<String> xVals = new ArrayList<String>();
//
//        try {
//            if(et1.getText().toString()!=null&&!et1.getText().toString().equalsIgnoreCase("")) {
//                pd.show();
//                WifiManager wifiMan = (WifiManager) this.getSystemService(
//                        Context.WIFI_SERVICE);
//                WifiInfo wifiInf = wifiMan.getConnectionInfo();
//
//                Intent i = getIntent();
//                //String fromdate = i.getStringExtra("FROM_DATE");
//                String macAddress = wifiInf.getMacAddress();
//                JSONObject json = new JSONObject();
//                json.put("ACTION", "GET_ENGINEER_DASHBOARD");
//                json.put("MAC_ADDRESS", macAddress);
//                // json.put("FROM_DATE",fromdate);
//                json.put("DATE", et1.getText().toString());
//                JsonObjectRequest req = new JsonObjectRequest(server.serverAddress, json, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
//                                pd.cancel();
//                                completed = response.getString("COMPLETED");
//                                pending = response.getString("PENDING");
//                                onhold = response.getString("ONHOLD");
//                                yVals1.add(new Entry((float) Integer.parseInt(completed), 1));
//                                xVals.add("Completed");
//                                yVals1.add(new Entry((float) Integer.parseInt(pending), 1));
//                                xVals.add("Pending");
//                                yVals1.add(new Entry((float) Integer.parseInt(onhold), 1));
//                                xVals.add("On Hold");
//                                PieDataSet dataSet = new PieDataSet(yVals1, "");
//                                dataSet.setSliceSpace(2f);
//                                // dataSet.setSelectionShift(5f);
//
//                                // add a lot of colors
//
//                                ArrayList<Integer> colors = new ArrayList<Integer>();
//
//                                for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                                    colors.add(c);
//
//                                for (int c : ColorTemplate.JOYFUL_COLORS)
//                                    colors.add(c);
//
//                                for (int c : ColorTemplate.COLORFUL_COLORS)
//                                    colors.add(c);
//
//                                for (int c : ColorTemplate.LIBERTY_COLORS)
//                                    colors.add(c);
//
//                                for (int c : ColorTemplate.PASTEL_COLORS)
//                                    colors.add(c);
//
//                                colors.add(ColorTemplate.getHoloBlue());
//
//                                dataSet.setColors(colors);
//                                //dataSet.setSelectionShift(0f);
//
//                                PieData data = new PieData(xVals, dataSet);
//                                data.setValueFormatter(new PercentFormatter());
//                                data.setValueTextSize(11f);
//                                data.setValueTextColor(Color.RED);
//                                data.setValueTypeface(tf);
//                                mChart.setData(data);
//
//                                // undo all highlights
//                                mChart.highlightValues(null);
//
//                                mChart.invalidate();
//
//                            } else {
//                                pd.cancel();
//                                Toast.makeText(getApplicationContext(), response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
//                                finish();
//                                Intent in= new Intent(DashboardActivity.this,DashboardActivity.class);
//                                startActivity(in);
//
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pd.cancel();
//                        Toast.makeText(getApplicationContext(), "Error while communicating loading failed.." + error.getMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//                });
//                AppController.getInstance().addToRequestQueue(req);
//            }else{
//                et1.setError("Please select date!");
//            }
//        }catch(Exception e){
//            AppController.getInstance().trackException(e);
//            e.printStackTrace();
//        }
//
//
//
//    }
//
//    private SpannableString generateCenterSpannableText() {
//
//        SpannableString s = new SpannableString("My Dashboard");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 0, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, s.length(), 0);
//        return s;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//}
