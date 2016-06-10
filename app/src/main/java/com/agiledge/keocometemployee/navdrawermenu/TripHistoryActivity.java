package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.agiledge.keocometemployee.R;

@SuppressLint("ResourceAsColor")
public class TripHistoryActivity extends Activity {

	ListView lv;
	Context context;

	

	
//    public static String [] thdate={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
//    public static String [] thdistance={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
//    public static String [] thlocation={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
//	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_history);
		
/*		  context=this;

//	        lv=(ListView) findViewById(R.id.ListViewTripHistory);
//	        lv.setAdapter(new TripHistoryCustomAdapter(this,thdate,thdistance,thlocation));
		
		String[] triphdateList =null;
		String[] triphdistanceList = null;
		String[] triphlocationList = null;
		
		context = this;

		lv = (ListView) findViewById(R.id.ListViewTripHistory);
		lv.setAdapter(new TripHistoryCustomAdapter(this, triphdateList,
				triphdistanceList, triphlocationList));

		try {


			JSONObject jobj = new JSONObject();
			TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tel.getDeviceId().toString();

			jobj.put("ACTION", "TRIP_HISTORY");
			jobj.put("IMEI", imei);

			ServerCommunication sobj = new ServerCommunication(jobj);
			sobj.setDataDownloadListen(new DataDownloadListener() {
				public void dataSuccess(String result) {
					try {
						Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
						if (result != null && !result.equalsIgnoreCase("")) {
						JSONObject robj;
							robj = new JSONObject(result);
					
							if (robj != null) {
								
							
	        
								
								
								
                      JSONArray  tripdate = robj.getJSONArray("tripdate");
                      JSONArray  distance = robj.getJSONArray("distance");
                     JSONArray  area = robj.getJSONArray("area");
                     for (int i = 0; i < tripdate.length(); i++) {
							
                    	 triphdateList[i] =tripdate.get(i).toString();
                 		 triphdistanceList[i] = distance.get(i).toString();
                 		 triphlocationList[i] = area.get(i).toString();
                    	 
                    	 
                    	 
                    	 
							}
						}

						else {
							Toast.makeText(getApplicationContext(),
									" No Internet", Toast.LENGTH_SHORT).show();
						
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				public void datafail() {

				Toast.makeText(getApplicationContext(),
							"oops communication error", Toast.LENGTH_SHORT)
							.show();
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
*/	}  

}