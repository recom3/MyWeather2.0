package com.myweather;

public class Weather {
    public int icon;
    public String time,temperature,windBearing,wind;
    public Weather(){
        super();
    }
    
    public Weather(String time, int icon, String temperature, String wind, String windBearing) {
        super();
        this.time = time;
        this.icon = icon;
        this.temperature = temperature;
        this.windBearing = windBearing;
        this.wind = wind;
    }
}