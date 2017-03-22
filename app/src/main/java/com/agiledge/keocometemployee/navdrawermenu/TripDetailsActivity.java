package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.agiledge.keocometemployee.utilities.JustifiedTextView;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("ResourceAsColor")
public class TripDetailsActivity extends AppCompatActivity {
	private CoordinatorLayout coordinatorLayout;
	SweetAlertDialog pDialog;
	public static TextView textView;
	private TransparentProgressDialog pd;
	private JustifiedTextView dcontct;
	TextView regno;
	TextView dname;
	//TextView dcontct;
	TextView tcode;
	TextView econt;
	TextView vendorname;
	TextView helpdesk;
	TextView shiftTime;
	String android_id="";
	public static String macAddress;
	String macaddress= CommenSettings.macAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripdetailsnew);

		Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
		SpannableStringBuilder SS = new SpannableStringBuilder("Trip Details");
		SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		getSupportActionBar().setTitle(SS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_band));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);


		pd = new TransparentProgressDialog(this, R.drawable.loading);
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		regno=(TextView) findViewById(R.id.td2);
		dname=(TextView) findViewById(R.id.td4);
		dcontct=(JustifiedTextView) findViewById(R.id.td6);
		dcontct.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
		dcontct.setLineSpacing(0);
		dcontct.setBackgroundColor(Color.TRANSPARENT);
		dcontct.setAlignment(Paint.Align.LEFT);
		dcontct.setTypeFace(Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf"));
		tcode=(TextView) findViewById(R.id.td8);
		vendorname=(TextView) findViewById(R.id.td10);
		helpdesk =(TextView) findViewById(R.id.td12);
		shiftTime =(TextView) findViewById(R.id.td14);

		pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText("Loading...");
		pDialog.setCancelable(false);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		imageclick();
		fillvalues();

	
	
		
	}

	public void fillvalues() {
		try {
			pDialog.show();
			JSONObject jobj = new JSONObject();
			jobj.put("ACTION", "GET_MYSTOP");
			jobj.put("IMEI_NUMBER",CommenSettings.android_id);

			JsonObjectRequest req = new JsonObjectRequest(CommenSettings.bus_serverAddress, jobj, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						Log.d("trip details page",response.toString());
						if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
							pDialog.dismiss();
							/*if(!response.getString("TRIP_ID").equalsIgnoreCase("")){*/
								regno.setText(response.getString("ROUTENAME"));
								dname.setText(response.getString("TIME"));
								dcontct.setText(response.getString("ADDRESS"));
								tcode.setText(response.getString("VEHICLENO"));
							vendorname.setText(response.getString("VENDOR"));
							helpdesk.setText(response.getString("HELPDESK"));
							shiftTime.setText(response.getString("SHIFTTIME"));
								/*if(response.getString("SECURITY").equalsIgnoreCase("YES"))
								{
									econt.setText(response.getString("ESCORT_CONTACT"));
								}*/
							/*}*/
						/*	else{
								pDialog.dismiss();
								{
//									new PromptDialog.Builder(TripDetailsActivity.this)
//											.setTitle("Trip Status")
//											.setCanceledOnTouchOutside(false)
//											.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
//											.setButton1TextColor(R.color.md_blue_400)
//
//											.setMessage("Trip Not Started")
//											.setButton1("OK", new PromptDialog.OnClickListener() {
//
//												@Override
//												public void onClick(Dialog dialog, int which) {
//													dialog.dismiss();
//													finish();
//												}
//											})
//											.show();
									alert();
								}

							}*/



						} else {
							pDialog.dismiss();
							alert();
//							new PromptDialog.Builder(TripDetailsActivity.this)
//									.setTitle("Trip Status")
//									.setCanceledOnTouchOutside(false)
//									.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
//									.setButton1TextColor(R.color.md_blue_400)
//
//									.setMessage("Trip Not Started")
//									.setButton1("OK", new PromptDialog.OnClickListener() {
//
//										@Override
//										public void onClick(Dialog dialog, int which) {
//											dialog.dismiss();
//											finish();
//										}
//									})
//									.show();
//							pd.dismiss();
//							Snackbar snackbar = Snackbar
//									.make(coordinatorLayout, "Something went Wrong", Snackbar.LENGTH_LONG);
//							View sbView = snackbar.getView();
//							TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//							textView.setTextColor(Color.RED);
//
//							snackbar.show();
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
		} catch (Exception e) {
			pDialog.dismiss();
			e.printStackTrace();
		}
		AppController.getInstance().trackScreenView("TripDetails screen");// for Google analytics data

	}
	
	public void imageclick()
	{
		ImageView imageview = (ImageView) findViewById(R.id.imageView1);
		imageview.setOnClickListener(new OnClickListener() {

	    		public void onClick(View v) {
	    			finish();
	    			//onBackPressed();
	    }
		 });
	}

	public void alert() {
		//pd.dismiss();
		pDialog.dismiss();
		new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setTitleText("Info")
				.setContentText("Your Trip Has Not Started")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.dismiss();
						finish();
					}
				})
				.show();



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
