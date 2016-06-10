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
		
		// cancel=(ImageButton)findViewById(R.id.cancel1);
		modify=(ImageView)findViewById(R.id.modify);
		 booking=(ImageView) findViewById(R.id.booking);
		// adhoc=(ImageButton)findViewById(R.id.adhoc4);
		 
			
		
			
			
//	        cancel.setOnClickListener(new OnClickListener()
//	        {
//
//				@Override
//				public void onClick(View v) {
//					Intent in = new Intent(getApplicationContext(),
//							CancelActivity.class);
//
					
//	cancel	
//					Bundle extras = getIntent().getExtras();
//					if (extras != null) {
//					    in.putExtra("FROM_DATE", extras.getString("FROM_DATE"));
//						in.putExtra("TO_DATE", extras.getString("TO_DATE"));
//						in.putExtra("LOG_IN", extras.getString("LOG_IN"));
//						in.putExtra("LOG_OUT", extras.getString("LOG_OUT"));
//						in.putExtra("ALTIMES", extras.getString("ALTIMES"));
//						int  times=0;
//						if(extras.getString("ALTIMES")!=null){
//						times=Integer.parseInt(extras.getString("ALTIMES")+"");
//						}
//						for(int i=1;i<=times;i++)
//						{
//							in.putExtra("ALTERDATE"+i, extras.getString("ALTERDATE"+i) );
//
//							in.putExtra("AL_LOG_IN"+i,extras.getString("AL_LOG_IN"+i) );
//							in.putExtra("AL_LOG_OUT"+i,extras.getString("AL_LOG_OUT"+i) );
//						}}
//	cancel				
					
//					 startActivity(in);
//	}
//
//	        });
	        
 modify.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent in = new Intent(getApplicationContext(),
							DateModify.class);
//					Bundle extras = getIntent().getExtras();
//					if (extras != null) {
//					    in.putExtra("FROM_DATE", extras.getString("FROM_DATE"));
//						in.putExtra("TO_DATE", extras.getString("TO_DATE"));
//						in.putExtra("LOG_IN", extras.getString("LOG_IN"));
//						in.putExtra("LOG_OUT", extras.getString("LOG_OUT"));
//						in.putExtra("ALTIMES", extras.getString("ALTIMES"));
//						in.putExtra("IN_LOGS_COUNT",extras.getString("IN_LOGS_COUNT"));
//						in.putExtra("OUT_LOGS_COUNT",extras.getString("OUT_LOGS_COUNT"));
//						int incount1=Integer.parseInt(extras.getString("IN_LOGS_COUNT"));
//						int outcount1=Integer.parseInt(extras.getString("OUT_LOGS_COUNT"));
//						for (int i = 1; i <= incount1; i++) {
//							in.putExtra("IN_LOGS"+i, extras.getString("IN_LOGS"+i) );
//						}
//						for (int i = 1; i <= outcount1; i++) {
//							in.putExtra("OUT_LOGS"+i, extras.getString("OUT_LOGS"+i) );
//						}
//
//						int  times=0;
//						if(extras.getString("ALTIMES")!=null){
//						times=Integer.parseInt(extras.getString("ALTIMES")+"");
//					//	Toast.makeText(getApplicationContext(),"SAN-"+times , Toast.LENGTH_LONG).show();
//						}
//						for(int i=1;i<=times;i++)
//						{
//							in.putExtra("ALTERDATE"+i, extras.getString("ALTERDATE"+i) );
//
//							in.putExtra("AL_LOG_IN"+i,extras.getString("AL_LOG_IN"+i) );
//							in.putExtra("AL_LOG_OUT"+i,extras.getString("AL_LOG_OUT"+i) );
//						}
//						}
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


//		booking.setOnClickListener(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent in = new Intent(getApplicationContext(),
//					Booking.class);
////			Bundle extras = getIntent().getExtras();
////			if (extras != null) {
////			    in.putExtra("FROM_DATE", extras.getString("FROM_DATE"));
////				in.putExtra("TO_DATE", extras.getString("TO_DATE"));
////				in.putExtra("LOG_IN", extras.getString("LOG_IN"));
////				in.putExtra("LOG_OUT", extras.getString("LOG_OUT"));
////				in.putExtra("ALTIMES", extras.getString("ALTIMES"));
////				in.putExtra("IN_LOGS_COUNT",extras.getString("IN_LOGS_COUNT"));
////				in.putExtra("OUT_LOGS_COUNT",extras.getString("OUT_LOGS_COUNT"));
////				int incount1=Integer.parseInt(extras.getString("IN_LOGS_COUNT"));
////				int outcount1=Integer.parseInt(extras.getString("OUT_LOGS_COUNT"));
////				for (int i = 1; i <= incount1; i++) {
////					in.putExtra("IN_LOGS"+i, extras.getString("IN_LOGS"+i) );
////				}
////				for (int i = 1; i <= outcount1; i++) {
////					in.putExtra("OUT_LOGS"+i, extras.getString("OUT_LOGS"+i) );
////				}
////
////				int  times=0;
////				if(extras.getString("ALTIMES")!=null){
////				times=Integer.parseInt(extras.getString("ALTIMES")+"");
////				}
////				for(int i=1;i<=times;i++)
////				{
////					in.putExtra("ALTERDATE"+i, extras.getString("ALTERDATE"+i) );
////
////					in.putExtra("AL_LOG_IN"+i,extras.getString("AL_LOG_IN"+i) );
////					in.putExtra("AL_LOG_OUT"+i,extras.getString("AL_LOG_OUT"+i) );
////				}
////				in.putExtra("ADHOCTYPECOUNT", extras.getString("ADHOCTYPECOUNT"));
////				for(int i=1;i<=Integer.parseInt(extras.getString("ADHOCTYPECOUNT")+"");i++)
////				{
////					in.putExtra("ADHOCTYPE"+i,extras.getString("ADHOCTYPE"+i) );
////
////				}
////				}
//			 startActivity(in);
//}
//
//});
	
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
//				Intent intent = new Intent(ManageBookingActivity.this, MapClass.class);
//				startActivity(intent);
//				finish();
				//System.exit(1);
		  //  	Toast.makeText(getApplicationContext(), "in start", Toast.LENGTH_LONG).show();
		    	
			}}
