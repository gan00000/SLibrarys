package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Intent;

import com.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.sdk.utils.DialogUtil;
import com.starpy.thirdlib.google.SGoogleSignIn;

/**
 * Created by gan on 2017/4/12.
 */

public class DialogLoginImpl implements ILogin {

    private SFacebookProxy sFacebookProxy;

    private SGoogleSignIn sGoogleSignIn;


    @Override
    public void onCreate(Activity activity) {

        // 2.实例EfunFacebookProxy
        sFacebookProxy = new SFacebookProxy(activity.getApplicationContext());

        sGoogleSignIn = new SGoogleSignIn(activity, DialogUtil.createLoadingDialog(activity, "Loading..."));

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (sFacebookProxy != null) {
            sFacebookProxy.onActivityResult(activity, requestCode, resultCode, data);
        }
        if (sGoogleSignIn != null){
            sGoogleSignIn.handleActivityResult(activity,requestCode,resultCode,data);
        }
    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        if (sFacebookProxy != null) {
            sFacebookProxy.onDestroy(activity);
        }
    }

    @Override
    public void startLogin(Activity activity, ILoginCallBack iLoginCallBack) {
        SLoginDialogV2 sLoginDialog = new SLoginDialogV2(activity , com.starpy.sdk.R.style.Starpy_Theme_AppCompat_Dialog_Notitle_Fullscreen);
        sLoginDialog.setSFacebookProxy(sFacebookProxy);
        sLoginDialog.setSGoogleSignIn(sGoogleSignIn);
        sLoginDialog.setLoginCallBack(iLoginCallBack);
        sLoginDialog.show();
    }
}
