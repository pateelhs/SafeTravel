package com.agiledge.keocometemployee.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.MyLocation;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LiveTrackingActivity extends Activity implements OnItemClickListener {
	
	//GetMacAddress macAddress;
	// Google Map
	//public static String macAddress;
	static boolean active=true;
	private GoogleMap googleMap;
	static int alertval=0,escval=0,tripstatusval=0;
	TelephonyManager tel;
	String IMEI;
	String TRIPID;
	double longitude,latitude;
	Handler mHandler;
	static boolean mapInitialize=true;
	static String emps="",msgview="NO";
	String panicstatus="no";
	
//	 private  AlertDialog.Builder builder1;
//	
//	 private DrawerLayout drawerLayout;
//		private ActionBarDrawerToggle drawerListener;
//		private ListView listView;
//		private String[] menuLists;
		
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				setContentView(R.layout.mycab);
		AppController.getInstance().trackScreenView("Live tracking screen");
		WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		String macAddress =CommenSettings.macAddress;
		
			
			     
		 tel=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			IMEI=tel.getDeviceId().toString();
			
		TextView ttime=(TextView) findViewById(R.id.t2);
		TextView tdate=(TextView) findViewById(R.id.t4);
//		TextView profilename=(TextView) findViewById(R.id.profilename);
//		TextView profileemail=(TextView) findViewById(R.id.profileemail);
//		ImageView profileimage=(ImageView) findViewById(R.id.profileimage);
		System.out.println("in 2");
		
		  ImageView trackadminbtn = (ImageView) findViewById(R.id.trackadminview);
		   trackadminbtn.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
				
					Intent intent = new Intent(LiveTrackingActivity.this, MapClass.class);
					startActivity(intent);
					
				}
			});
		
		try {
			
			Bundle extras = getIntent().getExtras();
			String displayname="",trip_time="",trip_id="",trip_code="",trip_log="",trip_date="",driver_name="",driver_contact="",reg_no="",emp_email="",emp_gender="";
			if (extras != null) {
			    displayname = extras.getString("EMP_NAME");
				msgview=extras.getString("MSGVIEW");
				alertfirsttime(displayname);
				emp_gender=extras.getString("EMP_GENDER");
				emp_email=extras.getString("EMP_EMAIL");
			    if(Integer.parseInt(extras.getString("EMPS_COUNT"))>0)
			    {
			    trip_id=extras.getString("TRIP_ID");
			    TRIPID=extras.getString("TRIP_ID");
			    trip_code=extras.getString("TRIP_CODE");
			    trip_date=extras.getString("TRIP_DATE");
	//		    trip_log=extras.getString("TRIP_LOG");
			    reg_no=extras.getString("REG_NO");
				driver_name=extras.getString("DRIVER_NAME");
				driver_contact=extras.getString("DRIVER_CONTACT");
				trip_time=extras.getString("TRIP_TIME")+" "+extras.getString("TRIP_LOG");
				
			}
				
			ttime.setText(trip_time);
		    tdate.setText(trip_date);
			this.mHandler = new Handler();
	        m_Runnable.run();
	        
			}
       	   
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	 
		
	 	 
	@Override
	protected void onStart() {
	super.onResume();	
	active=true;
	initilizeMap();
	}
	@Override
	protected void onResume() {
		//drawerLayout.closeDrawers();
		active=true;
		super.onResume();
		initilizeMap();
	}
	@Override
	protected void onStop() {
		//trimCache(this);
		super.onStop();
		 active=false;
		 alertval=0;
		 escval=0;
		 tripstatusval=0;
	}
	/**
	 * function to load map If map is not created it will create it for you
	 * */
	@SuppressLint("NewApi") private void initilizeMap() {
		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.map)).getMapAsync(o);
			
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void alertfirsttime(String displayname)
	{
		
//    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    
//    builder.setTitle("WELCOME MSG");
//    builder.setMessage("Hi "+displayname+", WELCOME TO Agiledge...");
//    builder.setCancelable(false);
//    
//    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
//    		{
//            	public void onClick(final DialogInterface dialog, final int id) 
//            	{
//            		dialog.cancel();
//            	}
//    		});
		 final Dialog dialog = new Dialog(LiveTrackingActivity.this);
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.welcomepopup);
         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
         lp.copyFrom(dialog.getWindow().getAttributes());
         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
         lp.gravity = Gravity.CENTER_VERTICAL;

         dialog.getWindow().setAttributes(lp);
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
         Button Ok = (Button) dialog.findViewById(R.id.welOK);

         TextView distxt= (TextView) dialog.findViewById(R.id.title1);
         distxt.setText("Hi "+displayname+", WELCOME TO Agiledge...");
        Ok.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
				
				dialog.cancel();
        }});
    if(msgview.equalsIgnoreCase("YES"))
    {
    dialog.show();
    }
    
	}
	
	  public void buildAlertMessageNoEscort() {
//	        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	        
//	        builder.setTitle("No Escort");
//	        builder.setMessage("Escort Not Boarded The Cab!");
//	        builder.setCancelable(false);
//	        
//	        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
//	        		{
//	                	public void onClick(final DialogInterface dialog, final int id) 
//	                	{
//	                		dialog.cancel();
//	                	}
//	        		});  
//	        if(escval==0)
//			{
//	        		    builder.create().show();
//	        		    escval=1;
//			}
		  final Dialog dialog = new Dialog(LiveTrackingActivity.this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         dialog.setContentView(R.layout.escortstatus);
	        
	         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	         lp.copyFrom(dialog.getWindow().getAttributes());
	         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	         lp.gravity = Gravity.CENTER_VERTICAL;

	         dialog.getWindow().setAttributes(lp);
	         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	         Button Ok = (Button) dialog.findViewById(R.id.escortok);
	        Ok.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
					
					dialog.cancel();
	        }});
	        if(escval==0)
				{
		        		    dialog.show();
		        		    escval=1;
				}
	        		}
	  
	  public void buildAlertMessageTRIPSTATUS() {
//	        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	        
//	        builder.setTitle("TRIP STATUS");
//	        builder.setMessage("Your Trip Has Not Started");
//	        builder.setCancelable(false);
//	        
//	        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
//	        		{
//	                	public void onClick(final DialogInterface dialog, final int id) 
//	                	{
//	                		dialog.cancel();
//	                	}
//	        		}); 
//	        if(tripstatusval==0)
//	        {
//    		    builder.create().show();
//    		    tripstatusval=1;
	//        }
		  final Dialog dialog = new Dialog(LiveTrackingActivity.this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         dialog.setContentView(R.layout.tripstatuspopup);
	        
	         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	         lp.copyFrom(dialog.getWindow().getAttributes());
	         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	         lp.gravity = Gravity.CENTER_VERTICAL;

	         dialog.getWindow().setAttributes(lp);
	         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	         Button Ok = (Button) dialog.findViewById(R.id.tripok);
	        Ok.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
					
					dialog.cancel();
	        }});
	        if(tripstatusval==0)
		        {
	    		    dialog.show();
	    		    tripstatusval=1;
		        }
	         
	  }
	        
	        
	   private double distance(double lat1, double lng1, double lat2, double lng2) {
		   
		    double earthRadius = 6371; // in miles, change to 6371 for kilometers

		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(lng2-lng1);

		    double sindLat = Math.sin(dLat / 2);
		    double sindLng = Math.sin(dLng / 2);

		    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
		        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		    double dist = earthRadius * c;

		    return dist;
		}
	   public void alertdist(double dist)
		{
		   float indist=(float) dist;
		   BigDecimal bd = new BigDecimal(Float.toString(indist));
		    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		   
		   
		 
		   
//	  final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	    
//	    builder.setTitle("CAB ALERT");
//	    builder.setMessage("Cab Is "+bd+" KM From Your Location");
//	    builder.setCancelable(false);
//	    
//	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
//	    		{
//	            	public void onClick(final DialogInterface dialog, final int id) 
//	            	{
//	            		dialog.cancel();
//	            	}
//	    		});
		    final Dialog dialog = new Dialog(LiveTrackingActivity.this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         dialog.setContentView(R.layout.cabalertpopup);
	         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	         lp.copyFrom(dialog.getWindow().getAttributes());
	         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	         lp.gravity = Gravity.CENTER_VERTICAL;

	         dialog.getWindow().setAttributes(lp);
	         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	         Button Ok = (Button) dialog.findViewById(R.id.cabOK);

	         TextView distxt= (TextView) dialog.findViewById(R.id.disttitle1);
	         distxt.setText("Cab is "+bd+" KM From Your Location");
	        Ok.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
					
					dialog.cancel();
	        }});
	    if(alertval==0)
		{
	   dialog.show();
	    alertval=1;
		}
	    
		}
	   
	   private final Runnable m_Runnable = new Runnable()
	    {
	        public void run()

	        { 
	        	try{
	        	 if (active) {
	        		 LiveTrackingActivity.this.mHandler.postDelayed(m_Runnable,10000); 
	        		 makeMap();
		        	 
				}
	        	 else
	        	 {

	        	 finish();
	        	 active=true;
	        	 }
	        	}catch(Exception e){e.printStackTrace();}
	                   
	        }

	    };
	//private ServerCommunication sobj;
	    
	    public void makeMap()
	    {
	    	Bundle extras = getIntent().getExtras();
	    	String tripid="";
	    	if (extras != null) {
	    	tripid=extras.getString("TRIP_ID");
	    	}
	    	try{
	    	JSONObject jobj=new JSONObject();
	    	 jobj.put("ACTION","VEHICLE_LOCATION");          
	         jobj.put("TRIP_ID", tripid);
	    	 if(mapInitialize)
			 {
				initilizeMap();
				mapInitialize=false;
			 }

				googleMap.clear();
				// Changing map type
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
						googleMap.setMyLocationEnabled(true);
					}
				} else {
					googleMap.setMyLocationEnabled(true);
				}
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.getUiSettings().setRotateGesturesEnabled(true);
				googleMap.getUiSettings().setZoomGesturesEnabled(true);


				JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("result").equalsIgnoreCase("TRUE")) {
								if(response.getString("LAT")!=null&&!response.getString("LAT").equalsIgnoreCase("")&&response.getString("LONG")!=null&&!response.getString("LONG").equalsIgnoreCase(""))
								{
									MyLocation myLocation;
									myLocation = new MyLocation(LiveTrackingActivity.this);
									longitude= Double.parseDouble(response.getString("LONG"));
									latitude=Double.parseDouble(response.getString("LAT"));
									String line="";
									List<String> boardids = new ArrayList<String>();
									String escortalert="NO";
									String cbalrt="YES";
									for(int i=1;i<=Integer.parseInt(response.getString("COUNT"));i++)
									{
										boardids.add(response.getString("BOARD_ID"+i));
									}
									Bundle extras = getIntent().getExtras();
									if(extras.getString("SECURITY").equalsIgnoreCase("YES"))
									{
										if(response.getString("ESCORT_STATUS").equalsIgnoreCase("SHOW"))
										{
											line="\n\tEscort\t\t\u2714"+"\n\n";
										}
										else
										{
											line="\n\tEscort"+"\n\n";
											escortalert="YES";
										}

									}
									if(!extras.getString("EMPS_COUNT").equalsIgnoreCase("")&&extras.getString("EMPS_COUNT")!=null)
									{
										for(int i=1;i<=Integer.parseInt(extras.getString("EMPS_COUNT"));i++)
										{
											String tick="";
											for(String id:boardids)
											{
												if(id.equalsIgnoreCase(extras.getString("EMP_ID"+i)))
													tick="\u2714";
												if(id.equalsIgnoreCase(extras.getString("EMP_ID")))
												{
													cbalrt="NO";
													panicstatus="yes";
												}
												else
													panicstatus="no";

											}
											line+="\t"+i+")"+extras.getString("EMP_NAME"+i)+" "+extras.getString("GENDER"+i)+" "+tick+" "+" "+"\n";

										}
										emps=line;
									}
									ImageView panicbtn = (ImageView) findViewById(R.id.panicalaram);
									if(panicstatus.equalsIgnoreCase("yes"))
									{
										//		panicbtn.setVisibility(View.VISIBLE);
										panicbtn.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												panicalert();


											}
										});
									}
									else
									{
										//		panicbtn.setVisibility(View.INVISIBLE);
									}
									double mylatitude = myLocation.getLatitude();
									double mylongitude = myLocation.getLongitude();
									double dist=distance(latitude,longitude,mylatitude,mylongitude);
									if(dist<2&&cbalrt.equalsIgnoreCase("YES"))
									{
										alertdist(dist);
									}
