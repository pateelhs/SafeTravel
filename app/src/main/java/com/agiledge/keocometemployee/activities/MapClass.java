package com.agiledge.keocometemployee.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.GCM.QuickstartPreferences;
import com.agiledge.keocometemployee.GCM.RegistrationIntentService;
import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.navdrawermenu.AboutActivity;
import com.agiledge.keocometemployee.navdrawermenu.EmergencyContactActivity;
import com.agiledge.keocometemployee.navdrawermenu.FeedBackActivity;
import com.agiledge.keocometemployee.navdrawermenu.HelpActivity;
import com.agiledge.keocometemployee.navdrawermenu.ManageBookingActivity;
import com.agiledge.keocometemployee.navdrawermenu.TripDetailsActivity;
import com.agiledge.keocometemployee.utilities.MyLocation;
import com.agiledge.keocometemployee.utilities.PopupAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MapClass extends Activity implements OnItemClickListener {
	//GCM
	private NotificationManager myNotificationManager;
	private int notificationIdOne = 111;
	private int notificationIdTwo = 112;
	private int numMessagesOne = 0;
	private int numMessagesTwo = 0;
	private static final int MY_NOTIFICATION_ID=1;
	NotificationManager notificationManager;
	Notification cabNotification;
	private static final int REQUEST_CODE = 0;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	//private static final String TAG = "FSM_GCM";
	Context context;
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private ProgressBar mRegistrationProgressBar;
	private TextView mInformationTextView;
	private boolean isReceiverRegistered;
	private static final String TAG = "Safe_GCM";
	//private ProgressDialog mdialog;
	private static int initial = 0;
	// Google Map
	static boolean active = true;
	private GoogleMap googleMap;
	static int alertval = 0, escval = 0, tripstatusval = 0;
	TelephonyManager tel;
	String IMEI;
	public String macAddress;
	String TRIPID;
	double longitude, latitude;
	Handler mHandler;
	static boolean mapInitialize = true;
	static String emps = "", msgview = "NO";
	String panicstatus = "no";
	String showempmenu = "yes";
	private AlertDialog.Builder builder1;

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerListener;
	private ListView listView;
	private String[] menuLists;
	private ProgressDialog mdialog;
	private Object userType;
	String displayname = "", trip_time = "", trip_id = "", trip_code = "",
			trip_log = "", trip_date = "", driver_name = "",
			driver_contact = "", reg_no = "", emp_email = "", emp_gender = ""
			// cancel
			, from_date = "", to_date = "", log_in = "", log_out = "";

	int incount = 0, outcount = 0, adhocCount = 0;
	private ArrayList<String> aldates;
	private ArrayList<String> al_login;
	private ArrayList<String> al_logout;
	private ArrayList<String> outlogs;
	private ArrayList<String> inlogs;
	private ArrayList<String> adhocTypes;
	private Bundle extras;
	int bckprsdcount=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.loading);

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			macAddress = wimanager.getConnectionInfo().getMacAddress();
			// Todo change the layout background
			//for gcm
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
			//for gcm end

			aldates = new ArrayList<String>();
			al_login = new ArrayList<String>();
			al_logout = new ArrayList<String>();
			inlogs = new ArrayList<String>();
			outlogs = new ArrayList<String>();
			adhocTypes = new ArrayList<String>();

			userType = null;

			initializeMapStats();
		} catch (Exception e) {
			e.printStackTrace();
			AppController.getInstance().trackException(e);
		}

	}

	private void initializeContentView(Bundle mextras) {
		extras = mextras;
		// TODO Auto-generated method stub
		if (extras != null) {

			if (extras.getString("user_type").equalsIgnoreCase("admin")) {

				setContentView(R.layout.mapadmin);
			} else {
				setContentView(R.layout.map);
			}

			displayname = extras.getString("EMP_NAME");
			msgview = extras.getString("MSGVIEW");
			emp_gender = extras.getString("EMP_GENDER");
			emp_email = extras.getString("EMP_EMAIL");

			if (extras.getString("user_type").equalsIgnoreCase("admin")) {
				msgview = "NO";
				alertfirsttime(displayname);
			}

			if (extras.getString("user_type").equalsIgnoreCase("admin")) {
				showempmenu = "no";
			}
		}

		//	tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//	IMEI = tel.getDeviceId().toString();

		TextView ttime = (TextView) findViewById(R.id.t2);
		TextView tdate = (TextView) findViewById(R.id.t4);
		TextView profilename = (TextView) findViewById(R.id.profilename);
		TextView profileemail = (TextView) findViewById(R.id.profileemail);
		ImageView profileimage = (ImageView) findViewById(R.id.profileimage);
		System.out.println("in 2");

		menuLists = getResources().getStringArray(R.array.menu_list);
		// programmatic way of getting menu list , instead of adding array
		// entries in xml file..
		listView = (ListView) findViewById(R.id.drawerList);
		ObjectDrawerItem[] drawerItem;
		if (showempmenu.equalsIgnoreCase("yes")) {
			drawerItem = new ObjectDrawerItem[7];
			drawerItem[0] = new ObjectDrawerItem(R.drawable.tripdetails_icon,
					"TripDetails");
			drawerItem[1] = new ObjectDrawerItem(R.drawable.about_icon, "About");
			drawerItem[2] = new ObjectDrawerItem(R.drawable.help_icon, "Help");
			drawerItem[3] = new ObjectDrawerItem(
					R.drawable.emergencycontact_icon, "Emergency Contact");
			drawerItem[4] = new ObjectDrawerItem(R.drawable.trip_history,
					"Trip History");
			drawerItem[5] = new ObjectDrawerItem(R.drawable.managr_booking,
					"Manage Booking");
			drawerItem[6] = new ObjectDrawerItem(R.drawable.feedback_20,
					"FeedBack");

			//	drawerItem[7] = new ObjectDrawerItem(R.drawable.managr_booking,"MY CAB");
		} else {
			drawerItem = new ObjectDrawerItem[8];

			drawerItem[0] = new ObjectDrawerItem(R.drawable.tripdetails_icon,
					"TripDetails");
			drawerItem[1] = new ObjectDrawerItem(R.drawable.about_icon, "About");
			drawerItem[2] = new ObjectDrawerItem(R.drawable.help_icon, "Help");
			drawerItem[3] = new ObjectDrawerItem(
					R.drawable.emergencycontact_icon, "Emergency Contact");
			drawerItem[4] = new ObjectDrawerItem(R.drawable.trip_history,
					"Trip History");
			drawerItem[5] = new ObjectDrawerItem(R.drawable.managr_booking,
					"Manage Booking");
			drawerItem[6] = new ObjectDrawerItem(R.drawable.feedback_20,
					"FeedBack");
			drawerItem[7] = new ObjectDrawerItem(R.drawable.singlelady_withesc,
					"MY CAB");

			ImageView panicbtn = (ImageView) findViewById(R.id.panicalaram);
			panicbtn.setVisibility(View.INVISIBLE);
		}

		// Pass the folderData to our ListView adapter
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listview_item_row, drawerItem);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
		System.out.println("in 3");
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		// creating a drawerLayout..

		/*
		 * // int height = getResources().getDisplayMetrics().heightPixels/2;
		 * DrawerLayout.LayoutParams params =
		 * (android.support.v4.widget.DrawerLayout.LayoutParams)
		 * drawerLayout.getLayoutParams(); params.height = 200;
		 * drawerLayout.setLayoutParams(params);
		 */

		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.nav_menu, R.string.drawer_open) { // inner class
			// methods to
			// tell the user
			// about menu
			// open and
			// close..
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

			}
		};
		drawerLayout.setDrawerListener(drawerListener);


		if (extras.getString("user_type").equalsIgnoreCase("admin")) {

			Button legpopup = (Button) findViewById(R.id.legend);
			legpopup.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					legendAlert();
				}
			});
		}
		// ----End of sliding menu code---
		ImageView imageview = (ImageView) findViewById(R.id.navigationicon);
		imageview.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

					drawerLayout.closeDrawers();
				} else {
					drawerLayout.openDrawer(Gravity.START);

				}
			}
		});

		if (emp_gender.equalsIgnoreCase("F")) {
			profileimage.setImageResource(R.drawable.avatar_female);
		} else {
			profileimage.setImageResource(R.drawable.avatar_male);
		}
		profilename.setText(displayname);
		profileemail.setText(emp_email);

		if (!extras.getString("user_type").equalsIgnoreCase("admin")) {

			ttime.setText(trip_time);
			tdate.setText(trip_date);

		} 
	/*	// cancel
		from_date = extras.getString("FROM_DATE");
		to_date = extras.getString("TO_DATE");
		log_in = extras.getString("LOG_IN");
		log_out = extras.getString("LOG_OUT");
		int times = 0;
		if (extras.getString("ALTIMES") != null) {
			times = Integer.parseInt(extras.getString("ALTIMES"));
		}
		for (int i = 1; i <= times; i++) {
			aldates.add(extras.getString("ALTERDATE" + i));
			al_login.add(extras.getString("ALTERDATE" + i));
			al_logout.add(extras.getString("AL_LOG_OUT" + i));
		}
		incount = Integer.parseInt(extras.getString("IN_LOGS_COUNT") + "");
		outcount = Integer.parseInt(extras.getString("OUT_LOGS_COUNT") + "");
		for (int i = 1; i <= incount; i++) {
			inlogs.add(extras.getString("IN_LOGS" + i));
		}
		for (int i = 1; i <= outcount; i++) {
			outlogs.add(extras.getString("OUT_LOGS" + i));
		}
		adhocCount = Integer.parseInt(extras.getString("ADHOCTYPECOUNT") + "");
		for (int i = 1; i <= adhocCount; i++) {
			adhocTypes.add(extras.getString("ADHOCTYPE" + i));
		}
		// cancel
*/
		if (Integer.parseInt(extras.getString("EMPS_COUNT")) > 0) {
			trip_id = extras.getString("TRIP_ID");
			TRIPID = extras.getString("TRIP_ID");
			trip_code = extras.getString("TRIP_CODE");
			trip_date = extras.getString("TRIP_DATE");
			// trip_log=extras.getString("TRIP_LOG");
			reg_no = extras.getString("REG_NO");
			driver_name = extras.getString("DRIVER_NAME");
			driver_contact = extras.getString("DRIVER_CONTACT");
			trip_time = extras.getString("TRIP_TIME") + " "
					+ extras.getString("TRIP_LOG");

		}
		if (!extras.getString("user_type").equalsIgnoreCase("admin")) {

			ttime.setText(trip_time);
			tdate.setText(trip_date);
		}

		this.mHandler = new Handler();
		m_Runnable.run();

		initilizeMap();
		makeMap();
	}
