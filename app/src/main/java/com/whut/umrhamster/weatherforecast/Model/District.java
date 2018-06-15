package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

/**
 * Created by 12421 on 2018/6/13.
 */

public class District extends LitePalSupport {
    private int id;
    private String districtName;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
