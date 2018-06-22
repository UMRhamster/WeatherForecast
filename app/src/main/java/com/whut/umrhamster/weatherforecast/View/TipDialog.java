package com.whut.umrhamster.weatherforecast.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.R;

/**
 * Created by 12421 on 2018/6/22.
 */

public class TipDialog extends Dialog {
    private int problemType = -1;  //问题类型   0-网络问题，1-城市无天气预报
    private TextView textViewContent; //提示内容
    private TextView textViewbtn; //确定按钮
    public TipDialog(@NonNull Context context,int problemType) {
        super(context, R.style.CustomDialog);
        this.problemType = problemType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_tip);
        initView();
    }

    private void initView(){
        textViewbtn = findViewById(R.id.dialog_tip_btn_tv);
        textViewContent = findViewById(R.id.dialog_tip_content);
        if (problemType == 0){
            textViewContent.setText(getContext().getResources().getString(R.string.tipContentNet));
        }else if (problemType == 1){
            textViewContent.setText(getContext().getResources().getString(R.string.tipContentWeather));
        }else {
            textViewContent.setText(getContext().getResources().getString(R.string.tipContentUnknow));
        }
        textViewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
