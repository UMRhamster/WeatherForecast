package com.whut.umrhamster.weatherforecast.View;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.DailyWeather;
import com.whut.umrhamster.weatherforecast.Model.Lunar;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
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
    private TextView textViewWCTemperature;  //体感温度
    private TextView textViewWindCondition;  //风力风向
    private TextView textViewLunarDate;      //农历日期
    private TextView textViewSituation;      //天气情况


    Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather,container,false);
        initView(view);    //初始化控件
        initData();  //开启线程进行网络请求
        return view;
    }

    private void initView(View view){
        textViewTemperature = view.findViewById(R.id.weather_fg_temperature);
        textViewWCTemperature = view.findViewById(R.id.weather_fg_wctemperature);
        textViewWindCondition = view.findViewById(R.id.weather_fg_windcondition);
        textViewLunarDate = view.findViewById(R.id.weather_fg_lunardate);
        textViewSituation = view.findViewById(R.id.weather_fg_situation);
    }
    private void initData(){
        new Thread(){
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("http://wthrcdn.etouch.cn/weather_mini?city=").append(getArguments().getString("place"));
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(stringBuilder.toString())
                        .build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    String json = response.body().string();
                    weather = Utils.Json2Weather(json);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //此处更新ui
                            DailyWeather today = weather.getForecast().get(0);
                            textViewTemperature.setText(weather.getWendu());
                            textViewWCTemperature.setText(String.format(getResources().getString(R.string.WCTemperature),weather.getWendu()));
                            textViewWindCondition.setText(String.format(getResources().getString(R.string.windCondition),today.getFx(),Utils.getfl(today.getFl())));
                            Calendar calendar = Calendar.getInstance();
                            Lunar lunar = new Lunar(calendar);
                            String month = Utils.numberChange(calendar.get(Calendar.MONTH)+1);
                            String day = Utils.numberChange(calendar.get(Calendar.DAY_OF_MONTH));
                            String week = Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK));
                            textViewLunarDate.setText(String.format(getResources().getString(R.string.lunardate),month,day,week,lunar.getMonthAndDay()));
                            textViewSituation.setText(today.getType());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public Weather getWeather() {
        return weather;
    }
}
