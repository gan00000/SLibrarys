package com.starpy.sdk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
//        initContentLayout();
    }

    public SWebViewDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.activity = (Activity)context;
//        initContentLayout();
    }

    protected SWebViewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity)context;
//        initContentLayout();
    }

    void initContentLayout(){
        //获得dialog的window窗口
        Window window = this.getWindow();

        int padDimension = activity.getResources().getDimensionPixelSize(R.dimen.px_20);
        window.getDecorView().setPadding(padDimension, padDimension, padDimension, padDimension);

        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        setCanceledOnTouchOutside(true);

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
        sWebViewLayout = new SWebViewLayout(activity);
        this.setContentView(sWebViewLayout);
        sWebView = sWebViewLayout.getsWebView();
        sWebViewLayout.getCloseImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sWebView.loadUrl(webUrl);

    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        PL.i("Dialog onAttachedToWindow");
    }

}
