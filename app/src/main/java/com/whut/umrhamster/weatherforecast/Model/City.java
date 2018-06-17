package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by 12421 on 2018/6/13.
 */

public class City  extends LitePalSupport implements Serializable{
    private String cityId;
    private String cityName;
    private String provinceId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
