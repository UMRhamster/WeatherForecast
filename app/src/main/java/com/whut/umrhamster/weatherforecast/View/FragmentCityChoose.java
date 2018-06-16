package com.whut.umrhamster.weatherforecast.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    private List<String> cityList;
    private TextView textViewTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_choose,container,false);
        initView(view);
        initEvent();
        return view;
    }
    private void initView(View view){
        textViewTitle = view.findViewById(R.id.fg_city_choose_tv);
        textViewTitle.setText(getArguments().getString("provinceName"));  //设置标题与选择城市对应
        editTextSearch = view.findViewById(R.id.fg_city_search_et);
        recyclerView = view.findViewById(R.id.fg_city_choose_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        cityList = new ArrayList<>();
        cityList.add("城市一");
        cityList.add("城市二");
        adapter = new CityChooseAdapter(cityList,getActivity());
        adapter.setOnItemClickListener(new CityChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDistrictChoose fragmentDistrictChoose = new FragmentDistrictChoose();
                Bundle bundle = new Bundle();
                bundle.putString("cityName",cityList.get(position));
                bundle.putString("provinceName",getArguments().getString("provinceName"));
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
}
