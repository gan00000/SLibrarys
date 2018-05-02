package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Intent;

import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.sdk.utils.DialogUtil;
import com.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.thirdlib.google.SGoogleSignIn;
import com.starpy.thirdlib.wx.SWXProxy;

/**
 * Created by gan on 2017/4/12.
 */

public class DialogLoginImpl implements ILogin {

    private SFacebookProxy sFacebookProxy;

    private SGoogleSignIn sGoogleSignIn;

    private  SLoginDialogV2 sLoginDialogV2;

    private SWXProxy swxProxy;//

    @Override
    public void onCreate(Activity activity) {

        sGoogleSignIn = new SGoogleSignIn(activity, DialogUtil.createLoadingDialog(activity, "Loading..."));
        if (StarPyUtil.isMainland(activity) && swxProxy == null){
            swxProxy = SWXProxy.getSWXProxy();
            swxProxy.init(activity, ResConfig.getConfigInAssets(activity,"star_sdk_wx_appid"),
                    ResConfig.getConfigInAssets(activity,"star_sdk_wx_appsecret"));
        }
    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
//        if (sFacebookProxy != null && requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
//            sFacebookProxy.onActivityResult(activity, requestCode, resultCode, data);
//        }
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

    }

    @Override
    public void startLogin(Activity activity, ILoginCallBack iLoginCallBack) {
        if (sLoginDialogV2 != null) {
            sLoginDialogV2.dismiss();
            sLoginDialogV2 = null;
        }
        sLoginDialogV2 = new SLoginDialogV2(activity , com.starpy.sdk.R.style.Starpy_Theme_AppCompat_Dialog_Notitle_Fullscreen);
        sLoginDialogV2.setSFacebookProxy(sFacebookProxy);
        sLoginDialogV2.setSGoogleSignIn(sGoogleSignIn);
        sLoginDialogV2.setLoginCallBack(iLoginCallBack);
        sLoginDialogV2.show();
    }

    @Override
    public void initFacebookPro(Activity activity, SFacebookProxy sFacebookProxy) {
        this.sFacebookProxy = sFacebookProxy;
    }
}
