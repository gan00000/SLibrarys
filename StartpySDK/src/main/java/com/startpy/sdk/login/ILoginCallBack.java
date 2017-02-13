package com.startpy.sdk.login;

import com.core.base.callback.ISCallBack;
import com.starpy.model.login.bean.SLoginResponse;

/**
 * Created by Efun on 2017/2/13.
 */

public interface ILoginCallBack extends ISCallBack{

    void onLogin(SLoginResponse sLoginResponse);

}
