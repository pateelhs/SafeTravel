package com.agiledge.keocometemployee.navdrawermenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class ModifyDetails extends Activity {

	public static TextView textView;
	String macaddress= GetMacAddress.getMacAddr();

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifydetails);
		
		TextView intime = (TextView) findViewById(R.id.modintimevalue);
		TextView outtime = (TextView) findViewById(R.id.modouttimevalue);
		TextView mdate = (TextView) findViewById(R.id.mdatevalue);
		TextView mmodifydone = (TextView) findViewById(R.id.mmodifydone);
		
		CheckBox moutcheckBox = (CheckBox) findViewById(R.id.modoutcheckBox1);
		CheckBox mincheckBox = (CheckBox) findViewById(R.id.modincheckBox1);

		Bundle extras = getIntent().getExtras();
		List<String> spinnerArrayIN=new ArrayList<String>();
		List<String> spinnerArrayOUT=new ArrayList<String>();
		final String date = extras.getString("CLICKDATE");
		if (extras != null) {

			for(int i=1;i<=Integer.parseInt(extras.getString("IN_LOGS_COUNT"));i++)
				
			{
				spinnerArrayIN.add(extras.getString("IN_LOGS"+i));
			
			}
		for(int i=1;i<=Integer.parseInt(extras.getString("OUT_LOGS_COUNT"));i++)
			
		{
			spinnerArrayOUT.add(extras.getString("OUT_LOGS"+i));
		
		}
			
			ArrayAdapter<String> adapterIn=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerArrayIN);
			ArrayAdapter<String> adapterOut=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerArrayOUT);
			adapterIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			adapterOut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner modinspinner = (Spinner)findViewById(R.id.modinspinner);
			Spinner modoutspinner = (Spinner)findViewById(R.id.modoutspinner);
			modinspinner.setAdapter(adapterIn);
			modoutspinner.setAdapter(adapterOut);
			mdate.setText(extras.getString("CLICKDATE1"));
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

		mmodifydone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				CheckBox moutcheckBox1 = (CheckBox) findViewById(R.id.modoutcheckBox1);
				CheckBox mincheckBox1 = (CheckBox) findViewById(R.id.modincheckBox1);
				if (moutcheckBox1.isChecked() == false
						&& mincheckBox1.isChecked() == false) {
					Toast.makeText(getApplicationContext(),
							"Select at least one Log-time", Toast.LENGTH_LONG)
							.show();
				} else {
					try {
						JSONObject jobj = new JSONObject();
						TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						String imei = tel.getDeviceId().toString();
						TextView intime1 = (TextView) findViewById(R.id.modintimevalue);
						TextView outtime1 = (TextView) findViewById(R.id.modouttimevalue);
						
						Spinner modinspinner = (Spinner)findViewById(R.id.modinspinner);
						Spinner modoutspinner = (Spinner)findViewById(R.id.modoutspinner);
						String inValue = modinspinner.getSelectedItem().toString();
						String outValue = modoutspinner.getSelectedItem().toString();
						
						jobj.put("ACTION", "SCHEDULE_ALTER");
						jobj.put("IMEI", CommenSettings.macAddress);
						jobj.put("DATE", date);
						if (mincheckBox1.isChecked() == true){
							jobj.put("LOG_IN", inValue);
						}else{
							jobj.put("LOG_IN", intime1.getText().toString());
						}
						if (moutcheckBox1.isChecked() == true){
							jobj.put("LOG_OUT", outValue);
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
									} else {
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

