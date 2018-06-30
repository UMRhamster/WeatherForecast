package com.whut.umrhamster.weatherforecast.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.Model.SPUtils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class WeatherManagementActivity extends AppCompatActivity {
    private TextView textViewEdit;

    private RecyclerView recyclerView;
    private WeatherManagementAdapter adapter;
    private List<Weather> weatherList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_management);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.citySearchtb));
        initView();
    }
    private void initView(){
        textViewEdit = findViewById(R.id.ac_weathermanagement_edit_tv);
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewEdit.getText().toString().equals("编辑")){
                    adapter.editStart(); //进入编辑模式，显示x按钮
                    textViewEdit.setText("完成");
                }else {
                    adapter.editEnd();;
                    textViewEdit.setText("编辑");
                }
            }
        });
        recyclerView = findViewById(R.id.ac_weathermanagement_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        weatherList = LitePal.findAll(Weather.class,true); //激进查询，用于联表情况
        adapter = new WeatherManagementAdapter(this,weatherList);
        adapter.setOnItemClickListener(new WeatherManagementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type) {
                if (type == 0){
                    if (position+1 == adapter.getItemCount()){  //加号选项
                        startActivityForResult(new Intent(WeatherManagementActivity.this, CitySearchActivity.class),5);
                        overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
                    }else {
                        //让主界面跳转到相应城市进行显示
                        Toast.makeText(getApplicationContext(),"你好",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == 6){
            setResult(4,data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(7);
        Log.d("WeatherManagement","hello");
        finish();
    }
}
