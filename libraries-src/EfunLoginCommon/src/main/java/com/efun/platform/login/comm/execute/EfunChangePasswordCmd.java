package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.dao.impl.EfunChagePwdImpl;

public class EfunChangePasswordCmd extends EfunBaseCmd {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunChangePasswordCmd(Context context, String userName, String password, String newPassword) {
		super(context,new EfunChagePwdImpl());
		
		this.listenerParameters.setUserName(userName);
		if (password.length() > 32 || newPassword.length() >= 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
	
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		listenerParameters.setNewPassword(EfunStringUtil.toMd5(newPassword, false));

	}


	@Override
	public void execute() throws Exception {
		super.execute();
	}
}
