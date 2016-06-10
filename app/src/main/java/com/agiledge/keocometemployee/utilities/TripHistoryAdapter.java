package com.agiledge.keocometemployee.utilities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;

public class TripHistoryAdapter extends ArrayAdapter<String> { 
	  private final Context context;
	  private final ArrayList<String>  thdate;
	  private final ArrayList<String>  thdistance;
	  public final ArrayList<String>  thlocation;
	  private LayoutInflater inflater; 
	  

	private String values;
	  static class ViewContainer {
		  public TextView txtCount;
		  public TextView thdate;
		  public TextView thdistance;
		  public TextView thlocation;
		 
		  
		//public ImageView imageViewGetInStatus;
		  }
	  public TripHistoryAdapter(Context context,  ArrayList<String>  thdate, ArrayList<String>  thdistance, ArrayList<String>  thlocation) {
		    super(context, R.layout.throwlayout);
		     inflater = (LayoutInflater) context
	    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    this.context = context;
		  //  this.values = values;
		    this.thdate=thdate;
		    this.thdistance=thdistance;
		    this.thlocation=thlocation;
		    
		  }


	  public View getView(int position, View convertView, ViewGroup parent) {

	    //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
	    View rowView =convertView;
		final ViewContainer viewContainer;
	    if(rowView==null)
	    {
	    	
	    	
	    	  viewContainer= new ViewContainer();
	    	  rowView=	inflater.inflate(R.layout.throwlayout, parent,false);
//	    	viewContainer.thdate= (TextView) rowView.findViewById(R.id.Thdate);
//	    	viewContainer.thdistance= (TextView) rowView.findViewById(R.id.Thdistance);
//	    	viewContainer.thlocation= (TextView) rowView.findViewById(R.id.ThLoction);	
			
	    	viewContainer.txtCount=(TextView)rowView.findViewById(R.id.textView1);
	    	
	    	rowView.setTag(viewContainer);
	    	
	    	 
	    	
	    }else
	    {
	    	 
	    	  viewContainer=(ViewContainer)rowView.getTag(	 );
	    }
	    
	   
	    viewContainer.thdate.setText(thdate.get(position));
	    
	 //   viewContainer.imageView.setImageResource(R.drawable.go);
	    viewContainer.txtCount.setText(Integer.toString(position+1));
	    viewContainer.thdistance.setText(thdistance.get(position));
	   viewContainer.thlocation.setText(thlocation.get(position));
	    
	    
	    
	     
	     
	    

	    return rowView;
	  }
}
