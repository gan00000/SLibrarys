package com.efun.platform.login.comm.callback;

import com.efun.core.callback.EfunCallBack;

/**
 * Created by Efun on 2016/12/15.
 */

public interface EfunLogoutListener extends EfunCallBack {

    /** 退出登陆回调 */
    void afterLogout();
}
