package com.starpy.base.task;


import com.starpy.base.task.command.abstracts.EfunCommand;


/**
 * 请求回调
 * @author Administrator
 *
 */
public interface EfunCommandCallBack {
	public void cmdCallBack(EfunCommand command);
}
