package com.agiledge.keocometemployee.navdrawermenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdhocActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adhoc);
		CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView3);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			// @Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				Intent k = new Intent(AdhocActivity.this, AdocDetails.class);
				Calendar validDate = Calendar.getInstance();
				validDate.set(year, month, dayOfMonth);
				SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
				Calendar curday=Calendar.getInstance();
				String dateString = sdfr.format( validDate.getTime() );
				k.putExtra("CLICKDATE",dateString);
				if (curday.after(validDate)) {
				    Toast.makeText(getApplicationContext(), "Please select date after today's!", Toast.LENGTH_LONG).show();
				}
				else {
				startActivity(k);
			}
			}

		});
	}
}
