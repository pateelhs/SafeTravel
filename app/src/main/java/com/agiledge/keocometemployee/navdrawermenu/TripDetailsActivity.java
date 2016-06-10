package com.agiledge.keocometemployee.navdrawermenu;

import com.agiledge.keocometemployee.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class TripDetailsActivity extends Activity {

	public static TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripdetails);
		TextView regno=(TextView) findViewById(R.id.td2);
		TextView dname=(TextView) findViewById(R.id.td4);
		TextView dcontct=(TextView) findViewById(R.id.td6);
		TextView tcode=(TextView) findViewById(R.id.td8);
		TextView econt=(TextView) findViewById(R.id.td10);
		imageclick();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		     tcode.setText(extras.getString("TRIP_CODE"));
			 regno.setText(extras.getString("REG_NO"));
			dname.setText(extras.getString("DRIVER_NAME"));
			dcontct.setText(extras.getString("DRIVER_CONTACT"));
			if(extras.getString("SECURITY").equalsIgnoreCase("YES"))
			{
				econt.setText(extras.getString("ESCORT_CONTACT"));
			}
		}
	
	
		
	}
	
	
	public void imageclick()
	{
		ImageView imageview = (ImageView) findViewById(R.id.imageView1);
		imageview.setOnClickListener(new OnClickListener() {

	    		public void onClick(View v) {
	    			finish();
	    			//onBackPressed();
	    }
		 });
	}
}
