package com.efun.core.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BaseWebView extends WebView {

    BaseWebChromeClient efunWebChromeClient;

    public BaseWebView(Context context) {
        super(context);
        initBaseWebView();
    }

    @SuppressLint("NewApi")
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBaseWebView();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBaseWebView();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBaseWebView();
    }


    public void setEfunWebChromeClient(BaseWebChromeClient efunWebChromeClient)
    {
        this.setWebChromeClient(efunWebChromeClient);
        this.efunWebChromeClient = efunWebChromeClient;
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initBaseWebView() {

        if (getContext() instanceof  Activity){
            efunWebChromeClient = new BaseWebChromeClient((Activity) getContext());
        }else{
            efunWebChromeClient = new BaseWebChromeClient();
        }
        this.setWebChromeClient(efunWebChromeClient);

        WebSettings ws = this.getSettings();

        ws.setDomStorageEnabled(true);
        //加上这句话才能使用javascript方法
        ws.setJavaScriptEnabled(true);

        ws.setAllowFileAccess(true);
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setDatabaseEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ws.setAllowContentAccess(true);
        }
        ws.setAppCachePath(getContext().getCacheDir().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ws.setAllowFileAccessFromFileURLs(true);
            ws.setAllowUniversalAccessFromFileURLs(true);
        }

        ws.setDefaultTextEncodingName("UTF-8");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ws.setLoadsImagesAutomatically(true);
        } else {
            ws.setLoadsImagesAutomatically(false);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (efunWebChromeClient != null) {
            efunWebChromeClient.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
