package com.starpy.sdk;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Window;
import android.view.WindowManager;

import com.core.base.utils.AppUtil;
import com.starpy.base.utils.Localization;

/**
 * Created by gan on 2017/3/31.
 */

public class SBaseDialog extends Dialog {


    public SBaseDialog(@NonNull Context context) {
        super(context);

        initContentLayout(context);
    }

    public SBaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initContentLayout(context);
    }

    protected SBaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initContentLayout(context);
    }

    private void initContentLayout(Context context){
        //获得dialog的window窗口
        Window window = this.getWindow();

        int padDimension = context.getResources().getDimensionPixelSize(R.dimen.px_15);
        window.getDecorView().setPadding(padDimension, padDimension, padDimension, padDimension);

        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();

        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        //将设置好的属性set回去
        window.setAttributes(lp);

        setCanceledOnTouchOutside(true);

        Localization.updateSGameLanguage(context);//设置应用内语言

        AppUtil.hideDialogBottomBar(this);

    }

    protected void setFullScreen(){

        //获得dialog的window窗口
        Window window = this.getWindow();

        window.getDecorView().setPadding(0, 0, 0, 0);

        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        //将设置好的属性set回去
        window.setAttributes(lp);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AppUtil.hideDialogBottomBar(this);
    }
}
