package com.agiledge.keocometemployee.navdrawermenu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
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
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cengalabs.flatui.FlatUI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdocDetails extends AppCompatActivity {
	private final int APP_THEME = R.array.snow;
	private CoordinatorLayout coordinatorLayout;
	private TransparentProgressDialog pd;
	private String date="";
	private String scheduleid="",msg="";
	String android_id="";
	SweetAlertDialog pDialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FlatUI.initDefaultValues(this);
		FlatUI.setDefaultTheme(FlatUI.BLOOD);
		setContentView(R.layout.modify_new);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.top_band));
		//getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Live_tracking) + "</font>")));
		Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
		SpannableStringBuilder SS = new SpannableStringBuilder("Modify Booking");
		SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		getSupportActionBar().setTitle(SS);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
		pDialog.setTitleText("Please wait...");
		pDialog.setCancelable(false);
		Spinner timespnr=(Spinner) findViewById(R.id.adhoctimespinner);
		pd = new TransparentProgressDialog(this, R.drawable.loading);
		android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		pDialog.show();
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
				pDialog.show();
				book();


			}
		});


	}

	public void fillvalues() {
		try {

			JSONObject jobj = new JSONObject();
			jobj.put("ACTION", "MODIFY_CHECK");
			jobj.put("IMEI",CommenSettings.android_id);
			jobj.put("DATE",date);
			final Spinner logoutspinner = (Spinner) findViewById(R.id.adhoctimespinner);
			JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
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
							pDialog.dismiss();

						} else {

							Toast.makeText(getApplicationContext(), ""+response.getString("MESSAGE"), Toast.LENGTH_LONG).show();
							finish();
							pDialog.dismiss();
						}


					} catch (Exception e) {
						pDialog.dismiss();
						AppController.getInstance().trackException(e);
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					pDialog.dismiss();
					Snackbar snackbar = Snackbar
							.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
							.setAction("RETRY", new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									fillvalues();
								}
							});

					// Changing message text color
					snackbar.setActionTextColor(Color.RED);

					// Changing action button text color
					View sbView = snackbar.getView();
					TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.YELLOW);
					pDialog.dismiss();
					snackbar.show();


				}
			});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(req);
		} catch (Exception e) {
			pDialog.dismiss();
			e.printStackTrace();
		}
		//  AppController.getInstance().trackScreenView("Login");// for Google analytics data

	}

	public void book() {
		try{

			Spinner time=(Spinner) findViewById(R.id.adhoctimespinner);
			TextView txtdate=(TextView) findViewById(R.id.adhocdatevalue);
			boolean valid=true;

			if(valid) {
				JSONObject jobj = new JSONObject();
				jobj.put("ACTION", "SCHEDULE_ALTER");
				jobj.put("SCHEDULE_ID",scheduleid);
				jobj.put("IMEI", CommenSettings.android_id);
				jobj.put("DATE", txtdate.getText().toString());
				jobj.put("LOG_OUT", time.getSelectedItem().toString());
					Log.d("alter request",""+jobj.toString());
				JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("RESPONSE** ALTER",""+response.toString());
						try {

							msg=response.getString("STATUS");
							pDialog.dismiss();
							{
//								new PromptDialog.Builder(AdocDetails.this)
//										.setTitle("Modify")
//										.setCanceledOnTouchOutside(false)
//										.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
//										.setButton1TextColor(R.color.md_blue_400)
//
//										.setMessage(""+response.getString("STATUS"))
//
//										.setButton1("OK", new PromptDialog.OnClickListener() {
//
//											@Override
//											public void onClick(Dialog dialog, int which) {
//												dialog.dismiss();
//												Intent manage= new Intent(AdocDetails.this,ManageBookingActivity.class);
//												startActivityForResult(manage, 0);
//												finish();
//											}
//										})
//										.show();
								success();
							}

//								pd.dismiss();
//								Toast.makeText(getApplicationContext(), ""+response.getString("STATUS"), Toast.LENGTH_SHORT).show();
//								finish();



						} catch (Exception e) {
							pDialog.dismiss();
							AppController.getInstance().trackException(e);
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pDialog.dismiss();
						Snackbar snackbar = Snackbar
								.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
								.setAction("RETRY", new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										book();
									}
								});

						// Changing message text color
						snackbar.setActionTextColor(Color.RED);

						// Changing action button text color
						View sbView = snackbar.getView();
						TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
						textView.setTextColor(Color.YELLOW);
						pDialog.dismiss();
						snackbar.show();

					}
				});

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(req);
			}else{
				pDialog.dismiss();
			}
		} catch (Exception e) {
			pDialog.dismiss();
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void alert() {
		//pd.dismiss();
		new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setTitleText("Oops...")
				.setContentText(msg)
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						finish();
					}
				})
				.show();
		pDialog.dismiss();


	}

	public void success() {
		pDialog.dismiss();
		new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText("Success :)")
				.setContentText(msg)
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						//pDialog.dismiss();
						sweetAlertDialog.dismiss();
						finish();
					}
				})
				.show();



	}
}
