package com.whut.umrhamster.weatherforecast.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.whut.umrhamster.weatherforecast.R;

public class CitySearchActivity extends AppCompatActivity {
    private Button buttonMoreCity;

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
                startActivity(new Intent(CitySearchActivity.this,CityChooseActivity.class));
                overridePendingTransition(R.anim.anim_fg_enter,R.anim.anim_do_nothing);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_fg_back_enter,R.anim.anim_fg_back_out);
    }
}
