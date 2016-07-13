package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.PromptDialog;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

@SuppressLint("ResourceAsColor")
public class TripDetailsActivity extends Activity {
	private CoordinatorLayout coordinatorLayout;

	public static TextView textView;
	private TransparentProgressDialog pd;
	TextView regno;
	TextView dname;
	TextView dcontct;
	TextView tcode;
	TextView econt;
	String android_id="";
	public static String macAddress;
	String macaddress= CommenSettings.macAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripdetails);
		pd = new TransparentProgressDialog(this, R.drawable.loading);
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		regno=(TextView) findViewById(R.id.td2);
		dname=(TextView) findViewById(R.id.td4);
		dcontct=(TextView) findViewById(R.id.td6);
		tcode=(TextView) findViewById(R.id.td8);
		econt=(TextView) findViewById(R.id.td10);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		macAddress = wimanager.getConnectionInfo().getMacAddress();
		imageclick();
		fillvalues();

	
	
		
	}

	public void fillvalues() {
		try {
			pd.show();
			JSONObject jobj = new JSONObject();
			jobj.put("ACTION", "TRIP_DETAILS");
			jobj.put("IMEI_NUMBER",android_id);
			JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {

						if (response.getString("result").equalsIgnoreCase("TRUE")) {
							pd.hide();
							if(!response.getString("TRIP_ID").equalsIgnoreCase("")){
								regno.setText(response.getString("REG_NO"));
								dname.setText(response.getString("DRIVER_NAME"));
								dcontct.setText(response.getString("DRIVER_CONTACT"));
								tcode.setText(response.getString("TRIP_CODE"));
								if(response.getString("SECURITY").equalsIgnoreCase("YES"))
								{
									econt.setText(response.getString("ESCORT_CONTACT"));
								}
							}
							else{
								pd.dismiss();
								{
									new PromptDialog.Builder(TripDetailsActivity.this)
											.setTitle("Trip Status")
											.setCanceledOnTouchOutside(false)
											.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
											.setButton1TextColor(R.color.md_blue_400)

											.setMessage("Trip Not Started")
											.setButton1("OK", new PromptDialog.OnClickListener() {

												@Override
												public void onClick(Dialog dialog, int which) {
													dialog.dismiss();
													finish();
												}
											})
											.show();
								}

							}



						} else {
							pd.hide();
							Snackbar snackbar = Snackbar
									.make(coordinatorLayout, "Something went Wrong", Snackbar.LENGTH_LONG);
							View sbView = snackbar.getView();
							TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
							textView.setTextColor(Color.RED);

							snackbar.show();
						}


					} catch (Exception e) {
						pd.hide();
						AppController.getInstance().trackException(e);
						e.printStackTrace();
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
									fillvalues();
								}
							});

					// Changing message text color
					snackbar.setActionTextColor(Color.RED);

					// Changing action button text color
					View sbView = snackbar.getView();
					TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.YELLOW);
					pd.hide();
					snackbar.show();




				}
			});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(req);
		} catch (Exception e) {
			pd.hide();
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
}
