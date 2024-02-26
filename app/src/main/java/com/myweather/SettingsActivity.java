package com.myweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity  {

	TextView mCurrentTemp;
    public boolean first;
	public static String result;
	public double latitude,oldLatitude;
	public double longitude,oldLongitude;
    public int testByte;
    String language,unit;
    String PreviousResult,temp;
    private Switch buttonUnit;
    private Switch buttonLanguage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		buttonUnit = (Switch) findViewById(R.id.switchUnit);
		buttonLanguage = (Switch) findViewById(R.id.switchLanguage);
    	SharedPreferences sharedpreferences = getSharedPreferences("com.myweather", Context.MODE_PRIVATE);
    	language = sharedpreferences.getString("Language", "en");
    	unit = sharedpreferences.getString("Unit", "F");
    	if (unit.equals("F")) { buttonUnit.setChecked(true); } else { buttonUnit.setChecked(false); }
    	if (language.equals("en")) { buttonLanguage.setChecked(true); } else { buttonLanguage.setChecked(false); }
    	buttonUnit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	    if (isChecked) { unit="F"; } else { unit="C"; }
        }
    	});
    	buttonLanguage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      	    if (isChecked) { language="en"; } else { language="fr"; }
        }
    	});

	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("Keydown: ("+keyCode+")");
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_DPAD_UP :
	        { 
	        	finish();
	        	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
	        	break;
	        }
	        case KeyEvent.KEYCODE_DPAD_CENTER :
	        {
	        	break;
	        }
	        case KeyEvent.KEYCODE_BACK :
	        {
	        	finish();
	        	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
	        	break;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
   
	protected void onPause() {
		super.onPause();
		SharedPreferences preferences = getSharedPreferences("com.myweather", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Language", String.valueOf(language) );
		editor.putString("Unit", String.valueOf(unit));
		editor.apply();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
    	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
	}
	
    @Override
	protected void onResume() {
		super.onResume();
	    LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
    	ImageView image = (ImageView) layout.findViewById(R.id.image);
    	image.setImageResource(R.drawable.scroll1);
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.RIGHT, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}