package com.dazhi.newsdemo2.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dazhi.newsdemo2.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().hide();  //去除actionBar

        mWebView = (WebView) findViewById(R.id.webview);

        Bundle bundle = getIntent().getExtras();
        String webUrl = bundle.getString("link") ;

        mWebView.loadUrl(webUrl);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);





    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
