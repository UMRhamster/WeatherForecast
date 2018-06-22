package com.whut.umrhamster.weatherforecast.Model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public static String getWeekByString(String str){
        switch (str.substring(str.length()-1,str.length())){
            case "一":
                return "周一";
            case "二":
                return "周二";
            case "三":
                return "周三";
            case "四":
                return "周四";
            case "五":
                return "周五";
            case "六":
                return "周六";
            case "天":
                return "周日";
            default:
                return "周日";
        }
    }

    public static String getfl(String fl){
        return fl.substring(9,fl.length()-3);
    }

    //查询省份
    public static List<Province> queryProvince(){
        List<Province> provinceList = null;
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0){
            Log.d("Utils->Province","数据库目前有"+provinceList.size()+"条Province记录");
            return provinceList;
        }else {
            queryFromServer("http://www.weather.com.cn/data/city3jdata/china.html","province","");
            return LitePal.findAll(Province.class);
        }
//        return null;
    }
    //查询市
    public static List<City> queryCity(String id){
        List<City> cityList = null;
        cityList = LitePal.where("provinceId = ?",id).find(City.class);
        if (cityList.size() > 0){
            Log.d("Utils->City",String.valueOf(cityList.size()));
            return cityList;
        }else {
            queryFromServer("http://www.weather.com.cn/data/city3jdata/provshi/"+id+".html","city",id);
            return LitePal.where("provinceId = ?",id).find(City.class);
        }
//        return null;
    }
    //查询地区
    public static List<District> queryDistrict(String cityId){
        List<District> districtList = LitePal.where("cityId = ?",cityId).find(District.class);
        if (districtList.size() > 0){
            Log.d("Utils->District",String.valueOf(districtList.size()));
            return districtList;
        }else {
            queryFromServer("http://www.weather.com.cn/data/city3jdata/station/"+cityId+".html","district",cityId);
            return LitePal.where("cityId = ?",cityId).find(District.class);
        }
//        return null;
    }
    private static void queryFromServer(String address, String type, String id){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            String responseJson = response.body().string();
            if("province".equals(type)){
                handleJsonProvince(responseJson);
            }else if ("city".equals(type)){
                handleJsonCity(responseJson,id);
            }else if ("district".equals(type)){
                handleJsonDistrict(responseJson,id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //http://www.weather.com.cn/data/city3jdata/china.html
    private static void handleJsonProvince(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String id = (String) iterator.next();
                Province province = new Province();
                province.setProvinceId(id);
                Log.d("Utilsprovince",String.valueOf(province.getProvinceId()));
                province.setProvinceName(jsonObject.getString(id));
                province.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //http://www.weather.com.cn/data/city3jdata/provshi/10120.html
    private static void handleJsonCity(String json, String provinceId){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String id = (String) iterator.next();
                City city = new City();
                city.setCityId(provinceId+id);  //直接使用cityId会导致id重复
                city.setCityName(jsonObject.getString(id));
                city.setProvinceId(provinceId);
                city.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //http://www.weather.com.cn/data/city3jdata/station/1012002.html
    private static void handleJsonDistrict(String json, String cityId){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()){
                String id = (String) iterator.next();
                District district = new District();
                district.setDistrictId(id);
                district.setDistrictName(jsonObject.getString(id));
                district.setCityId(cityId);
                district.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //修正城市名，去除“市”、“区”等  例如，武汉市->武汉
    public static String correctCityName(String cityName){
        return cityName.replace("市","");
    }
}
