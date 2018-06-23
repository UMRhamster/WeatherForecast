package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.whut.umrhamster.weatherforecast.MainActivity;
import com.whut.umrhamster.weatherforecast.Model.ChartUtils;
import com.whut.umrhamster.weatherforecast.Model.DailyWeather;
import com.whut.umrhamster.weatherforecast.Model.Lunar;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.Model.WeatherUtils;
import com.whut.umrhamster.weatherforecast.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12421 on 2018/6/12.
 */

public class FragmentWeather extends Fragment {
    private Weather weather;                 //天气对象，用于暂存天气数据
    private TextView textViewTemperature;    //温度
    private TextView textViewAqi;            //空气质量指数
    private TextView textViewWindCondition;  //风力风向
    private TextView textViewLunarDate;      //农历日期
    private TextView textViewSituation;      //天气情况

    private TextView textViewThird;
    private TextView textViewForth;
    private TextView textViewFifth;
    private TextView textViewSixth;
    private ImageView imageViewYesterday;
    private ImageView imageViewToday;
    private ImageView imageViewThird;
    private ImageView imageViewForth;
    private ImageView imageViewFifth;
    private ImageView imageViewSixth;

    private TextView[] textViewDate = new TextView[6];

    //以下显示三天情况
    private TextView[] textViewThree = new TextView[2];
    private ImageView[] imageViewsThree = new ImageView[3];
    private TextView[] textViewsThreeHigh = new TextView[3];
    private TextView[] textViewsThreeLow = new TextView[3];
    private TextView[] textViewsThreeType = new TextView[3];
    //温馨提示
    private TextView textViewTip;

    private LineChart lineChart; //6天天气预报折线图

    private NestedScrollView scrollView;
    private RelativeLayout relativeLayout;
//    Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather,container,false);
        initView(view);    //初始化控件
        initData();  //开启线程进行网络请求,更新后不需要开启线程
        return view;
    }

    private void initView(View view){
        textViewTemperature = view.findViewById(R.id.weather_fg_temperature);
        textViewAqi = view.findViewById(R.id.weather_fg_aqi);
        textViewWindCondition = view.findViewById(R.id.weather_fg_windcondition);
        textViewLunarDate = view.findViewById(R.id.weather_fg_lunardate);
        textViewSituation = view.findViewById(R.id.weather_fg_situation);
        //
        textViewThird = view.findViewById(R.id.fg_weather_third_tv);
        textViewForth = view.findViewById(R.id.fg_weather_forth_tv);
        textViewFifth = view.findViewById(R.id.fg_weather_fifth_tv);
        textViewSixth = view.findViewById(R.id.fg_weather_sixth_tv);
        imageViewYesterday = view.findViewById(R.id.fg_weather_yesterday_iv);
        imageViewToday = view.findViewById(R.id.fg_weather_today_iv);
        imageViewThird = view.findViewById(R.id.fg_weather_third_iv);
        imageViewForth = view.findViewById(R.id.fg_weather_forth_iv);
        imageViewFifth = view.findViewById(R.id.fg_weather_fifth_iv);
        imageViewSixth = view.findViewById(R.id.fg_weather_sixth_iv);
        //
        lineChart = view.findViewById(R.id.fg_weather_lc);
        //
        textViewDate[0] = view.findViewById(R.id.fg_weather_yesterday_date_tv);
        textViewDate[1] = view.findViewById(R.id.fg_weather_today_date_tv);
        textViewDate[2] = view.findViewById(R.id.fg_weather_third_date_tv);
        textViewDate[3] = view.findViewById(R.id.fg_weather_forth_date_tv);
        textViewDate[4] = view.findViewById(R.id.fg_weather_fifth_date_tv);
        textViewDate[5] = view.findViewById(R.id.fg_weather_sixth_date_tv);
        //对三天情况的初始化
        textViewThree[0] = view.findViewById(R.id.fg_weather_threedaytwo_tv);
        textViewThree[1] = view.findViewById(R.id.fg_weather_threedaythree_tv);
        imageViewsThree[0] = view.findViewById(R.id.fg_weather_threedayone_iv);
        imageViewsThree[1] = view.findViewById(R.id.fg_weather_threedaytwo_iv);
        imageViewsThree[2] = view.findViewById(R.id.fg_weather_threedaythree_iv);
        textViewsThreeHigh[0] = view.findViewById(R.id.fg_weather_threedayone_high_tv);
        textViewsThreeHigh[1] = view.findViewById(R.id.fg_weather_threedaytwo_high_tv);
        textViewsThreeHigh[2] = view.findViewById(R.id.fg_weather_threedaythree_high_tv);
        textViewsThreeLow[0] = view.findViewById(R.id.fg_weather_threedayone_low_tv);
        textViewsThreeLow[1] = view.findViewById(R.id.fg_weather_threedaytwo_low_tv);
        textViewsThreeLow[2] = view.findViewById(R.id.fg_weather_threedaythree_low_tv);
        textViewsThreeType[0] = view.findViewById(R.id.fg_weather_threedayone_type_tv);
        textViewsThreeType[1] = view.findViewById(R.id.fg_weather_threedaytwo_type_tv);
        textViewsThreeType[2] = view.findViewById(R.id.fg_weather_threedaythree_type_tv);

        textViewTip = view.findViewById(R.id.fg_weather_ganmao_tv);
        scrollView = view.findViewById(R.id.fg_weather_nsv);
        relativeLayout = view.findViewById(R.id.fg_weather_rv);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("scrollListener",String.valueOf(scrollY));
                if (scrollY < 360){
                    relativeLayout.setBackgroundColor(Color.argb(255*scrollY/613,0,0,0));
                    ((MainActivity)getActivity()).setAlpha(255*scrollY/613);
                }
            }
        });
    }
    private void initData(){
        weather = (Weather) getArguments().getSerializable("weather");
        DailyWeather today = weather.getForecast().get(0);
        textViewTemperature.setText(weather.getWendu());
        if (weather.getAqi().equals("无")){
            textViewAqi.setText(getResources().getString(R.string.aqiNo));
        }else {
            textViewAqi.setText(String.format(getResources().getString(R.string.aqi),weather.getWendu()));
        }
        textViewWindCondition.setText(String.format(getResources().getString(R.string.windCondition),today.getFx(),Utils.getfl(today.getFl())));
        Calendar calendar = Calendar.getInstance();
        Lunar lunar = new Lunar(calendar);
        String month = Utils.numberChange(calendar.get(Calendar.MONTH)+1);
        String day = Utils.numberChange(calendar.get(Calendar.DAY_OF_MONTH));
        String week = Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK));
        textViewLunarDate.setText(String.format(getResources().getString(R.string.lunardate),month,day,week,lunar.getMonthAndDay()));
        textViewSituation.setText(today.getType());
        ChartUtils.initChart(lineChart,weather);
        ChartUtils.setChartData(getActivity().getApplicationContext(),lineChart,weather);

        textViewThird.setText(Utils.getWeekByString(weather.getForecast().get(1).getDate()));
        textViewForth.setText(Utils.getWeekByString(weather.getForecast().get(2).getDate()));
        textViewFifth.setText(Utils.getWeekByString(weather.getForecast().get(3).getDate()));
        textViewSixth.setText(Utils.getWeekByString(weather.getForecast().get(4).getDate()));
        imageViewYesterday.setImageResource(WeatherUtils.getImgbyType(weather.getYesterday().getType()));
        imageViewToday.setImageResource(WeatherUtils.getImgbyType(weather.getForecast().get(0).getType()));
        imageViewThird.setImageResource(WeatherUtils.getImgbyType(weather.getForecast().get(1).getType()));
        imageViewForth.setImageResource(WeatherUtils.getImgbyType(weather.getForecast().get(2).getType()));
        imageViewFifth.setImageResource(WeatherUtils.getImgbyType(weather.getForecast().get(3).getType()));
        imageViewSixth.setImageResource(WeatherUtils.getImgbyType(weather.getForecast().get(4).getType()));
        //
        textViewTip.setText(weather.getGanmao());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        textViewDate[0].setText(String.format(getResources().getString(R.string.date),Utils.numberChange(calendar.get(Calendar.MONTH)+1),
                Utils.numberChange(calendar.get(Calendar.DAY_OF_MONTH))));
        calendar.add(Calendar.DAY_OF_MONTH,+1);
        for (int i=1;i<6;i++){
            textViewDate[i].setText(String.format(getResources().getString(R.string.date),Utils.numberChange(calendar.get(Calendar.MONTH)+1),
                    Utils.numberChange(calendar.get(Calendar.DAY_OF_MONTH))));
            calendar.add(Calendar.DAY_OF_MONTH,+1);
        }
        //三天情况数据加入
        textViewThree[0].setText(Utils.getWeekByString(weather.getForecast().get(1).getDate()));
        textViewThree[1].setText(Utils.getWeekByString(weather.getForecast().get(2).getDate()));
        for (int i=0;i<3;i++){
            imageViewsThree[i].setImageResource(WeatherUtils.getImgbyType(weather.getForecast().get(i).getType()));
            textViewsThreeHigh[i].setText(WeatherUtils.getTemperatureFormated(weather.getForecast().get(i).getHigh()));
            textViewsThreeLow[i].setText(WeatherUtils.getTemperatureFormated(weather.getForecast().get(i).getLow()));
            textViewsThreeType[i].setText(weather.getForecast().get(i).getType());
        }
