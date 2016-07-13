package com.agiledge.keocometemployee.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.GCM.QuickstartPreferences;
import com.agiledge.keocometemployee.GCM.RegistrationIntentService;
import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class OTPActivity extends Activity{
	//GCM
	private static final int REQUEST_CODE = 0;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	//private static final String TAG = "FSM_GCM";

	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private ProgressBar mRegistrationProgressBar;
	private TextView mInformationTextView;
	private boolean isReceiverRegistered;
	private static final String TAG = "Safe_GCM";


	Button submit;
	String android_id="";
	EditText otpnumber;
	String serverotp="",users="";
	String otp;
	String post_result;
	TelephonyManager tel;
	String IMEI;
	public static String macAddress;
	private ProgressDialog mdialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		AppController.getInstance().trackScreenView("OTP activity");

    	//gcm
		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
				SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences
						.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {
					//mInformationTextView.setText(getString(R.string.server_registering));
				} else {
					//mInformationTextView.setText(getString(R.string.gcm_error));
				}
			}
		};
		//mInformationTextView = (TextView) findViewById(R.id.informationTextView);

		// Registering BroadcastReceiver
		registerReceiver();
		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Intent intent = new Intent(this, RegistrationIntentService.class);
			startService(intent);
		}
    		
    	
        
        otpnumber = (EditText) findViewById(R.id.otpnumber);      

        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(0);
				
				Bundle extras = getIntent().getExtras();
				if (extras != null) {
					users=extras.getString("user_type");
				    serverotp = extras.getString("otpserver");
				}
				otp=otpnumber.getText().toString();
				if (otp.length() == 0)
				{	if(mdialog != null && mdialog.isShowing()){
		        		mdialog.dismiss();
		        	}
					otpnumber.setError("Enter OTP?"); }
					
				else if(otp.equalsIgnoreCase(serverotp))
				{
					try
					{
					JSONObject jobj=new JSONObject();
		            jobj.put("ACTION","TRIP_DETAILS");
					jobj.put("IMEI_NUMBER",android_id);
						JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getString("result").equalsIgnoreCase("TRUE")) {
										if (mdialog != null && mdialog.isShowing()) {
											mdialog.dismiss();
										}
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

										startActivity(in);
										finish();
									} else {
										if (mdialog != null && mdialog.isShowing()) {
											mdialog.dismiss();
										}
										Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

									}
								} catch (Exception e) {
									if (mdialog != null && mdialog.isShowing()) {
										mdialog.dismiss();
									}
									e.printStackTrace();
								}
							}
						}
							,new Response.ErrorListener()

							{
								@Override
								public void onErrorResponse (VolleyError error){
								Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
								if (mdialog != null && mdialog.isShowing()) {
									mdialog.dismiss();
								}

							}
						});

							// Adding request to request queue
							AppController.getInstance().addToRequestQueue(req);

					}catch(Exception e){e.printStackTrace();
						if(mdialog != null && mdialog.isShowing()){
							mdialog.dismiss();
						}
						AppController.getInstance().trackException(e);}
				}
				else{
					{	if(mdialog != null && mdialog.isShowing()){
		        		mdialog.dismiss();
		        	}
					otpnumber.setError("Enter Correct OTP"); }
				}
				
				
			}
		});
    }
	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		isReceiverRegistered = false;
		super.onPause();
	}
	private void registerReceiver(){
		if(!isReceiverRegistered) {
			LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
					new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
			isReceiverRegistered = true;
		}
	}
	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		
		 switch (id) {
	     case 0:
	    	 mdialog = new ProgressDialog(this);
	    	 mdialog.setMessage("Please Wait...");
	    	 mdialog.setCancelable(false);
	    	 mdialog.setIndeterminate(false);
	    	 mdialog.show();
	 }
		return super.onCreateDialog(id);
	}
			 
	}

