package com.agiledge.keocometemployee.activities;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	public static String macAddress;
	  private ProgressDialog mdialog;
	  private static int initial=0;
	private static final int REQUEST_CODE=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		macAddress = wimanager.getConnectionInfo().getMacAddress();
		
		
		
		//registerReceiver();

		if(initial==0)
		{
		initial();
		initial=1;
		}
		
		

         //dialog.show();
          
        
		
		}
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
	@Override
	protected void onResume() {
		super.onResume();
		//registerReceiver();
	}

	@Override
	protected void onPause() {
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//		isReceiverRegistered = false;
		super.onPause();
	}


	    
	    private void buildAlertMessageNoGps() {
//	        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	        
//	        builder.setTitle("GPS State");
//	        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
//	        builder.setCancelable(false);
//	        
//	        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
//	        		{
//	                	public void onClick(final DialogInterface dialog, final int id) 
//	                	{
//	                		initial=2;
//	                		launchGPSOptions(); 
//	                		
//	                	}
//	        		});
//	        
//	        builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
//	        		{
//	                	public void onClick(final DialogInterface dialog, final int id) 
//	                	{
//	                		dialog.cancel();
//
//	            			System.exit(1);
//	                		
//	                	}
//	        		});
//	        
//	        builder.create().show();
	        
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
					initial=2;
					dialog.cancel();
	        		launchGPSOptions();
					
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
	    	if(initial==2)
	    	{
	    		
	    	initial();
	    	}
	    	}

		public void initial()
		{
			showDialog(0);
		try
		{
		JSONObject jobj=new JSONObject();
		//TelephonyManager tel=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//String imei=tel.getDeviceId().toString();

		  boolean isEnabled = isGPSenabled();
	        //String macAddress= GetMacAddress.MAC_ADDRESS;
	        if(isEnabled)
	        {

		jobj.put("ACTION", "IMEI_CHECK");
		jobj.put("IMEI_NUMBER",macAddress);
				JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("result").equalsIgnoreCase("TRUE")) {
								if (mdialog != null && mdialog.isShowing()) {
									mdialog.dismiss();
								}
								Intent in = new Intent(getApplicationContext(), MapClass.class);
								in.putExtra("user_type", response.getString("user_type"));
								in.putExtra("TRIP_ID", response.getString("TRIP_ID"));
								in.putExtra("TRIP_CODE", response.getString("TRIP_CODE"));
								in.putExtra("TRIP_DATE", response.getString("TRIP_DATE"));
								in.putExtra("TRIP_LOG", response.getString("TRIP_LOG"));
								in.putExtra("REG_NO", response.getString("REG_NO"));
								in.putExtra("TRIP_TIME", response.getString("TRIP_TIME"));
								in.putExtra("DRIVER_NAME", response.getString("DRIVER_NAME"));
								in.putExtra("DRIVER_CONTACT", response.getString("DRIVER_CONTACT"));
								in.putExtra("EMPS_COUNT", response.getString("EMPS_COUNT"));
								in.putExtra("SECURITY", response.getString("SECURITY"));
								in.putExtra("EMP_ID", response.getString("EMP_ID"));

								in.putExtra("EMP_NAME", response.getString("EMP_NAME"));
								in.putExtra("EMP_PERSONNELNO", response.getString("EMP_PERSONNELNO"));
								in.putExtra("EMP_GENDER", response.getString("EMP_GENDER"));
								in.putExtra("EMP_EMAIL", response.getString("EMP_EMAIL"));
								in.putExtra("EMP_SITE", response.getString("EMP_SITE"));
								in.putExtra("MSGVIEW", "NO");
								if(response.getString("SECURITY").equalsIgnoreCase("YES"))
								{
									in.putExtra("ESCORT_NAME", response.getString("ESCORT_NAME"));
									in.putExtra("ESCORT_CONTACT", response.getString("ESCORT_CONTACT"));
								}

								if(response.getString("EMPS_COUNT")!=null&&!response.getString("EMPS_COUNT").equalsIgnoreCase("")) {
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
								Intent in = new Intent(getApplicationContext(),
										Onetimeregister.class);
								startActivity(in);
								finish();


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
//		ServerCommunication sobj=new ServerCommunication(jobj);
//		sobj.setDataDownloadListen(new DataDownloadListener()
//		{
//			public void dataSuccess(String result)
//			{
//				try {
//					if(result!=null&&!result.equalsIgnoreCase(""))
//					{
//					JSONObject robj;
//					robj = new JSONObject(result);
//
//				if(robj!=null && robj.getString("result").equalsIgnoreCase("true"))
//
//
//				{
//					if(mdialog != null && mdialog.isShowing()){
//    	        		mdialog.dismiss();
//    	        	}
//			//		Toast.makeText(getApplicationContext(),"valli"+ result, Toast.LENGTH_LONG).show();
//
//					Intent in = new Intent(getApplicationContext(), MapClass.class);
//							in.putExtra("user_type", robj.getString("user_type"));
//				            in.putExtra("TRIP_ID", robj.getString("TRIP_ID"));
//						    in.putExtra("TRIP_CODE", robj.getString("TRIP_CODE"));
//							in.putExtra("TRIP_DATE", robj.getString("TRIP_DATE"));
//							in.putExtra("TRIP_LOG", robj.getString("TRIP_LOG"));
//							in.putExtra("REG_NO", robj.getString("REG_NO"));
//							in.putExtra("TRIP_TIME", robj.getString("TRIP_TIME"));
//							in.putExtra("DRIVER_NAME", robj.getString("DRIVER_NAME"));
//							in.putExtra("DRIVER_CONTACT", robj.getString("DRIVER_CONTACT"));
//							in.putExtra("EMPS_COUNT", robj.getString("EMPS_COUNT"));
//							in.putExtra("SECURITY", robj.getString("SECURITY"));
//							in.putExtra("EMP_ID", robj.getString("EMP_ID"));
//
//							 in.putExtra("EMP_NAME", robj.getString("EMP_NAME"));
//					         in.putExtra("EMP_PERSONNELNO", robj.getString("EMP_PERSONNELNO"));
//					         in.putExtra("EMP_GENDER", robj.getString("EMP_GENDER"));
//					         in.putExtra("EMP_EMAIL", robj.getString("EMP_EMAIL"));
//					         in.putExtra("EMP_SITE", robj.getString("EMP_SITE"));
//							in.putExtra("MSGVIEW", "NO");
//
//
////							   // JSONARRAY
////					         in.putExtra("ALLATLONGS", robj.getJSONArray("LAT").length());
////					         for(int j=1;j<=robj.getJSONArray("LAT").length();j++){
////					        	 in.putExtra("LAT"+j,robj.getJSONArray("LAT").getString(j) );
////					         }
////					         for(int j=1;j<=robj.getJSONArray("LONG").length();j++){
////					        	 in.putExtra("LONG"+j,robj.getJSONArray("LONG").getString(j) );
////					         }
////					         for(int j=1;j<=robj.getJSONArray("REGNO").length();j++){
////					        	 in.putExtra("REGNO"+j,robj.getJSONArray("REGNO").getString(j) );
////					         }
////					        	 for(int j=1;j<=robj.getJSONArray("TRIPID").length();j++){
////						        	 in.putExtra("TRIPID"+j,robj.getJSONArray("TRIPID").getString(j) );
////					        	 }
//
//
////		cancel
//						/*	in.putExtra("FROM_DATE", robj.getString("FROM_DATE"));
//					         in.putExtra("TO_DATE", robj.getString("TO_DATE"));
//					         in.putExtra("LOG_IN", robj.getString("LOG_IN"));
//					         in.putExtra("LOG_OUT", robj.getString("LOG_OUT"));
//
//					         // JSONARRAY
//					         in.putExtra("ALTIMES", robj.getJSONArray("ALTERDATE").length()+"");
//					         in.putExtra("OUT_LOGS_COUNT",robj.getJSONArray("OUT_LOGS").length()+"" );
//					         in.putExtra("IN_LOGS_COUNT",robj.getJSONArray("IN_LOGS").length()+"" );
//								for(int s=0;s<robj.getJSONArray("IN_LOGS").length();s++ ){
//									in.putExtra("IN_LOGS"+(s+1),robj.getJSONArray("IN_LOGS").getString(s) );
//								}
//
//								for(int t=0;t<robj.getJSONArray("OUT_LOGS").length();t++ ){
//									in.putExtra("OUT_LOGS"+(t+1),robj.getJSONArray("OUT_LOGS").getString(t) );
//									}
//					         for(int j=0;j<robj.getJSONArray("ALTERDATE").length();j++){
//					        	 in.putExtra("ALTERDATE"+(j+1),robj.getJSONArray("ALTERDATE").getString(j) );
//					         }
//					         for(int j=0;j<robj.getJSONArray("AL_LOG_IN").length();j++){
//					        	 in.putExtra("AL_LOG_IN"+(j+1),robj.getJSONArray("AL_LOG_IN").getString(j) );
//					         }
//					         for(int j=0;j<robj.getJSONArray("AL_LOG_OUT").length();j++){
//					        	 in.putExtra("AL_LOG_OUT"+(j+1),robj.getJSONArray("AL_LOG_OUT").getString(j));
//					         }
//					         in.putExtra("ADHOCTYPECOUNT", robj.getJSONArray("ADHOCTYPE").length()+"");
//								for(int i=0;i<Integer.parseInt(robj.getJSONArray("ADHOCTYPE").length()+"");i++)
//								{
//									in.putExtra("ADHOCTYPE"+(i+1),robj.getJSONArray("ADHOCTYPE").getString(i) );
//
//								}
////		cancel		*/
//							if(robj.getString("SECURITY").equalsIgnoreCase("YES"))
//							{
//								in.putExtra("ESCORT_NAME", robj.getString("ESCORT_NAME"));
//								in.putExtra("ESCORT_CONTACT", robj.getString("ESCORT_CONTACT"));
//							}
//
//							if(robj.getString("EMPS_COUNT")!=null&&!robj.getString("EMPS_COUNT").equalsIgnoreCase(""))
//							{
//							for(int i=1;i<=Integer.parseInt(robj.getString("EMPS_COUNT"));i++)
//							{
//								in.putExtra("PERSONNEL_NO"+i, robj.getString("PERSONNEL_NO"+i));
//								in.putExtra("EMP_NAME"+i, robj.getString("EMP_NAME"+i));
//								in.putExtra("GENDER"+i, robj.getString("GENDER"+i));
//								in.putExtra("EMP_ID"+i, robj.getString("EMP_ID"+i));
//								in.putExtra("EMP_CONTACT"+i, robj.getString("EMP_CONTACT"+i));
//
//							}
//
//							// cancel
//						//	Toast.makeText(getApplicationContext(),"SAN"+ robj.getString("FROM_DATE")+robj.getString("TO_DATE")+robj.getString("LOG_IN")+robj.getString("LOG_OUT")+robj.getString("ALTERDATE"), Toast.LENGTH_LONG).show();
//
//							}
//					 startActivity(in);
//					 finish();
//				}
//				else if(robj!=null && robj.getString("result").equalsIgnoreCase("false"))
//				{
//					if(mdialog != null && mdialog.isShowing()){
//    	        		mdialog.dismiss();
//    	        	}
//					 Intent in = new Intent(getApplicationContext(),
//								Onetimeregister.class);
//			         startActivity(in);
//					 finish();
//				}
//
//				}
//					else
//					{
//						if(mdialog != null && mdialog.isShowing()){
//			        		mdialog.dismiss();
//			        	}
//						Toast.makeText(getApplicationContext(), " No Internet", Toast.LENGTH_SHORT).show();
//     //  Toast.makeText(getApplicationContext(), "Vicky", Toast.LENGTH_LONG).show();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			public void datafail()
//			{
//				if(mdialog != null && mdialog.isShowing()){
//	        		mdialog.dismiss();
//	        	}
//				Toast.makeText(getApplicationContext(), "oops communication error", Toast.LENGTH_SHORT).show();
//			}
//		});
//		sobj.execute();
	        }
	        else
	        {
	        	if(mdialog != null && mdialog.isShowing()){
	        		mdialog.dismiss();
	        	}
	        	buildAlertMessageNoGps();
	        	
	        }
		}catch(Exception e){e.printStackTrace();}
}
		
		public void onBackPressed()
		{

			super.onBackPressed();
			//System.exit(1);
	    //	Toast.makeText(getApplicationContext(), "in start", Toast.LENGTH_LONG).show();
	    	//finish();
		}

}


      