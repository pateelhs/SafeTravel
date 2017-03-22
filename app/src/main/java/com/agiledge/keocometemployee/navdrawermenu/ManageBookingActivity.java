package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.Shuttlebook.MapsActivity;
import com.agiledge.keocometemployee.activities.Booking;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.agiledge.keocometemployee.utilities.DateModify;
import com.agiledge.keocometemployee.utilities.PromptDialog;

@SuppressLint("ResourceAsColor")
public class ManageBookingActivity extends AppCompatActivity {
	
	

	public static TextView textView;
	ImageView modify,booking,adhoc;
	String user_type="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.managebookingnew);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.top_band));
		//getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Live_tracking) + "</font>")));
		Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
		SpannableStringBuilder SS = new SpannableStringBuilder("Manage Booking");
		SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		getSupportActionBar().setTitle(SS);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		modify=(ImageView)findViewById(R.id.modify);
		 booking=(ImageView) findViewById(R.id.booking);
		adhoc=(ImageView)findViewById(R.id.adhoc);
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			user_type=extras.getString("user_type");
			Log.d("user_type from main",user_type.toString());
		}

	        
 modify.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent in = new Intent(getApplicationContext(),
							DateModify.class);

				 startActivity(in);
	}

	        });
 
 booking.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent in = new Intent(getApplicationContext(),
					Booking.class);
			in.putExtra("user_type",user_type);
			 startActivity(in);
}

 });

		adhoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isConnected()) {
					Intent in = new Intent(getApplicationContext(),
							MapsActivity.class);
					startActivity(in);
				}
				else{
					new PromptDialog.Builder(ManageBookingActivity.this)
							.setTitle("No Internet")
							.setCanceledOnTouchOutside(false)
							.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
							.setButton1TextColor(R.color.md_blue_400)

							.setMessage("No Internet Connection")

							.setButton1("OK", new PromptDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog, int which) {
									dialog.dismiss();
									//finish();
								}
							})
							.show();
				}
			}

		});
	
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
	  public void onBackPressed()
			{

				super.onBackPressed();
		    	
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
