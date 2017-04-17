package com.starpy.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.core.base.SWebView;
import com.core.base.utils.PL;
import com.core.base.utils.ToastUtils;

/**
 * Created by gan on 2017/3/31.
 */

public class SWebViewDialog extends SBaseDialog {

    private Activity activity;
    private ProgressBar progressBar;
    private SWebView sWebView;

   private SWebViewLayout sWebViewLayout;

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    private String webUrl = "";

    public SWebViewDialog(@NonNull Context context) {
        super(context);
        this.activity = (Activity)context;
    }

    public SWebViewDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.activity = (Activity)context;
    }

    protected SWebViewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity)context;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        PL.i("Dialog onBackPressed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PL.i("Dialog onCreate");

        PL.i("SWebViewActivity url:" + webUrl);
        if (TextUtils.isEmpty(webUrl)){
            ToastUtils.toast(getContext(),"url error");
            PL.i("webUrl is empty");
            dismiss();
            return;
        }

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (sWebView != null){
                    sWebView.clearCache(true);
                    sWebView.clearHistory();
                    sWebView.destroy();
                    sWebView = null;
                    PL.i("dialog destory webview");
                }
            }
        });

        sWebViewLayout = new SWebViewLayout(activity);
        this.setContentView(sWebViewLayout);
        sWebView = sWebViewLayout.getsWebView();
        sWebViewLayout.getCloseImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (sWebView != null) {
            sWebView.loadUrl(webUrl);
        }

    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        PL.i("Dialog onAttachedToWindow");
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (sWebView != null){
            sWebView.onActivityResult(requestCode, resultCode, data);
        }
    }

}
