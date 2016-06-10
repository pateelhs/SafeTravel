package com.agiledge.keocometemployee.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agiledge.keocometemployee.Model.Trips;
import com.agiledge.keocometemployee.R;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Trips> TripItems;

    public CustomListAdapter(Activity activity, List<Trips> TripItems) {
        this.activity = activity;
        this.TripItems = TripItems;
    }

    @Override
    public int getCount() {
        return TripItems.size();
    }

    @Override
    public Object getItem(int location) {
        return TripItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        ImageView thumbNail = (ImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView CustomerName = (TextView) convertView.findViewById(R.id.CustomerName);
        TextView Issue = (TextView) convertView.findViewById(R.id.Issue);
        TextView Priority = (TextView) convertView.findViewById(R.id.Priority);
        TextView Schedule = (TextView) convertView.findViewById(R.id.time1);


        // getting Trips data for the row
        Trips m = TripItems.get(position);


        // Name
        CustomerName.setText(m.getCustomerName());

        // Issue
        Issue.setText("Service Type: " + String.valueOf(m.getIssue()));

        // Priority
        Priority.setText("Priority: " + String.valueOf(m.getPriority()));

        // release year
        Schedule.setText(String.valueOf(m.getTime()));

        return convertView;
    }

}