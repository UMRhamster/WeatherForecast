package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by 12421 on 2018/6/13.
 */

public class District extends LitePalSupport implements Serializable{
    private String districtId;
    private String districtName;
    private String cityId;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


}
