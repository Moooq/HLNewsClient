package com.jit.silly.hlnews.NewsT;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jit.silly.hlnews.LyricIndicator;
import com.jit.silly.hlnews.NewsT.News;
import com.jit.silly.hlnews.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.jit.silly.hlnews.R.mipmap.ic_launcher;
import static com.jit.silly.hlnews.Url.UrlAPI.urlNews;

/**
 * Created by moqiandemac on 2017/6/16.
 */


public class View_Home extends Fragment {

    private Context context;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        Log.i("asdsa", "onAttach: "+context);
    }
    private LyricIndicator lyricIndicator;
    String[] str = new String[]{"Apple","International",
            "Home", "Mobile", "VR",
            "IT", "Military"};
    List<View> data = new ArrayList<View>();
    private PullToRefreshView mPullToRefreshView;
    private ViewPager viewPager;



    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        final View view_home = inflater.inflate(R.layout.view_home, container, false);
        //导航栏，viewpager
        LayoutInflater minflater = getActivity().getLayoutInflater();
        for (int i = 0; i < str.length; i++) {
            View v = minflater.inflate(R.layout.news, null);
            data.add(v);
        }
        viewPager = (ViewPager) view_home.findViewById(R.id.viewpager);
        lyricIndicator = (LyricIndicator) view_home.findViewById(R.id.indicator);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(data.get(position));
                return data.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(data.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return str[position];
            }
        });
        lyricIndicator.setupWithViewPager(viewPager);
        Log.i("Newsapi", "api:" + urlNews[0]);
        //OkGo
        for (int x = 0; x < urlNews.length; x++) {

            final int finalX = x;
            OkGo.get(urlNews[x]).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Gson gson = new Gson();
                    final News news = gson.fromJson(s, News.class);
                    BaseAdapter adapter = new BaseAdapter() {
                        Context context = getActivity().getBaseContext();

                        @Override
                        public int getCount() {
                            return 8;
                        }

                        @Override
                        public Object getItem(int position) {
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater mInflater = LayoutInflater.from(context);
                            if (convertView == null) {
                                convertView = mInflater.inflate(R.layout.item_news, null);
                            }
                            TextView title = (TextView) convertView.findViewById(R.id.title_news);
                            TextView ctime = (TextView) convertView.findViewById(R.id.ctime_news);
                            ImageView image = (ImageView) convertView.findViewById(R.id.image_news);
                            if (!news.getNewslist()[position].getPicUrl().isEmpty()) {
                                Picasso.with(context).load(news.getNewslist()[position].getPicUrl()).into(image);
                            } else {
                                Picasso.with(context).load(ic_launcher).into(image);
                            }
                            title.setText("" + news.getNewslist()[position].getTitle());
                            ctime.setText("" + news.getNewslist()[position].getCtime());
                            return convertView;
                        }
                    };

                    ListView listView = (ListView) data.get(finalX).findViewById(R.id.home_news);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.jit.silly.hlnews",
                                    "com.jit.silly.hlnews.NewsT.NewsActivity"));
                            intent.putExtra("url", news.getNewslist()[position].getUrl());
                            intent.putExtra("picurl", news.getNewslist()[position].getPicUrl());
                            intent.putExtra("title", news.getNewslist()[position].getTitle());
                            startActivity(intent);
                        }
                    });


                }
            });
        }

        //REFRESH
        mPullToRefreshView = (PullToRefreshView) data.get(0).findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return view_home;
    }

}
