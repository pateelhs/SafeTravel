package com.agiledge.keocometemployee.navdrawermenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdocDetails extends Activity {
	private TransparentProgressDialog pd;
	private String date="";
	private String scheduleid="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adocdetails);
		Spinner timespnr=(Spinner) findViewById(R.id.adhoctimespinner);
		pd = new TransparentProgressDialog(this, R.drawable.loading);
		pd.show();
		timespnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(parent.getChildAt(0)!=null) {
					((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

				}



			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

				// sometimes you need nothing here
			}});
		// indate
		TextView adone = (TextView) findViewById(R.id.adhocdone);
		TextView adhdate = (TextView) findViewById(R.id.adhocdatevalue);
		Bundle extras = getIntent().getExtras();
		if(extras!=null) {
			adhdate.setText(extras.getString("CLICKDATE"));
			date=extras.getString("CLICKDATE");
		}
		fillvalues();
		adone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd.show();
				book();


			}
		});


	}

	public void fillvalues() {
		try {
			WifiManager wifiMan = (WifiManager) this.getSystemService(
					Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			String macAddress = wifiInf.getMacAddress();
			JSONObject jobj = new JSONObject();
			jobj.put("ACTION", "MODIFY_CHECK");
			jobj.put("IMEI",macAddress);
			jobj.put("DATE",date);
			final Spinner logoutspinner = (Spinner) findViewById(R.id.adhoctimespinner);
			JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {

							JSONArray logouts = response.getJSONArray("LOGS");
							scheduleid=response.getString("SCHEDULEID");
							ArrayList<String> logoutslist = new ArrayList<>();
							int position=0;
							for (int i = 0; i < logouts.length(); i++) {
								logoutslist.add(logouts.getString(i));
								if(response.getString("LOGTIME").equalsIgnoreCase(logouts.getString(i))){
									position=i;
								}
							}
							ArrayAdapter<String> logoutsadp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, logoutslist);
							logoutsadp.setDropDownViewResource(R.layout.my_spinner);
							logoutspinner.setAdapter(logoutsadp);
							logoutspinner.setSelection(position);
							pd.dismiss();

						} else {

							Toast.makeText(getApplicationContext(), ""+response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
							finish();
							pd.dismiss();
						}


					} catch (Exception e) {
						AppController.getInstance().trackException(e);
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
					pd.dismiss();


				}
			});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//  AppController.getInstance().trackScreenView("Login");// for Google analytics data

	}

	public void book() {
		try{
			WifiManager wifiMan = (WifiManager) this.getSystemService(
					Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			Spinner time=(Spinner) findViewById(R.id.adhoctimespinner);
			TextView txtdate=(TextView) findViewById(R.id.adhocdatevalue);
			boolean valid=true;
			String macAddress = wifiInf.getMacAddress();
			if(valid) {
				JSONObject jobj = new JSONObject();
				jobj.put("ACTION", "SCHEDULE_ALTER");
				jobj.put("SCHEDULE_ID",scheduleid);
				jobj.put("IMEI", macAddress);
				jobj.put("DATE", txtdate.getText().toString());
				jobj.put("LOG_OUT", time.getSelectedItem().toString());

				JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {

								pd.dismiss();
								Toast.makeText(getApplicationContext(), ""+response.getString("STATUS"), Toast.LENGTH_SHORT).show();
								finish();



						} catch (Exception e) {
							AppController.getInstance().trackException(e);
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_SHORT).show();
						pd.dismiss();


					}
				});

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(req);
			}else{
				pd.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class TransparentProgressDialog extends Dialog {

		private ImageView iv;

		public TransparentProgressDialog(Context context, int resourceIdOfImage) {
			super(context, R.style.TransparentProgressDialog);
			WindowManager.LayoutParams wlmp = getWindow().getAttributes();
			wlmp.gravity = Gravity.CENTER_HORIZONTAL;
			getWindow().setAttributes(wlmp);
			setTitle(null);
			setCancelable(false);
			setOnCancelListener(null);
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			iv = new ImageView(context);
			iv.setImageResource(resourceIdOfImage);
			layout.addView(iv, params);
			addContentView(layout, params);
		}

		@Override
		public void show() {
			super.show();
			RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			anim.setInterpolator(new LinearInterpolator());
			anim.setRepeatCount(Animation.INFINITE);
			anim.setDuration(1000);
			iv.setAnimation(anim);
			iv.startAnimation(anim);
		}
	}
}
