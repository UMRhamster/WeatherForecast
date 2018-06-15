package com.whut.umrhamster.weatherforecast.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/13.
 */

public class Utils {
    /*
     * 将Json数据转换成java对象
     * 输入：json数据
     * 输出：weather想
     */
    public static Weather Json2Weather(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonData = jsonObject.getJSONObject("data");
            Weather weather = new Weather();                         //weather对象
            weather.setCity(jsonData.getString("city"));      //设置城市
            weather.setGanmao(jsonData.getString("ganmao"));  //设置感冒指数
            weather.setWendu(jsonData.getString("wendu"));    //设置体感温度
            //
            DailyWeather yesterday = new DailyWeather();
            JSONObject yesterdayJson = jsonData.getJSONObject("yesterday");
            yesterday.setDate(yesterdayJson.getString("date"));
            yesterday.setFl(yesterdayJson.getString("fl"));
            yesterday.setFx(yesterdayJson.getString("fx"));
            yesterday.setHigh(yesterdayJson.getString("high"));
            yesterday.setLow(yesterdayJson.getString("low"));
            yesterday.setType(yesterdayJson.getString("type"));
            weather.setYesterday(yesterday);                           //设置昨天天气
            JSONArray jsonArray = jsonData.getJSONArray("forecast");
            //
            List<DailyWeather> forecast = new ArrayList<>();
            for (int i=0;i<5;i++){                                     //依次添加五天的天气对象
                DailyWeather forecastWeather = new DailyWeather();
                forecastWeather.setDate(jsonArray.getJSONObject(i).getString("date"));
                forecastWeather.setFl(jsonArray.getJSONObject(i).getString("fengli"));
                forecastWeather.setFx(jsonArray.getJSONObject(i).getString("fengxiang"));
                forecastWeather.setHigh(jsonArray.getJSONObject(i).getString("high"));
                forecastWeather.setLow(jsonArray.getJSONObject(i).getString("low"));
                forecastWeather.setType(jsonArray.getJSONObject(i).getString("type"));
                forecast.add(forecastWeather);
            }
            weather.setForecast(forecast);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String numberChange(int number){
        if (number < 10){
            return "0"+number;
        }else {
            return String.valueOf(number);
        }
    }

    public static String getWeek(int week){
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return weekDays[week-1];
    }

    public static String getfl(String fl){
        return fl.substring(9,fl.length()-3);
    }
}
