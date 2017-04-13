package com.starpy.sdk.login;

import android.app.Activity;

import com.starpy.BaseView;
import com.starpy.IBasePresenter;

/**
 * Created by gan on 2017/4/13.
 */

public class LoginContract {

    public interface ILoginView extends BaseView {

        public void LoginSuccess();

    }

   public interface ILoginPresenter extends IBasePresenter {

        public void starpyAccountLogin(Activity activity, String account, String pwd);

        public void fbLogin(Activity activity);

        public void macLogin(Activity activity);

        public void register(Activity activity, String account, String pwd);


    }

}
