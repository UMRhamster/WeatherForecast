package com.whut.umrhamster.weatherforecast.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.NetWorkUtils;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class FragmentCityChoose extends Fragment {
    private ImageView imageViewBack;
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private CityChooseAdapter adapter;
    private List<City> cityList;
    private TextView textViewTitle;

    private Province province;
    Handler handler = new Handler();
    //以下用于搜索
    private RecyclerView recyclerViewForSearch;
    private List<Object> objectList;
    private CitySearchAdapter adapterForSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_choose,container,false);
        initView(view);
        initEvent();
        initData();
        return view;
    }
    private void initView(View view){
        imageViewBack = view.findViewById(R.id.fg_city_choose_back_iv);
        textViewTitle = view.findViewById(R.id.fg_city_choose_tv);
        province = (Province) getArguments().getSerializable("province");
        textViewTitle.setText(province.getProvinceName());  //设置标题与选择城市对应
        editTextSearch = view.findViewById(R.id.fg_city_search_et);
        recyclerView = view.findViewById(R.id.fg_city_choose_rv);
        recyclerViewForSearch = view.findViewById(R.id.fg_city_choose_rv_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerForSearch = new LinearLayoutManager(getActivity());
        recyclerViewForSearch.setLayoutManager(linearLayoutManagerForSearch);
        cityList = new ArrayList<>();
        objectList = new ArrayList<>();
//        cityList = Utils.queryCity();
        adapter = new CityChooseAdapter(cityList,getActivity());
        adapterForSearch = new CitySearchAdapter(objectList,getActivity());
        adapterForSearch.setOnItemClickListener(new CitySearchAdapter.OnItemClickListener() {
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
                            if (!NetWorkUtils.checkNetState(getActivity())){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(getActivity(),getResources().getString(R.string.tipContentNet));
                                        tipDialog.show();
                                    }
                                });
                                return;
                            }
                            //再检查是否有该城市的天气预报
                            if (Utils.hasWeather(((District) objectList.get(position)).getDistrictName())){
                                Intent intent = new Intent();
                                intent.putExtra("cityName",((District) objectList.get(position)).getDistrictName());
                                getActivity().setResult(2,intent);
                                getActivity().finish();
                            }else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TipDialog tipDialog = new TipDialog(getActivity(),getResources().getString(R.string.tipContentWeather));
                                        tipDialog.show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
        adapter.setOnItemClickListener(new CityChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDistrictChoose fragmentDistrictChoose = new FragmentDistrictChoose();
                Bundle bundle = new Bundle();
                bundle.putSerializable("city",cityList.get(position));
                bundle.putSerializable("province",province);
                fragmentDistrictChoose.setArguments(bundle);
                fm.beginTransaction().setCustomAnimations(R.anim.anim_fg_enter,R.anim.anim_fg_out).replace(R.id.ac_citychoose_cl,fragmentDistrictChoose).commit();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerViewForSearch.setAdapter(adapterForSearch);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //交给activity处理，返回上一级
            }
        });
    }
    private void initEvent(){
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("FragmentProvinceChoose","afterTextChanged");
                objectList.clear();
                if (editable.length() > 0){
                    recyclerView.setVisibility(View.GONE);
                    recyclerViewForSearch.setVisibility(View.VISIBLE);
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
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewForSearch.setVisibility(View.GONE);
                }
                adapterForSearch.notifyDataSetChanged();
            }
        });
    }

    private void initData(){
        new Thread(){
            @Override
            public void run() {
                cityList.clear();
                Log.d("FragmentCityChoose111",province.getProvinceId());
                cityList.addAll(Utils.queryCity(province.getProvinceId()));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
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
                        adapterForSearch.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
}
