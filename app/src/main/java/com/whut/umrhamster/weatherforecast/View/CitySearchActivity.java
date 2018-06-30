package com.whut.umrhamster.weatherforecast.View;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.NetWorkUtils;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.Weather;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CitySearchActivity extends AppCompatActivity {
    private Button buttonMoreCity;
    private EditText editTextSearch;

    private RecyclerView recyclerView;
    private List<Object> objectList;
    private CitySearchAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private GridLayout gridLayout;
    private TextView textViewHotCity;
    private Context context;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.citySearchtb));

        initView();
        initEvent();

    }

    private void initView(){
        context = this;
        buttonMoreCity = findViewById(R.id.ac_citysearch_morecity_btn);
        editTextSearch = findViewById(R.id.ac_citychoose_et);
        recyclerView = findViewById(R.id.ac_citysearch_rv);
        gridLayout = findViewById(R.id.ac_citysearch_gl);
        textViewHotCity = findViewById(R.id.ac_citysearch_hotcity_tv);
        for (int i=0;i<gridLayout.getChildCount();i++){
            if (LitePal.isExist(Weather.class,"city = ?",((TextView)gridLayout.getChildAt(i)).getText().toString())){ //如果已存在改城市，变成灰色
                ((TextView)gridLayout.getChildAt(i)).setTextColor(getResources().getColor(R.color.hotCityTextColorChoosen,null));
            }
            gridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //热门城市点击事件
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先检查网络是否可用
                            if (!NetWorkUtils.checkNetState(getApplicationContext())){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(context,getResources().getString(R.string.tipContentNet));
                                        tipDialog.show();
                                    }
                                });
                                return;
                            }
                            //再检查是否有该城市的天气预报
                            if (Utils.hasWeather(((TextView)view).getText().toString())){
                                Intent intent = new Intent();
                                intent.putExtra("cityName",((TextView)view).getText());
                                setResult(6,intent);
                                finish();
                            }else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(context,getResources().getString(R.string.tipContentWeather));
                                        tipDialog.show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });
        }
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        objectList = new ArrayList<>();
        adapter = new CitySearchAdapter(objectList,this);
        adapter.setOnItemClickListener(new CitySearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                //对搜索的内容进行点击事件处理
                if (objectList.get(position) instanceof Province){
                    String provinceId = ((Province) objectList.get(position)).getProvinceId();
                    searchData("city",provinceId);
                }else if (objectList.get(position) instanceof City){
                    String cityId  = ((City) objectList.get(position)).getCityId();
                    searchData("district",cityId);
                }else if (objectList.get(position) instanceof District){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先检查网络是否可用
                            if (!NetWorkUtils.checkNetState(getApplicationContext())){
//                                Log.d("CitySearch","无网络");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(context,getResources().getString(R.string.tipContentNet));
                                        tipDialog.show();
                                    }
                                });
                                return;
                            }
                            //再检查是否有该城市的天气预报
                            if (Utils.hasWeather(((District) objectList.get(position)).getDistrictName())){
                                Intent intent = new Intent();
                                intent.putExtra("cityName",((District) objectList.get(position)).getDistrictName());
                                setResult(4,intent);
                                finish();
                            }else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(context,getResources().getString(R.string.tipContentWeather));
                                        tipDialog.show();
                                    }
                                });
                            }
                        }
                    }).start();
//                    Intent intent = new Intent();
//                    intent.putExtra("cityName",((District) objectList.get(position)).getDistrictName());
//                    setResult(4,intent);
//                    finish();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initEvent(){
        buttonMoreCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitySearchActivity.this,CityChooseActivity.class);
//                intent.putExtra("type","province");
//                intent.putExtra("object",new Province());  //此匿名Province对象无任何作用
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.d("CitySearch",String.valueOf(gridLayout.getChildCount()));
                objectList.clear();
                if (editable.length() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    buttonMoreCity.setVisibility(View.GONE);
                    textViewHotCity.setVisibility(View.INVISIBLE);
                    gridLayout.setVisibility(View.INVISIBLE);
                    List<Province> provinceList = LitePal.where("provinceName like ?","%"+editable.toString()+"%").find(Province.class);
                    if (provinceList != null){
                        objectList.addAll(provinceList);
                    }
                    List<City> cityList = LitePal.where("cityName like ?","%"+editable.toString()+"%").find(City.class);
                    if (cityList != null){
                        objectList.addAll(cityList);
                    }
                    List<District> districtList = LitePal.where("districtName like ?","%"+editable.toString()+"%").find(District.class);
                    if (cityList != null){
                        objectList.addAll(districtList);
                    }
                }else {
                    recyclerView.setVisibility(View.GONE);
                    buttonMoreCity.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.VISIBLE);
                    textViewHotCity.setVisibility(View.VISIBLE);
//                    for (int i=0;i<24;i++){
//                        gridLayout.getChildAt(i).setVisibility(View.VISIBLE);
//                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void searchData(final String string, final String id){
        new Thread(){
            @Override
            public void run() {
                objectList.clear();
                if ("city".equals(string)){
                    objectList.addAll(Utils.queryCity(id));
                }else if ("district".equals(string)){
                    objectList.addAll(Utils.queryDistrict(id));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2){
            setResult(6,data);
            finish();
        }
    }
}
