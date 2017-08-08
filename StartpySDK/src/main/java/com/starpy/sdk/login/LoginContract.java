package com.starpy.sdk.login;

import android.app.Activity;

import com.om.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.BaseView;
import com.starpy.IBasePresenter;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.thirdlib.google.SGoogleSignIn;

/**
 * Created by gan on 2017/4/13.
 */

public class LoginContract {

    public interface ILoginView extends BaseView {

        public void LoginSuccess(SLoginResponse sLoginResponse);
        public void changePwdSuccess(SLoginResponse sLoginResponse);
        public void findPwdSuccess(SLoginResponse sLoginResponse);

        public void showAutoLoginTips(String tips);

        public void showAutoLoginView();

        public void showLoginView();

        public void showAutoLoginWaitTime(String time);

        public void accountBindSuccess(SLoginResponse sLoginResponse);
    }

    public interface ILoginPresenter extends IBasePresenter<ILoginView> {

        public void starpyAccountLogin(Activity activity, String account, String pwd);

        public void fbLogin(Activity activity, SFacebookProxy sFacebookProxy);
        public void googleLogin(Activity activity, SGoogleSignIn sGoogleSignIn);

        public void thirdPlatLogin(Activity activity, String thirdPlatId, String registPlatform);

        public void macLogin(Activity activity);

        public void register(Activity activity, String account, String pwd, String email);
        public void changePwd(Activity activity, String account, String oldPwd, String newPwd);
        public void findPwd(Activity activity, String account, String email);
        public void accountBind(Activity activity, String account, String pwd, String email, int bindType, SFacebookProxy sFacebookProxy);
        public void accountInject(Activity activity, String account, String pwd,String uid);

        public void autoLogin(Activity activity);

        public void autoLoginChangeAccount(Activity activity);

        public boolean hasAccountLogin();

        public void destory(Activity activity);

    }

}
