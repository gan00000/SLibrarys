package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Intent;

import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;

/**
 * Created by gan on 2017/4/12.
 */

public class AcrivityLoginImpl implements ILogin {

    private ILoginCallBack loginCallBack;

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

    @Override
    public void startLogin(Activity activity, ILoginCallBack iLoginCallBack) {

        this.loginCallBack = iLoginCallBack;

        Intent intent = new Intent(activity, SLoginActivity.class);

        activity.startActivityForResult(intent, SLoginActivity.S_LOGIN_REQUEST);//開啟登入

    }
}
