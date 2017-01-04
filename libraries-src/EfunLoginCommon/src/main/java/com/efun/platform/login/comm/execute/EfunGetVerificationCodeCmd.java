package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.dao.impl.EfunGetVerificationCodeImpl;


public class EfunGetVerificationCodeCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public EfunGetVerificationCodeCmd(Context context, String userName, String password, String phoneNumber,String email) {
		super(context, new EfunGetVerificationCodeImpl());
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		
		if (EfunStringUtil.isAllEmpty(phoneNumber,email)) {
			Log.e("efunLog", "手机号和email不能同时为空");
			return;
		}
		this.listenerParameters.setUserName(userName);
		this.listenerParameters.setPassword(password);
		this.listenerParameters.setPhoneNumber(phoneNumber);
		this.listenerParameters.setEmail(email);
		
	}
	
	

	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
