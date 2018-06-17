package com.whut.umrhamster.weatherforecast.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.whut.umrhamster.weatherforecast.Model.City;
import com.whut.umrhamster.weatherforecast.Model.District;
import com.whut.umrhamster.weatherforecast.Model.Province;
import com.whut.umrhamster.weatherforecast.R;

import org.litepal.LitePal;

public class CitySearchActivity extends AppCompatActivity {
    private Button buttonMoreCity;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.citySearchtb));

        buttonMoreCity = findViewById(R.id.ac_citysearch_morecity_btn);
        buttonMoreCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CitySearchActivity.this,CityChooseActivity.class),1);
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
        editTextSearch = findViewById(R.id.ac_citychoose_et);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    LitePal.where("provinceName like ?","%"+editable.toString()+"%").find(Province.class);
                    LitePal.where("cityName like ?","%"+editable.toString()+"%").find(City.class);
                    LitePal.where("districtName like ?","%"+editable.toString()+"%").find(District.class);
                }
            }
        });
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
            setResult(4,data);
            finish();
        }
    }
}
