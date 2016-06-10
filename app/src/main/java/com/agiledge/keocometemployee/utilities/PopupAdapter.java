package com.agiledge.keocometemployee.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.agiledge.keocometemployee.*;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class PopupAdapter implements InfoWindowAdapter {
      String emps;
	  LayoutInflater inflater=null;

	  public PopupAdapter(LayoutInflater inflater,String emps) {
	    this.inflater=inflater;
	    this.emps=emps;
	  }

	  @Override
	  public View getInfoContents(Marker marker) {
	    return(null);
	  }

	  @Override
	  public View getInfoWindow(Marker marker) {
	    View popup=inflater.inflate(R.layout.popup, null);

	    TextView tv=(TextView)popup.findViewById(R.id.title);
	    tv.setText("My Cab");
	    tv=(TextView)popup.findViewById(R.id.snippet);
	    tv.setText(emps);

	    return(popup);
	  }
	}