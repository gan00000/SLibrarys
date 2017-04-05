package com.starpy.sdk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.core.base.BaseWebChromeClient;
import com.core.base.BaseWebViewClient;
import com.core.base.SWebView;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;

/**
 * Created by gan on 2017/3/31.
 */

public class SWebViewDialog extends SBaseDialog {


    private ProgressBar progressBar;
    private SWebView sWebView;

    public SWebViewDialog(@NonNull Context context) {
        super(context);
        initContentLayout();
    }

    public SWebViewDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initContentLayout();
    }

    protected SWebViewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initContentLayout();
    }

    void initContentLayout(){

//        FrameLayout frameLayout = new FrameLayout(getContext());
        this.setContentView(R.layout.s_web_view_layout);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        PL.i("onBackPressed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PL.i("onCreate");
        String webUrl = "https://www.baidu.com";

        if (TextUtils.isEmpty(webUrl)){
            ToastUtils.toast(getContext(),"url error");
            PL.i("webUrl is empty");
            dismiss();
            return;
        }

        initTitle("");

        progressBar = (ProgressBar) findViewById(R.id.s_webview_pager_loading_percent);
        sWebView = (SWebView) findViewById(R.id.s_webview_id);

        sWebView.setBaseWebChromeClient(new BaseWebChromeClient(progressBar,getOwnerActivity()));
        sWebView.setWebViewClient(new BaseWebViewClient(getOwnerActivity()));
        PL.i("SWebViewActivity url:" + webUrl);
        sWebView.loadUrl(webUrl);

    }

    private void initTitle(String webTitle) {

        View titleLayout = findViewById(R.id.py_title_layout_id);
        if (SStringUtil.isNotEmpty(webTitle)){
            titleLayout.setVisibility(View.VISIBLE);
            titleLayout.setBackgroundResource(R.mipmap.py_title_sdk_bg);
        }else{
            titleLayout.setVisibility(View.GONE);
            return;
        }

        findViewById(R.id.py_back_button).setVisibility(View.GONE);
        TextView titleTextView = (TextView) findViewById(R.id.py_title_id);
        titleTextView.setText(webTitle);
        View rightCloseView = findViewById(R.id.py_title_right_button);
        rightCloseView.setVisibility(View.VISIBLE);
        rightCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        PL.i("onAttachedToWindow");
    }

}
