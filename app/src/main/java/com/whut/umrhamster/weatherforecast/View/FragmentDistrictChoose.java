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
import android.widget.TextView;
import android.widget.Toast;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.Model.Utils;
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
    private LinearLayoutManager linearLayoutManager;
    private DistrictChooseAdapter adapter;
    private List<District> districtList;
    private TextView textViewTitle;

    private City city;
    Handler handler = new Handler();
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
        textViewTitle = view.findViewById(R.id.fg_district_choose_tv);
        city = (City)getArguments().getSerializable("city");
        textViewTitle.setText(city.getCityName());  //设置标题与选择城市对应
        editTextSearch = view.findViewById(R.id.fg_district_search_et);
        recyclerView = view.findViewById(R.id.fg_district_choose_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        districtList = new ArrayList<>();
//        districtList = Utils.queryDistrict();
        adapter = new DistrictChooseAdapter(districtList,getActivity());
        adapter.setOnItemClickListener(new DistrictChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("cityName",districtList.get(position).getDistrictName());
                getActivity().setResult(2,intent);
                Toast.makeText(getActivity(),"你选择了 "+districtList.get(position).getDistrictName(),Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        if(s.equals(null)){
//            imageButtonDelete.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        if(s.length()>0){
//            imageButtonDelete.setVisibility(View.VISIBLE);  //删除图标可见
//        }else{
//            imageButtonDelete.setVisibility(View.GONE);  //删除图标不可见
//        }
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
//                if (editable.length() > 0){
//                    LitePal.where("provinceName like ?","%"+editable.toString()+"%").find(Province.class);
//                }
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
}
