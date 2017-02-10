package com.core.base.task;


import com.core.base.task.command.abstracts.EfunCommand;


/**
 * 请求回调
 * @author Administrator
 *
 */
public interface EfunCommandCallBack {
	public void cmdCallBack(EfunCommand command);
}
