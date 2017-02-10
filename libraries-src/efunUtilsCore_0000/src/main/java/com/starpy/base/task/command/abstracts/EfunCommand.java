package com.starpy.base.task.command.abstracts;

import com.starpy.base.task.command.EfunBaseCommand;

import android.content.DialogInterface.OnCancelListener;

/**
 * Class Description：
 * @author Joe
 * @date 2013-4-16
 * @version 1.0
 */
public abstract class EfunCommand extends EfunBaseCommand {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * getRequest Method
	 * Method Description :返回值
	 */
	public abstract String getResponse();

	/**
	 * 执行操作
	 * @throws Exception
	 */
	public abstract void execute() throws Exception;
	
	/**
	 * 任务是否已经该取消
	 */
	protected volatile boolean hasCancel = false;

	/**
	 * @return the hasCancel
	 */
	public boolean isHasCancel() {
		return hasCancel;
	}

	/**
	 * @param hasCancel the hasCancel to set
	 */
	public void setHasCancel(boolean hasCancel) {
		this.hasCancel = hasCancel;
	}
	
	private boolean b = false;
	public void setCanceledOnTouchOutside(boolean b){
		this.b = b;
	}
	public boolean getCanceledOnTouchOutside(){
		return this.b;
	}
	
	private boolean cancelable = true;
	public void setCancelable(boolean cancelable){
		this.cancelable = cancelable;
	}
	public boolean getCancelable(){
		return this.cancelable;
	}
	
	private OnCancelListener cancelListener = null;
	public void setCancelListener(OnCancelListener cancelListener){
		this.cancelListener = cancelListener;
	}
	
	public OnCancelListener getCancelListener(){
		return this.cancelListener;
	}
	
}
