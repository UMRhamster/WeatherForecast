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
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.NetWorkUtils;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.Model.WeatherUtils;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class FragmentDistrictChoose extends Fragment {
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private DistrictChooseAdapter adapter;
    private List<District> districtList;
    private TextView textViewTitle;
    private ImageView imageViewBack;

    private City city;
    Handler handler = new Handler();
    //一下用于搜索
    private RecyclerView recyclerViewForSearch;
    private List<Object> objectList;
    private CitySearchAdapter adapterForSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_choose,container,false);
        initView(view);
        initEvent();
        initData();
        return view;
    }
    private void initView(View view){
        imageViewBack = view.findViewById(R.id.fg_district_back_iv);
        textViewTitle = view.findViewById(R.id.fg_district_choose_tv);
        city = (City)getArguments().getSerializable("city");
        textViewTitle.setText(city.getCityName());  //设置标题与选择城市对应
        editTextSearch = view.findViewById(R.id.fg_district_search_et);
        recyclerView = view.findViewById(R.id.fg_district_choose_rv);
        recyclerViewForSearch = view.findViewById(R.id.fg_district_choose_rv_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerForSearch = new LinearLayoutManager(getActivity());
        recyclerViewForSearch.setLayoutManager(linearLayoutManagerForSearch);
        districtList = new ArrayList<>();
        objectList = new ArrayList<>();
//        districtList = Utils.queryDistrict();
        adapter = new DistrictChooseAdapter(districtList,getActivity());
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
                            //检查是否已有改城市
                            if (WeatherUtils.hasCity(((District) objectList.get(position)).getDistrictName())){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"您已添加过"+((District) objectList.get(position)).getDistrictName()+",请选择其它城市",Toast.LENGTH_SHORT).show();
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
        adapter.setOnItemClickListener(new DistrictChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, final int position) {
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
                        //检查是否已有改城市
                        if (WeatherUtils.hasCity(((District) objectList.get(position)).getDistrictName())){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"您已添加过"+((District) objectList.get(position)).getDistrictName()+",请选择其它城市",Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        //再检查是否有改城市的天气预报
                        if (Utils.hasWeather(districtList.get(position).getDistrictName())){
                            Intent intent = new Intent();
                            intent.putExtra("cityName",districtList.get(position).getDistrictName());
                            getActivity().setResult(2,intent);
//                Toast.makeText(getActivity(),"你选择了 "+districtList.get(position).getDistrictName(),Toast.LENGTH_SHORT).show();
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
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //交给activity处理，返回上一级
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerViewForSearch.setAdapter(adapterForSearch);
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
                districtList.clear();
//                Log.d("FragmentDistrictChoose",city.getProvinceId()+"  "+city.getCityId());
                districtList.addAll(Utils.queryDistrict(city.getCityId()));
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