//	public void notifycab(){
//
//			Context context = getApplicationContext();
//			Intent myIntent = new Intent(Intent.ACTION_VIEW);
//			PendingIntent pendingIntent = PendingIntent.getActivity(
//					MapClass.this,
//					0,
//					myIntent,
//					Intent.FLAG_ACTIVITY_NEW_TASK);
//
//			cabNotification = new Notification.Builder(context)
//					.setContentTitle("Exercise of Notification!")
//					.setContentText("http://android-er.blogspot.com/")
//					.setTicker("Notification!")
//					.setWhen(System.currentTimeMillis())
//					.setContentIntent(pendingIntent)
//					.setDefaults(Notification.DEFAULT_SOUND)
//					.setAutoCancel(true)
//					.setSmallIcon(R.drawable.ic_launcher)
//					.build();
//
//			notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//			notificationManager.notify(MY_NOTIFICATION_ID, cabNotification);
//
//		}
	protected void displayNotificationOne() {

		// Invoking the default notification service
		NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

		mBuilder.setContentTitle("CAB PROXIMITY ALERT");
		mBuilder.setContentText("Cab Alert");
		mBuilder.setTicker("Your cab Arriving");
		mBuilder.setSmallIcon(R.drawable.location_icon);

		// Increase notification number every time a new notification arrives
		mBuilder.setNumber(++numMessagesOne);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MapClass.class);
		resultIntent.putExtra("notificationId", notificationIdOne);

		//This ensures that navigating backward from the Activity leads out of the app to Home page
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent
		stackBuilder.addParentStack(MapClass.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_ONE_SHOT //can only be used once
				);
		// start the activity when the user clicks the notification text
		mBuilder.setContentIntent(resultPendingIntent);

		myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// pass the Notification object to the system
		myNotificationManager.notify(notificationIdOne, mBuilder.build());
	}


	private void initializeMapStats() {
		// TODO Auto-generated method stub
		mdialog = new ProgressDialog(this);
		mdialog.setMessage("Please Wait...");
		mdialog.setCancelable(false);
		mdialog.setIndeterminate(false);
		mdialog.show();
		try {
			JSONObject jobj = new JSONObject();

			jobj.put("ACTION", "IMEI_CHECK");
			jobj.put("IMEI_NUMBER", macAddress);
			JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						if (response.getString("result").equalsIgnoreCase("TRUE")) {
							if (mdialog != null && mdialog.isShowing()) {
								mdialog.dismiss();
							}
							Bundle in = new Bundle();
							in.putString("user_type",
									response.getString("user_type"));
							in.putString("TRIP_ID",
									response.getString("TRIP_ID"));
							in.putString("TRIP_CODE",
									response.getString("TRIP_CODE"));
							in.putString("TRIP_DATE",
									response.getString("TRIP_DATE"));
							in.putString("TRIP_LOG",
									response.getString("TRIP_LOG"));
							in.putString("REG_NO", response.getString("REG_NO"));
							in.putString("TRIP_TIME",
									response.getString("TRIP_TIME"));
							in.putString("DRIVER_NAME",
									response.getString("DRIVER_NAME"));
							in.putString("DRIVER_CONTACT",
									response.getString("DRIVER_CONTACT"));
							in.putString("EMPS_COUNT",
									response.getString("EMPS_COUNT"));
							in.putString("SECURITY",
									response.getString("SECURITY"));
							in.putString("EMP_ID", response.getString("EMP_ID"));

							in.putString("EMP_NAME",
									response.getString("EMP_NAME"));
							in.putString("EMP_PERSONNELNO",
									response.getString("EMP_PERSONNELNO"));
							in.putString("EMP_GENDER",
									response.getString("EMP_GENDER"));
							in.putString("EMP_EMAIL",
									response.getString("EMP_EMAIL"));
							in.putString("EMP_SITE",
									response.getString("EMP_SITE"));
							in.putString("MSGVIEW", "NO");
							if (response.getString("SECURITY")
									.equalsIgnoreCase("YES")) {
								in.putString("ESCORT_NAME",
										response.getString("ESCORT_NAME"));
								in.putString("ESCORT_CONTACT",
										response.getString("ESCORT_CONTACT"));
							}

							if (response.getString("EMPS_COUNT") != null
									&& !response.getString("EMPS_COUNT")
									.equalsIgnoreCase("")) {
								for (int i = 1; i <= Integer.parseInt(response
										.getString("EMPS_COUNT")); i++) {
									in.putString(
											"PERSONNEL_NO" + i,
											response.getString("PERSONNEL_NO"
													+ i));
									in.putString("EMP_NAME" + i,
											response.getString("EMP_NAME" + i));
									in.putString("GENDER" + i,
											response.getString("GENDER" + i));
									in.putString("EMP_ID" + i,
											response.getString("EMP_ID" + i));
									in.putString(
											"EMP_CONTACT" + i,
											response.getString("EMP_CONTACT"
													+ i));

								}

								// cancel
								// Toast.makeText(getApplicationContext(),"SAN"+
								// robj.getString("FROM_DATE")+robj.getString("TO_DATE")+robj.getString("LOG_IN")+robj.getString("LOG_OUT")+robj.getString("ALTERDATE"),
								// Toast.LENGTH_LONG).show();

							}
							initializeContentView(in);


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
					, new Response.ErrorListener()

			{
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
					if (mdialog != null && mdialog.isShowing()) {
						mdialog.dismiss();
					}

				}
			});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(req);
//			ServerCommunication sobj = new ServerCommunication(jobj);
//			sobj.setDataDownloadListen(new DataDownloadListener() {
//				public void dataSuccess(String result) {
//					try {
//						if (result != null && !result.equalsIgnoreCase("")) {
//							JSONObject robj;
//							robj = new JSONObject(result);
//
//							if (robj != null
//									&& robj.getString("result")
//											.equalsIgnoreCase("true"))
//
//							{
//								if (mdialog != null && mdialog.isShowing()) {
//									mdialog.dismiss();
//								}
//								// Toast.makeText(getApplicationContext(),"valli"+
//								// result, Toast.LENGTH_LONG).show();
//
//								Bundle in = new Bundle();
//								in.putString("user_type",
//										robj.getString("user_type"));
//								in.putString("TRIP_ID",
//										robj.getString("TRIP_ID"));
//								in.putString("TRIP_CODE",
//										robj.getString("TRIP_CODE"));
//								in.putString("TRIP_DATE",
//										robj.getString("TRIP_DATE"));
//								in.putString("TRIP_LOG",
//										robj.getString("TRIP_LOG"));
//								in.putString("REG_NO", robj.getString("REG_NO"));
//								in.putString("TRIP_TIME",
//										robj.getString("TRIP_TIME"));
//								in.putString("DRIVER_NAME",
//										robj.getString("DRIVER_NAME"));
//								in.putString("DRIVER_CONTACT",
//										robj.getString("DRIVER_CONTACT"));
//								in.putString("EMPS_COUNT",
//										robj.getString("EMPS_COUNT"));
//								in.putString("SECURITY",
//										robj.getString("SECURITY"));
//								in.putString("EMP_ID", robj.getString("EMP_ID"));
//
//								in.putString("EMP_NAME",
//										robj.getString("EMP_NAME"));
//								in.putString("EMP_PERSONNELNO",
//										robj.getString("EMP_PERSONNELNO"));
//								in.putString("EMP_GENDER",
//										robj.getString("EMP_GENDER"));
//								in.putString("EMP_EMAIL",
//										robj.getString("EMP_EMAIL"));
//								in.putString("EMP_SITE",
//										robj.getString("EMP_SITE"));
//								in.putString("MSGVIEW", "NO");
//
//								// // JSONARRAY
//								// in.putString("ALLATLONGS",
//								// robj.getJSONArray("LAT").length());
//								// for(int
//								// j=1;j<=robj.getJSONArray("LAT").length();j++){
//								// in.putString("LAT"+j,robj.getJSONArray("LAT").getString(j)
//								// );
//								// }
//								// for(int
//								// j=1;j<=robj.getJSONArray("LONG").length();j++){
//								// in.putString("LONG"+j,robj.getJSONArray("LONG").getString(j)
//								// );
//								// }
//								// for(int
//								// j=1;j<=robj.getJSONArray("REGNO").length();j++){
//								// in.putString("REGNO"+j,robj.getJSONArray("REGNO").getString(j)
//								// );
//								// }
//								// for(int
//								// j=1;j<=robj.getJSONArray("TRIPID").length();j++){
//								// in.putString("TRIPID"+j,robj.getJSONArray("TRIPID").getString(j)
//								// );
//								// }
//
//								// cancel
//	/*							in.putString("FROM_DATE",
//										robj.getString("FROM_DATE"));
//								in.putString("TO_DATE",
//										robj.getString("TO_DATE"));
//								in.putString("LOG_IN", robj.getString("LOG_IN"));
//								in.putString("LOG_OUT",
//										robj.getString("LOG_OUT"));
//
//								// JSONARRAY
//								in.putString("ALTIMES",
//										robj.getJSONArray("ALTERDATE").length()
//												+ "");
//								in.putString("OUT_LOGS_COUNT", robj
//										.getJSONArray("OUT_LOGS").length() + "");
//								in.putString("IN_LOGS_COUNT",
//										robj.getJSONArray("IN_LOGS").length()
//												+ "");
//								for (int s = 0; s < robj
//										.getJSONArray("IN_LOGS").length(); s++) {
//									in.putString("IN_LOGS" + (s + 1),
//											robj.getJSONArray("IN_LOGS")
//													.getString(s));
//								}
//
//								for (int t = 0; t < robj.getJSONArray(
//										"OUT_LOGS").length(); t++) {
//									in.putString("OUT_LOGS" + (t + 1), robj
//											.getJSONArray("OUT_LOGS")
//											.getString(t));
//								}
//								for (int j = 0; j < robj.getJSONArray(
//										"ALTERDATE").length(); j++) {
//									in.putString("ALTERDATE" + (j + 1), robj
//											.getJSONArray("ALTERDATE")
//											.getString(j));
//								}
//								for (int j = 0; j < robj.getJSONArray(
//										"AL_LOG_IN").length(); j++) {
//									in.putString("AL_LOG_IN" + (j + 1), robj
//											.getJSONArray("AL_LOG_IN")
//											.getString(j));
//								}
//								for (int j = 0; j < robj.getJSONArray(
//										"AL_LOG_OUT").length(); j++) {
//									in.putString("AL_LOG_OUT" + (j + 1), robj
//											.getJSONArray("AL_LOG_OUT")
//											.getString(j));
//								}
//								in.putString("ADHOCTYPECOUNT", robj
//										.getJSONArray("ADHOCTYPE").length()
//										+ "");
//								for (int i = 0; i < Integer.parseInt(robj
//										.getJSONArray("ADHOCTYPE").length()
//										+ ""); i++) {
//									in.putString("ADHOCTYPE" + (i + 1), robj
//											.getJSONArray("ADHOCTYPE")
//											.getString(i));
//
//								}
//								// cancel*/
//								if (robj.getString("SECURITY")
//										.equalsIgnoreCase("YES")) {
//									in.putString("ESCORT_NAME",
//											robj.getString("ESCORT_NAME"));
//									in.putString("ESCORT_CONTACT",
//											robj.getString("ESCORT_CONTACT"));
//								}
//
//								if (robj.getString("EMPS_COUNT") != null
//										&& !robj.getString("EMPS_COUNT")
//												.equalsIgnoreCase("")) {
//									for (int i = 1; i <= Integer.parseInt(robj
//											.getString("EMPS_COUNT")); i++) {
//										in.putString(
//												"PERSONNEL_NO" + i,
//												robj.getString("PERSONNEL_NO"
//														+ i));
//										in.putString("EMP_NAME" + i,
//												robj.getString("EMP_NAME" + i));
//										in.putString("GENDER" + i,
//												robj.getString("GENDER" + i));
//										in.putString("EMP_ID" + i,
//												robj.getString("EMP_ID" + i));
//										in.putString(
//												"EMP_CONTACT" + i,
//												robj.getString("EMP_CONTACT"
//														+ i));
//
//									}
//
//									// cancel
//									// Toast.makeText(getApplicationContext(),"SAN"+
//									// robj.getString("FROM_DATE")+robj.getString("TO_DATE")+robj.getString("LOG_IN")+robj.getString("LOG_OUT")+robj.getString("ALTERDATE"),
//									// Toast.LENGTH_LONG).show();
//
//								}
//								initializeContentView(in);
//							} else if (robj != null
//									&& robj.getString("result")
//											.equalsIgnoreCase("false")) {
//								if (mdialog != null && mdialog.isShowing()) {
//									mdialog.dismiss();
//								}
//								Intent in = new Intent(getApplicationContext(),
//										Onetimeregister.class);
//								startActivity(in);
//								finish();
//							}
//
//						} else {
//							if (mdialog != null && mdialog.isShowing()) {
//								mdialog.dismiss();
//							}
//							Toast.makeText(getApplicationContext(),
//									" No Internet", Toast.LENGTH_SHORT).show();
//							// Toast.makeText(getApplicationContext(),
//							// "Vicky", Toast.LENGTH_LONG).show();
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						AppController.getInstance().trackException(e);
//					}
//				}
//
//				public void datafail() {
//					if (mdialog != null && mdialog.isShowing()) {
//						mdialog.dismiss();
//					}
//					Toast.makeText(getApplicationContext(),
//							"oops communication error", Toast.LENGTH_SHORT)
//							.show();
//				}
//			});
//			sobj.execute();

		} catch (Exception e) {
			e.printStackTrace();
			AppController.getInstance().trackException(e);
		}
	}

	// --- code for providing functionality to the Sliding Menu..
	// method synchronizes the drawer menu state with the image(menu_icon)..
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (drawerListener != null)
			drawerListener.syncState();
	}


	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		isReceiverRegistered = false;
		super.onPause();
	}

	// method handles item click in the menu..

	/***********
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * if(drawerListener.onOptionsItemSelected(item)){ return true; }
	 * return super.onOptionsItemSelected(item);
	 * <p>
	 * }
	 *******/

	// Handling configuration change..if any new configuration is made(i.e, if
	// screen size changes), it changes appropriately..
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig); // we are passing
		// drawerlistener
		// object to change
		// configuration
		// appropriately..
	}

	// private class DrawerItemClickListener implements
	// ListView.OnItemClickListener {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// selectItem(position);
	// }
	//
	// }

	// method displays the menu item selected..

	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// Toast.makeText(this, menuLists[position]+
		// " was selected ",Toast.LENGTH_LONG).show();
		selectItem(position);

		/*
		 * if(menuLists[position].equals("Home")){ startActivity(new
		 * Intent(this, MapClass.class)); startActivity(getIntent());
		 * this.mHandler = new Handler(); m_Runnable.run(); }
		 */
		if (menuLists[position].equals("Help")) {
			// startActivity(new Intent(this, HelpActivity.class));
			Intent in = new Intent(getApplicationContext(), HelpActivity.class);
			startActivity(in);

		}

		if (menuLists[position].equals("TripDetails")) {

			Intent in = new Intent(getApplicationContext(),
					TripDetailsActivity.class);
			//Bundle extras = getIntent().getExtras();
			if (extras != null) {
				in.putExtra("TRIP_CODE", extras.getString("TRIP_CODE"));
				in.putExtra("REG_NO", extras.getString("REG_NO"));
				in.putExtra("DRIVER_NAME", extras.getString("DRIVER_NAME"));
				in.putExtra("DRIVER_CONTACT",
						extras.getString("DRIVER_CONTACT"));
				in.putExtra("SECURITY", extras.getString("SECURITY"));
				if (extras.getString("SECURITY").equalsIgnoreCase("YES")) {
					in.putExtra("ESCORT_NAME", extras.getString("ESCORT_NAME"));
					in.putExtra("ESCORT_CONTACT",
							extras.getString("ESCORT_CONTACT"));
				}
			}

			startActivity(in);

		}
		if (menuLists[position].equals("About")) {
			Intent in = new Intent(getApplicationContext(), AboutActivity.class);
			/*
			 * //Bundle extras = getIntent().getExtras(); if(extras!=null) {
			 * in.putExtra("TRIP_ID", extras.getString("TRIP_ID"));
			 * in.putExtra("TRIP_CODE", extras.getString("TRIP_CODE"));
			 * in.putExtra("TRIP_DATE", extras.getString("TRIP_DATE"));
			 * in.putExtra("TRIP_LOG", extras.getString("TRIP_LOG"));
			 * in.putExtra("REG_NO", extras.getString("REG_NO"));
			 * in.putExtra("TRIP_TIME", extras.getString("TRIP_TIME"));
			 * in.putExtra("DRIVER_NAME", extras.getString("DRIVER_NAME"));
			 * in.putExtra("DRIVER_CONTACT",
			 * extras.getString("DRIVER_CONTACT")); in.putExtra("EMPS_COUNT",
			 * extras.getString("EMPS_COUNT")); in.putExtra("SECURITY",
			 * extras.getString("SECURITY")); in.putExtra("EMP_ID",
			 * extras.getString("EMP_ID")); in.putExtra("MSGVIEW", "NO");
			 * 
			 * if(extras.getString("SECURITY").equalsIgnoreCase("YES")) {
			 * in.putExtra("ESCORT_NAME", extras.getString("ESCORT_NAME"));
			 * in.putExtra("ESCORT_CONTACT",
			 * extras.getString("ESCORT_CONTACT")); }
			 * 
			 * if(!extras.getString("EMPS_COUNT").equalsIgnoreCase("")&&extras.
			 * getString("EMPS_COUNT")!=null) { for(int
			 * i=1;i<=Integer.parseInt(extras.getString("EMPS_COUNT"));i++) {
			 * in.putExtra("PERSONNEL_NO"+i,
			 * extras.getString("PERSONNEL_NO"+i)); in.putExtra("EMP_NAME"+i,
			 * extras.getString("EMP_NAME"+i)); in.putExtra("GENDER"+i,
			 * extras.getString("GENDER"+i)); in.putExtra("EMP_ID"+i,
			 * extras.getString("EMP_ID"+i)); in.putExtra("EMP_CONTACT"+i,
			 * extras.getString("EMP_CONTACT"+i));
			 * 
			 * } } }
			 */
			// this.mHandler.removeCallbacks(m_Runnable);
			startActivity(in);
			// finish();

		}
		if (menuLists[position].equals("Emergency Contact")) {
			// startActivity(new Intent(this, HelpActivity.class));
			Intent in = new Intent(getApplicationContext(),
					EmergencyContactActivity.class);
			//Bundle extras = getIntent().getExtras();
			if (extras != null) {
				in.putExtra("EMP_ID", extras.getString("EMP_ID"));
			}
			startActivity(in);
		}
		if (menuLists[position].equals("Trip History")) {
			// startActivity(new Intent(this, HelpActivity.class));
			Intent in = new Intent(getApplicationContext(),
					TripHistoryList.class);

			startActivity(in);
		}

		if (menuLists[position].equals("Manage Booking")) {
			// startActivity(new Intent(this, HelpActivity.class));
			Intent in = new Intent(getApplicationContext(),
					ManageBookingActivity.class);

			// cancel
			//Bundle extras = getIntent().getExtras();
//			if (extras != null) {
//				in.putExtra("FROM_DATE", extras.getString("FROM_DATE"));
//				in.putExtra("TO_DATE", extras.getString("TO_DATE"));
//				in.putExtra("LOG_IN", extras.getString("LOG_IN"));
//				in.putExtra("LOG_OUT", extras.getString("LOG_OUT"));
//				in.putExtra("ALTIMES", extras.getString("ALTIMES"));
//				in.putExtra("IN_LOGS_COUNT", extras.getString("IN_LOGS_COUNT"));
//				in.putExtra("OUT_LOGS_COUNT",
//						extras.getString("OUT_LOGS_COUNT"));
//				int incount1 = Integer.parseInt(extras
//						.getString("IN_LOGS_COUNT"));
//				int outcount1 = Integer.parseInt(extras
//						.getString("OUT_LOGS_COUNT"));
//				for (int i = 1; i <= incount1; i++) {
//					in.putExtra("IN_LOGS" + i, extras.getString("IN_LOGS" + i));
//				}
//				for (int i = 1; i <= outcount1; i++) {
//					in.putExtra("OUT_LOGS" + i,
//							extras.getString("OUT_LOGS" + i));
//				}
//				int times = 0;
//				if (extras.getString("ALTIMES") != null) {
//					times = Integer.parseInt(extras.getString("ALTIMES") + "");
//				}
//				for (int i = 1; i <= times; i++) {
//					in.putExtra("ALTERDATE" + i,
//							extras.getString("ALTERDATE" + i));
//
//					in.putExtra("AL_LOG_IN" + i,
//							extras.getString("AL_LOG_IN" + i));
//					in.putExtra("AL_LOG_OUT" + i,
//							extras.getString("AL_LOG_OUT" + i));
//				}
//				in.putExtra("ADHOCTYPECOUNT",
//						extras.getString("ADHOCTYPECOUNT"));
//				for (int i = 1; i <= Integer.parseInt(extras
//						.getString("ADHOCTYPECOUNT") + ""); i++) {
//					in.putExtra("ADHOCTYPE" + i,
//							extras.getString("ADHOCTYPE" + i));
//
//				}
//			}
//			// cancel
			startActivity(in);
		}
		if (menuLists[position].equals("FeedBack")) {

			Intent in = new Intent(getApplicationContext(),
					FeedBackActivity.class);
			startActivity(in);
		}

		if (menuLists[position].equals("MY CAB")) {

			//Bundle extras = getIntent().getExtras();

			Intent in = new Intent(getApplicationContext(),
					LiveTrackingActivity.class);
			// in.putExtra("user_type", "emp");
			in.putExtra("TRIP_ID", extras.getString("TRIP_ID"));
			in.putExtra("TRIP_CODE", extras.getString("TRIP_CODE"));
			in.putExtra("TRIP_DATE", extras.getString("TRIP_DATE"));
			in.putExtra("TRIP_LOG", extras.getString("TRIP_LOG"));
			in.putExtra("REG_NO", extras.getString("REG_NO"));
			in.putExtra("TRIP_TIME", extras.getString("TRIP_TIME"));
			in.putExtra("DRIVER_NAME", extras.getString("DRIVER_NAME"));
			in.putExtra("DRIVER_CONTACT", extras.getString("DRIVER_CONTACT"));
			in.putExtra("EMPS_COUNT", extras.getString("EMPS_COUNT"));
			in.putExtra("SECURITY", extras.getString("SECURITY"));
			in.putExtra("EMP_ID", extras.getString("EMP_ID"));

			in.putExtra("EMP_NAME", extras.getString("EMP_NAME"));
			in.putExtra("EMP_PERSONNELNO", extras.getString("EMP_PERSONNELNO"));
			in.putExtra("EMP_GENDER", extras.getString("EMP_GENDER"));
			in.putExtra("EMP_EMAIL", extras.getString("EMP_EMAIL"));
			in.putExtra("EMP_SITE", extras.getString("EMP_SITE"));
			in.putExtra("MSGVIEW", "NO");

			if (extras.getString("SECURITY").equalsIgnoreCase("YES")) {
				in.putExtra("ESCORT_NAME", extras.getString("ESCORT_NAME"));
				in.putExtra("ESCORT_CONTACT",
						extras.getString("ESCORT_CONTACT"));
			}

			if (extras.getString("EMPS_COUNT") != null
					&& !extras.getString("EMPS_COUNT").equalsIgnoreCase("")) {
				for (int i = 1; i <= Integer.parseInt(extras
						.getString("EMPS_COUNT")); i++) {
					in.putExtra("PERSONNEL_NO" + i,
							extras.getString("PERSONNEL_NO" + i));
					in.putExtra("EMP_NAME" + i,
							extras.getString("EMP_NAME" + i));
					in.putExtra("GENDER" + i, extras.getString("GENDER" + i));
					in.putExtra("EMP_ID" + i, extras.getString("EMP_ID" + i));
					in.putExtra("EMP_CONTACT" + i,
							extras.getString("EMP_CONTACT" + i));

				}
			}
			startActivity(in);
		}

	}

	// method to get the selected itemPosition and calls setTitle() to set
	// ActionBar title with the selected Item..
	private void selectItem(int position) {
		listView.setItemChecked(position, true);
		setTitle(menuLists[position]);
	}

	// method to set the actionbar title with the menu item selected..

	// -------------

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
			// case R.id.action_settings:
			// return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		active = true;
		initilizeMap();
	}

	@Override
	protected void onResume() {
		// drawerLayout.closeDrawers();
		active = true;
		super.onResume();
		//registerReceiver();
		//finish();
	}

	@Override
	protected void onStop() {
		// trimCache(this);
		super.onStop();


		active = false;
		alertval = 0;
		escval = 0;
		tripstatusval = 0;
	}


	/**
	 * function to load map If map is not created it will create it for you
	 */
	@SuppressLint("NewApi")
	private void initilizeMap() {
		if (googleMap == null) {
			Fragment mapFragment = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map));
			if (mapFragment != null) {
				googleMap = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.map)).getMap();

				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	public void alertfirsttime(String displayname) {

		// final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//
		// builder.setTitle("WELCOME MSG");
		// builder.setMessage("Hi "+displayname+", WELCOME TO Agiledge...");
		// builder.setCancelable(false);
		//
		// builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		// {
		// public void onClick(final DialogInterface dialog, final int id)
		// {
		// dialog.cancel();
		// }
		// });
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.welcomepopup);
		AppController.getInstance().trackScreenView("Map class");
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER_VERTICAL;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Button Ok = (Button) dialog.findViewById(R.id.welOK);

		TextView distxt = (TextView) dialog.findViewById(R.id.title1);
		distxt.setText("Hi " + displayname + ", WELCOME TO Agiledge...");
		Ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dialog.cancel();
			}
		});
		if (msgview.equalsIgnoreCase("YES")) {
			dialog.show();
		}

	}

	public void buildAlertMessageNoEscort() {
		// final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//
		// builder.setTitle("No Escort");
		// builder.setMessage("Escort Not Boarded The Cab!");
		// builder.setCancelable(false);
		//
		// builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		// {
		// public void onClick(final DialogInterface dialog, final int id)
		// {
		// dialog.cancel();
		// }
		// });
		// if(escval==0)
		// {
		// builder.create().show();
		// escval=1;
		// }
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.escortstatus);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER_VERTICAL;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Button Ok = (Button) dialog.findViewById(R.id.escortok);
		Ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dialog.cancel();
			}
		});
		if (escval == 0) {
			dialog.show();
			escval = 1;
		}
	}

	public void buildAlertMessageTRIPSTATUS() {
		// final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//
		// builder.setTitle("TRIP STATUS");
		// builder.setMessage("Your Trip Has Not Started");
		// builder.setCancelable(false);
		//
		// builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		// {
		// public void onClick(final DialogInterface dialog, final int id)
		// {
		// dialog.cancel();
		// }
		// });
		// if(tripstatusval==0)
		// {
		// builder.create().show();
		// tripstatusval=1;
		// }
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.tripstatuspopup);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER_VERTICAL;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Button Ok = (Button) dialog.findViewById(R.id.tripok);
		Ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dialog.cancel();
			}
		});
		if (tripstatusval == 0) {
			//dialog.show();
			tripstatusval = 1;
		}

	}

	private double distance(double lat1, double lng1, double lat2, double lng2) {

		double earthRadius = 6371; // in miles, change to 6371 for kilometers

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);

		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double dist = earthRadius * c;

		return dist;
	}

	public void alertdist(double dist) {
		float indist = (float) dist;
		BigDecimal bd = new BigDecimal(Float.toString(indist));
		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 builder.setTitle("CAB ALERT");
		 builder.setMessage("Cab Is "+bd+" KM From Your Location");
		 builder.setCancelable(false);

		 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		 {
		 public void onClick(final DialogInterface dialog, final int id)
		 {
		 dialog.cancel();
		 }
		 });
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.cabalertpopup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER_VERTICAL;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Button Ok = (Button) dialog.findViewById(R.id.cabOK);

		TextView distxt = (TextView) dialog.findViewById(R.id.disttitle1);
		distxt.setText("Cab is " + bd + " KM From Your Location");
		Ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dialog.cancel();
			}
		});
		// if(alertval==0&&showempmenu.equalsIgnoreCase("yes"))

		if (alertval == 0) {
			dialog.show();
			alertval = 1;
		}

	}

	private final Runnable m_Runnable = new Runnable() {
		public void run()

		{
			try {
				if (active) {
					long i = 15000;
					if (!showempmenu.equalsIgnoreCase("yes")) {
						i = 50000;

					}
					MapClass.this.mHandler.postDelayed(m_Runnable, i);
					makeMap();

				} else {

					finish();
					active = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AppController.getInstance().trackException(e);
			}

		}

	};

	public void makeMap() {

		try {
			if (!showempmenu.equalsIgnoreCase("yes")) {

				JSONObject jobj = new JSONObject();
				jobj.put("ACTION", "admin_track");
				if (mapInitialize) {
					initilizeMap();
					mapInitialize = false;
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
								JSONArray LAT = response.getJSONArray("LAT");
								JSONArray LONG = response.getJSONArray("LONG");
								JSONArray REGNO = response.getJSONArray("REGNO");
								JSONArray TRIPCODE = response
										.getJSONArray("TRIPCODE");
								JSONArray LOGSTATUS = response
										.getJSONArray("LOGSTATUS");
								JSONArray LADYCOUNT = response
										.getJSONArray("LADYCOUNT");
								JSONArray EMPINCOUNT = response
										.getJSONArray("EMPINCOUNT");
								JSONArray ESCORT = response.getJSONArray("ESCORT");

								if (LAT.length() == LONG.length()) {
									for (int i = 0; i < LAT.length(); i++) {
										longitude = Double.parseDouble(LONG.get(i).toString());
										latitude = Double.parseDouble(LAT.get(i).toString());
										MarkerOptions marker = new MarkerOptions()
												.position(
														new LatLng(latitude,
																longitude))
												.title(REGNO.get(i).toString()
														+ ","
														+ "   "
														+ "TRIPCODE"
														+ " "
														+ TRIPCODE.get(i)
														.toString());
										// googleMap.setInfoWindowAdapter(new
										// PopupAdapter(getLayoutInflater(),REGNO.get(i).toString()));

										if (LOGSTATUS.get(i).toString()
												.equalsIgnoreCase("danger")) {
											marker.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.pan_car));
										} else {

											if (Integer.parseInt(EMPINCOUNT
													.get(i).toString()) == 0) {
												marker.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.only_driver));
											} else if (Integer
													.parseInt(LADYCOUNT.get(i)
															.toString()) < 1) {
												marker.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.only_guys));
											} else if (Integer
													.parseInt(LADYCOUNT.get(i)
															.toString()) > 0
													&& Integer
													.parseInt(EMPINCOUNT
															.get(i)
															.toString()) > 1) {
												marker.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.atleast_onelady));
											} else if (Integer
													.parseInt(LADYCOUNT.get(i)
															.toString()) == 1
													&& Integer
													.parseInt(EMPINCOUNT
															.get(i)
															.toString()) == 1
													&& ESCORT
													.get(i)
													.toString()
													.equalsIgnoreCase(
															"NO")) {
												marker.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.singlelady_noesc));
											} else if (Integer
													.parseInt(LADYCOUNT.get(i)
															.toString()) == 1
													&& Integer
													.parseInt(EMPINCOUNT
															.get(i)
															.toString()) == 1
													&& ESCORT
													.get(i)
													.toString()
													.equalsIgnoreCase(
															"YES")) {
												marker.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.singlelady_withesc));
											}

										}

										googleMap.addMarker(marker);
									}
									CameraPosition cameraPosition = new CameraPosition.Builder()
											.target(new LatLng(latitude,
													longitude)).zoom(11)
											.build();

									googleMap.animateCamera(CameraUpdateFactory
											.newCameraPosition(cameraPosition));

								} else {
									if (mdialog != null && mdialog.isShowing()) {
										mdialog.dismiss();
									}
									Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

								}
							}
						} catch (Exception e) {
							if (mdialog != null && mdialog.isShowing()) {
								mdialog.dismiss();
								AppController.getInstance().trackException(e);
							}
							e.printStackTrace();
						}
					}
				}
						, new Response.ErrorListener()

				{
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
						if (mdialog != null && mdialog.isShowing()) {
							mdialog.dismiss();
						}

					}
				});

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(req);
//				ServerCommunication sobj = new ServerCommunication(jobj);
//				sobj.setDataDownloadListen(new DataDownloadListener() {
//					public void dataSuccess(String result) {
//						try {
//							MyLocation myLocation1;
//							myLocation1 = new MyLocation(MapClass.this);
//							if (result != null && !result.equalsIgnoreCase("")) {
//								JSONObject robj;
//								robj = new JSONObject(result);
//								JSONArray LAT = robj.getJSONArray("LAT");
//								JSONArray LONG = robj.getJSONArray("LONG");
//								JSONArray REGNO = robj.getJSONArray("REGNO");
//								JSONArray TRIPCODE = robj
//										.getJSONArray("TRIPCODE");
//								JSONArray LOGSTATUS = robj
//										.getJSONArray("LOGSTATUS");
//								JSONArray LADYCOUNT = robj
//										.getJSONArray("LADYCOUNT");
//								JSONArray EMPINCOUNT = robj
//										.getJSONArray("EMPINCOUNT");
//								JSONArray ESCORT = robj.getJSONArray("ESCORT");
//
//								if (LAT.length() == LONG.length()) {
//									for (int i = 0; i < LAT.length(); i++) {
//										longitude = Double.parseDouble(LONG
//												.get(i).toString());
//										latitude = Double.parseDouble(LAT
//												.get(i).toString());
//										MarkerOptions marker = new MarkerOptions()
//												.position(
//														new LatLng(latitude,
//																longitude))
//												.title(REGNO.get(i).toString()
//														+ ","
//														+ "   "
//														+ "TRIPCODE"
//														+ " "
//														+ TRIPCODE.get(i)
//																.toString());
//										// googleMap.setInfoWindowAdapter(new
//										// PopupAdapter(getLayoutInflater(),REGNO.get(i).toString()));
//
//										if (LOGSTATUS.get(i).toString()
//												.equalsIgnoreCase("danger")) {
//											marker.icon(BitmapDescriptorFactory
//													.fromResource(R.drawable.pan_car));
//										} else {
//
//											if (Integer.parseInt(EMPINCOUNT
//													.get(i).toString()) == 0) {
//												marker.icon(BitmapDescriptorFactory
//														.fromResource(R.drawable.only_driver));
//											} else if (Integer
//													.parseInt(LADYCOUNT.get(i)
//															.toString()) < 1) {
//												marker.icon(BitmapDescriptorFactory
//														.fromResource(R.drawable.only_guys));
//											} else if (Integer
//													.parseInt(LADYCOUNT.get(i)
//															.toString()) > 0
//													&& Integer
//															.parseInt(EMPINCOUNT
//																	.get(i)
//																	.toString()) > 1) {
//												marker.icon(BitmapDescriptorFactory
//														.fromResource(R.drawable.atleast_onelady));
//											} else if (Integer
//													.parseInt(LADYCOUNT.get(i)
//															.toString()) == 1
//													&& Integer
//															.parseInt(EMPINCOUNT
//																	.get(i)
//																	.toString()) == 1
//													&& ESCORT
//															.get(i)
//															.toString()
//															.equalsIgnoreCase(
//																	"NO")) {
//												marker.icon(BitmapDescriptorFactory
//														.fromResource(R.drawable.singlelady_noesc));
//											} else if (Integer
//													.parseInt(LADYCOUNT.get(i)
//															.toString()) == 1
//													&& Integer
//															.parseInt(EMPINCOUNT
//																	.get(i)
//																	.toString()) == 1
//													&& ESCORT
//															.get(i)
//															.toString()
//															.equalsIgnoreCase(
//																	"YES")) {
//												marker.icon(BitmapDescriptorFactory
//														.fromResource(R.drawable.singlelady_withesc));
//											}
//
//										}
//
//										googleMap.addMarker(marker);
//									}
//									CameraPosition cameraPosition = new CameraPosition.Builder()
//											.target(new LatLng(latitude,
//													longitude)).zoom(11)
//											.build();
//
//									googleMap.animateCamera(CameraUpdateFactory
//											.newCameraPosition(cameraPosition));
//
//								}
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							AppController.getInstance().trackException(e);
//						}
//					}
//
//					public void datafail() {
//
//						Toast.makeText(getApplicationContext(),
//								"oops communication error", Toast.LENGTH_SHORT)
//								.show();
//					}
//				});
//				sobj.execute();
			} else {

				//Bundle extras = getIntent().getExtras();
				String tripid = "";
				if (extras != null) {
					tripid = extras.getString("TRIP_ID");
				}
				try {
					JSONObject jobj = new JSONObject();
					jobj.put("ACTION", "VEHICLE_LOCATION");
					jobj.put("TRIP_ID", tripid);
					if (mapInitialize) {
						initilizeMap();
						mapInitialize = false;
					}

//					googleMap.clear();
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

					// add marker here
					if (tripid.equalsIgnoreCase("")) {
						JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getString("result").equalsIgnoreCase("TRUE")) {
										if (response.getString("LAT") != null
												&& !response.getString("LAT")
												.equalsIgnoreCase("")
												&& response.getString("LONG") != null
												&& !response.getString("LONG")
												.equalsIgnoreCase("")) {
											MyLocation myLocation;
											myLocation = new MyLocation(
													MapClass.this);
											longitude = Double.parseDouble(response
													.getString("LONG"));
											latitude = Double.parseDouble(response
													.getString("LAT"));
											String line = "";
											List<String> boardids = new ArrayList<String>();
											String escortalert = "NO";
											String cbalrt = "YES";
											for (int i = 1; i <= Integer
													.parseInt(response
															.getString("COUNT")); i++) {
												boardids.add(response
														.getString("BOARD_ID" + i));
											}
											//Bundle extras = getIntent().getExtras();
											if (extras.getString("SECURITY")
													.equalsIgnoreCase("YES")) {
												if (response.getString("ESCORT_STATUS")
														.equalsIgnoreCase("SHOW")) {
													line = "\n\tEscort\t\t\u2714"
															+ "\n\n";
												} else {
													line = "\n\tEscort" + "\n\n";
													escortalert = "YES";
												}

											}
											if (!extras.getString("EMPS_COUNT")
													.equalsIgnoreCase("")
													&& extras
													.getString("EMPS_COUNT") != null) {
												for (int i = 1; i <= Integer.parseInt(extras
														.getString("EMPS_COUNT")); i++) {
													String tick = "";
													for (String id : boardids) {
														if (id.equalsIgnoreCase(extras
																.getString("EMP_ID"
																		+ i)))
															tick = "\u2714";
														if (id.equalsIgnoreCase(extras
																.getString("EMP_ID"))) {
															cbalrt = "NO";
															panicstatus = "yes";
														} else
															panicstatus = "no";

													}
													line += "\t"
															+ i
															+ ")"
															+ extras.getString("EMP_NAME"
															+ i)
															+ " "
															+ extras.getString("GENDER"
															+ i) + " "
															+ tick + " " + " "
															+ "\n";

												}
												emps = line;
											}
											ImageView panicbtn = (ImageView) findViewById(R.id.panicalaram);
											if (panicstatus.equalsIgnoreCase("yes")) {
												// panicbtn.setVisibility(View.VISIBLE);
												panicbtn.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {

														panicalert();

													}
												});
											} else {
												// panicbtn.setVisibility(View.INVISIBLE);
											}
											double mylatitude = myLocation
													.getLatitude();
											double mylongitude = myLocation
													.getLongitude();
											double dist = distance(latitude,
													longitude, mylatitude,
													mylongitude);
											if (dist < 2
													&& cbalrt
													.equalsIgnoreCase("YES")) {
												displayNotificationOne();
											}
											MarkerOptions marker = new MarkerOptions()
													.position(new LatLng(latitude,
															longitude));
											googleMap
													.setInfoWindowAdapter(new PopupAdapter(
															getLayoutInflater(),
															emps));
											marker.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.only_guys));
											googleMap.addMarker(marker);
											CameraPosition cameraPosition = new CameraPosition.Builder()
													.target(new LatLng(latitude,
															longitude)).zoom(15)
													.build();

											googleMap
													.animateCamera(CameraUpdateFactory
															.newCameraPosition(cameraPosition));
											if (escortalert.equalsIgnoreCase("Yes")) {
												buildAlertMessageNoEscort();
											}

										} else {
											String tripstatusalert = "YES";
											if (tripstatusalert
													.equalsIgnoreCase("Yes")) {
												buildAlertMessageTRIPSTATUS();
											}
										}

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
									AppController.getInstance().trackException(e);
								}
							}
						}
								, new Response.ErrorListener()

						{
							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
								if (mdialog != null && mdialog.isShowing()) {
									mdialog.dismiss();
								}

							}
						});

						// Adding request to request queue
						AppController.getInstance().addToRequestQueue(req);