//        new Thread(){
//            @Override
//            public void run() {
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("http://wthrcdn.etouch.cn/weather_mini?city=").append(getArguments().getString("place"));
//                OkHttpClient okHttpClient = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url(stringBuilder.toString())
//                        .build();
//                Response response = null;
//                try {
//                    response = okHttpClient.newCall(request).execute();
//                    String json = response.body().string();
//                    weather = Utils.Json2Weather(json);
////                    Log.d("FragmentWeatherinitData",weather.getCity());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            //此处更新ui
//                            DailyWeather today = weather.getForecast().get(0);
//                            textViewTemperature.setText(weather.getWendu());
//                            textViewWCTemperature.setText(String.format(getResources().getString(R.string.WCTemperature),weather.getWendu()));
//                            textViewWindCondition.setText(String.format(getResources().getString(R.string.windCondition),today.getFx(),Utils.getfl(today.getFl())));
//                            Calendar calendar = Calendar.getInstance();
//                            Lunar lunar = new Lunar(calendar);
//                            String month = Utils.numberChange(calendar.get(Calendar.MONTH)+1);
//                            String day = Utils.numberChange(calendar.get(Calendar.DAY_OF_MONTH));
//                            String week = Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK));
//                            textViewLunarDate.setText(String.format(getResources().getString(R.string.lunardate),month,day,week,lunar.getMonthAndDay()));
//                            textViewSituation.setText(today.getType());
//
//                            //得到weather以后再绘制图表
//                            ChartUtils.initChart(lineChart,weather);
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    //设置滑动位置
    public void setScrollTo(int y){
        scrollView.setScrollY(y);
    }
    //返回当前滑动的位置
    public int getScrollPosition(){
        return scrollView.getScrollY();
    }
    //获取当前天气卡片背景透明度
    public Drawable getAlpha(){
        return relativeLayout.getBackground();
    }
    //设置背景透明度
    public void setAlpha(Drawable drawable){
        relativeLayout.setBackground(drawable);
    }

}
