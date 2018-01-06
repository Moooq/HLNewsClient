package com.jit.silly.hlnews.personal;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jit.silly.hlnews.R;

public class RegisterActivity extends AppCompatActivity {
    FragmentManager fm;
    Register_Email register_email;
    Register_Profile register_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fm=getFragmentManager();
        setTabSelection(1);
    }
     void setTabSelection(int index) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 1:
                if ( register_email== null) {
                    register_email = new Register_Email();
                    transaction.add(R.id.fragmentContainer_rg, register_email);
                } else {
                    transaction.show(register_email);

                }
                break;
            case 2:
                if (register_profile == null) {
                    register_profile = new Register_Profile();
                    transaction.add(R.id.fragmentContainer_rg, register_profile);
                } else {
                    transaction.show(register_profile);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (register_profile != null) {
            transaction.hide(register_profile);
        }
        if (register_email != null) {
            transaction.hide(register_email);
        }
    }
}
