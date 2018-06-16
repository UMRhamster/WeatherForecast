package com.whut.umrhamster.weatherforecast.View;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

import com.whut.umrhamster.weatherforecast.R;

public class CityChooseActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private Fragment fragmentProvinceChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);

        coordinatorLayout = findViewById(R.id.ac_citychoose_cl);
        setDefaultFragment();  //设置默认fragment
    }
    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragmentProvinceChoose = new FragmentProvinceChoose();
        ft.replace(R.id.ac_citychoose_cl,fragmentProvinceChoose);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (getSupportFragmentManager().getFragments().get(0) instanceof FragmentCityChoose){  //从city选择界面返回，则显示省份
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.ac_citychoose_cl,new FragmentProvinceChoose()).commit();
                return true;
            }else if (getSupportFragmentManager().getFragments().get(0) instanceof FragmentProvinceChoose){
//                Log.d("CityChooseActivity","province");
                return super.onKeyDown(keyCode, event);
            }else if (getSupportFragmentManager().getFragments().get(0) instanceof  FragmentDistrictChoose){
                Bundle bundle = new Bundle();
//                Log.d("CityChooseActivity",getSupportFragmentManager().getFragments().get(0).getArguments().getString("provinceName"));
                bundle.putString("provinceName",getSupportFragmentManager().getFragments().get(0).getArguments().getString("provinceName"));
                FragmentManager fm = getSupportFragmentManager();
                FragmentCityChoose fragmentCityChoose = new FragmentCityChoose();
                fragmentCityChoose.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ac_citychoose_cl,fragmentCityChoose).commit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
