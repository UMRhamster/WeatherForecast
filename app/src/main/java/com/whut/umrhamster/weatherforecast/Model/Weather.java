package com.whut.umrhamster.weatherforecast.Model;

import java.util.List;

/**
 * Created by 12421 on 2018/6/13.
 */

//总天气情况
public class Weather {
    private DailyWeather yesterday;        //昨天天气
    private List<DailyWeather> forecast;   //今天及以后天气情况
    private String city;                   //城市
    private String ganmao;                 //感冒指数
    private String wendu;                  //体感温度

    public DailyWeather getYesterday() {
        return yesterday;
    }

    public void setYesterday(DailyWeather yesterday) {
        this.yesterday = yesterday;
    }

    public List<DailyWeather> getForecast() {
        return forecast;
    }

    public void setForecast(List<DailyWeather> forecast) {
        this.forecast = forecast;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }
}
