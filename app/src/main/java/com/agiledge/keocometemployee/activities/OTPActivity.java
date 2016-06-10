package com.agiledge.keocometemployee.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class OTPActivity extends Activity{
	Button submit;
	
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
		AppController.getInstance().trackScreenView("OTP activity");
		WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		macAddress = wimanager.getConnectionInfo().getMacAddress();
    	
    		
    	
        
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
					jobj.put("IMEI_NUMBER",macAddress);
						JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getString("result").equalsIgnoreCase("TRUE")) {
										if (mdialog != null && mdialog.isShowing()) {
											mdialog.dismiss();
										}
										Intent in = new Intent(getApplicationContext(),
												MapClass.class);
										Bundle extras = getIntent().getExtras();
										in.putExtra("user_type", response.getString("user_type"));
										in.putExtra("EMP_NAME", extras.getString("EMP_NAME"));
										in.putExtra("EMP_PERSONNELNO", extras.getString("EMP_PERSONNELNO"));
										in.putExtra("EMP_GENDER", extras.getString("EMP_GENDER"));
										in.putExtra("EMP_EMAIL", extras.getString("EMP_EMAIL"));
										in.putExtra("EMP_SITE", extras.getString("EMP_SITE"));
										in.putExtra("EMP_ID", extras.getString("EMP_ID"));
										in.putExtra("TRIP_ID", response.getString("TRIP_ID"));
										in.putExtra("TRIP_CODE", response.getString("TRIP_CODE"));
										in.putExtra("TRIP_DATE", response.getString("TRIP_DATE"));
										in.putExtra("TRIP_LOG", response.getString("TRIP_LOG"));
										in.putExtra("REG_NO", response.getString("REG_NO"));
										in.putExtra("TRIP_TIME", response.getString("TRIP_TIME"));
										in.putExtra("DRIVER_NAME", response.getString("DRIVER_NAME"));
										in.putExtra("DRIVER_CONTACT", response.getString("DRIVER_CONTACT"));
										in.putExtra("EMPS_COUNT", response.getString("EMPS_COUNT"));
										in.putExtra("MSGVIEW", "YES");
										in.putExtra("SECURITY", response.getString("SECURITY"));
										if (response.getString("SECURITY").equalsIgnoreCase("YES")) {
											in.putExtra("ESCORT_NAME", response.getString("ESCORT_NAME"));
											in.putExtra("ESCORT_CONTACT", response.getString("ESCORT_CONTACT"));
										}
										if (!response.getString("EMPS_COUNT").equalsIgnoreCase("") && response.getString("EMPS_COUNT") != null) {
											for (int i = 1; i <= Integer.parseInt(response.getString("EMPS_COUNT")); i++) {
												in.putExtra("PERSONNEL_NO" + i, response.getString("PERSONNEL_NO" + i));
												in.putExtra("EMP_NAME" + i, response.getString("EMP_NAME" + i));
												in.putExtra("GENDER" + i, response.getString("GENDER" + i));
												in.putExtra("EMP_ID" + i, response.getString("EMP_ID" + i));
												in.putExtra("EMP_CONTACT" + i, response.getString("EMP_CONTACT" + i));

											}

										}
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
//					ServerCommunication sobj=new ServerCommunication(jobj);
//					sobj.setDataDownloadListen(new DataDownloadListener()
//					{
//						public void dataSuccess(String result)
//						{
//							try {
//								if(result!=null&&!result.equalsIgnoreCase(""))
//								{
//								JSONObject robj;
//								robj = new JSONObject(result);
//							if(robj.getString("result").equalsIgnoreCase("true"))
//							{
//								if(mdialog != null && mdialog.isShowing()){
//			    	        		mdialog.dismiss();
//			    	        	}
//								Intent in = new Intent(getApplicationContext(),
//										MapClass.class);
//								 Bundle extras = getIntent().getExtras();
//								 in.putExtra("user_type", robj.getString("user_type"));
//								 in.putExtra("EMP_NAME", extras.getString("EMP_NAME"));
//						         in.putExtra("EMP_PERSONNELNO", extras.getString("EMP_PERSONNELNO"));
//						         in.putExtra("EMP_GENDER", extras.getString("EMP_GENDER"));
//						         in.putExtra("EMP_EMAIL", extras.getString("EMP_EMAIL"));
//						         in.putExtra("EMP_SITE", extras.getString("EMP_SITE"));
//						         in.putExtra("EMP_ID", extras.getString("EMP_ID"));
//						        	 	in.putExtra("TRIP_ID", robj.getString("TRIP_ID"));
//									    in.putExtra("TRIP_CODE", robj.getString("TRIP_CODE"));
//										in.putExtra("TRIP_DATE", robj.getString("TRIP_DATE"));
//										in.putExtra("TRIP_LOG", robj.getString("TRIP_LOG"));
//										in.putExtra("REG_NO", robj.getString("REG_NO"));
//										in.putExtra("TRIP_TIME", robj.getString("TRIP_TIME"));
//										in.putExtra("DRIVER_NAME", robj.getString("DRIVER_NAME"));
//										in.putExtra("DRIVER_CONTACT", robj.getString("DRIVER_CONTACT"));
//										in.putExtra("EMPS_COUNT", robj.getString("EMPS_COUNT"));
//										in.putExtra("MSGVIEW", "YES");
//										in.putExtra("SECURITY", robj.getString("SECURITY"));
//										if(robj.getString("SECURITY").equalsIgnoreCase("YES"))
//										{
//											in.putExtra("ESCORT_NAME", robj.getString("ESCORT_NAME"));
//											in.putExtra("ESCORT_CONTACT", robj.getString("ESCORT_CONTACT"));
//										}
//										if(!robj.getString("EMPS_COUNT").equalsIgnoreCase("")&&robj.getString("EMPS_COUNT")!=null)
//										{
//										for(int i=1;i<=Integer.parseInt(robj.getString("EMPS_COUNT"));i++)
//										{
//											in.putExtra("PERSONNEL_NO"+i, robj.getString("PERSONNEL_NO"+i));
//											in.putExtra("EMP_NAME"+i, robj.getString("EMP_NAME"+i));
//											in.putExtra("GENDER"+i, robj.getString("GENDER"+i));
//											in.putExtra("EMP_ID"+i, robj.getString("EMP_ID"+i));
//											in.putExtra("EMP_CONTACT"+i, robj.getString("EMP_CONTACT"+i));
//
//										}
//
//						         }
//								 startActivity(in);
//								 finish();
//							}
//								else
//								{
//									if(mdialog != null && mdialog.isShowing()){
//						        		mdialog.dismiss();
//						        	}
//									Toast.makeText(getApplicationContext(), "Oops Communication Failure", Toast.LENGTH_SHORT).show();
//
//								}
//								}
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								AppController.getInstance().trackException(e);
//							}
//							}
//						public void datafail()
//						{
//							if(mdialog != null && mdialog.isShowing()){
//				        		mdialog.dismiss();
//				        	}
//							Toast.makeText(getApplicationContext(), "Oops Communication Failure", Toast.LENGTH_SHORT).show();
//						}
//					});
//					sobj.execute();
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

