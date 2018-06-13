package com.whut.umrhamster.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient = null;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.main_cityname_tv);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        mLocationClient.start();
    }


    private void initLocation(){
        LocationClientOption option  = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true); //设置地址信息可用
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("经度:").append(bdLocation.getLatitude())
//                    .append("\n");
//            stringBuilder.append("纬度:").append(bdLocation.getLongitude())
//                    .append("\n");
//            stringBuilder.append("定位方式:");
//            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
//                stringBuilder.append("GPS");
//            }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//                stringBuilder.append("网络");
//            }

            textView.setText(bdLocation.getDistrict());
        }
    }

}
