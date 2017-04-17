package com.star.apptest;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.core.base.utils.PL;

import java.util.Locale;

public class MainActivity extends Activity {

    String url = "http://10.10.10.109:8086/chat.html";
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.testwebview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, SWebViewActivity.class);
//                i.putExtra(SWebViewActivity.PLAT_WEBVIEW_URL,url);
//                i.putExtra(SWebViewActivity.PLAT_WEBVIEW_TITLE,title);
//                startActivity(i);

                BatchTask b = new BatchTask();
                b.start();
            }
        });

        findViewById(R.id.testchangelang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changLanguage();

            }
        });
        findViewById(R.id.startnewactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(getApplicationContext(),MainActivity2.class));

            }
        });

    }


    private void changLanguage() {
        Resources resources = getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        PL.i("onConfigurationChanged:" + config.toString());
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        config.locale = Locale.US; //简体中文
        resources.updateConfiguration(config, dm);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PL.i("onConfigurationChanged:" + newConfig.toString());
    }
}
