package com.whut.umrhamster.weatherforecast.Model;

import com.whut.umrhamster.weatherforecast.R;

/**
 * Created by 12421 on 2018/6/18.
 */

public class WeatherUtils {
    //"高温 31°C"
    public static String getTemperatureFormated(String temperature){
        return temperature.substring(3,temperature.length()-1)+"°";
    }

    public static float getTemperatureFloat(String temperature){
        return Float.valueOf(temperature.substring(3,temperature.length()-1));
    }

    public static int getTemperatureInt(String temperature){
        return Integer.valueOf(temperature.substring(3,temperature.length()-1));
    }

    public static int getImgbyType(String type){
        switch (type){
            case "晴":
                return R.drawable.qingtian;
            case "阴":
                return R.drawable.yintian;
            case "小雨":
            case "小到中雨":
                return R.drawable.xiaoyu;
            case "中雨":
                return R.drawable.zhongyu;
            case "大雨":
                return R.drawable.dayu;
            case "多云":
                return R.drawable.duoyun;
            case "阵雨":
                return R.drawable.zhenyu;
            case "雷阵雨":
                return R.drawable.leizhenyu;
            default:
                return R.drawable.shachenbao;
        }
    }
}
