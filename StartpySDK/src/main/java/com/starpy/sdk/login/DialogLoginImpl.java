package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Intent;

import com.facebook.sfb.SFacebookProxy;
import com.starpy.data.login.ILoginCallBack;

/**
 * Created by gan on 2017/4/12.
 */

public class DialogLoginImpl implements ILogin {

    private SFacebookProxy sFacebookProxy;

    @Override
    public void onCreate(Activity activity) {


        // 2.实例EfunFacebookProxy
        sFacebookProxy = new SFacebookProxy(activity.getApplicationContext());

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        sFacebookProxy.onActivityResult(activity, requestCode, resultCode, data);
    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        sFacebookProxy.onDestroy(activity);
    }

    @Override
    public void startLogin(Activity activity, ILoginCallBack iLoginCallBack) {
        SLoginDialog sLoginDialog = new SLoginDialog(activity , com.starpy.sdk.R.style.StarDialogTheme);
        sLoginDialog.setsFacebookProxy(sFacebookProxy);
        sLoginDialog.show();
    }
}
