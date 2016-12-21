package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.dao.impl.EfunAssistBindImpl;


public class EfunBindEmailPhoneCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public EfunBindEmailPhoneCmd(Context context, String userName, String code, String phoneNumber,String email) {
		super(context, new EfunAssistBindImpl());
		if (TextUtils.isEmpty(code)) {
			Log.e("efunLog", "验证码code为空");
			return;
		}
		
		if (EfunStringUtil.isAllEmpty(phoneNumber,email)) {
			Log.e("efunLog", "手机号和email不能同时为空");
			return;
		}
		
		this.listenerParameters.setUserName(userName);
		this.listenerParameters.setPhoneNumber(phoneNumber);
		this.listenerParameters.setEmail(email);
		this.listenerParameters.setVerificationCode(code);
		
	}
	

	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
