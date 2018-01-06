package com.jit.silly.hlnews.personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jit.silly.hlnews.MyApplication;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity {

    EditText fg_email, vf_code_fg, fg_psw1, fg_psw2;
    Button btn_mail_fg, btn_submit_fg;
    TextView tv_mail_fg;
    returnVerify returnInfo;
    returnList returnUser;
    ImageView img_cancel_fg;
    String fgEmail;
    UserInfo fgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        img_cancel_fg = (ImageView) findViewById(R.id.cancel_e_fg);
        fg_email = (EditText) findViewById(R.id.fg_email);
        btn_mail_fg = (Button) findViewById(R.id.fg_btn1);
        tv_mail_fg = (TextView) findViewById(R.id.fg_e_v);
        vf_code_fg = (EditText) findViewById(R.id.fg_code);
        btn_submit_fg = (Button) findViewById(R.id.btn_submit_fg);


        fg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_submit_fg.setClickable(false);
                if (fg_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+")&&s.length()>0){
                    btn_mail_fg.setClickable(true);
                    btn_mail_fg.setBackground(getResources().getDrawable(R.drawable.rg_buttom));
                }else {
                    btn_mail_fg.setClickable(false);
                    btn_mail_fg.setBackground(getResources().getDrawable(R.drawable.log_button_f));
                }
            }
        });


        btn_mail_fg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("code", "1:" + fg_email.getText().toString());
                OkGo.post(UrlAPI.urlFgtpsw)
                        .params("username", fg_email.getText().toString())
                        .params("type", 1)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                returnInfo = gson.fromJson(s, returnVerify.class);
                                Log.i("code", "" + returnInfo.getCode());
                                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                                    Log.i("code", "code:" + returnInfo.getCode());
                                    tv_mail_fg.setText(R.string.mail_sended);
                                    MyApplication.vercode1 = returnInfo.getData();
                                    Log.i("code", "ver:" + MyApplication.vercode1.getVerification());
                                    btn_submit_fg.setClickable(true);
                                    fg_email.setFocusable(false);
                                    btn_mail_fg.setClickable(false);
                                } else {
                                    Toast.makeText(ForgetActivity.this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        btn_submit_fg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                    Log.i("code", MyApplication.vercode1.getVerification());
                    if (MyApplication.vercode1.getVerification().equals(vf_code_fg.getText().toString())) {
                        Toast.makeText(ForgetActivity.this, R.string.verify_success, Toast.LENGTH_SHORT).show();
                        fgEmail = fg_email.getText().toString();
                        OkGo.post(UrlAPI.urlFgtpsw)
                                .params("username", fg_email.getText().toString())
                                .params("type", 2)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Gson gson = new Gson();
                                        returnUser = gson.fromJson(s, returnList.class);
                                        if (returnUser.getCode() == 200 && returnUser.getMsg().equals("success")) {
                                            Log.i("code", "code:" + returnInfo.getCode());
                                            fgUser = returnUser.getData();
                                            LayoutInflater inflater = (LayoutInflater) ForgetActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                                            final View fgview = inflater.inflate(R.layout.password_forget, null);
                                            new AlertDialog.Builder(ForgetActivity.this)
                                                    .setIcon(R.drawable.password)
                                                    .setTitle(R.string.modify_psw)
                                                    .setCancelable(true)
                                                    .setView(fgview)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            fg_psw1 = (EditText) fgview.findViewById(R.id.fg_psw1);
                                                            fg_psw2 = (EditText) fgview.findViewById(R.id.fg_psw2);
                                                            if (fg_psw1.getText().toString().equals(fg_psw2.getText().toString())) {
                                                                Log.i("code", "fg:" + fg_psw1.getText().toString());
                                                                Log.i("code", "fg.information:" + fgUser.getIntroduction());
                                                                OkGo.post(UrlAPI.urlReUser)
                                                                        .params("id", fgUser.getId())
                                                                        .params("username", fgUser.getUsername())
                                                                        .params("password", fg_psw1.getText().toString())
                                                                        .params("nickname", fgUser.getNickname())
                                                                        .params("sex", fgUser.isSex())
                                                                        .params("birthday", fgUser.getBirthday())
                                                                        .params("introduction", fgUser.getIntroduction())
                                                                        .params("imageUrl", fgUser.getImageUrl())
                                                                        .execute(new StringCallback() {
                                                                            @Override
                                                                            public void onSuccess(String s, Call call, Response response) {
                                                                                Gson gsonFg = new Gson();
                                                                                returnInfo = gsonFg.fromJson(s, returnVerify.class);
                                                                                Log.i("code", "onSuccess: "+returnInfo.getCode());
                                                                                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                                                                                    Intent intent = getIntent();
                                                                                    setResult(1, intent);
                                                                                    Log.i("code", "" + returnInfo.getCode());
                                                                                    finish();
                                                                                }
                                                                            }
                                                                        });
                                                            } else {
                                                                Toast.makeText(ForgetActivity.this, R.string.inconsistent, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    })
                                                    .create()
                                                    .show();
                                        } else {
                                            Toast.makeText(ForgetActivity.this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(ForgetActivity.this, R.string.verify_fail, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgetActivity.this, R.string.verify_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_cancel_fg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetActivity.this.finish();
            }
        });
        fg_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_submit_fg.setClickable(false);
                fg_email.setFocusable(true);
                btn_mail_fg.setClickable(true);
            }
        });
    }
}
