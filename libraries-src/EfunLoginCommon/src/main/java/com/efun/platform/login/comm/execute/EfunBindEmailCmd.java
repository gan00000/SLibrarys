package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.dao.impl.EfunBindEmailImpl;


public class EfunBindEmailCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EfunBindEmailCmd(Context context, String userName, String password, String email) {
		super(context,new EfunBindEmailImpl());
		listenerParameters.setUserName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		listenerParameters.setEmail(email);
	}
	

	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
