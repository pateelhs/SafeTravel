package com.agiledge.keocometemployee.navdrawermenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class CancelDetails extends Activity {

	public static TextView textView;

	// TextView canceldone;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canceldetails);

		// cancel
		TextView intime = (TextView) findViewById(R.id.cintimevalue);
		TextView outtime = (TextView) findViewById(R.id.couttimevalue);
		TextView cdate = (TextView) findViewById(R.id.cdatevalue);
		TextView canceldone = (TextView) findViewById(R.id.ccanceldone);

		CheckBox coutcheckBox = (CheckBox) findViewById(R.id.coutcheckBox1);
		CheckBox cincheckBox = (CheckBox) findViewById(R.id.cincheckBox1);

		Bundle extras = getIntent().getExtras();
		final String date = extras.getString("CLICKDATE");
		if (extras != null) {

			cdate.setText(extras.getString("CLICKDATE1"));
			intime.setText(extras.getString("LOG_IN"));
			outtime.setText(extras.getString("LOG_OUT"));
			int times = 0;
			if (extras.getString("ALTIMES") != null) {
				times = Integer.parseInt(extras.getString("ALTIMES"));
			}
			for (int i = 1; i <= times; i++) {

				if ((extras.getString("CLICKDATE") + " 00:00:00.0")
						.equalsIgnoreCase(extras.getString("ALTERDATE" + i))) {
					intime.setText(extras.getString("AL_LOG_IN" + i));
					outtime.setText(extras.getString("AL_LOG_OUT" + i));
				}
			}
		}
		canceldone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				CheckBox coutcheckBox1 = (CheckBox) findViewById(R.id.coutcheckBox1);
				CheckBox cincheckBox1 = (CheckBox) findViewById(R.id.cincheckBox1);
				if (coutcheckBox1.isChecked() == false
						&& cincheckBox1.isChecked() == false) {
					Toast.makeText(getApplicationContext(),
							"Select at least one Log-time", Toast.LENGTH_LONG)
							.show();
				} else {
					try {
						JSONObject jobj = new JSONObject();
						TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						String imei = tel.getDeviceId().toString();
						TextView intime1 = (TextView) findViewById(R.id.cintimevalue);
						TextView outtime1 = (TextView) findViewById(R.id.couttimevalue);
						jobj.put("ACTION", "SCHEDULE_CANCEL");
						jobj.put("IMEI", imei);
						jobj.put("DATE", date);
						if (cincheckBox1.isChecked() == true){
							jobj.put("LOG_IN", "weekly off");
						}else{
							jobj.put("LOG_IN", intime1.getText().toString());
						}
						if (coutcheckBox1.isChecked() == true){
							jobj.put("LOG_OUT", "weekly off");
						}else{
							jobj.put("LOG_OUT", outtime1.getText().toString());
						}
						JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress, jobj, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getString("result").equalsIgnoreCase("TRUE")) {
										Toast.makeText(
												getApplicationContext(),
												response.getString("STATUS"),
												Toast.LENGTH_LONG).show();

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
//						ServerCommunication sobj = new ServerCommunication(jobj);
//						sobj.setDataDownloadListen(new DataDownloadListener() {
//							public void dataSuccess(String result) {
//								try {
//									if (result != null
//											&& !result.equalsIgnoreCase("")) {
//										JSONObject robj;
//										robj = new JSONObject(result);
//
//										if (robj != null) {
//											Toast.makeText(
//													getApplicationContext(),
//													robj.getString("STATUS"),
//													Toast.LENGTH_LONG).show();
//										}
//									} else {
//
//										Toast.makeText(getApplicationContext(),
//												"Oops Error In Communication",
//												Toast.LENGTH_SHORT).show();
//
//									}
//
//								} catch (JSONException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							}
//
//							public void datafail() {
//
//								Toast.makeText(getApplicationContext(),
//										"No Network", Toast.LENGTH_SHORT)
//										.show();
//							}
//						});
//						sobj.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		});
		// cancel

	}

}
