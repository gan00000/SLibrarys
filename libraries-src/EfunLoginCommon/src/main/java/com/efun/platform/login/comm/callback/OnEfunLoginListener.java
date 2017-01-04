package com.efun.platform.login.comm.callback;

import com.efun.core.callback.EfunCallBack;
import com.efun.platform.login.comm.bean.LoginParameters;



/**
 * Class Description：厂商回调接口
 * @author Joe
 * @date 2013-4-16
 * @version 1.0
 */
public interface OnEfunLoginListener extends EfunCallBack {
	void onFinishLoginProcess(LoginParameters params);
}
