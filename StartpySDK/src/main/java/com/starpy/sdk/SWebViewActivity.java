package com.starpy.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.core.base.BaseWebChromeClient;
import com.core.base.BaseWebViewClient;
import com.core.base.SBaseActivity;
import com.core.base.SWebView;
import com.core.base.utils.PL;
import com.core.base.utils.ToastUtils;
import com.startpy.sdk.R;

/**
 * Created by Efun on 2016/12/1.
 */

public class SWebViewActivity extends SBaseActivity {

    public static final String PLAT_WEBVIEW_URL = "PLAT_WEBVIEW_URL";
    public static final String PLAT_WEBVIEW_TITLE = "PLAT_WEBVIEW_TITLE";
    private SWebView sWebView;
    private String webUrl;
    String webTitle;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.s_web_view_layout);

        if (getIntent() != null) {
            webUrl = getIntent().getStringExtra(PLAT_WEBVIEW_URL);
            webTitle = getIntent().getStringExtra(PLAT_WEBVIEW_TITLE);
        }

        if (TextUtils.isEmpty(webUrl)){
            ToastUtils.toast(getApplicationContext(),"url error");
            PL.i("webUrl is empty");
            finish();
            return;
        }


        progressBar = (ProgressBar) findViewById(R.id.s_webview_pager_loading_percent);
        sWebView = (SWebView) findViewById(R.id.s_webview_id);

        sWebView.setBaseWebChromeClient(new BaseWebChromeClient(progressBar,this));
        sWebView.setWebViewClient(new BaseWebViewClient(this));

        sWebView.loadUrl(webUrl);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (sWebView != null){
            sWebView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sWebView != null){
            sWebView.destroy();
            sWebView = null;
        }
    }
}