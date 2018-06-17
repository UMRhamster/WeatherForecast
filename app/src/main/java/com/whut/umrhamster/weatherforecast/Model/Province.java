package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by 12421 on 2018/6/13.
 */

public class Province extends LitePalSupport implements Serializable{
    private String provinceId;
    private String provinceName;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
