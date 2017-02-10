package com.starpy.sdk.callback;

import com.core.base.callback.EfunCallBack;

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
