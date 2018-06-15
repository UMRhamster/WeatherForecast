package com.whut.umrhamster.weatherforecast.Model;

import org.litepal.crud.LitePalSupport;

/**
 * Created by 12421 on 2018/6/13.
 */

public class Province extends LitePalSupport {
    private int id;
    private String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
