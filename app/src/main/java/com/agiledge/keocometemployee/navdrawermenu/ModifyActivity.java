package com.agiledge.keocometemployee.navdrawermenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

import com.agiledge.keocometemployee.R;

public class ModifyActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify);
	
		CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView2);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			// @Override
		        public void onSelectedDayChange(CalendarView view, int year, int month,
		                int dayOfMonth) {
		            Intent k = new Intent(ModifyActivity.this, ModifyDetails.class);
//		            k.putExtra("godina", year);
//		            k.putExtra("mesec", month);
//		            k.putExtra("dan", dayOfMonth);
		            
		            Bundle extras = getIntent().getExtras();
					if (extras != null) {
					    k.putExtra("FROM_DATE", extras.getString("FROM_DATE"));
						k.putExtra("TO_DATE", extras.getString("TO_DATE"));
						k.putExtra("LOG_IN", extras.getString("LOG_IN"));
						k.putExtra("LOG_OUT", extras.getString("LOG_OUT"));
						k.putExtra("ALTIMES", extras.getString("ALTIMES"));
						k.putExtra("IN_LOGS_COUNT",extras.getString("IN_LOGS_COUNT"));
						k.putExtra("OUT_LOGS_COUNT",extras.getString("OUT_LOGS_COUNT"));
						int incount1=Integer.parseInt(extras.getString("IN_LOGS_COUNT"));
						int outcount1=Integer.parseInt(extras.getString("OUT_LOGS_COUNT"));
						for (int i = 1; i <= incount1; i++) {
							k.putExtra("IN_LOGS"+i, extras.getString("IN_LOGS"+i) );
						}
						for (int i = 1; i <= outcount1; i++) {
							k.putExtra("OUT_LOGS"+i, extras.getString("OUT_LOGS"+i) );
						}
						
						int  times=0;
						//Toast.makeText(getApplicationContext(), extras.getString("ALTIMES")+"", Toast.LENGTH_LONG).show();
						if(extras.getString("ALTIMES")!=null){
						times=Integer.parseInt(extras.getString("ALTIMES")+"");
						}
						for(int i=1;i<=times;i++)
						{
							k.putExtra("ALTERDATE"+i, extras.getString("ALTERDATE"+i) );
							 
							k.putExtra("AL_LOG_IN"+i,extras.getString("AL_LOG_IN"+i) );
							k.putExtra("AL_LOG_OUT"+i,extras.getString("AL_LOG_OUT"+i) );
						}
					}
					//sandesh
					
					String[] fromDate=extras.getString("FROM_DATE").split("-");
					String[] toDate=extras.getString("TO_DATE").split("-");
					Calendar validDate = Calendar.getInstance();
					validDate.set(year, month, dayOfMonth);
					SimpleDateFormat sdfr = new SimpleDateFormat("dd-MMM-yyyy");
					String dateString = sdfr.format( validDate.getTime() );
					k.putExtra("CLICKDATE1",dateString);
					SimpleDateFormat sdfr1 = new SimpleDateFormat("yyyy-MM-dd");
					String date1 = sdfr1.format( validDate.getTime() );
					k.putExtra("CLICKDATE",date1);
					Calendar from = Calendar.getInstance();
					from.set(Integer.parseInt(fromDate[0]), Integer.parseInt(fromDate[1])-1,Integer.parseInt( fromDate[2])-1);
					Calendar to = Calendar.getInstance();
					to.set(Integer.parseInt(toDate[0]), Integer.parseInt(toDate[1])-1,Integer.parseInt( toDate[2]));
					if (from.after(validDate)) {
					    Toast.makeText(getApplicationContext(), "You are not scheduled on this date", Toast.LENGTH_LONG).show();
					}else if (validDate.after(to)) {
					    Toast.makeText(getApplicationContext(), "You are not scheduled on this date", Toast.LENGTH_LONG).show();
					}else{
		            
		            
		            
		            startActivity(k);
		    }
		};
});
	
	}}
