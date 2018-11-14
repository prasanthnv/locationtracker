package com.codemagos.locationtracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdaptor extends ArrayAdapter{
    ArrayList speed;
    ArrayList distance;
    ArrayList date;
    Activity activity;
    public HistoryAdaptor( Activity activity, ArrayList speed,ArrayList date,ArrayList distance) {
        super(activity, R.layout.history_tile, speed);
       this.activity = activity;
       this.distance = distance;
       this.speed = speed;
       this.date  =date;

    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.history_tile, null);

        TextView speed = (TextView) rowView.findViewById(R.id.speed);
        TextView distance = (TextView) rowView.findViewById(R.id.distance);


        speed.setText(this.speed.get(position)+"km/h");
        distance.setText("D : "+this.distance.get(position)+" Km\n"+this.date.get(position));

        if(position == 0){
            speed.setTextColor(Color.parseColor("#F44336"));
            speed.setTypeface(speed.getTypeface(), Typeface.BOLD);
        }




        return rowView;

    }

}
