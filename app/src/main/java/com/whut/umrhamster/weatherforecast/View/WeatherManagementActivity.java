package com.whut.umrhamster.weatherforecast.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherManagementActivity extends AppCompatActivity {
    private TextView textViewEdit;
    private ImageView imageViewRefresh;
    private ImageView imageViewBack;

    private RecyclerView recyclerView;
    private WeatherManagementAdapter adapter;
    private List<Weather> weatherList;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_management);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.citySearchtb));
        initView();
    }
    private void initView(){
        imageViewRefresh = findViewById(R.id.ac_weathermanagement_refresh_iv);
        imageViewBack = findViewById(R.id.ac_weathermanagement_back_iv);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textViewEdit = findViewById(R.id.ac_weathermanagement_edit_tv);
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewEdit.getText().toString().equals("编辑")){
                    adapter.editStart(); //进入编辑模式，显示x按钮
                    textViewEdit.setText("完成");
                    textViewEdit.setTextColor(getColor(R.color.bluePopular));
                    imageViewRefresh.setVisibility(View.GONE);
                }else {
                    adapter.editEnd();;
                    textViewEdit.setText("编辑");
                    textViewEdit.setTextColor(getColor(R.color.textDarker));
                    imageViewRefresh.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView = findViewById(R.id.ac_weathermanagement_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        weatherList = LitePal.findAll(Weather.class,true); //激进查询，用于联表情况
        adapter = new WeatherManagementAdapter(this,weatherList);
        imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //处理全部刷新
                updateAllWeather();
            }
        });
        adapter.setOnItemClickListener(new WeatherManagementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type) {
                if (type == 0){
                    if (position+1 == adapter.getItemCount()){  //加号选项
                        startActivityForResult(new Intent(WeatherManagementActivity.this, CitySearchActivity.class),5);
                        overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
                    }else {
                        //让主界面跳转到相应城市进行显示
                        Toast.makeText(getApplicationContext(),weatherList.get(position).getCity(),Toast.LENGTH_SHORT).show();
                    }
                }else if (type == 1){
                    for (int i=0;i<6;i++){
                        weatherList.get(position).getForecast().get(i).delete();
                    }
                    weatherList.get(position).delete();
                    weatherList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void updateAllWeather(){
        new Thread(){
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient(); //放在循环外，避免重复创建
                for (int i=0;i<weatherList.size();i++){
                    String url = "http://wthrcdn.etouch.cn/weather_mini?city="+weatherList.get(i).getCity();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = null;
                    try {
                        response = okHttpClient.newCall(request).execute();
                        String json = response.body().string();
                        Weather weather = Utils.Json2Weather(json);
                        for (int j=0;j<6;j++){
                            weather.getForecast().get(j).update(weatherList.get(i).getForecast().get(j).getId());
                        }
                        weather.update(weatherList.get(i).getId());
                        final int k = i;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemChanged(k); //只更新需要更新的
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }


    private void addWeater(final String cityName){
        new Thread(new Runnable() {
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
                    final Weather weather = Utils.Json2Weather(json);  //构造weather对象，
                    for (int i=0;i<6;i++){
                        weather.getForecast().get(i).save();
                    }
                    weather.save();
                    weatherList.add(weather); //添加到集合中
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemChanged(weatherList.size()-1);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 6){
//            setResult(4,data);
//            finish();
            addWeater(data.getStringExtra("cityName"));

        }
    }

    @Override
    public void onBackPressed() {
        setResult(7);
        super.onBackPressed();
//        Log.d("WeatherManagement","hello");
        overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
    }
}
