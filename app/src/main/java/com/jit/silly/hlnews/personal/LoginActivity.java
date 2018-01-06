package com.jit.silly.hlnews.personal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.wang.avi.AVLoadingIndicatorView;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    EditText userPasswd;
    Bundle bd;
    returnList returnInfo;
    TextView forget_psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.et_uname);
        userPasswd = (EditText) findViewById(R.id.et_psw);
        final CheckBox autoLog = (CheckBox)findViewById(R.id.auto_login);
        final Button btn_login = (Button) findViewById(R.id.btn_login);
        forget_psw=(TextView)findViewById(R.id.forget_psw);
        Button btn_register= (Button)findViewById(R.id.btn_register);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+")&&s.length()>0){
                    btn_login.setClickable(true);
                    btn_login.setBackground(getResources().getDrawable(R.drawable.log_button));
                }
                else {
                    btn_login.setClickable(false);
                    btn_login.setBackground(getResources().getDrawable(R.drawable.log_button_f));
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.log_load);
                loadingIndicatorView.setVisibility(View.VISIBLE);
                final String uName = userName.getText().toString();
                final String uPasswd = userPasswd.getText().toString();
                Log.i("username",uName);
                Log.i("upasswd",uPasswd);
                OkGo.post(UrlAPI.urlLog)
                        .params("username",uName)
                        .params("password",uPasswd)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                bd=new Bundle();
                                Gson gson = new Gson();
                                returnInfo = gson.fromJson(s,returnList.class);
                                Log.i("code",returnInfo.getCode()+"");
                                if(returnInfo.getCode()==200&&returnInfo.getMsg().equals("success")){
                                    Log.i("code", "");
                                    if(autoLog.isChecked()){
                                        bd.putSerializable("auto",0);
                                    }else{
                                        bd.putSerializable("auto",1);
                                    }
                                    bd.putSerializable("user",returnInfo.getData());
                                    MyApplication.user1=returnInfo.getData();
                                    Intent intent = LoginActivity.this.getIntent();
                                    intent.putExtras(bd);
                                    LoginActivity.this.setResult(0,intent);
                                    LoginActivity.this.finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, R.string.user_psw_wrong, Toast.LENGTH_SHORT).show();
                                    loadingIndicatorView.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

            }
        });
        ImageView img_cancel= (ImageView)findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent,0);
            }
        });

        forget_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==0){
            Toast.makeText(this,R.string.reg_success,Toast.LENGTH_SHORT).show();
        }
        if(requestCode==1&&resultCode==1){
            Toast.makeText(this,R.string.modify_psw_success,Toast.LENGTH_SHORT).show();
        }
    }

}
