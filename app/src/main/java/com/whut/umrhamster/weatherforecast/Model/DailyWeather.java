package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by 12421 on 2018/6/13.
 */

public class DailyWeather extends LitePalSupport implements Serializable{
    private int id;
    private String date;  //日期, 13号星期五
    private String high;  //最高温度
    private String low;   //最低温度
    private String fx;    //风向
    private String fl;    //风力
    private String type;  //天气类型

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
