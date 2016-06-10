package com.agiledge.keocometemployee.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agiledge.keocometemployee.R;
import com.agiledge.keocometemployee.navdrawermenu.TripHistoryActivity;

public class TripHistoryCustomAdapter extends BaseAdapter{   
    String [] result;
    String [] result1;
    String [] result2;
    Context context;
 int [] imageId;
      private static LayoutInflater inflater=null;
    public TripHistoryCustomAdapter(TripHistoryActivity triphistoryactivity, String[] thdate, String[] thdistance, String[] thlocation ) {
        // TODO Auto-generated constructor stub
        result=thdate;
        result1=	thdistance;
        result2=	thlocation;
       
        context=triphistoryactivity;
   //     imageId=prgmImages;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView tv1;
        TextView tv2;
      //  ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.throwlayout, null);
             holder.tv=(TextView) rowView.findViewById(R.id.Thdate);
             holder.tv1=(TextView) rowView.findViewById(R.id.Thdistance);
             holder.tv2=(TextView) rowView.findViewById(R.id.ThLoction);
        //     holder.img=(ImageView) rowView.findViewById(R.id.imageView1);       
         holder.tv.setText(result[position]);
         holder.tv1.setText(result1[position]);
         holder.tv2.setText(result2[position]);
      //   holder.img.setImageResource(imageId[position]);         
//         rowView.setOnClickListener(new OnClickListener() {            
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
//            }
//        });   
        return rowView;
    }

} 