package com.core.base.request;


import com.core.base.request.command.abstracts.EfunCommand;


/**
 * 请求回调
 * @author Administrator
 *
 */
public interface EfunCommandCallBack {
	public void cmdCallBack(EfunCommand command);
}
