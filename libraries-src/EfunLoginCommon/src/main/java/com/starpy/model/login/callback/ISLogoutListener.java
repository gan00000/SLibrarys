package com.starpy.model.login.callback;

import com.core.base.callback.ISCallBack;

/**
 * 退出登陆回调
 * Created by Efun on 2016/12/15.
 */

public interface ISLogoutListener extends ISCallBack {

    /** 退出登陆回调 */
    void afterLogout();
}
