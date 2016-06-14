package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.activities.Booking;
import com.agiledge.keocometemployee.utilities.DateModify;

@SuppressLint("ResourceAsColor")
public class ManageBookingActivity extends AppCompatActivity {
	
	

	public static TextView textView;
	ImageView modify,booking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.managebookingnew);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		modify=(ImageView)findViewById(R.id.modify);
		 booking=(ImageView) findViewById(R.id.booking);

	        
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
			 startActivity(in);
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
		    	
			}}
