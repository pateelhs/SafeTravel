package com.agiledge.keocometemployee.navdrawermenu;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;

@SuppressLint("ResourceAsColor")
public class AboutActivity extends Activity {

	public static TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		textView =(TextView) findViewById(R.id.textView_version);
		try {
			String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			textView.setText("Version: "+version);
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("tag", e.getMessage());
		}
		
	
		
	}

	
	
}
