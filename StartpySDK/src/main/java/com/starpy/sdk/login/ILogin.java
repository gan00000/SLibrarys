package com.starpy.sdk.login;

import android.app.Activity;

import com.core.base.callback.IGameLifeCycle;
import com.starpy.data.login.ILoginCallBack;

/**
 * Created by gan on 2017/4/12.
 */

public interface ILogin extends IGameLifeCycle {

    public void startLogin(Activity activity,ILoginCallBack iLoginCallBack);
}
