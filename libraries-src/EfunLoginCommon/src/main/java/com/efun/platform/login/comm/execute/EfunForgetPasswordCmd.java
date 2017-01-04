package com.efun.platform.login.comm.execute;

import android.content.Context;

import com.efun.platform.login.comm.dao.impl.EfunForgetPwdImpl;

public class EfunForgetPasswordCmd extends EfunBaseCmd {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunForgetPasswordCmd(Context context,String userName, String email) {
		super(context,new EfunForgetPwdImpl());
		this.listenerParameters.setUserName(userName);
		this.listenerParameters.setEmail(email);
	}
	
	public EfunForgetPasswordCmd(Context context,String userName,String email, String phoneNumber) {
		super(context,new EfunForgetPwdImpl(100));
		this.listenerParameters.setUserName(userName);
		this.listenerParameters.setEmail(email);
		this.listenerParameters.setPhoneNumber(phoneNumber);
	}
	public EfunForgetPasswordCmd(Context context,String userName,String email, String phoneNumber,String verificationCode) {
		super(context,new EfunForgetPwdImpl(100));
		this.listenerParameters.setUserName(userName);
		this.listenerParameters.setEmail(email);
		this.listenerParameters.setPhoneNumber(phoneNumber);
		this.listenerParameters.setVerificationCode(verificationCode);
	}
	

	@Override
	public void execute() throws Exception {
		super.execute();
		//找回密码
	}

}
