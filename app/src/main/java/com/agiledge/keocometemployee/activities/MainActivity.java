package com.agiledge.keocometemployee.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.agiledge.keocometemployee.constants.GetMacAddress;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MainActivity extends Activity {

	public static String macAddress;
	private static final int REQUEST_CODE=0;
	boolean startapp=false;
	String android_id="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			CommenSettings.macAddress= GetMacAddress.getMacAddr();
		}catch (Exception e){e.printStackTrace();}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	//	Toast.makeText(getApplicationContext(),""+CommenSettings.macAddress.toString(),Toast.LENGTH_LONG).show();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if(isConnected())
		start();
		else{
			new AlertDialog.Builder(this)
					.setTitle("No Internet")
					.setMessage("This app needs internet connectivity,please enable and try again!")
					.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.exit(1);
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}

		}


          
        
		
		public void start(){
			SharedPreferences sharedpref=getPreferences(Context.MODE_PRIVATE);
			String username=sharedpref.getString("APP_USERNAME","NOT_FOUND");
			String email=sharedpref.getString("APP_EMAIL","NOT_FOUND");
			String gender=sharedpref.getString("APP_EMP_GENDER","NOT_FOUND");
			String user_type=sharedpref.getString("APP_USERTYPE","NOT_FOUND");
			String empid=sharedpref.getString("APP_EMPID","NOT_FOUND");

			if(username.equalsIgnoreCase("NOT_FOUND")||email.equalsIgnoreCase("NOT_FOUND")||gender.equalsIgnoreCase("NOT_FOUND")||user_type.equalsIgnoreCase("NOT_FOUND")||empid.equalsIgnoreCase("NOT_FOUND")) {
				if(!android_id.equalsIgnoreCase(""))
				initial();
				else
					Toast.makeText(getApplicationContext(),"Error occured code-001",Toast.LENGTH_LONG).show();
			}
			else {

					Intent in = new Intent(getApplicationContext(), Home_Activity.class);
					in.putExtra("user_type", user_type);
					in.putExtra("displayname",username);
					in.putExtra("gender",gender);
					in.putExtra("email",email);
					in.putExtra("empid",empid);
					if(!android_id.equalsIgnoreCase("")) {
						startActivity(in);
						finish();
					}
				}
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
					finish();
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
//			if(startapp)
//				start();
	    	}

		public void initial()
		{
		try
		{
		JSONObject jobj=new JSONObject();
					jobj.put("ACTION", "IMEI_CHECK");
					jobj.put("IMEI_NUMBER", android_id);

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
									editor.commit();
									Intent in = new Intent(getApplicationContext(), Home_Activity.class);
									in.putExtra("user_type", response.getString("user_type"));
									in.putExtra("displayname",response.getString("EMP_NAME"));
									in.putExtra("gender",response.getString("EMP_GENDER"));
									in.putExtra("email",response.getString("EMP_EMAIL"));
									in.putExtra("empid",response.getString("EMP_ID"));

									startActivity(in);
									finish();

								} else {
									Intent in = new Intent(getApplicationContext(),
											Onetimeregister.class);
									startActivity(in);
									finish();


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


		}catch(Exception e){e.printStackTrace();}
}

		public void onBackPressed()
		{
			super.onBackPressed();
			System.exit(1);
		}
public boolean isConnected(){
	ConnectivityManager cm =
			(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	boolean isConnected = activeNetwork != null &&
			activeNetwork.isConnectedOrConnecting();
	return isConnected;
}

}


      