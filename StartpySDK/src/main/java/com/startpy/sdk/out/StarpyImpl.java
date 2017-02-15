package com.startpy.sdk.out;

import android.app.Activity;
import android.content.Intent;

import com.starpy.base.cfg.ConfigRequest;
import com.starpy.model.login.bean.SLoginResponse;
import com.startpy.sdk.login.ILoginCallBack;
import com.startpy.sdk.login.SLoginActivity;

/**
 * Created by Efun on 2017/2/13.
 */

public class StarpyImpl implements IStarpy {

    private static ILoginCallBack loginCallBack;

    @Override
    public void initSDK(Activity activity) {
        ConfigRequest.requestCfg(activity.getApplicationContext());//下载配置文件
        ConfigRequest.requestTermsCfg(activity.getApplicationContext());//下载服务条款
    }

    @Override
    public void setGameLanguage(Activity activity, String gameLanguage) {

    }

    @Override
    public void registerRoleInfo(Activity activity, String roleId, String roleName, String roleLevel, String severCode, String serverName) {

    }

    @Override
    public void login(Activity activity,ILoginCallBack iLoginCallBack) {

        this.loginCallBack = iLoginCallBack;

        Intent intent = new Intent(activity,SLoginActivity.class);

        activity.startActivityForResult(intent,SLoginActivity.S_LOGIN_REQUEST);

    }

    @Override
    public void pay(Activity activity, String cpOrderId, String productId, String roleLevel, String customize) {

    }

    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

        if (requestCode == SLoginActivity.S_LOGIN_REQUEST && resultCode == SLoginActivity.S_LOGIN_RESULT && data != null){
            SLoginResponse sLoginResponse = (SLoginResponse) data.getSerializableExtra(SLoginActivity.S_LOGIN_RESPONSE_OBJ);
            if (this.loginCallBack != null) {
                this.loginCallBack.onLogin(sLoginResponse);
            }
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
}
