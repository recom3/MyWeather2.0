package com.myweather;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

//import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.HUDWebService;
import com.reconinstruments.os.connectivity.IHUDConnectivity;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements IHUDConnectivity {

	static String BASE_API = "http://api.open-meteo.com/v1/forecast";
	String apiUrlWithParamCurrent = BASE_API + "?latitude=%s&longitude=%s&current_weather=true";
	String apiUrlWithParamHourly = BASE_API + "?latitude=%s&longitude=%s&hourly=temperature&hourly=weathercode&hourly=is_day&hourly=apparent_temperature";

	OpenMeteoParser openMeteoParser = new OpenMeteoParser();

	HUDWebService mHUDWebService;

	private LocationManager locationManager;
	private String provider;
	private MyLocationListener mylistener;
	private Criteria criteria;


	public static HUDConnectivityManager mHUDConnectivityManager = null;
    public boolean first;
	private TextView status;
	private TextView temperature,textressentie,temperature1,temperature2,textView5;
	private ImageView iconimage,iconimage1,iconimage2;
	public static String result;
	public double latitude,oldLatitude;
	public double longitude,oldLongitude;
	static String key = "28faca837266a521f823ab10d1a45050";
    String language,unit,vitesse;
    String icon,PreviousResult,temp,statusline;
	boolean Mydebug, nointernet, nogps;
	private String un,city;
	private String feel,press,wind,humid,time;

	private static final String mac_file = "mac_myweather.json";
	public static boolean phoneConnected = false;
	public static String phoneAddress = "";

	public static HashMap<String, String[]> mapOpenMeteoCodes = new HashMap<>();
	public static String[][] openMeteoCodes =
		{{"0","Clear sky","clear_day","clear_night"},
		{"1","Mainly clear","clear_day","clear_night"},
		{"2","partly cloudy","partly_cloudy_day","partly_cloudy_night"},
		{"3","and overcast","cloudy","cloudy"},
		{"45","Fog and ","fog","fog"},
		{"48","depositing rime fog","fog","fog"},
		{"51","Drizzle: Light","rain","rain"},
		{"53","moderate","rain","rain"},
		{"55","and dense intensity","rain","rain"},
		{"56","Freezing Drizzle: Light and ","rain","rain"},
		{"57","dense intensity","rain","rain"},
		{"61","Rain: Slight","rain","rain"},
		{"63","moderate and ","rain","rain"},
		{"65","heavy intensity","rain","rain"},
		{"66","Freezing Rain: Light and ","sleet","sleet"},
		{"67","heavy intensity","sleet","sleet"},
		{"71","Snow fall: Slight","snow","snow"},
		{"73","moderate","snow","snow"},
		{"75","and heavy intensity","snow","snow"},
		{"77","Snow grains","snow","snow"},
		{"80","Rain showers: Slight","rain","rain"},
		{"81","moderate","rain","rain"},
		{"82","and violent","rain","rain"},
		{"85","Snow showers slight and ","sleet","sleet"},
		{"86","heavy","sleet","sleet"},
		{"95","Thunderstorm: Slight or moderate","rain","rain"},
		{"96","Thunderstorm with slight and ","rain","rain"},
		{"99","heavy hail","rain","rain"}};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, HUDWebService.class);
		bindService(intent, this.hudWebSrvConn, Context.BIND_AUTO_CREATE);

		//!recom3
		//System.load("/system/lib/libreconinstruments_jni.so");
		//!recom3
		//mHUDConnectivityManager = (HUDConnectivityManager) HUDOS.getHUDService(HUDOS.HUD_CONNECTIVITY_SERVICE);
		/*
		try {
			UUID phoneRequestUUID = UUID.fromString(HUDSPPService.g[0]);

			UUID hudRequestUUID = UUID.fromString(HUDSPPService.g[1]);

			this.mHUDConnectivityManager = new HUDConnectivityManager(this, null, true, true,
					"com.recom3.myweather",
					//hudRequestUUID, phoneRequestUUID
					phoneRequestUUID, hudRequestUUID
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		TimeZone tz = TimeZone.getDefault();
		setContentView(R.layout.activity_main2);
//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
		status = (TextView) findViewById(R.id.status);
		temperature = (TextView) findViewById(R.id.Temperature);
		temperature1 = (TextView) findViewById(R.id.Temperature1);
		temperature2 = (TextView) findViewById(R.id.Temperature2);
		textressentie = (TextView) findViewById(R.id.textressentie);
		textView5 = (TextView) findViewById(R.id.textView5);
    	iconimage = (ImageView) findViewById(R.id.icon);
		iconimage1 = (ImageView) findViewById(R.id.icon1);
		iconimage2 = (ImageView) findViewById(R.id.icon2);
	    statusline=""; city=""; language="en";

		//Recom3: receiver are register to hear to BT activity
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, filter);

	    //Uncomment to load mac address from file
	    loadMacAddres();

	    //Create open meteo map to translate codes to icons
		createMapOpenMeteo();
	}

	public static void createMapOpenMeteo()
	{
		int indexCode = 0;
		int indexDay = 2;
		int indexNight = 3;
		for(int index=0;index<openMeteoCodes.length;index++)
		{
			String[] nightDay = new String[2];
			nightDay[0] = openMeteoCodes[index][indexDay];
			nightDay[1] = openMeteoCodes[index][indexNight];
			mapOpenMeteoCodes.put(openMeteoCodes[index][0], nightDay);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		out("KeyUp: (" + keyCode + ")");
	    switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER :
            {
                doRefresh();
                return true;
            }
	        case KeyEvent.KEYCODE_DPAD_DOWN :
	        {
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
//	        	startActivity(new Intent(MainActivity.this, SettingsActivity.class));
	        	overridePendingTransition(R.anim.slideup_in, R.anim.slideup_out);
                return true;
	        }
	        case KeyEvent.KEYCODE_DPAD_UP :
	        {
	        	startActivity(new Intent(MainActivity.this, HoursActivity.class));
	        	overridePendingTransition(R.anim.slidedown_in, R.anim.slidedown_out);
                return true;
	        }
	        case KeyEvent.KEYCODE_BACK :
	        {
                out("Bye Bye...");
				MainActivity.this.finish();
				break;
	        }
	    }
	    return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onPause() {
		mHUDConnectivityManager.unregister(this);
		super.onPause();
	}

    @Override
	protected void onDestroy() {
		//mHUDConnectivityManager.unregister(this);
    	super.onDestroy();
	}
    
    @Override
	protected void onResume() {
		//mHUDConnectivityManager.register(this);
		doRefresh();
		super.onResume();
    }

	/**
	 * Called by WebRequestTask3 onPostExecute
	 * @param data
	 */
	private void onDisplay(String data) {
		if (nointernet ) { statusline ="No Internet access";}
		if (nogps ) { statusline ="No Gps signal";}
		if (nointernet & nogps) { statusline = "no gps and no Internet"; }
		Date date; double date1=0;
		if (data !=null & data!="") {
			out("Displaying Current data");

			displayOpenMeteo(data);

			return;

			/*
    		ForecastIO fio = new ForecastIO(key);
    		fio.getForecast(data);
    		FIOCurrently currently = new FIOCurrently(fio);
    		String icon =  currently.get().getByKey("icon").replace("\"", "");
    		String icon1 = "@drawable/"+icon.replace("-", "_");
    		Resources res = getResources();
    		int resourceId = res.getIdentifier(icon1, "drawable", getPackageName() );
    		iconimage.setImageResource( resourceId);
			setTitle("MyWeather : currently");
    		feel = getString(R.string.feellike_en);
    		press = getString(R.string.pressure_en);
    		wind = getString(R.string.wind_en);
    		humid = getString(R.string.humid_en);
    		vitesse = "mi";
    	    String [] f  = currently.get().getFieldsArray();
    		temperature.setText(DoubleToI(currently.get().getByKey("temperature"))+"°");
    		textressentie.setText(DoubleToI(currently.get().getByKey("apparentTemperature"))+"°");
			textView5.setText("Feels like ");

			out("Displaying Next 1h");
			FIOHourly hourly = new FIOHourly(fio);
			Date date2 = new Date();
			SimpleDateFormat sdfm = new SimpleDateFormat("mm");
			sdfm.setTimeZone(TimeZone.getDefault());
			int next=1;
			String date3=sdfm.format(date2);
			if (Integer.valueOf(date3)>29) { next=2; }
			icon = "@drawable/"+hourly.getHour(next).icon().replace("\"", "").replace("-", "_");
			resourceId = res.getIdentifier(icon, "drawable", getPackageName() );
			iconimage1.setImageResource(resourceId);
			temperature1.setText(DoubleToI(hourly.getHour(next).getByKey("temperature")) + "°");

			out("Displaying Next 2h");
			icon = "@drawable/"+hourly.getHour(next+1).icon().replace("\"", "").replace("-", "_");
			resourceId = res.getIdentifier(icon, "drawable", getPackageName());
			iconimage2.setImageResource( resourceId);
			temperature2.setText(DoubleToI(hourly.getHour(next + 1).getByKey("temperature")) + "°");
			out("Displaying city");
			if (city !=null & city!="") { statusline=city; }
    		status.setText(statusline);
    		String substr=data.substring(data.indexOf("hourly\":{\"")+20);
    		substr=substr.substring(0, substr.indexOf("\""));
    		*/
    	} else {
    		String icon1 = "@drawable/unknown";
    		Resources res = getResources();
    		int resourceId = res.getIdentifier(icon1, "drawable", getPackageName() );
			iconimage.setImageResource(resourceId);
			out("nothing to display or bad json...");
			out("nointernet=" + nointernet);
			out("nogps="+nogps);
			status.setText(statusline);
    	}
    }

    private void displayOpenMeteo(String data)
	{
		//JSONObject currentWeather = null;
		OpenMeteoParser.WeatherInfo[] weatherInfo = null;
		try {
			//JSONObject object = new JSONObject(data);
			//currentWeather = object.getJSONObject("current_weather");
			weatherInfo = openMeteoParser.parseHourlyWeather(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			//String weatherCode = currentWeather.getString("weathercode");
			String weatherCode = weatherInfo[0].getCurrendCode();
			if(mapOpenMeteoCodes.containsKey(weatherCode))
			{
				int nightDay = Integer.parseInt(weatherInfo[0].getCurrentIsDay());
				String icon =  mapOpenMeteoCodes.get(weatherCode)[nightDay];
				String icon1 = "@drawable/"+icon.replace("-", "_");
				Resources res = getResources();
				int resourceId = res.getIdentifier(icon1, "drawable", getPackageName() );
				iconimage.setImageResource( resourceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("MyWeather : currently");
		feel = getString(R.string.feellike_en);
		press = getString(R.string.pressure_en);
		wind = getString(R.string.wind_en);
		humid = getString(R.string.humid_en);
		vitesse = "mi";

		//String [] f  = currently.get().getFieldsArray();
		try {
			temperature.setText(DoubleToI(weatherInfo[0].getCurrentTemp())+"°");
			textressentie.setText(DoubleToI(weatherInfo[0].getApparentTemperature())+"°");
			textView5.setText("Feels like ");

			out("Displaying Next 1h");

			//FIOHourly hourly = new FIOHourly(fio);
			Date date2 = new Date();
			SimpleDateFormat sdfm = new SimpleDateFormat("mm");
			sdfm.setTimeZone(TimeZone.getDefault());
			int next=1;
			String date3=sdfm.format(date2);
			if (Integer.valueOf(date3)>29) { next=2; }
			//icon = "@drawable/"+hourly.getHour(next).icon().replace("\"", "").replace("-", "_");
			//resourceId = res.getIdentifier(icon, "drawable", getPackageName() );
			//iconimage1.setImageResource(resourceId);

			String weatherCode = weatherInfo[1].getCurrendCode();
			if(mapOpenMeteoCodes.containsKey(weatherCode))
			{
				int nightDay = Integer.parseInt(weatherInfo[1].getCurrentIsDay());
				String icon =  mapOpenMeteoCodes.get(weatherCode)[nightDay];
				String icon1 = "@drawable/"+icon.replace("-", "_");
				Resources res = getResources();
				int resourceId = res.getIdentifier(icon1, "drawable", getPackageName() );
				iconimage1.setImageResource( resourceId);
			}

			temperature1.setText(DoubleToI(weatherInfo[1].getCurrentTemp()) + "°");

			out("Displaying Next 2h");

			//icon = "@drawable/"+hourly.getHour(next+1).icon().replace("\"", "").replace("-", "_");
			//resourceId = res.getIdentifier(icon, "drawable", getPackageName());
			//iconimage2.setImageResource( resourceId);

			weatherCode = weatherInfo[2].getCurrendCode();
			if(mapOpenMeteoCodes.containsKey(weatherCode))
			{
				int nightDay = Integer.parseInt(weatherInfo[2].getCurrentIsDay());
				String icon =  mapOpenMeteoCodes.get(weatherCode)[nightDay];
				String icon1 = "@drawable/"+icon.replace("-", "_");
				Resources res = getResources();
				int resourceId = res.getIdentifier(icon1, "drawable", getPackageName() );
				iconimage2.setImageResource( resourceId);
			}

			temperature2.setText(DoubleToI(weatherInfo[2].getCurrentTemp()) + "°");

			out("Displaying city");
			if (city !=null & city!="") { statusline=city; }
			status.setText(statusline);

			String substr=data.substring(data.indexOf("hourly\":{\"")+20);
			substr=substr.substring(0, substr.indexOf("\""));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
////////////////////////////////////////////////////


	/**
	 * Called by DPAD center and onResume
	 */
	private void doRefresh() {
		out("doRefresh()");
		status.setText("Refreshing...");
		result = "";
		statusline = ""; nointernet=false; nogps=false;city="";
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default
		criteria.setCostAllowed(false);
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		mylistener = new MyLocationListener();
		out("Get Location ");

		//Testing
		boolean isTesting = false;
		if(isTesting) {
			location = new Location("");
			location.setLatitude(40.416775d);
			location.setLongitude(-3.703790d);
			//
			//	remplacement de la localisation pour test
			//
			//latitude=50.647392; longitude=3.130481; // my home
			//latitude=45.092624; longitude=6.068348; // alpe d'huez
			//latitude=45.125263; longitude=6.127609; // Pic Blanc
			//latitude=41.919229; longitude=8.738635; //Ajaccio
			//latitude=46.192683; longitude=48.205964; //Russie
			//latitude=49.168602; longitude=25.351872; //bulgarie
			//latitude=36.752887; longitude=3.042048; //alger
			statusline = "Test location";
		}

		if (location != null) {
			out("I have got a location ");
			mylistener.onLocationChanged(location);
			locationManager.requestLocationUpdates(provider, 2000, 100, mylistener);
			String a = "" + location.getLatitude();

			//Logging
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();

				out("New Lat trouve:" + latitude + " / long:" + longitude);
				oldLatitude = latitude;
				oldLongitude = longitude;
			} else {
				out("no Lat trouve:");
				nogps=true;
				statusline = "No GPS. Previous location used";
				latitude = oldLatitude;
				longitude = oldLongitude;
			}

			un = "us";
			city = ""; language="en";
			out("Fetching data...");
			String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=false";
//			new WebRequestTask4(url).execute();
			url = "http://api.forecast.io/forecast/" + key + "/" + latitude + "," + longitude + "?lang=" + language + "&units=" + un;

			//recom3: new url
			url = "http://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&current_weather=true";
			//new WebRequestTask3(url).execute();

		} else {
			out("No GPS found");
			status.setText("No Gps signal");
			nogps=true;
		}
	}

	public String DoubleToP(String sourceDouble) {
		DecimalFormat df = new DecimalFormat("#");
		double db=Double.valueOf(sourceDouble);
		return df.format(db*100);
	}

	public String DoubleToI(String sourceDouble) {
		DecimalFormat df = new DecimalFormat("#");
		double db=Double.valueOf(sourceDouble);
		return df.format(db);
	}

	/**
	 * Used by connect service with phone
	 */
	private class WebRequestTask3 extends AsyncTask<Void, Void, String> {

		String mUrl;
		String mComment;

		public WebRequestTask3(String url) {
			mUrl = url;
		}

		@Override
		protected String doInBackground(Void... voids) {
			try {
				out("try get " + mUrl);
				HUDHttpRequest request = new HUDHttpRequest(HUDHttpRequest.RequestMethod.GET, mUrl);
				HUDHttpResponse response = mHUDConnectivityManager.sendWebRequest(request);
				if (response.hasBody()) {
					out("Response.sendWebRequest = 200");
					return new String(response.getBody());
				}
			} catch (Exception e) {
				out("HUD not connected - No Internet");
				nointernet=true;
				statusline ="No Internet access";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				oldLatitude = latitude;
				oldLongitude = longitude;
				out("Displaying data...");
				onDisplay(result);
				out("Data displayed...");
			} else {
				nointernet = true;
				statusline = "No Internet access";
				status.setText(statusline);
			}
		}
	}

	private class WebRequestTask4 extends AsyncTask<Void, Void, String> {

		String mUrl;
		String mComment;

		public WebRequestTask4(String url) {
			mUrl = url;
		}

		@Override
		protected String doInBackground(Void... voids) {
			try {
				out("try get " + mUrl);
				HUDHttpRequest request = new HUDHttpRequest(HUDHttpRequest.RequestMethod.GET, mUrl);
				HUDHttpResponse response = mHUDConnectivityManager.sendWebRequest(request);
				if (response.hasBody()) {
					out("Response.sendWebRequest = 200");
					return new String(response.getBody());
				}
			} catch (Exception e) {
				out("HUD not connected - No Internet");
				nointernet=true;
				statusline ="No Internet access";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				out("City received...");
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject = new JSONObject(result);
					int i=0;
					while (!((JSONArray)jsonObject.get("results")).getJSONObject(1).getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("locality")) {
						i += 1;
					}
					city = "near "+((JSONArray)jsonObject.get("results")).getJSONObject(1).getJSONArray("address_components").getJSONObject(i).getString("short_name");
				} catch (JSONException e) {
					e.printStackTrace();
					out("error while requesting google api");
					city="location unknown";
				}
				out("city="+city);
				status.setText(city);
			}
		}
	}

	public static String headingToString2(double x)
	{
		String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
		return directions[ (int)Math.round((  ((double)x % 360) / 45)) ];
	}

	@Override
	public void onDeviceName(String deviceName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnectionStateChanged(ConnectionState state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNetworkEvent(NetworkEvent networkEvent, boolean hasNetworkAccess) {
		if(!hasNetworkAccess) {
			out("HUD not connected (onNetworkEvent)- No Internet");
			nointernet=true;
			statusline ="No Internet access";
			status.setText(statusline);
//			onDisplay(PreviousResult);
		} else {
			out("HUD connected (onNetworkEvent)");
		}
	}

	public void out(String Trace) {
		System.out.println(Trace);
		Log.i("MainActivity", Trace);

		File file = new File(Environment.getExternalStorageDirectory()+"/ReconApps/MyWeather2/" + "Mydebug");
		if (file.exists() == true) {
			try {
				FileWriter fos = new FileWriter(Environment.getExternalStorageDirectory()+"/ReconApps/MyWeather2/" + "Mydebug",true);
				fos.write(Trace+"\n");
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// Initialize the location fields
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			out("Location OnStatusChanged, status="+status);
		}

		@Override
		public void onProviderEnabled(String provider) {
			out("Location onProviderDisabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			out("Location onProviderDisabled");
		}
	}

	private ServiceConnection hudWebSrvConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {

			Log.i("HUDWebService", "onServiceConnected");

			HUDWebService.LocalBinder binder = (HUDWebService.LocalBinder) param1IBinder;
			MainActivity.this.mHUDWebService = binder.getService();

			//Helper.getInstance().setHUDConnectivityManager(MainActivity.this.mHUDWebService.hudConnectivityManager);
			//Helper.getInstance().UpdateResortFile();

			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//String url = "http://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&current_weather=true";
			String url = String.format(apiUrlWithParamHourly, latitude, longitude);

			Log.i("MainActivity", "Performing call to " + url);

			MainActivity.this.mHUDConnectivityManager = MainActivity.this.mHUDWebService.hudConnectivityManager;

			//Send web request to phone
			new WebRequestTask3(url).execute();
		}

		@Override
		public void onServiceDisconnected(ComponentName param1ComponentName) {
			MainActivity.this.mHUDWebService = null;
		}
	};

	//The BroadcastReceiver that listens for bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//Device found
			}
			else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				//Device is now connected

				Log.i("MainActivity", "ACTION_ACL_CONNECTED");

				BluetoothClass btClass = device.getBluetoothClass();
				if(btClass.getDeviceClass()== BluetoothClass.Device.PHONE_SMART
						|| btClass.getDeviceClass()== BluetoothClass.Device.PHONE_CELLULAR)
				{
					phoneConnected = true;
					phoneAddress = device.getAddress();
					SaveMacDataToFile(phoneAddress);

					Log.i("MainActivity", "SaveMacDataToFile");

					mHUDWebService.connect();
				}
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//Done searching
			}
			else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
				//Device is about to disconnect
			}
			else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				//Device has disconnected
				BluetoothClass btClass = device.getBluetoothClass();
				if(btClass.getDeviceClass()== BluetoothClass.Device.PHONE_SMART
						|| btClass.getDeviceClass()== BluetoothClass.Device.PHONE_CELLULAR)
				{
					phoneConnected = false;
				}
			}
		}
	};

	private void SaveMacDataToFile(String data) {
		FileOutputStream fos;
		try {
			fos = this.openFileOutput(mac_file, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadMacAddres()
	{
		File fl = new File(this.getFilesDir()+"/"+ mac_file);
		if (fl.exists() ) {
			FileInputStream fin;
			try {
				fin = new FileInputStream(fl);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
				StringBuilder sb = new StringBuilder();
				String line="";
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line);//
					}
					reader.close();
					phoneAddress=sb.toString();
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
