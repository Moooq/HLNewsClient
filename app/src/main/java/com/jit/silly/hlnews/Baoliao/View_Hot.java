package com.jit.silly.hlnews.Baoliao;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.jit.silly.hlnews.personal.returnList;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by moqiandemac on 2017/6/16.
 */

public class View_Hot extends Fragment {

    returnBao returnInfo;
    private PullToRefreshView mPullToRefreshView;
    ListView hot_news;
    View view_hot;
    int bs;
    ArrayList<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view_hot = inflater.inflate(R.layout.view_hot, container, false);
        hot_news = (ListView) view_hot.findViewById(R.id.hot_news);


        mPullToRefreshView = (PullToRefreshView) view_hot.findViewById(R.id.pull_to_refresh2);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        getNetData();
                    }
                }, 2000);
            }
        });
        getNetData();
        return view_hot;
    }


    private void getNetData() {
        OkGo.get(UrlAPI.urlBao).execute(new StringCallback() {
            @Override
            public void onSuccess(String arg0, Call arg1, Response arg2) {
                Gson gson = new Gson();
                returnInfo = gson.fromJson(arg0, returnBao.class);
                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                    bs = returnInfo.getData().length;
                    Log.i("body", "" + bs);
                    MyBaseAdapter adapter = new MyBaseAdapter(returnInfo.getData());
                    hot_news.setAdapter(adapter);
                    hot_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(getActivity(), BaonewsActivity.class);
                            list.clear();
                            list.add(returnInfo.getData()[bs - 1 - position].getEditorid());
                            list.add(returnInfo.getData()[bs - 1 - position].getId() + "");
                            list.add(returnInfo.getData()[bs - 1 - position].getTitle());
                            list.add(returnInfo.getData()[bs - 1 - position].getContent());
                            list.add(returnInfo.getData()[bs - 1 - position].getPicurl1());
                            list.add(returnInfo.getData()[bs - 1 - position].getPicurl2());
                            list.add(returnInfo.getData()[bs - 1 - position].getPicurl3());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ahot", list);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 3);
                        }
                    });
                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 3 && resultCode == 3) {
                Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
                getNetData();
            }
        }
    }

    private class MyBaseAdapter extends BaseAdapter {
        private BaoInfo[] data;

        private MyBaseAdapter(BaoInfo[] list) {
            this.data = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return bs;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return data[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            final View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_hots, null);
            final TextView bliaolistItemContent = (TextView) view.findViewById(R.id.content_hots);
            final ImageView bliaolist_item_pic = (ImageView) view.findViewById(R.id.image_hots);
            final TextView bliaolist_item_title = (TextView) view.findViewById(R.id.title_hots);
            final ImageView bliaolist_item_user_head = (ImageView) view.findViewById(R.id.editor_head_hots);
            final TextView bliaolist_item_user_nick = (TextView) view.findViewById(R.id.editor_hots);
            final BaoInfo blItem = data[bs - arg0 - 1];
            Log.i("body", "" + blItem.getEditorid());
            OkGo.get(UrlAPI.urlBaoer)
                    .params("id", blItem.getEditorid())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Gson gson = new Gson();
                            returnBaoer returnbaoer = gson.fromJson(s, returnBaoer.class);
                            if (returnbaoer.getCode() == 200 && returnbaoer.getMsg().equals("success")) {
                                bliaolist_item_user_nick.setText(returnbaoer.getData().getNickname());
                                if (returnbaoer.getData().getImageUrl().length() > 10) {
                                    Glide.with(getActivity().getBaseContext()).load(returnbaoer.getData().getImageUrl()).into(bliaolist_item_user_head);
                                } else {
                                    Glide.with(getActivity().getBaseContext()).load(R.drawable.immale).into(bliaolist_item_user_head);
                                }
                                bliaolist_item_title.setText(blItem.getTitle());
                                bliaolistItemContent.setText(blItem.getContent());
                                Log.i("imageeee", "" + blItem.getPicurl1());
                                if (!blItem.getPicurl1().equals(" ")) {
                                    Log.i("imageeee", "" + blItem.getPicurl1());
                                    Glide.with(getActivity().getBaseContext()).load(blItem.getPicurl1()).into(bliaolist_item_pic);
                                } else {
                                    Glide.with(getActivity().getBaseContext()).load(R.drawable.nopic).into(bliaolist_item_pic);
                                }

                            }

                        }
                    });
            return view;
        }
    }

}
