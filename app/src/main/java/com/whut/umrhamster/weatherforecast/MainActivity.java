package com.whut.umrhamster.weatherforecast;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.whut.umrhamster.weatherforecast.View.CitySearchActivity;
import com.whut.umrhamster.weatherforecast.View.FragmentWeather;
import com.whut.umrhamster.weatherforecast.View.MyFragmentPagerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public LocationClient mLocationClient = null;
    private TextView textViewTitle;
    private ViewPager viewPager;
    private MyFragmentPagerView myFragmentPagerView;
    private List<Fragment> fragmentList;   //用于存储显示天气预报的fragment
    private boolean isLocated = false;             //是否第一次定位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);   //初始化litepal


        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        initView();   //初始化空控件
        initEvent();  //初始化事件
        mLocationClient.start();

    }


    private void initLocation(){
        LocationClientOption option  = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true); //设置地址信息可用
        mLocationClient.setLocOption(option);
    }
    private void initView(){
        toolbar = findViewById(R.id.main_tb);
        textViewTitle = findViewById(R.id.main_cityname_tv);
        viewPager = findViewById(R.id.main_vp);
        fragmentList = new ArrayList<>();
        myFragmentPagerView = new MyFragmentPagerView(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerView);
    }
    private void initEvent(){
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CitySearchActivity.class));
            }
        });
        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CitySearchActivity.class));
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewTitle.setText(((FragmentWeather)fragmentList.get(position)).getWeather().getCity());
//                Log.d("MainActivity",((FragmentWeather)fragmentList.get(position)).getWeather().getCity());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (!isLocated){
                textViewTitle.setText(bdLocation.getCity());    //获得地区名，例如：武昌区
                //通过定位获得地区名，创建fragment
                addFragment(bdLocation.getCity());
                isLocated = true;
            }
        }
    }

    private void addFragment(String cityName){
        Bundle bundle = new Bundle();
        bundle.putString("place",cityName);
        FragmentWeather fragmentWeather = new FragmentWeather();
        fragmentWeather.setArguments(bundle);
        fragmentList.add(fragmentWeather);
        myFragmentPagerView.notifyDataSetChanged();
    }
}
