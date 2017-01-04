package com.efun.platform.login.comm.callback;

import com.efun.core.callback.EfunCallBack;

/**
 * 平台系统设置回调接口（因为UI相关，暂时放在登陆公共Jar）
 * Created by Efun on 2017/1/4.
 */

public interface EfunSystemSettingCallback extends EfunCallBack {

    /**
     * 系统设置回调方法
     * @param what 功能code
     * @param obj 返回封装好的对象作为回参
     */
    void onProcessFinished(int what, Object obj);
}
