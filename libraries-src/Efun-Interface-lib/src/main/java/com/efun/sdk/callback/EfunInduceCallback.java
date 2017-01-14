package com.efun.sdk.callback;

import com.efun.core.callback.EfunCallBack;

public interface EfunInduceCallback extends EfunCallBack{
	
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
