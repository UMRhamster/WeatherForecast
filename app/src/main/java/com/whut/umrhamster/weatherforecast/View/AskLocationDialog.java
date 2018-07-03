package com.whut.umrhamster.weatherforecast.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.whut.umrhamster.weatherforecast.R;

/**
 * Created by 12421 on 2018/6/30.
 */

public class AskLocationDialog extends Dialog{
    private String content;
    private TextView textViewContent;
    private TextView textViewBTNOK;
    private TextView textViewBTNCancel;

    private OnClickListener clickListener;

    public AskLocationDialog(@NonNull Context context, String content) {
        super(context,R.style.CustomDialog);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_asklocation);

        textViewContent = findViewById(R.id.dialog_asklocation_content);
        textViewBTNOK = findViewById(R.id.dialog_asklocation_ok_btn_tv);
        textViewBTNCancel = findViewById(R.id.dialog_asklocation_cancel_btn_tv);

        textViewContent.setText(content);
        textViewBTNOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //处理“确定”点击事件
                clickListener.onClick();
                dismiss();
            }
        });
        textViewBTNCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface OnClickListener{
        void onClick();
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.clickListener = clickListener;
    }
}
