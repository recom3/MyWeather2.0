package com.myweather;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.ForecastIO;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import org.lucasr.twowayview.TwoWayView;

public class DaysActivity extends Activity {
	
	String data,language,unit;
	static String key = "28faca837266a521f823ab10d1a45050";
    String icon,time,temperature,precipitation,wind,vitesse;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hours);
		//Restore previous session
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	data = sharedpreferences.getString("PreviousResult", "");
    	language = sharedpreferences.getString("Language", "en");
    	unit = sharedpreferences.getString("Unit", "F"); 
    	System.out.println("get preferences..");
		ForecastIO fio = new ForecastIO(key);
		fio.getForecast(data);
		FIODaily daily = new FIODaily(fio);
		new FIODaily(fio);
		if (language=="en") vitesse = "mph"; else vitesse = "kmh";
		Weather weather_data[] = new Weather[daily.days()] ;
		for(int i = 0; i<daily.days(); i++){
    		time = daily.getDay(i).getByKey("time");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date date; double date1=0;
			try {
				date = (Date)formatter.parse(time);
				date1 = (date.getTime()/1000)+7200;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Date date2 = new Date((long) (date1*1000));
			SimpleDateFormat sdf = new SimpleDateFormat("E");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
			time = sdf.format(date2);
			icon =  daily.getDay(i).icon().replace("\"", "");
    		String icon1 = "@drawable/"+icon.replace("-", "_");
			Resources res = getResources();
    		int icon = res.getIdentifier(icon1, "drawable", getPackageName() );
			temperature = DoubleToI(daily.getDay(i).getByKey("temperatureMin"))+"°/"+DoubleToI(daily.getDay(i).getByKey("temperatureMax"))+"°";
			String dir=headingToString2(Integer.valueOf(daily.getDay(i).getByKey("windBearing")));
			wind = DoubleToI(daily.getDay(i).getByKey("windSpeed"))+" "+vitesse;//+"\n"+dir;
			weather_data[i] = new Weather(time,icon,temperature,wind,dir);
		}
    	System.out.println("get weather content..");
        final WeatherAdapter adapter = new WeatherAdapter(this, R.layout.listview_item_row, weather_data);
        final TwoWayView listView1 = (TwoWayView) findViewById(R.id.lvItems);
        listView1.setItemMargin(1);
        listView1.setAdapter(adapter);
		listView1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			int originalBackgroundColor = -872415232;
			int highLigthColor = Color.parseColor("#606060");

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				for (int i = 0; i < listView1.getChildCount(); i++) {
					if( i == position - listView1.getFirstVisiblePosition()){
						listView1.getChildAt(i).setBackgroundColor(highLigthColor);
					}else{
						listView1.getChildAt(i).setBackgroundColor(originalBackgroundColor);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//System.out.println("Keydown: ("+keyCode+")");
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_DPAD_DOWN :
	        { 
	        	finish();
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	        	break;
	        }
	        case KeyEvent.KEYCODE_DPAD_CENTER :
	        {
	        	break;
	        }

	        case KeyEvent.KEYCODE_BACK :
	        {
	        	finish();
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	        	break;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
    @Override
	protected void onResume() {
		super.onResume();
	    LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
    	ImageView image = (ImageView) layout.findViewById(R.id.image);
    	image.setImageResource(R.drawable.scroll4);
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.RIGHT, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
	}

	public String DoubleToI(String sourceDouble) {
		DecimalFormat df = new DecimalFormat("#");    		
		double db=Double.valueOf(sourceDouble);
		return df.format(db);
	}

	public static String headingToString2(double x)
    {
        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        return directions[ (int)Math.round((  ((double)x % 360) / 45)) ];
    }

}