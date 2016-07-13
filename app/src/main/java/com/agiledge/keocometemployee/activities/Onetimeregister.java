package com.agiledge.keocometemployee.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.constants.GetMacAddress;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class Onetimeregister extends Activity {
	
	private static final int REQUEST_CODE = 0;

	public String SERVER_PREFERENCE = "SERVER_PREFERENCE";
	String macAddress;
	 TextView tvIsConnected;
	String uniqueno, mobileno;
	EditText mobilenumber;
	Button submit;
	String android_id="";

	String post_result;
	TelephonyManager tel;
	static String action="INITIAL_LOGIN";
	static int check=0;
	
	  private ProgressDialog mdialog;
	String macaddress=GetMacAddress.getMacAddr();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.onetimeregister);
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		AppController.getInstance().trackScreenView("Onetime register Activity");
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
		//uniquenumber = (EditText) findViewById(R.id.uniquenumber);
		mobilenumber = (EditText) findViewById(R.id.mobilenumber);
		submit = (Button) findViewById(R.id.submit);

        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xffffff);
            tvIsConnected.setText("Internet Connected");
        }
        else{
            tvIsConnected.setText("Internet NOT Connected");
        }
        

		
		  boolean isEnabled = isGPSenabled();
	        
	  //      displayGPSState(isEnabled);
	        
	        if(isEnabled)
	        {
	        	myfunction();
	        }
	        else
	        {

	        	buildAlertMessageNoGps();	
	        }
	        
	}
	  
	
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	

	protected void onResume() {
		super.onResume();
	}
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			super.onActivityResult(requestCode, resultCode, data);
			
			if(requestCode == REQUEST_CODE)
			{
	//			displayGPSState(isGPSenabled());
				myfunction();
			}
		}

	/*	private void displayGPSState(boolean isEnabled)
		{
			String status = "GPS is " + (isEnabled ? "Enabled" : "Disabled");
			 
			 ((TextView)findViewById(R.id.text_gpsstatus)).setText(status);
		}*/

		private boolean isGPSenabled()
	    {	
	    	final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    	
	    	return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    }
	    
	    private void buildAlertMessageNoGps() {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        
	        builder.setTitle("GPS State");
	        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
	        builder.setCancelable(false);
	        
	        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
	        		{
	                	public void onClick(final DialogInterface dialog, final int id) 
	                	{
	                		launchGPSOptions(); 
	                	}
	        		});
	        
	        builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
	        		{
	                	public void onClick(final DialogInterface dialog, final int id) 
	                	{
	                		dialog.cancel();
	                	}
	        		});
	        
	        builder.create().show();
	    }

	    private void launchGPSOptions()
	    {
	    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	startActivityForResult(intent, REQUEST_CODE);
	    
	    }
	    
	    private boolean isConnected() {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	        if (networkInfo != null && networkInfo.isConnected()) 
	            return true;
	        else
	            return false; 
			
		}
	    
	    private void myfunction()
	    {
	    	submit.setOnClickListener(new OnClickListener() {

				@Override
	    		public void onClick(View v) {
	    			showDialog(0); 
	    		//	uniqueno = uniquenumber.getText().toString();
	    			mobileno = mobilenumber.getText().toString();

	    			/*if (uniqueno.length() == 0){
	    				if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
	    				uniquenumber.setError("Enter Unique Number!");
	    			}else*/ if (mobileno.length() == 0)
	    			{
	    				if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
	    				mobilenumber.setError("Enter Mobile Number!");
	    			}else if (mobileno.length() < 10)
	    			{
	    				if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
	    				mobilenumber
	    						.setError("Mobile Number should be Minimum 10 digits!");
	    			}else if (mobileno.length() > 10)
	    			{
	    				if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
	    				mobilenumber
	    						.setError("Mobile Number should be Maximum 10 digits!");

	    			}/*else if (!uniqueno.equalsIgnoreCase(s1)
	    				//	||!uniqueno.equalsIgnoreCase(s2)||!uniqueno.equalsIgnoreCase(s2)
	    					) {
	    				if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
	    				uniquenumber.setError("Invalid Unique No!");
	    				} */
	    			else
	    			{
//	    				SharedPreferences sharedPreference = getSharedPreferences(SERVER_PREFERENCE, Context.MODE_PRIVATE);
//	    				SharedPreferences.Editor editor = sharedPreference.edit();
//	    				editor.putString("uniqueno", uniquenumber.getText().toString());
//	    				editor.commit();
//	    				
//	    				//SharedPreferences sharedPreference = getSharedPreferences(SERVER_PREFERENCE, Context.MODE_PRIVATE);
//	    				//SharedPreferences.Editor editor = sharedPreference.edit();
//	    				sharedPreference.getString("uniqueno", "0000");
	    				
	    				try{
	    				JSONObject jobj=new JSONObject();
	    				jobj.put("ACTION", "INITIAL_LOGIN");
	    				jobj.put("MOBILE_NUMBER", mobilenumber.getText().toString());

	    				jobj.put("IMEI_NUMBER",android_id);
							JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									try {

										if (response.getString("result").equalsIgnoreCase("TRUE")) {

											if(mdialog != null && mdialog.isShowing()){
												mdialog.dismiss();
											}
											Intent in = new Intent(getApplicationContext(),
													OTPActivity.class);
											in.putExtra("user_type", response.getString("user_type"));
											in.putExtra("otpserver",response.getString("otp"));
											in.putExtra("EMP_ID", response.getString("EMP_ID"));
											in.putExtra("EMP_NAME", response.getString("EMP_NAME"));
											in.putExtra("EMP_PERSONNELNO", response.getString("EMP_PERSONNELNO"));
											in.putExtra("EMP_GENDER", response.getString("EMP_GENDER"));
											in.putExtra("EMP_EMAIL", response.getString("EMP_EMAIL"));
											in.putExtra("EMP_SITE", response.getString("EMP_SITE"));
											startActivity(in);
											finish();
											//		 Toast.makeText(getApplicationContext(), "otp"+robj.getString("otp"), Toast.LENGTH_LONG).show();
										}
										else
										{
											if(mdialog != null && mdialog.isShowing()){
												mdialog.dismiss();
											}
											mobilenumber.setError("Mobile Number Not Found!");
										}



									} catch (Exception e) {
										if(mdialog != null && mdialog.isShowing()){
											mdialog.dismiss();
										}
										AppController.getInstance().trackException(e);
										e.printStackTrace();
									}

								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
									if(mdialog != null && mdialog.isShowing()){
										mdialog.dismiss();
									}

								}
							});

							// Adding request to request queue
							AppController.getInstance().addToRequestQueue(req);
	    		    /*    ServerCommunication sobj=new ServerCommunication(jobj);
	    		  		sobj.setDataDownloadListen(new DataDownloadListener()
	    		  		{
	    		  			public void dataSuccess(String result)
	    		  			{
	    		  					if(result!=null&&!result.equalsIgnoreCase(""))
	    		  					{
	    		  						JSONObject robj;
	    		  						try{	
	    		  					robj = new JSONObject(result);
	       		  				if(robj.getString("result").equalsIgnoreCase("true"))
	    		  				{
	    		  					if(mdialog != null && mdialog.isShowing()){
	    		  		        		mdialog.dismiss();
	    		  		        	}
	    		  			         Intent in = new Intent(getApplicationContext(),
	    		  								OTPActivity.class);
	    		  			       in.putExtra("user_type", robj.getString("user_type"));
	    		  			         in.putExtra("otpserver",robj.getString("otp"));
	    		  			       in.putExtra("EMP_ID", robj.getString("EMP_ID"));
	    		  			         in.putExtra("EMP_NAME", robj.getString("EMP_NAME"));
	    		  			         in.putExtra("EMP_PERSONNELNO", robj.getString("EMP_PERSONNELNO"));
	    		  			         in.putExtra("EMP_GENDER", robj.getString("EMP_GENDER"));
	    		  			         in.putExtra("EMP_EMAIL", robj.getString("EMP_EMAIL"));
	    		  			         in.putExtra("EMP_SITE", robj.getString("EMP_SITE"));
	    		  					 startActivity(in);
	    		  					 finish();
	    		  			//		 Toast.makeText(getApplicationContext(), "otp"+robj.getString("otp"), Toast.LENGTH_LONG).show();
	    		  				}
	    		  					else
	    		  				{
	    		  					if(mdialog != null && mdialog.isShowing()){
	    		      	        		mdialog.dismiss();
	    		      	        	}
	    		  					 mobilenumber.setError("Mobile Number Not Found!");
	    		  				}
	    		  						}catch(Exception e){e.printStackTrace();
											AppController.getInstance().trackException(e);}
	    		  					}
	    		  					
	    		  			}
	   
	    		  			public void datafail()
	    		  			{
	    		  				if(mdialog != null && mdialog.isShowing()){
	    		  	        		mdialog.dismiss();
	    		  	        	}
	    		  				Toast.makeText(getApplicationContext(), "Oops Communication Failure", Toast.LENGTH_SHORT).show();
	    		  			}
	    		  		});
	    		  		sobj.execute();*/
	    				}catch(Exception e){
							if(mdialog != null && mdialog.isShowing()){
								mdialog.dismiss();
							}
	    					e.printStackTrace();
							AppController.getInstance().trackException(e);
	    				}
	    		}
	    		}
		    	});
	    		
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
	 


      