package com.efun.platform.login.comm.execute;

import com.efun.platform.login.comm.dao.impl.EfunGetVerificationCodePhoneImpl;

import android.content.Context;

public class EfunGetVerificationCodeByPhoneCmd extends EfunBaseCmd {

	public EfunGetVerificationCodeByPhoneCmd(Context context,String phoneNum) {
		super(context, new EfunGetVerificationCodePhoneImpl());
		this.listenerParameters.setPhoneNumber(phoneNum);
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
	
	}

}
