package com.efun.platform.login.comm.callback;

import com.efun.core.callback.EfunCallBack;
import com.efun.platform.login.comm.bean.SwitchParameters;

/**
 * @author Xiong
 * 统一开关回调接口
 */
public interface OnEfunSwitchCallBack extends EfunCallBack{
	void switchCallBack(SwitchParameters parameters);
}
