package com.efun.core.task;


import com.efun.core.task.command.abstracts.EfunCommand;


/**
 * 请求回调
 * @author Administrator
 *
 */
public interface EfunCommandCallBack {
	public void cmdCallBack(EfunCommand command);
}
