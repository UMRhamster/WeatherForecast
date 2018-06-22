package com.whut.umrhamster.weatherforecast;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.View.CitySearchActivity;
import com.whut.umrhamster.weatherforecast.View.FragmentWeather;
import com.whut.umrhamster.weatherforecast.View.MyFragmentPagerView;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//序列化时，一定要注意所依赖的对象也要实现序列化接口
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public LocationClient mLocationClient = null;
    private TextView textViewTitle;
    private ViewPager viewPager;
    private MyFragmentPagerView myFragmentPagerView;
    private List<Fragment> fragmentList;   //用于存储显示天气预报的fragment
    private boolean isLocated = false;             //是否第一次定位
    private LinearLayout linearLayout;
//    private List<String> cityNameList;  //用于保存城市名，防止viewpager快速滑动崩溃问题
    //维护一个List<Weather> 用于管理各城市天气，也可以解决上述快速滑动，weather对象为null导致崩溃问题
    private List<Weather> weatherList;

    private CoordinatorLayout coordinatorLayout;

    Handler handler = new Handler();
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
        weatherList = new ArrayList<>();
        toolbar = findViewById(R.id.main_tb);
        textViewTitle = findViewById(R.id.main_cityname_tv);
        viewPager = findViewById(R.id.main_vp);
        linearLayout = findViewById(R.id.main_tb_ll);
        fragmentList = new ArrayList<>();
//        cityNameList = new ArrayList<>();
        myFragmentPagerView = new MyFragmentPagerView(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerView);
        //背景
        coordinatorLayout = findViewById(R.id.main_cl);
//        changeColor();
    }
    private void initEvent(){
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CitySearchActivity.class),3);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,CitySearchActivity.class),3);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("MainActivity","onPageSelected");
//                textViewTitle.setText(cityNameList.get(position));
                textViewTitle.setText(weatherList.get(position).getCity());
                ((ImageView)linearLayout.getChildAt(position)).setImageResource(R.drawable.point_bright);
                for (int i=0;i<linearLayout.getChildCount();i++){
                    if (i !=position )
                    ((ImageView)linearLayout.getChildAt(i)).setImageResource(R.drawable.point_dark);
                }
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
                textViewTitle.setText(Utils.correctCityName(bdLocation.getCity()));    //获得地区名，例如：武昌区
                //通过定位获得地区名，创建fragment
                getWeatherData(Utils.correctCityName(bdLocation.getCity()));
//                addFragment(Utils.correctCityName(bdLocation.getCity()));
                isLocated = true;
            }
        }
    }

    private void addFragment(final Weather weather){
//        cityNameList.add(cityName);

        handler.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("weather",weather);
                FragmentWeather fragmentWeather = new FragmentWeather();
                fragmentWeather.setArguments(bundle);

                fragmentList.add(fragmentWeather);
                myFragmentPagerView.notifyDataSetChanged();

                ImageView imageView = new ImageView(getApplicationContext());
                if (linearLayout.getChildCount() == 0){
                    imageView.setImageResource(R.drawable.point_bright);  //如何之前没有天气卡片，则默认显示第一个被选中，且只有一个，亮色
                }else {
                    imageView.setImageResource(R.drawable.point_dark);    //如果已经有天气卡片，则默认不被选中，为暗色
                }
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Utils.dp2px(getApplicationContext(),10),Utils.dp2px(getApplicationContext(),10));
                imageView.setLayoutParams(param);
                linearLayout.addView(imageView);
            }
        });
    }

    //先在主界面产生weather对象，在生成weatherfragment进行天气显示
    private void getWeatherData(final String cityName){
        new Thread(){
            @Override
            public void run() {
                String url = "http://wthrcdn.etouch.cn/weather_mini?city="+cityName;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    String json = response.body().string();
                    Weather weather = Utils.Json2Weather(json);  //构造weather对象，
                    weatherList.add(weather); //添加到集合中
                    addFragment(weather);   //生成天气界面
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 4){
            getWeatherData(data.getStringExtra("cityName"));
//            addFragment(data.getStringExtra("cityName"));
        }
    }

    private void changeColor(){
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{getResources().getDrawable(R.mipmap.main_bg,null),
                getResources().getDrawable(R.mipmap.bg_rain,null)});
        coordinatorLayout.setBackground(transitionDrawable);
        transitionDrawable.startTransition(2000);
    }
}
