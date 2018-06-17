package com.whut.umrhamster.weatherforecast.View;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.whut.umrhamster.weatherforecast.Model.Province;
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
    private ProvinceChooseAdapter adapter;
    private List<Province> provinceList;

    Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_choose,container,false);
        initView(view);
        initEvent();
        initData();
        return view;
    }
    private void initView(View view){
        editTextSearch = view.findViewById(R.id.fg_province_search_et);
        recyclerView = view.findViewById(R.id.fg_province_choose_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        provinceList = new ArrayList<>();
//        provinceList = Utils.queryProvince(); //通过查询获取省份列表********
        adapter = new ProvinceChooseAdapter(provinceList,getActivity());
        adapter.setOnItemClickListener(new ProvinceChooseAdapter.OnItemClickListener() { //条目点击事件处理
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentCityChoose fragmentCityChoose = new FragmentCityChoose();
                Bundle bundle = new Bundle();
                bundle.putSerializable("province",provinceList.get(position));
                Log.d("asdafdss",provinceList.get(position).getProvinceId());
//                bundle.putString("provinceName",provinceList.get(position).getProvinceName());
                fragmentCityChoose.setArguments(bundle);
                fm.beginTransaction().setCustomAnimations(R.anim.anim_fg_enter,R.anim.anim_fg_out).replace(R.id.ac_citychoose_cl,fragmentCityChoose).commit();
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
//                Log.d("FragmentProvinceChoose","1");
                provinceList.clear();
                provinceList.addAll(Utils.queryProvince());
//                Log.d("FragmentProvinceChoose",String.valueOf(provinceList.get(3).getProvinceName()));
//                Log.d("FragmentProvinceChoose","2");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //此处更新Ui
//                        Log.d("FragmentProvinceChoose","3");
                        adapter.notifyDataSetChanged();
//                        Log.d("FragmentProvinceChoose","4");
                    }
                });
            }
        }.start();
    }

    private void getProvince(){

    }
}
