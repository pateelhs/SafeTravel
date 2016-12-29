package com.agiledge.keocometemployee.navdrawermenu;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.constants.GetMacAddress;
import com.agiledge.keocometemployee.utilities.PromptDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FeedBackActivity extends Activity {

	RadioButton vehile1, vehile2, vehile3, vehile4;
	RadioButton driver1, driver2, driver3, driver4;
	RadioButton time1, time2, time3, time4;
	RadioButton overall1, overall2, overall3, overall4;
	EditText comment;
	String android_id="";
	TextView feedbackdone;
	int selectedvehiclecondition, selecteddriverbehaviour, selectedtraveltime,
			selectedoverallexp;
	private ProgressDialog mdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		List<String> dates = new ArrayList<String>();
		List<String> logs = new ArrayList<String>();
		//logs.add("IN");
		logs.add("OUT");
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		dates.add(format.format(cal.getTime()));
		for (int i = 1; i <= 9; i++) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
			dates.add(format.format(cal.getTime()));
		}
		ArrayAdapter<String> datePicked = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dates);
		ArrayAdapter<String> logType = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, logs);

		datePicked
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		logType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner logTypespinner = (Spinner) findViewById(R.id.feedlogspinner);
		Spinner datespinner = (Spinner) findViewById(R.id.feeddatespinner);

		logTypespinner.setAdapter(logType);
		datespinner.setAdapter(datePicked);

		vehile1 = (RadioButton) findViewById(R.id.Radiobutton1);
		vehile2 = (RadioButton) findViewById(R.id.Radiobutton2);
		vehile3 = (RadioButton) findViewById(R.id.Radiobutton3);
		vehile4 = (RadioButton) findViewById(R.id.Radiobutton4);

		driver1 = (RadioButton) findViewById(R.id.Radiobutton5);
		driver2 = (RadioButton) findViewById(R.id.Radiobutton6);
		driver3 = (RadioButton) findViewById(R.id.Radiobutton7);
		driver4 = (RadioButton) findViewById(R.id.Radiobutton8);

		time1 = (RadioButton) findViewById(R.id.Radiobutton9);
		time2 = (RadioButton) findViewById(R.id.Radiobutton10);
		time3 = (RadioButton) findViewById(R.id.Radiobutton11);
		time4 = (RadioButton) findViewById(R.id.Radiobutton12);

		overall1 = (RadioButton) findViewById(R.id.Radiobutton13);
		overall2 = (RadioButton) findViewById(R.id.Radiobutton14);
		overall3 = (RadioButton) findViewById(R.id.Radiobutton15);
		overall4 = (RadioButton) findViewById(R.id.Radiobutton16);
		comment = (EditText) findViewById(R.id.othercommentseditText1);
		feedbackdone = (TextView) findViewById(R.id.feedbackdone);

		vehile1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vehile1.setChecked(true);
				vehile2.setChecked(false);
				vehile3.setChecked(false);
				vehile4.setChecked(false);
				selectedvehiclecondition = 1;
			}
		});
		vehile2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vehile2.setChecked(true);
				vehile1.setChecked(false);
				vehile3.setChecked(false);
				vehile4.setChecked(false);
				selectedvehiclecondition = 2;
			}
		});
		vehile3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vehile3.setChecked(true);
				vehile1.setChecked(false);
				vehile2.setChecked(false);
				vehile4.setChecked(false);
				selectedvehiclecondition = 3;
			}
		});
		vehile4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vehile4.setChecked(true);
				vehile1.setChecked(false);
				vehile3.setChecked(false);
				vehile2.setChecked(false);
				selectedvehiclecondition = 4;
			}
		});
		driver1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				driver1.setChecked(true);
				driver2.setChecked(false);
				driver3.setChecked(false);
				driver4.setChecked(false);
				selecteddriverbehaviour = 1;
			}
		});
		driver2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				driver2.setChecked(true);
				driver1.setChecked(false);
				driver3.setChecked(false);
				driver4.setChecked(false);
				selecteddriverbehaviour = 2;
			}
		});
		driver3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				driver3.setChecked(true);
				driver2.setChecked(false);
				driver1.setChecked(false);
				driver4.setChecked(false);
				selecteddriverbehaviour = 3;
			}
		});
		driver4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				driver4.setChecked(true);
				driver2.setChecked(false);
				driver1.setChecked(false);
				driver3.setChecked(false);
				selecteddriverbehaviour = 4;
			}
		});
		time1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				time1.setChecked(true);
				time2.setChecked(false);
				time3.setChecked(false);
				time4.setChecked(false);
				selectedtraveltime = 1;
			}
		});
		time2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				time2.setChecked(true);
				time1.setChecked(false);
				time3.setChecked(false);
				time4.setChecked(false);
				selectedtraveltime = 2;
			}
		});
		time3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				time3.setChecked(true);
				time2.setChecked(false);
				time1.setChecked(false);
				time4.setChecked(false);
				selectedtraveltime = 3;
			}
		});
		time4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				time4.setChecked(true);
				time2.setChecked(false);
				time3.setChecked(false);
				time1.setChecked(false);
				selectedtraveltime = 4;
			}
		});

		overall1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				overall1.setChecked(true);
				overall2.setChecked(false);
				overall3.setChecked(false);
				overall4.setChecked(false);
				selectedoverallexp = 1;
			}
		});

		overall2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				overall2.setChecked(true);
				overall1.setChecked(false);
				overall3.setChecked(false);
				overall4.setChecked(false);
				selectedoverallexp = 2;
			}
		});

		overall3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				overall3.setChecked(true);
				overall2.setChecked(false);
				overall1.setChecked(false);
				overall4.setChecked(false);
				selectedoverallexp = 3;
			}
		});

		overall4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				overall4.setChecked(true);
				overall2.setChecked(false);
				overall3.setChecked(false);
				overall1.setChecked(false);
				selectedoverallexp = 4;
			}
		});

		feedbackdone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vehile1.isChecked() == false
						&& vehile2.isChecked() == false
						&& vehile3.isChecked() == false
						&& vehile4.isChecked() == false) {
					Toast.makeText(getBaseContext(),
							"Select from Vehicle Condition", Toast.LENGTH_SHORT)
							.show();
				} else if (driver1.isChecked() == false
						&& driver2.isChecked() == false
						&& driver3.isChecked() == false
						&& driver4.isChecked() == false) {
					Toast.makeText(getBaseContext(),
							"Select from Driver Behaviour", Toast.LENGTH_SHORT)
							.show();
				} else if (time1.isChecked() == false
						&& time2.isChecked() == false
						&& time3.isChecked() == false
						&& time4.isChecked() == false) {
					Toast.makeText(getBaseContext(), "Select from Travel Time",
							Toast.LENGTH_SHORT).show();
				} else if (overall1.isChecked() == false
						&& overall2.isChecked() == false
						&& overall3.isChecked() == false
						&& overall4.isChecked() == false) {
					Toast.makeText(getBaseContext(),
							"Select from Overall Experience",
							Toast.LENGTH_SHORT).show();
				}

				else {

					try {
						JSONObject jobj = new JSONObject();
						Spinner logTypespinner = (Spinner) findViewById(R.id.feedlogspinner);
						Spinner datespinner = (Spinner) findViewById(R.id.feeddatespinner);
						String logType = logTypespinner.getSelectedItem()
								.toString();
						String date1 = datespinner.getSelectedItem().toString();



						String macaddress= GetMacAddress.getMacAddr();
						jobj.put("ACTION", "EMP_FEEDBACK");
						EditText othercmts = (EditText) findViewById(R.id.othercommentseditText1);
						jobj.put("IMEI", android_id);
						jobj.put("DATE", date1);
						jobj.put("LOGTYPE", logType);
						jobj.put("VC", selectedvehiclecondition + "");
						jobj.put("DRIVER", selecteddriverbehaviour + "");
						jobj.put("TRAVEL", selectedtraveltime + "");
						jobj.put("OVERALL", selectedoverallexp + "");
						jobj.put("OTHER", othercmts.getText().toString());
						JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									Log.d("feedback****",""+response.toString());
									if (response.getString("RESULT").equalsIgnoreCase("TRUE")) {
										if (mdialog != null && mdialog.isShowing()) {
											mdialog.dismiss();
										}
										new PromptDialog.Builder(FeedBackActivity.this)
												.setTitle("Feedback")
												.setCanceledOnTouchOutside(false)
												.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
												.setButton1TextColor(R.color.md_blue_400)

												.setMessage(""+response.getString("STATUS"))
												.setButton1("OK", new PromptDialog.OnClickListener() {

													@Override
													public void onClick(Dialog dialog, int which) {
														dialog.dismiss();
														finish();
													}
												})
												.show();


									} else {
										if (mdialog != null && mdialog.isShowing()) {
											mdialog.dismiss();
										}


										new PromptDialog.Builder(FeedBackActivity.this)
												.setTitle("Feedback")
												.setCanceledOnTouchOutside(false)
												.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
												.setButton1TextColor(R.color.md_blue_400)

												.setMessage(""+response.getString("STATUS"))
												.setButton1("OK", new PromptDialog.OnClickListener() {

													@Override
													public void onClick(Dialog dialog, int which) {
														dialog.dismiss();
														finish();
													}
												})
												.show();

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
								Toast.makeText(getApplicationContext(), "Error while communicating", Toast.LENGTH_LONG).show();
								if (mdialog != null && mdialog.isShowing()) {
									mdialog.dismiss();
								}

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
//										if (mdialog != null
//												&& mdialog.isShowing()) {
//											mdialog.dismiss();
//										}
//
//										FeedBackActivity.this.finish();
//										Toast.makeText(getApplicationContext(),
//												robj.getString("STATUS"),
//												Toast.LENGTH_LONG).show();
//										onBackPressed();
//
//									} else {
//										if (mdialog != null
//												&& mdialog.isShowing()) {
//											mdialog.dismiss();
//										}
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
//								if (mdialog != null && mdialog.isShowing()) {
//									mdialog.dismiss();
//								}
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

	}

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
	
	 public void onBackPressed()
		{

			 super.onBackPressed();
//			Intent intent = new Intent(FeedBackActivity.this, MapClass.class);
//			startActivity(intent);
			//System.exit(1);
	  //  	Toast.makeText(getApplicationContext(), "in start", Toast.LENGTH_LONG).show();
	    	//finish();
		}
	
}
	/* @Override
	    public void onBackPressed() {
	       
	            super.onBackPressed();

	           
	                Intent intent = new Intent(this, MapClass.class);
	                finish();
	                startActivity(intent);
	           
	            }
	        }
	    }
*/
	// else
	// {
	// Toast.makeText(getBaseContext(),"Thank You For Your Feedback",Toast.LENGTH_SHORT).show();
	// finish();
	// comment.setText("Vehicle -"+
	// selectedvehiclecondition+" Driver -"+selecteddriverbehaviour+" Time -"+selectedtraveltime+
	// " OverAll -"+selectedoverallexp);
	// }

// hide server
// });
// }
// }
// hide server

