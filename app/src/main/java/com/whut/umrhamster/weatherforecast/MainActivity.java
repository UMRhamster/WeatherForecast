package com.whut.umrhamster.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.whut.umrhamster.weatherforecast.Controller.UpdateService;
import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.DailyWeather;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.Model.WeatherUtils;
import com.whut.umrhamster.weatherforecast.View.AskLocationDialog;
import com.whut.umrhamster.weatherforecast.View.FragmentWeather;
import com.whut.umrhamster.weatherforecast.View.MyFragmentPagerView;
import com.whut.umrhamster.weatherforecast.View.TipDialog;
import com.whut.umrhamster.weatherforecast.View.WeatherManagementActivity;

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
    private List<FragmentWeather> fragmentList;   //用于存储显示天气预报的fragment
    private boolean isLocated = false;             //是否第一次定位
    private LinearLayout linearLayout;
//    private List<String> cityNameList;  //用于保存城市名，防止viewpager快速滑动崩溃问题
    //维护一个List<Weather> 用于管理各城市天气，也可以解决上述快速滑动，weather对象为null导致崩溃问题
    private List<Weather> weatherList;
    //维护一个position保存当前位置
    private int currentPosition = 0;

    private CoordinatorLayout coordinatorLayout;
    private Context context;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);   //初始化litepal
        if (LitePal.count(Province.class) < 1){
            Utils.initAllCitys();
        }
        Intent intent = new Intent(this, UpdateService.class);
        startService(intent);
