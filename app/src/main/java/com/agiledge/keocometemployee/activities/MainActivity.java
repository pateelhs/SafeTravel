package com.agiledge.keocometemployee.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.Util;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MainActivity extends Activity {
	private CoordinatorLayout coordinatorLayout;
	public static String macAddress;
	private static final int REQUEST_CODE=0;
	boolean startapp=false;
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		if(Util.Isconnected(this))
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
		CommenSettings.displayname=sharedpref.getString("APP_USERNAME","NOT_FOUND");
		CommenSettings.email=sharedpref.getString("APP_EMAIL","NOT_FOUND");
		CommenSettings.gender=sharedpref.getString("APP_EMP_GENDER","NOT_FOUND");
		CommenSettings.user_type=sharedpref.getString("APP_USERTYPE","NOT_FOUND");
		CommenSettings.empid=sharedpref.getString("APP_EMPID","NOT_FOUND");
		try {
			CommenSettings.android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		catch (Exception e)
		{e.printStackTrace();}

		if(CommenSettings.displayname.equalsIgnoreCase("NOT_FOUND")||CommenSettings.email.equalsIgnoreCase("NOT_FOUND")||CommenSettings.gender.equalsIgnoreCase("NOT_FOUND")||CommenSettings.user_type.equalsIgnoreCase("NOT_FOUND")||CommenSettings.empid.equalsIgnoreCase("NOT_FOUND")) {
			initial();
		}
		else {
			boolean isEnabled = isGPSenabled();
			if (isEnabled) {
				Intent in = new Intent(getApplicationContext(), Home_Activity.class);
//					in.putExtra("user_type", user_type);
//					in.putExtra("displayname",username);
//					in.putExtra("gender",gender);
//					in.putExtra("email",email);
//					in.putExtra("empid",empid);
				startActivity(in);
				finish();
			} else {
				buildAlertMessageNoGps();


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
			start();
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
				jobj.put("IMEI_NUMBER",CommenSettings.android_id);
				Log.d("ANDROID_IDD******",CommenSettings.android_id);
				JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.d("IMEI_CHEK******",""+response.toString());
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
								CommenSettings.empid=response.getString("EMP_ID");

								CommenSettings.displayname=response.getString("EMP_NAME");
								CommenSettings.user_type=response.getString("user_type");
								CommenSettings.email=response.getString("EMP_EMAIL");
								CommenSettings.gender=response.getString("EMP_GENDER");
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
						//Toast.makeText(getApplicationContext(), "Server took too long to respond or your internet connectivity is low", Toast.LENGTH_LONG).show();
						Snackbar snackbar = Snackbar
								.make(coordinatorLayout, "Server took too long to respond or internet connectivity is low!", Snackbar.LENGTH_LONG)
								.setAction("RETRY", new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										initial();
									}
								});
//
//							// Changing message text color
						snackbar.setActionTextColor(Color.RED);

						// Changing action button text color
						View sbView = snackbar.getView();
						TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
						textView.setTextColor(Color.YELLOW);
						textView.setMaxLines(3);
						//pd.hide();
						snackbar.show();

					}
				});

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(req);

			}
			else
			{
				buildAlertMessageNoGps();

			}
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