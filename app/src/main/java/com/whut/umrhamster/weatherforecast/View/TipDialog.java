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
    private String tip; //提示
    private TextView textViewContent; //提示内容
    private TextView textViewbtn; //确定按钮
    public TipDialog(@NonNull Context context,String tip) {
        super(context, R.style.CustomDialog);
        this.tip = tip;
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
        textViewContent.setText(tip);
        textViewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
