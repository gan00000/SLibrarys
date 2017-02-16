package com.starpy.sdk.out;

import android.app.Activity;
import android.content.Intent;

import com.starpy.base.cfg.ConfigRequest;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.pay.PayType;
import com.starpy.sdk.SWebViewActivity;
import com.starpy.sdk.login.SLoginActivity;

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
        StarPyUtil.saveRoleInfo(activity, roleId, roleLevel, severCode, serverName);//保存角色信息
    }

    @Override
    public void login(Activity activity, ILoginCallBack iLoginCallBack) {

        this.loginCallBack = iLoginCallBack;

        Intent intent = new Intent(activity, SLoginActivity.class);

        activity.startActivityForResult(intent, SLoginActivity.S_LOGIN_REQUEST);//開啟登入

    }

    @Override
    public void pay(Activity activity, PayType payType, String cpOrderId, String productId, String roleLevel, String customize) {

        if (payType == PayType.OTHERS){//第三方储值

            Intent i = new Intent(activity, SWebViewActivity.class);
            i.putExtra(SWebViewActivity.PLAT_WEBVIEW_URL,"https://www.baidu.com/");
            activity.startActivity(i);

        }else{//默认Google储值

        }
    }

    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

        if (requestCode == SLoginActivity.S_LOGIN_REQUEST && resultCode == SLoginActivity.S_LOGIN_RESULT && data != null) {
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
