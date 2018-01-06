package com.jit.silly.hlnews;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Dialog mDialog;


    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        button = (Button) findViewById(R.id.btn);
        button.setText(R.string.switch_language);
        //监听切换语言按钮,弹出dialog选择语言
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialog == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_select_lanuage,null);
                    TextView english = (TextView) layout.findViewById(R.id.select_english);
                    TextView chinese = (TextView) layout.findViewById(R.id.select_chinese);
                    mDialog = new Dialog(SettingActivity.this, R.style.dialogTheme);
                    mDialog.setCanceledOnTouchOutside(false);
                    english.setOnClickListener(SettingActivity.this);
                    chinese.setOnClickListener(SettingActivity.this);
                    mDialog.setContentView(layout);
                }
                mDialog.show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        mDialog.dismiss();
        switch (v.getId()) {
            case R.id.select_english:
                //切换为英文
                switchLanguage("en");
                break;
            case R.id.select_chinese:
                //切换为中文
                switchLanguage("zh");
                break;

            default:
                break;
        }
        //更新语言后，destroy当前页面，重新绘制
        finish();

        Intent it = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(it);
    }


}
