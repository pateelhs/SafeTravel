package com.agiledge.keocometemployee.navdrawermenu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.app.AppController;
import com.agiledge.keocometemployee.constants.CommenSettings;
import com.agiledge.keocometemployee.utilities.CustomTypefaceSpan;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

@SuppressLint("ResourceAsColor")
public class EmergencyContactActivity extends AppCompatActivity{

	public static TextView textView;
	String PersonName1, PersonMob1,PersonName2, PersonMob2;
	EditText edname1,ednumber1,edname2,ednumber2;
	private ProgressDialog mdialog;
	String empid="";
	ImageView back;
	Typeface type;
	@Override
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergencycontact);
	//	showDialog(0);

		Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/AvantGarde Md BT.ttf");
		SpannableStringBuilder SS = new SpannableStringBuilder("Emergency Contacts");
		SS.setSpan (new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		getSupportActionBar().setTitle(SS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_band));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		back=(ImageView) findViewById(R.id.imageView1);
		edname1 = (EditText) findViewById(R.id.ename1);
		ednumber1 = (EditText) findViewById(R.id.enumber1);
		edname2 = (EditText) findViewById(R.id.ename2);
		ednumber2 = (EditText) findViewById(R.id.enumber2);

		type = Typeface.createFromAsset(getAssets(),"fonts/AvantGarde Md BT.ttf");
		edname1.setTypeface(type);
		ednumber1.setTypeface(type);
		edname2.setTypeface(type);
		ednumber2.setTypeface(type);

		ButtonClick();
		ButtonSave();
		Bundle extras = getIntent().getExtras();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		if (extras != null) {
		    empid = extras.getString("empid");
		}
		try{
		JSONObject jobj=new JSONObject();
		jobj.put("ACTION", "ICE_DATA");
		jobj.put("EMP_ID",CommenSettings.empid);
			JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {Log.d("Emergencey con",""+response.toString());
						if (response.getString("result").equalsIgnoreCase("TRUE")) {

							if (mdialog != null && mdialog.isShowing()) {
								mdialog.dismiss();
							}
							EditText edname1 = (EditText) findViewById(R.id.ename1);
							EditText ednumber1 = (EditText) findViewById(R.id.enumber1);
							EditText edemail1 = (EditText) findViewById(R.id.eemail1);
							EditText edname2 = (EditText) findViewById(R.id.ename2);
							EditText ednumber2 = (EditText) findViewById(R.id.enumber2);
							EditText edemail2 = (EditText) findViewById(R.id.eemail2);
							edname1.setText(response.getString("CONTACT_NAME1"));
							ednumber1.setText(response.getString("CONTACT_NUMBER1"));
							edemail1.setText(response.getString("CONTACT_EMAIL1"));
							edname2.setText(response.getString("CONTACT_NAME2"));
							ednumber2.setText(response.getString("CONTACT_NUMBER2"));
							edemail2.setText(response.getString("CONTACT_EMAIL2"));

							edname1.setTypeface(type);
							ednumber1.setTypeface(type);
							edemail1.setTypeface(type);
							edname2.setTypeface(type);
							edemail2.setTypeface(type);
							ednumber2.setTypeface(type);

						} else {
							if (mdialog != null && mdialog.isShowing()) {
								mdialog.dismiss();
							}
							//Toast.makeText(getApplicationContext(), "Contact Details not found please fill in!", Toast.LENGTH_LONG).show();
							LayoutInflater inflater = getLayoutInflater();
							View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
							TextView msg=(TextView) toastLayout.findViewById(R.id.custom_toast_message);
							msg.setText("Contact Details not found please fill in!");

							Toast toast = new Toast(getApplicationContext());
							toast.setDuration(Toast.LENGTH_LONG);
							toast.setView(toastLayout);
							toast.show();

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
					if (mdialog != null && mdialog.isShowing()) {
						mdialog.dismiss();
					}
					//Toast.makeText(getApplicationContext(), "Error while communicating", Toast.LENGTH_LONG).show();
					LayoutInflater inflater = getLayoutInflater();
					View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
					TextView msg=(TextView) toastLayout.findViewById(R.id.custom_toast_message);
					msg.setText("Ooops! That was an Error");

					Toast toast = new Toast(getApplicationContext());
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(toastLayout);
					toast.show();

				}
			});

			// Adding request to request queue
		AppController.getInstance().addToRequestQueue(req);
