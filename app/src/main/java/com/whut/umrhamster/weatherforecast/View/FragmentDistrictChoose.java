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
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/16.
 */

public class FragmentDistrictChoose extends Fragment {
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CityChooseAdapter adapter;
    private List<String> districtList;
    private TextView textViewTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_district_choose,container,false);
        initView(view);
        initEvent();
        return view;
    }
    private void initView(View view){
        textViewTitle = view.findViewById(R.id.fg_district_choose_tv);
        textViewTitle.setText(getArguments().getString("cityName"));  //设置标题与选择省份对应
        editTextSearch = view.findViewById(R.id.fg_district_search_et);
        recyclerView = view.findViewById(R.id.fg_district_choose_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        districtList = new ArrayList<>();
        districtList.add("地区一");
        districtList.add("地区二");
        adapter = new CityChooseAdapter(districtList,getActivity());
        adapter.setOnItemClickListener(new CityChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"你选择了 "+districtList.get(position),Toast.LENGTH_SHORT).show();
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
