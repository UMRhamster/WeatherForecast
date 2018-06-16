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

import com.whut.umrhamster.weatherforecast.Model.Utils;
import com.whut.umrhamster.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class FragmentProvinceChoose extends Fragment {
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CityChooseAdapter adapter;
    private List<String> provinceList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_choose,container,false);
        initView(view);
        initEvent();
        return view;
    }
    private void initView(View view){
        editTextSearch = view.findViewById(R.id.fg_province_search_et);
        recyclerView = view.findViewById(R.id.fg_province_choose_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        provinceList = new ArrayList<>();
        provinceList.add("北京");
        provinceList.add("上海");
        adapter = new CityChooseAdapter(provinceList,getActivity());
        adapter.setOnItemClickListener(new CityChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentCityChoose fragmentCityChoose = new FragmentCityChoose();
                Bundle bundle = new Bundle();
                bundle.putString("provinceName",provinceList.get(position));
                fragmentCityChoose.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ac_citychoose_cl,fragmentCityChoose).commit();
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

    private void getProvince(){

    }
}
