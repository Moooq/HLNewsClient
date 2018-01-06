package com.jit.silly.hlnews.Baoliao;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.jit.silly.hlnews.returnSuccess;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.jit.silly.hlnews.MyApplication.lg;
import static com.jit.silly.hlnews.MyApplication.user1;

public class BaonewsActivity extends AppCompatActivity {

    ListView hotComments;
    CircleImageView baoHead;
    TextView baoEditor;
    TextView hotContent, scmtTv;
    ImageView imageView, pic1, pic2, pic3;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    CardView scmtCv;
    EditText scmtEt;
    int cnum;
    LinearLayout ly_0;
    ListView hotCmts;
    String blId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baonews);

        Intent intent = getIntent();
        ArrayList<String> list = (ArrayList) intent.getSerializableExtra("ahot");
//        list.add(returnInfo.getData()[position].getEditorid());0
//        list.add(returnInfo.getData()[position].getId() + "");1
//        list.add(returnInfo.getData()[position].getTitle());2
//        list.add(returnInfo.getData()[position].getContent());3
//        list.add(returnInfo.getData()[position].getPicurl1());4
//        list.add(returnInfo.getData()[position].getPicurl2());5
//        list.add(returnInfo.getData()[position].getPicurl3());6
        blId = list.get(1);
        String title = list.get(2);
        String content = list.get(3);
        String editorid = list.get(0);
        String cntpic[] = new String[]{list.get(4), list.get(5), list.get(5)};

        toolbar = (Toolbar) findViewById(R.id.hotnews_toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_hot);
        imageView = (ImageView) findViewById(R.id.hot_image_view);
        hotContent = (TextView) findViewById(R.id.hot_content);
        baoEditor = (TextView) findViewById(R.id.bao_editor);
        baoHead = (CircleImageView) findViewById(R.id.bao_head);
        pic1 = (ImageView) findViewById(R.id.cntpic1);
        pic2 = (ImageView) findViewById(R.id.cntpic2);
        pic3 = (ImageView) findViewById(R.id.cntpic3);
        scmtCv = (CardView) findViewById(R.id.send_comments_cv);
        ly_0=(LinearLayout)findViewById(R.id.lin_o);
        scmtEt = (EditText) findViewById(R.id.send_comments_et);
        scmtTv = (TextView) findViewById(R.id.send_comments_tv);
        hotCmts = (ListView) findViewById(R.id.hot_comments);
        Glide.with(this).load(cntpic[0]).into(imageView);

        scmtEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (scmtEt.getText().length() < 3) {
                    scmtTv.setClickable(false);
                    scmtTv.setTextColor(getResources().getColor(R.color.black_semi_transparent));
                } else {
                    scmtTv.setClickable(true);
                    scmtTv.setTextColor(getResources().getColor(R.color.text_color));
                }
            }
        });


        if (lg == 0) {
            scmtCv.setVisibility(View.VISIBLE);
            ly_0.setVisibility(View.VISIBLE);
        } else {
            scmtCv.setVisibility(View.INVISIBLE);
            ly_0.setVisibility(View.INVISIBLE);
        }

        getBaoer(editorid);

        getCmts(blId);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(title);
        hotContent.setText(content);
        Glide.with(getBaseContext()).load(cntpic[0]).into(pic1);
        Glide.with(getBaseContext()).load(cntpic[1]).into(pic2);
        Glide.with(getBaseContext()).load(cntpic[2]).into(pic3);

        scmtTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComments(blId);
            }
        });

    }

    private void getBaoer(String edid) {
        OkGo.get(UrlAPI.urlBaoer)
                .params("id", edid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        returnBaoer returnbaoer = gson.fromJson(s, returnBaoer.class);
                        if (returnbaoer.getCode() == 200 && returnbaoer.getMsg().equals("success")) {
                            BaoerInfo baoer = returnbaoer.getData();
                            baoEditor.setText(baoer.getNickname());
                            if (baoer.getImageUrl().length() > 10) {
                                Glide.with(getBaseContext()).load(baoer.getImageUrl()).into(baoHead);
                            } else {
                                Glide.with(getBaseContext()).load(R.drawable.immale).into(baoHead);
                            }
                            if (lg==0){
                                if(user1.getId()== baoer.getId()){
                                    ImageView baoDel=(ImageView)findViewById(R.id.bao_del);
                                    baoDel.setVisibility(View.VISIBLE);
                                    baoDel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.i("dele", "123123123:"+blId);
                                            OkGo.post(UrlAPI.urlDelBao)
                                                    .params("id",blId)
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onSuccess(String s, Call call, Response response) {
                                                            Gson gson = new Gson();
                                                            returnSuccess rss = gson.fromJson(s, returnSuccess.class);
                                                            if(rss.getCode()==200&&rss.getMsg().equals("success")){
                                                                Intent intent = BaonewsActivity.this.getIntent();
                                                                BaonewsActivity.this.setResult(3,intent);
                                                                BaonewsActivity.this.finish();
                                                            }
                                                            else {
                                                                Toast.makeText(BaonewsActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }
                        }

                    }

                });
    }

    private void getCmts(String baoid) {
        OkGo.get(UrlAPI.urlCmt)
                .params("id", baoid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        returnCmts returncmts = gson.fromJson(s, returnCmts.class);
                        if (returncmts.getCode() == 200 && returncmts.getMsg().equals("success")) {
                            MyBaseAdapter adapter = new MyBaseAdapter(returncmts.getData());
                            hotCmts.setAdapter(adapter);

                        }
                    }
                });
    }

    private void sendComments(String id) {
        int baoid = Integer.parseInt(id);
        Log.i("addcmt", "baoid" + baoid);
        final String baoid2 = id;
        String cmt = scmtEt.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(new Date());
        Log.i("time", time);
        String cmts = cmt + "$" + time;
        Log.i("addcmt", "cmt:" + cmts);
        OkGo.get(UrlAPI.urlUpCmt)
                .params("sponsor", user1.getId())
                .params("content", cmts)
                .params("baoliaoid", baoid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        returnSuccess returnsss = gson.fromJson(s, returnSuccess.class);
                        if (returnsss.getCode() == 200 && returnsss.getMsg().equals("success")) {
                            Toast.makeText(BaonewsActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                            getCmts(baoid2);
                            scmtEt.setText("");
                            scmtEt.clearFocus();
                        } else {
                            Toast.makeText(BaonewsActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class MyBaseAdapter extends BaseAdapter {
        private CmtInfo[] data;

        private MyBaseAdapter(CmtInfo[] list) {
            this.data = list;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_comment, null);
            final CircleImageView cmtHead = (CircleImageView) view.findViewById(R.id.cmt_head);
            final TextView cmtSponsor = (TextView) view.findViewById(R.id.cmt_sponsor);
            final TextView cmtContent = (TextView) view.findViewById(R.id.cmt_content);
            final TextView cmtTime = (TextView) view.findViewById(R.id.cmt_time);
            final TextView cmtDel = (TextView) view.findViewById(R.id.cmt_del);
            final CmtInfo cmtinfo = data[data.length-position-1];
            final ImageView like = (ImageView) view.findViewById(R.id.like);
            cmtContent.setText(getContent(cmtinfo.getContent()) + "");
            cmtTime.setText(getTime(cmtinfo.getContent()) + "");
            OkGo.get(UrlAPI.urlBaoer)
                    .params("id", cmtinfo.getSponsor())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Gson gson = new Gson();
                            returnBaoer returnCmter = gson.fromJson(s, returnBaoer.class);
                            if (returnCmter.getCode() == 200 && returnCmter.getMsg().equals("success")) {
                                BaoerInfo Cmter = returnCmter.getData();
                                cmtSponsor.setText(Cmter.getNickname());
                                if (Cmter.getImageUrl().length() > 10) {
                                    Glide.with(getBaseContext()).load(Cmter.getImageUrl()).into(cmtHead);
                                } else {
                                    Glide.with(getBaseContext()).load(R.drawable.immale).into(cmtHead);
                                }
                                if (lg == 0) {
                                    if (user1.getId() == cmtinfo.getSponsor()) {
                                        Log.i("==", user1.getId() + ":" + cmtinfo.getSponsor() + "=" + (user1.getId() == cmtinfo.getSponsor()));
                                        cmtDel.setText(R.string.delete);
                                        cmtDel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                OkGo.post(UrlAPI.urlDelCmt)
                                                        .params("id", cmtinfo.getId())
                                                        .execute(new StringCallback() {
                                                            @Override
                                                            public void onSuccess(String s, Call call, Response response) {
                                                                Gson gson = new Gson();
                                                                returnSuccess rs = gson.fromJson(s, returnSuccess.class);
                                                                if (rs.getCode() == 200 && rs.getMsg().equals("success")) {
                                                                    Toast.makeText(BaonewsActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                                                                    getCmts(cmtinfo.getBaoliaoid() + "");
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                }
                            }

                        }

                    });


            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setImageResource(R.drawable.like0);
                }
            });
            return view;
        }
    }

    private String getContent(String s) {
        StringBuffer sb = new StringBuffer(s);
        int n = sb.indexOf("$", 0);
        if (n != -1) {
            return sb.substring(0, n).toString();
        } else {
            return sb.toString();
        }
    }

    private String getTime(String s) {
        return s.substring(s.indexOf("$") + 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BaonewsActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


