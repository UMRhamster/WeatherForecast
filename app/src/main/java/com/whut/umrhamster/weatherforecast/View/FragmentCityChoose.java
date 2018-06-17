package com.whut.umrhamster.weatherforecast.View;

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
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class FragmentCityChoose extends Fragment {
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CityChooseAdapter adapter;
    private List<City> cityList;
    private TextView textViewTitle;

    private Province province;
    Handler handler = new Handler();
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
        textViewTitle = view.findViewById(R.id.fg_city_choose_tv);
        province = (Province) getArguments().getSerializable("province");
        textViewTitle.setText(province.getProvinceName());  //设置标题与选择城市对应
        editTextSearch = view.findViewById(R.id.fg_city_search_et);
        recyclerView = view.findViewById(R.id.fg_city_choose_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        cityList = new ArrayList<>();
//        cityList = Utils.queryCity();
        adapter = new CityChooseAdapter(cityList,getActivity());
        adapter.setOnItemClickListener(new CityChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDistrictChoose fragmentDistrictChoose = new FragmentDistrictChoose();
                Bundle bundle = new Bundle();
                bundle.putSerializable("city",cityList.get(position));
                bundle.putSerializable("province",province);
                fragmentDistrictChoose.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ac_citychoose_cl,fragmentDistrictChoose).commit();
            }
        });
        recyclerView.setAdapter(adapter);
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

            }
        });
    }

    private void initData(){
        new Thread(){
            @Override
            public void run() {
                cityList.clear();
//                Log.d("FragmentCityChoose",province.getProvinceId());
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
}
