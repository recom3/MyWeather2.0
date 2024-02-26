package com.myweather;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.res.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class WeatherAdapter extends ArrayAdapter<Weather>{

    Context context; 
    int layoutResourceId;    
    Weather data[] = null;
    
    public WeatherAdapter(Context context, int layoutResourceId, Weather[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);            
            holder = new WeatherHolder();
            holder.txtTime = (TextView)row.findViewById(R.id.txtTime);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTemperature = (TextView)row.findViewById(R.id.txtTemperature);
            holder.txtwindBearing = (TextView)row.findViewById(R.id.txtWindBearing);
            holder.txtWind = (TextView)row.findViewById(R.id.txtWind);
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }        
        Weather weather = data[position];
        holder.txtTime.setText(weather.time);
        holder.imgIcon.setImageResource(weather.icon);
        holder.txtTemperature.setText(weather.temperature);
        holder.txtwindBearing.setText(weather.windBearing);
        holder.txtWind.setText(weather.wind);
        return row;
    }
    

	static class WeatherHolder
    {
    	TextView txtTime;
        ImageView imgIcon;
        TextView txtTemperature;
        TextView txtwindBearing;
        TextView txtWind;
    }
	

}