//									MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude));
//									googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),emps));
//									marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.only_guys));
//									googleMap.addMarker(marker);
//									CameraPosition cameraPosition = new CameraPosition.Builder()
//											.target(new LatLng(latitude,
//													longitude)).zoom(15).build();

//									googleMap.animateCamera(CameraUpdateFactory
//											.newCameraPosition(cameraPosition));
									if(escortalert.equalsIgnoreCase("Yes"))
									{
										buildAlertMessageNoEscort();
									}

								}else
								{
									String tripstatusalert="YES";
									if(tripstatusalert.equalsIgnoreCase("Yes"))
									{
										buildAlertMessageTRIPSTATUS();
									}
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
						,new Response.ErrorListener()

				{
					@Override
					public void onErrorResponse (VolleyError error){
						Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();


					}
				});

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(req);
//			sobj=new ServerCommunication(jobj);
//			sobj.setDataDownloadListen(new DataDownloadListener()
//			{
//				public void dataSuccess(String result)
//				{
//					try {
//						if(result!=null&&!result.equalsIgnoreCase(""))
//						{
//						JSONObject robj;
//						robj = new JSONObject(result);
//					if(robj.getString("LAT")!=null&&!robj.getString("LAT").equalsIgnoreCase("")&&robj.getString("LONG")!=null&&!robj.getString("LONG").equalsIgnoreCase(""))
//					{
//						MyLocation myLocation;
//						myLocation = new MyLocation(LiveTrackingActivity.this);
//						longitude= Double.parseDouble(robj.getString("LONG"));
//						latitude=Double.parseDouble(robj.getString("LAT"));
//						String line="";
//						List<String> boardids = new ArrayList<String>();
//						String escortalert="NO";
//						String cbalrt="YES";
//						for(int i=1;i<=Integer.parseInt(robj.getString("COUNT"));i++)
//						{
//							boardids.add(robj.getString("BOARD_ID"+i));
//						}
//						Bundle extras = getIntent().getExtras();
//						if(extras.getString("SECURITY").equalsIgnoreCase("YES"))
//						{
//							if(robj.getString("ESCORT_STATUS").equalsIgnoreCase("SHOW"))
//							{
//								line="\n\tEscort\t\t\u2714"+"\n\n";
//							}
//						else
//						{
//							line="\n\tEscort"+"\n\n";
//							escortalert="YES";
//						}
//
//						}
//						if(!extras.getString("EMPS_COUNT").equalsIgnoreCase("")&&extras.getString("EMPS_COUNT")!=null)
//						{
//						for(int i=1;i<=Integer.parseInt(extras.getString("EMPS_COUNT"));i++)
//						{
//							String tick="";
//							for(String id:boardids)
//							{
//								if(id.equalsIgnoreCase(extras.getString("EMP_ID"+i)))
//								tick="\u2714";
//								if(id.equalsIgnoreCase(extras.getString("EMP_ID")))
//								{
//									cbalrt="NO";
//									panicstatus="yes";
//								}
//								else
//									panicstatus="no";
//
//							}
//							line+="\t"+i+")"+extras.getString("EMP_NAME"+i)+" "+extras.getString("GENDER"+i)+" "+tick+" "+" "+"\n";
//
//						}
//						emps=line;
//						}
//						ImageView panicbtn = (ImageView) findViewById(R.id.panicalaram);
//						if(panicstatus.equalsIgnoreCase("yes"))
//						{
//					//		panicbtn.setVisibility(View.VISIBLE);
//						 panicbtn.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//
//									panicalert();
//
//
//								}
//							});
//						}
//						else
//						{
//					//		panicbtn.setVisibility(View.INVISIBLE);
//						}
//						double mylatitude = myLocation.getLatitude();
//						double mylongitude = myLocation.getLongitude();
//						double dist=distance(latitude,longitude,mylatitude,mylongitude);
//								if(dist<2&&cbalrt.equalsIgnoreCase("YES"))
//								{
//									alertdist(dist);
//								}
//								MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude));
//								googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),emps));
//								marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.only_guys));
//								googleMap.addMarker(marker);
//									CameraPosition cameraPosition = new CameraPosition.Builder()
//											.target(new LatLng(latitude,
//													longitude)).zoom(15).build();
//
//									googleMap.animateCamera(CameraUpdateFactory
//											.newCameraPosition(cameraPosition));
//									if(escortalert.equalsIgnoreCase("Yes"))
//									{
//										buildAlertMessageNoEscort();
//									}
//
//					}else
//					{
//						String tripstatusalert="YES";
//						if(tripstatusalert.equalsIgnoreCase("Yes"))
//						{
//							buildAlertMessageTRIPSTATUS();
//						}
//					}
//					}
//						else
//						{
//							Toast.makeText(getApplicationContext(), "Oops Communication Failure", Toast.LENGTH_SHORT).show();
//
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				public void datafail()
//				{
//
//					Toast.makeText(getApplicationContext(), "Oops Communication Failure", Toast.LENGTH_SHORT).show();
//				}
//			});
//			sobj.execute();
	    	}catch(Exception e){e.printStackTrace();}
	    }
	   
	    public void onBackPressed()
		{

			super.onBackPressed();
			Intent intent = new Intent(LiveTrackingActivity.this, MapClass.class);
			startActivity(intent);
			//System.exit(1);
	  //  	Toast.makeText(getApplicationContext(), "in start", Toast.LENGTH_LONG).show();
	    	//finish();
		}
	  
	   private void panicalert(){
	    final Dialog dialog = new Dialog(LiveTrackingActivity.this);
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.panicpopup);
       
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       
        ImageView yes = (ImageView) dialog.findViewById(R.id.yespanic);
        Button no = (Button) dialog.findViewById(R.id.nopanic);
        yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				panicactivated();
				dialog.cancel();
				try{




					JSONObject jobj=new JSONObject();
					jobj.put("ACTION", "PANIC_ACTIVATED");

					MyLocation myLocation;
			    	myLocation = new MyLocation(LiveTrackingActivity.this);
					double latitude=myLocation.getLatitude();
					double longitude=myLocation.getLongitude();
					Bundle extras = getIntent().getExtras();
			    	String tripid="",empid="";
			    	if (extras != null) {
			    	tripid=extras.getString("TRIP_ID");
			    	empid=extras.getString("EMP_ID");
			    	}
			    	
					jobj.put("EMP_ID",empid);
					jobj.put("IMEI_NUMBER",CommenSettings.macAddress);
					jobj.put("TRIP_ID",tripid);
					jobj.put("LAT",latitude+"");
					jobj.put("LONG", longitude+"");
					JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getString("result").equalsIgnoreCase("TRUE")) {
									if (response.getString("PANIC_STATUS").equalsIgnoreCase("ACTIVATED")) {
										Toast.makeText(getApplicationContext(), "Panic Activated!", Toast.LENGTH_LONG).show();
										ImageView panicactiv = (ImageView) findViewById(R.id.panicalaram);
										panicactiv.setImageResource(R.drawable.panic_activated);


									}
								} else if (response.getString("result").equalsIgnoreCase("false"))

								{
									Toast.makeText(getApplicationContext(), "Opertaion Failed!", Toast.LENGTH_LONG).show();
								}else{
								Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();


							}

							} catch (Exception e) {

								e.printStackTrace();
							}
						}
					}
							,new Response.ErrorListener()

					{
						@Override
						public void onErrorResponse (VolleyError error){
							Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();


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
//
//							if(robj!=null && robj.getString("result").equalsIgnoreCase("true"))
//							{
//								if(robj.getString("PANIC_STATUS").equalsIgnoreCase("ACTIVATED"))
//								{
//									Toast.makeText(getApplicationContext(), "Panic Activated!", Toast.LENGTH_LONG).show();
//									ImageView panicactiv=(ImageView) findViewById(R.id.panicalaram);
//									panicactiv.setImageResource(R.drawable.panic_activated);
//
//
//								}
//							}
//							else if(robj!=null && robj.getString("result").equalsIgnoreCase("false"))
//
//							{
//								Toast.makeText(getApplicationContext(), "Opertaion Failed!", Toast.LENGTH_LONG).show();
//							}
//
//							}
//							//	else
//
//									//Toast.makeText(getApplicationContext(), "Oops Error In Communication", Toast.LENGTH_SHORT).show();
//
//
//
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//						public void datafail()
//							{
//
//							Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
//						}
//
//					});
//					sobj.execute();
				}catch(Exception e){e.printStackTrace();}
					

			
			}	
				
					
			});


				
					
        no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();

   		
				
			}
		});
        dialog.show();
   }
	   
	   private void panicactivated(){
		    final Dialog dialog = new Dialog(LiveTrackingActivity.this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.panicactivatedpopup);
	       
	        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	        lp.copyFrom(dialog.getWindow().getAttributes());
	        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	        lp.gravity = Gravity.CENTER;

	        dialog.getWindow().setAttributes(lp);
	        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	       
	        Button yes = (Button) dialog.findViewById(R.id.hidepanic);
	        yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
	       		dialog.cancel();
					
				}
			});
	      
	        dialog.show();
	   

	 
	   }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}




//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		
//	}
}



