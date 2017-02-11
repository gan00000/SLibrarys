package com.starpy.model.login.callback;

import com.core.base.callback.ISCallBack;
import com.starpy.model.login.bean.LoginParameters;



/**
 * Class Description：厂商回调接口
 * @author Joe
 * @date 2013-4-16
 * @version 1.0
 */
public interface OnISLoginListener extends ISCallBack {
	void onFinishLoginProcess(LoginParameters params);
}