//							ServerCommunication sobj = new ServerCommunication(jobj);
//							sobj.setDataDownloadListen(new DataDownloadListener() {
//								public void dataSuccess(String result) {
//									try {
//										if (result != null
//												&& !result.equalsIgnoreCase("")) {
//											JSONObject robj;
//											robj = new JSONObject(result);
//											if (robj.getString("LAT") != null
//													&& !robj.getString("LAT")
//													.equalsIgnoreCase("")
//													&& robj.getString("LONG") != null
//													&& !robj.getString("LONG")
//													.equalsIgnoreCase("")) {
//												MyLocation myLocation;
//												myLocation = new MyLocation(
//														MapClass.this);
//												longitude = Double.parseDouble(robj
//														.getString("LONG"));
//												latitude = Double.parseDouble(robj
//														.getString("LAT"));
//												String line = "";
//												List<String> boardids = new ArrayList<String>();
//												String escortalert = "NO";
//												String cbalrt = "YES";
//												for (int i = 1; i <= Integer
//														.parseInt(robj
//																.getString("COUNT")); i++) {
//													boardids.add(robj
//															.getString("BOARD_ID" + i));
//												}
//												//Bundle extras = getIntent().getExtras();
//												if (extras.getString("SECURITY")
//														.equalsIgnoreCase("YES")) {
//													if (robj.getString("ESCORT_STATUS")
//															.equalsIgnoreCase("SHOW")) {
//														line = "\n\tEscort\t\t\u2714"
//																+ "\n\n";
//													} else {
//														line = "\n\tEscort" + "\n\n";
//														escortalert = "YES";
//													}
//
//												}
//												if (!extras.getString("EMPS_COUNT")
//														.equalsIgnoreCase("")
//														&& extras
//														.getString("EMPS_COUNT") != null) {
//													for (int i = 1; i <= Integer.parseInt(extras
//															.getString("EMPS_COUNT")); i++) {
//														String tick = "";
//														for (String id : boardids) {
//															if (id.equalsIgnoreCase(extras
//																	.getString("EMP_ID"
//																			+ i)))
//																tick = "\u2714";
//															if (id.equalsIgnoreCase(extras
//																	.getString("EMP_ID"))) {
//																cbalrt = "NO";
//																panicstatus = "yes";
//															} else
//																panicstatus = "no";
//
//														}
//														line += "\t"
//																+ i
//																+ ")"
//																+ extras.getString("EMP_NAME"
//																+ i)
//																+ " "
//																+ extras.getString("GENDER"
//																+ i) + " "
//																+ tick + " " + " "
//																+ "\n";
//
//													}
//													emps = line;
//												}
//												ImageView panicbtn = (ImageView) findViewById(R.id.panicalaram);
//												if (panicstatus.equalsIgnoreCase("yes")) {
//													// panicbtn.setVisibility(View.VISIBLE);
//													panicbtn.setOnClickListener(new OnClickListener() {
//
//														@Override
//														public void onClick(View v) {
//
//															panicalert();
//
//														}
//													});
//												} else {
//													// panicbtn.setVisibility(View.INVISIBLE);
//												}
//												double mylatitude = myLocation
//														.getLatitude();
//												double mylongitude = myLocation
//														.getLongitude();
//												double dist = distance(latitude,
//														longitude, mylatitude,
//														mylongitude);
//												if (dist < 2
//														&& cbalrt
//														.equalsIgnoreCase("YES")) {
//													alertdist(dist);
//												}
//												MarkerOptions marker = new MarkerOptions()
//														.position(new LatLng(latitude,
//																longitude));
//												googleMap
//														.setInfoWindowAdapter(new PopupAdapter(
//																getLayoutInflater(),
//																emps));
//												marker.icon(BitmapDescriptorFactory
//														.fromResource(R.drawable.only_guys));
//												googleMap.addMarker(marker);
//												CameraPosition cameraPosition = new CameraPosition.Builder()
//														.target(new LatLng(latitude,
//																longitude)).zoom(15)
//														.build();
//
//												googleMap
//														.animateCamera(CameraUpdateFactory
//																.newCameraPosition(cameraPosition));
//												if (escortalert.equalsIgnoreCase("Yes")) {
//													buildAlertMessageNoEscort();
//												}
//
//											} else {
//												String tripstatusalert = "YES";
//												if (tripstatusalert
//														.equalsIgnoreCase("Yes")) {
//													buildAlertMessageTRIPSTATUS();
//												}
//											}
//										} else {
//											Toast.makeText(getApplicationContext(),
//													"Oops Communication Failure",
//													Toast.LENGTH_SHORT).show();
//
//										}
//									} catch (JSONException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//										AppController.getInstance().trackException(e);
//									}
//								}
//
//								public void datafail() {
//
//									Toast.makeText(getApplicationContext(),
//											"Oops Communication Failure",
//											Toast.LENGTH_SHORT).show();
//								}
//							});
//							sobj.execute();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						AppController.getInstance().trackException(e);
//					}
//
//				}
					}

				} catch (Exception e) {
					e.printStackTrace();
					AppController.getInstance().trackException(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppController.getInstance().trackException(e);
		}
	}

	public void onBackPressed() {
		if(bckprsdcount>1) {
			this.mHandler.removeCallbacks(m_Runnable);
			super.onBackPressed();

		}else{
			bckprsdcount++;
			Toast.makeText(getApplicationContext(),"Press back again to exit!",Toast.LENGTH_LONG).show();
		}

	}

	private void panicalert() {
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.panicpopup);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		ImageView yes = (ImageView) dialog.findViewById(R.id.yespanic);
		Button no = (Button) dialog.findViewById(R.id.nopanic);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				panicactivated();
				dialog.cancel();
				try {
					JSONObject jobj = new JSONObject();
					jobj.put("ACTION", "PANIC_ACTIVATED");
					tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

					MyLocation myLocation;
					myLocation = new MyLocation(MapClass.this);
					double latitude = myLocation.getLatitude();
					double longitude = myLocation.getLongitude();
					//Bundle extras = getIntent().getExtras();
					String tripid = "", empid = "";
					if (extras != null) {
						tripid = extras.getString("TRIP_ID");
						empid = extras.getString("EMP_ID");
					}

					jobj.put("EMP_ID", empid);
					jobj.put("IMEI_NUMBER",macAddress );
					jobj.put("TRIP_ID", tripid);
					jobj.put("LAT", latitude + "");
					jobj.put("LONG", longitude + "");
					JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getString("result").equalsIgnoreCase("TRUE")) {
										if (response != null
												&& response.getString("result")
												.equalsIgnoreCase("true")) {
											if (response.getString("PANIC_STATUS")
													.equalsIgnoreCase("ACTIVATED")) {
												Toast.makeText(
														getApplicationContext(),
														"Panic Activated!",
														Toast.LENGTH_LONG).show();
												ImageView panicactiv = (ImageView) findViewById(R.id.panicalaram);
												panicactiv
														.setImageResource(R.drawable.panic_activated);

											}
										} else if (response != null
												&& response.getString("result")
												.equalsIgnoreCase("false"))

										{
											Toast.makeText(getApplicationContext(),
													"Opertaion Failed!",
													Toast.LENGTH_LONG).show();
										}



								} else {
									Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();



								}
							} catch (Exception e) {


								e.printStackTrace();
								AppController.getInstance().trackException(e);
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
				} catch (Exception e) {
					e.printStackTrace();
					AppController.getInstance().trackException(e);
				}

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

	private void panicactivated() {
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.panicactivatedpopup);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		Button yes = (Button) dialog.findViewById(R.id.hidepanic);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.cancel();

			}
		});

		dialog.show();
		
	}
	public void buildAlertMessageEXIT() {
		  final Dialog dialog = new Dialog(MapClass.this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         dialog.setContentView(R.layout.exitpopup);


	        
	         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	         lp.copyFrom(dialog.getWindow().getAttributes());
	         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	         lp.gravity = Gravity.CENTER;

	         dialog.getWindow().setAttributes(lp);
	         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	        
	         Button yes = (Button) dialog.findViewById(R.id.yes);
	         Button no = (Button) dialog.findViewById(R.id.no);
	         yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				//	initial=2;
					dialog.cancel();
					Intent intent = new Intent(Intent.ACTION_MAIN);
			        intent.addCategory(Intent.CATEGORY_HOME);
			        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
			        startActivity(intent);
			        finish();
			        System.exit(0);
					
	        	//	launchGPSOptions();
					
				}
			});
	         no.setOnClickListener(new OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				dialog.cancel();

	    		//	System.exit(1);
	 				
	 			}
	 		});
	         dialog.show();
	    
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



	public void legendAlert() {
		final Dialog dialog = new Dialog(MapClass.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.legentpopup);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;

		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		Button ok = (Button) dialog.findViewById(R.id.legentok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.cancel();

			}
		});
	
		dialog.show();
	
	}

//	  public void onHomePressed() {
//	        // do something here...
//		
//		  System.exit(1);
//		  super.onBackPressed();
//	//	  initializeMapStats();
//	//	  initilizeMap();
//	//		makeMap();
//			
//		  
//	    }


}
