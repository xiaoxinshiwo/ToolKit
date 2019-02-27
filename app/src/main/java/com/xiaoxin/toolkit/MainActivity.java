package com.xiaoxin.toolkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 打开本地缓存提供JS调用,可以使用localstorage
        settings.setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/www/workdayCalc.html");


    }

}
