package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

public interface ISInduceCallback extends ISCallBack {
	
	public void onError(String errMsg);
	/**
	 * 用户购买诱导商品成功
	 */
	public void onPaySuccess();
	/**
	 * 用户关闭诱导储值页面
	 */
	public void onPageClosed();
	
}
