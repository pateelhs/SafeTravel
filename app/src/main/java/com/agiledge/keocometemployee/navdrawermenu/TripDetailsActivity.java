package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

@SuppressLint("ResourceAsColor")
public class TripDetailsActivity extends Activity {

	public static TextView textView;
	private TransparentProgressDialog pd;
	TextView regno;
	TextView dname;
	TextView dcontct;
	TextView tcode;
	TextView econt;
	public static String macAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripdetails);
		pd = new TransparentProgressDialog(this, R.drawable.loading);
		regno=(TextView) findViewById(R.id.td2);
		dname=(TextView) findViewById(R.id.td4);
		dcontct=(TextView) findViewById(R.id.td6);
		tcode=(TextView) findViewById(R.id.td8);
		econt=(TextView) findViewById(R.id.td10);
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
			jobj.put("IMEI_NUMBER",macAddress);
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
								Toast.makeText(getApplicationContext(),"Trip not started!",Toast.LENGTH_LONG).show();
							}



						} else {
							pd.hide();
							Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
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
					Toast.makeText(getApplicationContext(), "Error while communicating" , Toast.LENGTH_LONG).show();
					pd.hide();


				}
			});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(req);
		} catch (Exception e) {
			pd.hide();
			e.printStackTrace();
		}
		//  AppController.getInstance().trackScreenView("Login");// for Google analytics data

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
