package com.jit.silly.hlnews.NewsT;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jit.silly.hlnews.R;

import java.net.MalformedURLException;
import java.net.URL;

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar)findViewById(R.id.news_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        String url = bundle.getString("url");
        String picurl = bundle.getString("picurl");
        String title = bundle.getString("title");
        ImageView pic = (ImageView)findViewById(R.id.news_image_view);
        WebView web = (WebView) findViewById(R.id.news_webView);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(title);

        Glide.with(this).load(picurl).into(pic);

        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                try {
                    URL realUrl = new URL(url);
                    System.out.println(realUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return true;
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
