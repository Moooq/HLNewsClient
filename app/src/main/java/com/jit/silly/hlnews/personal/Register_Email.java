package com.jit.silly.hlnews.personal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jit.silly.hlnews.MainActivity;
import com.jit.silly.hlnews.MyApplication;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.jit.silly.hlnews.MyApplication.Rgemail;

/**
 * Created by moqiandemac on 2017/6/19.
 */

public class Register_Email extends Fragment {

    EditText rg_email, vf_code;
    Button btn_mail, btn_submit;
    TextView tv_mail;
    returnVerify returnInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View register_Email = inflater.inflate(R.layout.register_email, container, false);
        ImageView img_cancel_e = (ImageView) register_Email.findViewById(R.id.cancel_e_rg);
        rg_email = (EditText) register_Email.findViewById(R.id.rg_email);
        btn_mail = (Button) register_Email.findViewById(R.id.button2);
        tv_mail = (TextView) register_Email.findViewById(R.id.textView3);
        vf_code = (EditText) register_Email.findViewById(R.id.vf_code);
        btn_submit = (Button) register_Email.findViewById(R.id.btn_submit_rg);

        btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("code", "1" );
                OkGo.post(UrlAPI.urlVerfy)
                        .params("email", rg_email.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                returnInfo = gson.fromJson(s, returnVerify.class);
                                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                                    Log.i("code", "code:" + returnInfo.getCode());
                                    tv_mail.setText(R.string.mail_sended);
                                    MyApplication.vercode1= returnInfo.getData();
                                    btn_submit.setClickable(true);
                                    rg_email.setFocusable(false);
                                    btn_mail.setClickable(false);
                                    Log.i("code", "ver:"+MyApplication.vercode1.getVerification());
                                } else {
                                    Toast.makeText(getActivity(), R.string.send_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        rg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_submit.setClickable(false);
                btn_submit.setBackground(getResources().getDrawable(R.drawable.log_button_f));
                if (rg_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+")&&s.length()>0){
                    btn_mail.setClickable(true);
                    btn_mail.setBackground(getResources().getDrawable(R.drawable.rg_buttom));
                }else {
                    btn_mail.setClickable(false);
                    btn_mail.setBackground(getResources().getDrawable(R.drawable.log_button_f));
                }
            }
        });
        vf_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==4){
                    btn_submit.setClickable(true);
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.rg_buttom));
                }else {
                    btn_submit.setClickable(false);
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.log_button_f));
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")){
                    Log.i("code", MyApplication.vercode1.getVerification());
                    if (MyApplication.vercode1.getVerification().equals(vf_code.getText().toString())) {
                        Toast.makeText(getActivity(), R.string.verify_success, Toast.LENGTH_SHORT).show();
                        Rgemail=rg_email.getText().toString();
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.fragmentContainer_rg, new Register_Profile()).commit();
                    }
                    else{
                        Toast.makeText(getActivity(), R.string.verify_fail, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.verify_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_cancel_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                getActivity().setResult(1,intent);
                getActivity().finish();
            }
        });


        return register_Email;
    }
}
