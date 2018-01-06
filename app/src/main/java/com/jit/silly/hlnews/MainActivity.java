package com.jit.silly.hlnews;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.jit.silly.hlnews.Baoliao.BaoActivity;
import com.jit.silly.hlnews.Baoliao.View_Hot;
import com.jit.silly.hlnews.NewsT.View_Home;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.jit.silly.hlnews.personal.ProfileActivity;
import com.jit.silly.hlnews.personal.returnList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import com.jit.silly.hlnews.personal.LoginActivity;
import com.jit.silly.hlnews.personal.UserInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import static com.jit.silly.hlnews.MyApplication.lg;
import static com.jit.silly.hlnews.MyApplication.password1;
import static com.jit.silly.hlnews.MyApplication.user1;
import static com.jit.silly.hlnews.MyApplication.username1;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    View_Home view_home;
    View_Hot view_hot;
    private DrawerLayout mDrawerLayout;
    SharedPreferences sp;
    ActionBar actionBar;
    SharedPreferences.Editor editor;
    FloatingActionsMenu rightLabels;
    CircleImageView head;
    TextView uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_personal);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        username1 = sp.getString("username", null);
        password1 = sp.getString("password", null);
        loginAuto();
        editor = sp.edit();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nologin);
        }
        //chouti
        navView.setCheckedItem(R.id.nav_personal);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_out:
                        user1 = null;
                        lg = 1;
                        username1 = null;
                        password1 = null;
                        actionBar.setHomeAsUpIndicator(R.drawable.nologin);
                        editor.putString("username", null);
                        editor.putString("password", null);
                        editor.commit();
                        break;
                    case R.id.nav_shot:
                        Intent intent = new Intent(MainActivity.this, BaoActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.nav_profile:
                        Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivityForResult(intent2,10);
                        break;
                    case R.id.nav_set:
                        Intent intent3 = new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent3);

                }
                return true;
            }
        });

        fm = getFragmentManager();


        setTabSelection(1);
        if (lg == 0) {
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
        }
        rightLabels =(FloatingActionsMenu)findViewById(R.id.right_labels);
        com.getbase.floatingactionbutton.FloatingActionButton fab1 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.home_btn);
        com.getbase.floatingactionbutton.FloatingActionButton fab2 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.hot_btn);
        rightLabels.collapse();
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
                rightLabels.collapse();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0);
                rightLabels.collapse();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (lg == 1) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 0);
                }
                if (lg == 0) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && requestCode == 0) {
            if (data != null) {
                user1 = (UserInfo) data.getSerializableExtra("user");

                int auto = (int) data.getSerializableExtra("auto");
                if (auto == 0) {
                    editor.putString("username", user1.getUsername());
                    editor.putString("password", user1.getPassword());
                    editor.commit();
                    Log.i("sp", "login username1:" + user1.getUsername() + " to " + sp.getString("username", null));
                    Log.i("sp", "login password1:" + user1.getPassword() + " to " + sp.getString("password", null));
                }
                initUser(user1);
            }
        }
        if (requestCode == 1 && resultCode == 1) {
            Toast.makeText(this, R.string.publish_success, Toast.LENGTH_SHORT).show();
        }
        if(requestCode==10&&resultCode==10){
            Log.i("reg", "imageurl:"+user1.getImageUrl());
            Glide.with(this).load(user1.getImageUrl()).into(head);
            TextView uname = (TextView) findViewById(R.id.nickname);
            uname.setText(user1.getNickname());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setTabSelection(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setTabSelection(0);
                return true;
            }
        });

        return true;
    }


    private void setTabSelection(int index) {

        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (view_hot == null) {
                    view_hot = new View_Hot();
                    transaction.add(R.id.fragmentContainer, view_hot);
                } else {
                    transaction.show(view_hot);
                }
                break;
            case 1:
                if (view_home == null) {
                    view_home = new View_Home();
                    transaction.add(R.id.fragmentContainer, view_home);
                } else {
                    transaction.show(view_home);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (view_home != null) {
            transaction.hide(view_home);
        }
        if (view_hot != null) {
            transaction.hide(view_hot);
        }
    }

    private void loginAuto() {
        if (username1 != null && password1 != null) {
            OkGo.post(UrlAPI.urlLog)
                    .params("username", username1)
                    .params("password", password1)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Gson gson = new Gson();
                            returnList returnInfo = gson.fromJson(s, returnList.class);
                            Log.i("code", returnInfo.getCode() + "");
                            if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                                user1 = returnInfo.getData();
                                Log.i("user1", "" + user1.getNickname());
                                initUser(user1);
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Email/Password ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.i("sp", "username1:" + username1);
            Log.i("sp", "password1:" + password1);
        }
    }

    private void initUser(UserInfo userinfo) {
        lg = 0;
        Log.i("user1", "" + userinfo.getNickname());
        uname= (TextView)this.findViewById(R.id.nickname);
        if (uname == null) {
            Log.i("uname", "null 1"+userinfo.getNickname());
        } else {
            ImageView sex = (ImageView) findViewById(R.id.image_sex);
            Log.i("user1", "" + userinfo.getNickname());
            uname.setText(userinfo.getNickname());
            if (userinfo.isSex() == 0) {
                sex.setImageResource(R.drawable.male);
            } else if (userinfo.isSex() == 1) {
                sex.setImageResource(R.drawable.female);
            }
            TextView email = (TextView) findViewById(R.id.mail);
            email.setText(userinfo.getUsername());
            head = (CircleImageView) findViewById(R.id.icon_image);
            if (userinfo.getImageUrl().length() > 10) {
                Glide.with(this).load(userinfo.getImageUrl()).into(head);
            } else {
                if (userinfo.isSex()==0){
                    Glide.with(this).load(R.drawable.immale).into(head);
                }
                if (userinfo.isSex()==1){
                    Glide.with(this).load(R.drawable.imfemale).into(head);
                }
            }
            actionBar.setHomeAsUpIndicator(R.drawable.person);
            Log.i("image", userinfo.getImageUrl());
        }
    }


}