//		ServerCommunication sobj=new ServerCommunication(jobj);
//		sobj.setDataDownloadListen(new DataDownloadListener()
//		{
//			public void dataSuccess(String result)
//			{
//				try {
//					if(result!=null&&!result.equalsIgnoreCase(""))
//					{
//					JSONObject robj;
//					robj = new JSONObject(result);
//
//				if(robj!=null && robj.getString("result").equalsIgnoreCase("true"))
//				{
//    				if(mdialog != null && mdialog.isShowing()){
//    	        		mdialog.dismiss();
//    	        	}
//				{
//					EditText edname1 = (EditText) findViewById(R.id.ename1);
//					EditText ednumber1 = (EditText) findViewById(R.id.enumber1);
//					EditText edemail1 = (EditText) findViewById(R.id.eemail1);
//					EditText edname2 = (EditText) findViewById(R.id.ename2);
//					EditText ednumber2 = (EditText) findViewById(R.id.enumber2);
//					EditText edemail2 = (EditText) findViewById(R.id.eemail2);
//					edname1.setText(robj.getString("CONTACT_NAME1"));
//					ednumber1.setText(robj.getString("CONTACT_NUMBER1"));
//					edemail1.setText(robj.getString("CONTACT_EMAIL1"));
//					edname2.setText(robj.getString("CONTACT_NAME2"));
//					ednumber2.setText(robj.getString("CONTACT_NUMBER2"));
//					edemail2.setText(robj.getString("CONTACT_EMAIL2"));
//
//				}}
//				else if(robj!=null && robj.getString("result").equalsIgnoreCase("false"))
//				{
//    				if(mdialog != null && mdialog.isShowing()){
//    	        		mdialog.dismiss();
//    	        	}
//
//				//	Toast.makeText(getApplicationContext(), "Opertaion Failed!", Toast.LENGTH_LONG).show();
//
//				}
//				}
//					else
//						{
//		    				if(mdialog != null && mdialog.isShowing()){
//		    	        		mdialog.dismiss();
//		    	        	}
//						Toast.makeText(getApplicationContext(), "Oops Error In Communication", Toast.LENGTH_SHORT).show();
//
//					}
//
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			public void datafail()
//				{
//    				if(mdialog != null && mdialog.isShowing()){
//    	        		mdialog.dismiss();
//    	        	}
//				Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
//			}
//		});
//		sobj.execute();

}catch(Exception e){e.printStackTrace();}
		
	
		
	}

	public void ButtonClick()
	{
		Button closebtn = (Button) findViewById(R.id.closebtn);
		closebtn.setOnClickListener(new OnClickListener() {

	    		public void onClick(View v) {
	    			finish();
	    			
	    }
		 });
	}
	
	
	public void ButtonSave()
	{
		Button savebtn = (Button) findViewById(R.id.savebtn);
		savebtn.setOnClickListener(new OnClickListener() {

	    		public void onClick(View v) {
	    			
	    			PersonName1 = edname1.getText().toString();
	    			PersonMob1 = ednumber1.getText().toString();
	    			PersonName2 = edname2.getText().toString();
	    			PersonMob2 = ednumber2.getText().toString();
					boolean flag = false;

					if (PersonName1.length() == 0)
					{
	    				if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
						edname1.setError("Enter Person Name1?");
						flag = true;

					}

					else if (PersonMob1.length() == 0)
						{if(mdialog != null && mdialog.isShowing()){
	    	        		mdialog.dismiss();
	    	        	}
						ednumber1.setError("Enter Mobile Number?");flag = true;}
					else if (PersonMob1.length() < 10)
					{if(mdialog != null && mdialog.isShowing()){
    	        		mdialog.dismiss();
    	        	}
						ednumber1.setError("Mobile Number should be Minimum 10 digits?");flag = true;}
					else if (PersonMob1.length() > 10)
					{if(mdialog != null && mdialog.isShowing()){
    	        		mdialog.dismiss();
    	        	}
						ednumber1.setError("Mobile Number should be Maximum 10 digits?");flag = true;}
					else if( PersonName2.length() != 0 || PersonMob2.length() != 0)
					{
						if( PersonName2.length() == 0){
						if(mdialog != null && mdialog.isShowing()){
    	        		mdialog.dismiss();
    	        	}
						edname2.setError("Enter Person Name2?");flag = true;}
					else if (PersonMob2.length() == 0)
					{if(mdialog != null && mdialog.isShowing()){
    	        		mdialog.dismiss();
    	        	}
						ednumber2.setError("Enter Mobile Number?");flag = true;}
					else if (PersonMob2.length() < 10)
					{if(mdialog != null && mdialog.isShowing()){
    	        		mdialog.dismiss();
    	        	}
						ednumber2.setError("Mobile Number should be Minimum 10 digits?");flag = true;}
					else if (PersonMob2.length() > 10)
					{if(mdialog != null && mdialog.isShowing()){
    	        		mdialog.dismiss();
    	        	}
						ednumber2.setError("Mobile Number should be Maximum 10 digits?");flag = true;}}
					//else
					if(!flag)
					{

	    			try{
	    			JSONObject jobj=new JSONObject();
	    			jobj.put("ACTION", "ICE_REGISTER");
	    			jobj.put("EMP_ID",empid);
	    			EditText edname1 = (EditText) findViewById(R.id.ename1);
	    			EditText ednumber1 = (EditText) findViewById(R.id.enumber1);
	    			EditText edemail1 = (EditText) findViewById(R.id.eemail1);
	    			EditText edname2 = (EditText) findViewById(R.id.ename2);
	    			EditText ednumber2 = (EditText) findViewById(R.id.enumber2);
	    			EditText edemail2 = (EditText) findViewById(R.id.eemail2);
	    			jobj.put("CONTACT_NAME1", edname1.getText().toString());
	    			jobj.put("CONTACT_NUMBER1", ednumber1.getText().toString());
	    			jobj.put("CONTACT_EMAIL1", edemail1.getText().toString());
	    			jobj.put("CONTACT_NAME2", edname2.getText().toString());
	    			jobj.put("CONTACT_NUMBER2", ednumber2.getText().toString());
	    			jobj.put("CONTACT_EMAIL2", edemail2.getText().toString());
						JsonObjectRequest req = new JsonObjectRequest(CommenSettings.serverAddress_wemp, jobj, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getString("result").equalsIgnoreCase("TRUE")) {
										if (mdialog != null && mdialog.isShowing()) {
											mdialog.dismiss();
										}

										Toast.makeText(getApplicationContext(), "Emergency Contact Details Saved", Toast.LENGTH_LONG).show();
										finish();
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
								Toast.makeText(getApplicationContext(), "Error while communicating" + error.getMessage(), Toast.LENGTH_LONG).show();
								if (mdialog != null && mdialog.isShowing()) {
									mdialog.dismiss();
								}

							}
						});

						// Adding request to request queue
						AppController.getInstance().addToRequestQueue(req);
//	    			ServerCommunication sobj=new ServerCommunication(jobj);
//	    			sobj.setDataDownloadListen(new DataDownloadListener()
//	    			{
//	    				public void dataSuccess(String result)
//	    				{
//	    					try {
//	    						if(result!=null&&!result.equalsIgnoreCase(""))
//	    						{
//	    						JSONObject robj;
//	    						robj = new JSONObject(result);
//
//	    					if(robj!=null && robj.getString("result").equalsIgnoreCase("true"))
//	    					{
//	    	    				if(mdialog != null && mdialog.isShowing()){
//	    	    	        		mdialog.dismiss();
//	    	    	        	}
//
//
//	    						Toast.makeText(getApplicationContext(), "Emergency Contact Details Saved", Toast.LENGTH_LONG).show();
//	    						EmergencyContactActivity.this.finish();
//	    						//	onBackPressed();
//
//	    						Intent intent = new Intent(EmergencyContactActivity.this, MapClass.class);
//	    		    			startActivity(intent);
//
//	    					}
//	    					else if(robj!=null && robj.getString("result").equalsIgnoreCase("false"))
//	    					{
//	    	    				if(mdialog != null && mdialog.isShowing()){
//	    	    	        		mdialog.dismiss();
//	    	    	        	}
//	    					//	Toast.makeText(getApplicationContext(), "Opertaion Failed!", Toast.LENGTH_LONG).show();
//
//	    					}
//	    					}
//	    						else
//	    						{
//	    		    				if(mdialog != null && mdialog.isShowing()){
//	    		    	        		mdialog.dismiss();
//	    		    	        	}
//
//	    							Toast.makeText(getApplicationContext(), "Oops Error In Communication", Toast.LENGTH_SHORT).show();
//
//	    						}
//
//	    					} catch (JSONException e) {
//	    						// TODO Auto-generated catch block
//	    						e.printStackTrace();
//	    					}
//	    				}
//	    				public void datafail()
//	    				{
//	        				if(mdialog != null && mdialog.isShowing()){
//	        	        		mdialog.dismiss();
//	        	        	}
//	    					Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
//	    				}
//	    			});
//	    			sobj.execute();
		}catch(Exception e){e.printStackTrace();}
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
				//Intent intent = new Intent(EmergencyContactActivity.this, MapClass.class);
				//startActivity(intent);
				//System.exit(1);
		  //  	Toast.makeText(getApplicationContext(), "in start", Toast.LENGTH_LONG).show();
		    	//finish();
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
}