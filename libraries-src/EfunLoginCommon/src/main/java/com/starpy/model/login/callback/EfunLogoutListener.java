package com.starpy.model.login.callback;

import com.starpy.base.callback.EfunCallBack;

/**
 * 退出登陆回调
 * Created by Efun on 2016/12/15.
 */

public interface EfunLogoutListener extends EfunCallBack {

    /** 退出登陆回调 */
    void afterLogout();
}