//        LitePal.deleteAll(Weather.class);
//        LitePal.deleteAll(DailyWeather.class);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        initView();   //初始化空控件
        initEvent();  //初始化事件
        mLocationClient.start();

        //天气数据 数据库
        List<Weather> weathers = LitePal.findAll(Weather.class);
        Log.d("MainActivity","数据库中天气数据数量为："+weathers.size());
        Log.d("MainActivity","数据库中每日天气数量为："+LitePal.count(DailyWeather.class));
        Log.d("MainActivity","数据库中的省数量为："+LitePal.count(Province.class));
        Log.d("MainActivity","数据库中的市数量为："+LitePal.count(City.class));
        Log.d("MainActivity","数据库中的区数量为："+LitePal.count(District.class));
    }


    private void initLocation(){
        LocationClientOption option  = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true); //设置地址信息可用
        mLocationClient.setLocOption(option);
    }
    private void initView(){
        context = this;
        toolbar = findViewById(R.id.main_tb);
        textViewTitle = findViewById(R.id.main_cityname_tv);
        viewPager = findViewById(R.id.main_vp);
        linearLayout = findViewById(R.id.main_tb_ll);
        coordinatorLayout = findViewById(R.id.main_cl);
        fragmentList = new ArrayList<>();
        weatherList = LitePal.findAll(Weather.class,true); //从数据库中获取天气信息,注意 需要激进查找
        myFragmentPagerView = new MyFragmentPagerView(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerView);
        initWeatherFragment();   //初始化天气卡片，显示数据中已经存在的天气
    }
    private void initEvent(){
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, WeatherManagementActivity.class),3);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,WeatherManagementActivity.class),3);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (fragmentList.size() > 1){
                    if (currentPosition == 0){
                        if(fragmentList.get(1).isVisible()){
                            fragmentList.get(1).setScrollTo(fragmentList.get(0).getScrollPosition());
                            fragmentList.get(1).setAlpha(fragmentList.get(0).getAlpha());
                        }
                    }else if (currentPosition == fragmentList.size()-1){
                        if (fragmentList.get(fragmentList.size()-2).isVisible()){
                            fragmentList.get(fragmentList.size()-2).setScrollTo(fragmentList.get(currentPosition).getScrollPosition());
                            fragmentList.get(fragmentList.size()-2).setAlpha(fragmentList.get(currentPosition).getAlpha());
                        }
                    }else {
                        if (fragmentList.get(currentPosition-1).isVisible()){
                            fragmentList.get(currentPosition-1).setScrollTo(fragmentList.get(currentPosition).getScrollPosition());
                            fragmentList.get(currentPosition-1).setAlpha(fragmentList.get(currentPosition).getAlpha());
                        }
                        if (fragmentList.get(currentPosition+1).isVisible()){  //先判断fragment是否已经可见
                            fragmentList.get(currentPosition+1).setScrollTo(fragmentList.get(currentPosition).getScrollPosition());
                            fragmentList.get(currentPosition+1).setAlpha(fragmentList.get(currentPosition).getAlpha());
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                switch (weatherList.get(position).getForecast().get(1).getType()){ //背景渐变替换
                    case "晴":
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.main_bg);
                        break;
                    case "小雨":      //
                    case "小到中雨":  //两者显示一样
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.bg_rain);
                        break;
                    case "多云":
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.bg_duoyun);
                        break;
                    case "阴":
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.bg_ying);
                        break;
                    case "暴雨":
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.bg_leizhenyu);
                        break;
                    case "阵雨":
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.bg_zhenyu);
                        break;
                    default:
                        changeBackGround(coordinatorLayout.getBackground(),R.mipmap.main_bg);
                        break;
                }
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
        public void onReceiveLocation(final BDLocation bdLocation) {
            if (!isLocated){
                //通过定位获得城市名，判断是否已有该城市，若无，进行提示是否添加
                if (!LitePal.isExist(Weather.class,"city = ?",Utils.correctCityName(bdLocation.getCity()))){
//                    Log.d("MAINlOCATION",Utils.correctCityName(bdLocation.getCity()));
                    AskLocationDialog askLocationDialog = new AskLocationDialog(context,String.format(getResources().getString(R.string.askLocation),Utils.correctCityName(bdLocation.getCity())));
                    askLocationDialog.setOnClickListener(new AskLocationDialog.OnClickListener() {
                        @Override
                        public void onClick() {
                            getWeatherData(Utils.correctCityName(bdLocation.getCity()));
                        }
                    });
                    askLocationDialog.show();
                }
                isLocated = true;
            }
        }
    }
    //初始化天气数据，从数据库中添加
    private void initWeatherFragment(){
        if (weatherList.size() >0){
            //设置初始化标题
            textViewTitle.setText(weatherList.get(0).getCity());
            //设置初始化背景
            coordinatorLayout.setBackground(getDrawable(WeatherUtils.getBGbyType(weatherList.get(0).getForecast().get(1).getType())));
        }
        for (int i=0;i<weatherList.size();i++){
            Bundle bundle = new Bundle();
            bundle.putSerializable("weather",weatherList.get(i));
            FragmentWeather fragmentWeather = new FragmentWeather();
            fragmentWeather.setArguments(bundle);
            fragmentList.add(fragmentWeather);
            addTagPoint();
        }
        myFragmentPagerView.notifyDataSetChanged();
//        Log.d("MainActivity update",myFragmentPagerView.getCount()+" "+viewPager.getChildCount());
    }
    private void addFragment(final Weather weather){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("weather",weather);
                FragmentWeather fragmentWeather = new FragmentWeather();
                fragmentWeather.setArguments(bundle);

                fragmentList.add(fragmentWeather);
                myFragmentPagerView.notifyDataSetChanged();
                addTagPoint();
                if (fragmentList.size() == 1){  //即是第一次添加
                    textViewTitle.setText(weatherList.get(0).getCity());
                    coordinatorLayout.setBackground(getDrawable(WeatherUtils.getBGbyType(weatherList.get(0).getForecast().get(1).getType())));
                }
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
                    for (int i=0;i<6;i++){
                        weather.getForecast().get(i).save();
                    }
                    weather.save();
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
        if (requestCode == 3 && resultCode == 7){
            updateWeather();
        }
    }

    private void updateWeather(){
        Log.d("MainActivity","update");
        weatherList.clear();
        weatherList.addAll(LitePal.findAll(Weather.class,true));
//        Log.d("MainActivity update",String.valueOf(weatherList.size()));
        fragmentList.clear();
        viewPager.removeAllViews();
        linearLayout.removeAllViews();
        initWeatherFragment();
//        myFragmentPagerView.notifyDataSetChanged();
    }

    private void changeBackGround(Drawable oldBg, int newBg){
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{oldBg,
                getResources().getDrawable(newBg,null)});
        coordinatorLayout.setBackground(transitionDrawable);
        transitionDrawable.startTransition(800);
    }

    //跟随fragmentweather一起改变透明度
    public void setAlpha(int alpha){
        toolbar.setBackgroundColor(Color.argb(alpha,0,0,0));
    }

    private void addTagPoint(){
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

    public void updateFragment(Bundle bundle) {
        fragmentList.get(viewPager.getCurrentItem()).setArguments(bundle);
        myFragmentPagerView.notifyDataSetChanged();
    }

}
