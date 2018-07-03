package com.whut.umrhamster.weatherforecast.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.whut.umrhamster.weatherforecast.Model.*;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateService extends Service {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("UpdateService","onStartCommand");
        updateWeather();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 2*60*60*1000;
        Intent i = new Intent(this,UpdateService.class);
        PendingIntent pendingIntent  = PendingIntent.getService(this,0,i,0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+time,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        Log.d("UpdateService","后台更新天气数据...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Weather> weatherList = LitePal.findAll(Weather.class,true);
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
                        Weather weather = com.whut.umrhamster.weatherforecast.Model.Utils.Json2Weather(json);
                        for (int j=0;j<6;j++){
                            weather.getForecast().get(j).update(weatherList.get(i).getForecast().get(j).getId());
                        }
                        weather.update(weatherList.get(i).getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
