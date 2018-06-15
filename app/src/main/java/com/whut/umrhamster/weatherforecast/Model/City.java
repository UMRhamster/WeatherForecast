package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

/**
 * Created by 12421 on 2018/6/13.
 */

public class City  extends LitePalSupport {
    private int id;
    private String cityName;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
