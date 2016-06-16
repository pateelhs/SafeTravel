package com.agiledge.keocometemployee.activities;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.TransparentProgressDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MainActivity extends Activity {

	public static String macAddress;
	  private ProgressDialog mdialog;
	private static final int REQUEST_CODE=0;
	private TransparentProgressDialog pd;
	boolean startapp=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		pd = new TransparentProgressDialog(this, R.drawable.loading);
		pd.show();
		WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		macAddress = wimanager.getConnectionInfo().getMacAddress();
		
		
		
		//registerReceiver();
		SharedPreferences sharedpref=getPreferences(Context.MODE_PRIVATE);
		String username=sharedpref.getString("APP_USERNAME","NOT_FOUND");
		String email=sharedpref.getString("APP_EMAIL","NOT_FOUND");
		String gender=sharedpref.getString("APP_EMP_GENDER","NOT_FOUND");
		if(username.equalsIgnoreCase("NOT_FOUND")||email.equalsIgnoreCase("NOT_FOUND")||gender.equalsIgnoreCase("NOT_FOUND")) {
			initial();
		}
		else{
			pd.hide();
			boolean isEnabled = isGPSenabled();
			if(isEnabled) {
				Intent in = new Intent(getApplicationContext(), Home_Activity.class);
				startActivity(in);
				finish();
			}
			else{
				buildAlertMessageNoGps();


			}

		}

		



         //dialog.show();
          
        
		
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			super.onActivityResult(requestCode, resultCode, data);
			
		
		}

	
		private boolean isGPSenabled()
	    {	
	    	final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    	
	    	return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    }



	    
	    private void buildAlertMessageNoGps() {
	        final Dialog dialog = new Dialog(MainActivity.this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         dialog.setContentView(R.layout.gpspopup);
	        
	         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	         lp.copyFrom(dialog.getWindow().getAttributes());
	         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	         lp.gravity = Gravity.BOTTOM;

	         dialog.getWindow().setAttributes(lp);
	         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	        
	         Button yes = (Button) dialog.findViewById(R.id.yes);
	         Button no = (Button) dialog.findViewById(R.id.no);
	         yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.cancel();
	        		launchGPSOptions();
					startapp=true;
					
				}
			});
	         no.setOnClickListener(new OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				dialog.cancel();

	    			System.exit(1);
	 				
	 			}
	 		});
	         dialog.show();
	    }

	    private void launchGPSOptions()
	    {
	    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	startActivityForResult(intent, REQUEST_CODE);

	    }
	    protected void onStart() {
	    	super.onStart();
			if(startapp)
				initial();
	    	}

		public void initial()
		{
		try
		{
		JSONObject jobj=new JSONObject();

		  boolean isEnabled = isGPSenabled();
			if(isEnabled)
	        {

					jobj.put("ACTION", "IMEI_CHECK");
					jobj.put("IMEI_NUMBER", macAddress);
					JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getString("result").equalsIgnoreCase("TRUE")) {
									SharedPreferences sharedpref=getPreferences(Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = sharedpref.edit();
									editor.putString("APP_USERNAME", response.getString("EMP_NAME"));
									editor.putString("APP_EMAIL", response.getString("EMP_EMAIL"));
									editor.putString("APP_EMP_GENDER", response.getString("EMP_GENDER"));
									editor.putString("APP_EMPID",response.getString("EMP_ID"));
									editor.putString("APP_USERTYPE",response.getString("user_type"));
									editor.apply();
									pd.hide();
									Intent in = new Intent(getApplicationContext(), Home_Activity.class);
									in.putExtra("user_type", response.getString("user_type"));
									startActivity(in);
									finish();

								} else {
									pd.hide();
									Intent in = new Intent(getApplicationContext(),
											Onetimeregister.class);
									startActivity(in);
									finish();


								}
							} catch (Exception e) {
								pd.hide();
								e.printStackTrace();
							}
						}
					}
							, new Response.ErrorListener()

					{
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
							pd.hide();

						}
					});

					// Adding request to request queue
					AppController.getInstance().addToRequestQueue(req);

	        }
	        else
	        {
	        	pd.hide();
	        	buildAlertMessageNoGps();
	        	
	        }
		}catch(Exception e){e.printStackTrace();}
}
		
		public void onBackPressed()
		{
			super.onBackPressed();
			System.exit(1);
		}

}


      