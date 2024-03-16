package com.myweather;

import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

/**
 * Created by recom3 on 16/03/2024.
 */

public class OpenMeteoParser {

    public class WeatherInfo
    {
        private String currentTemp;
        private String currendCode;
        private String currentIsDay;
        private String apparentTemperature;

        public WeatherInfo(String currentTemp, String currendCode, String currentIsDay, String apparentTemperature) {
            this.currentTemp = currentTemp;
            this.currendCode = currendCode;
            this.currentIsDay = currentIsDay;
            this.apparentTemperature = apparentTemperature;
        }

        @Override
        public String toString() {
            String str = String.format("{temp:%s,code:%s,day:%s,apparent:%s}", currentTemp, currendCode, currentIsDay, apparentTemperature);
            return str;
        }

        public String getCurrentTemp() {
            return currentTemp;
        }

        public void setCurrentTemp(String currentTemp) {
            this.currentTemp = currentTemp;
        }

        public String getCurrendCode() {
            return currendCode;
        }

        public void setCurrendCode(String currendCode) {
            this.currendCode = currendCode;
        }

        public String getCurrentIsDay() {
            return currentIsDay;
        }

        public void setCurrentIsDay(String currentIsDay) {
            this.currentIsDay = currentIsDay;
        }

        public String getApparentTemperature() {
            return apparentTemperature;
        }

        public void setApparentTemperature(String apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
        }
    }

    void parseCurrentWeather(String data)
    {
        JSONObject currentWeather = null;
        try {
            JSONObject object = new JSONObject(data);
            currentWeather = object.getJSONObject("current_weather");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    WeatherInfo[] parseHourlyWeather(String data)
    {
        WeatherInfo[] result = null;
        JSONObject hourlyWeather = null;

        try {
            JSONObject object = new JSONObject(data);
            hourlyWeather = object.getJSONObject("hourly");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray temperature = hourlyWeather.getJSONArray("temperature");
            JSONArray weatherCode = hourlyWeather.getJSONArray("weathercode");
            JSONArray isDay = hourlyWeather.getJSONArray("is_day");
            JSONArray apparentTemperature = hourlyWeather.getJSONArray("apparent_temperature");

            String currentTemp = temperature.get(0).toString();
            String currendCode = weatherCode.get(0).toString();
            String currentIsDay = isDay.get(0).toString();
            String currentApparentTemperature = apparentTemperature.get(0).toString();

            int hoursToAdd = 3;
            result = new WeatherInfo[hoursToAdd];
            for( int i=0; i<hoursToAdd; i++)
            {
                currentTemp = temperature.get(0).toString();
                currendCode = weatherCode.get(0).toString();
                currentIsDay = isDay.get(0).toString();
                currentApparentTemperature = apparentTemperature.get(0).toString();
                result[i] = new WeatherInfo(currentTemp, currendCode, currentIsDay, currentApparentTemperature);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
