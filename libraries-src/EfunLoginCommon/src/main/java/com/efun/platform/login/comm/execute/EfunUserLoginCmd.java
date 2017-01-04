package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.constant.EfunLoginType;
import com.efun.platform.login.comm.dao.impl.EfunLoginImpl;

public class EfunUserLoginCmd extends EfunBaseCmd{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunUserLoginCmd(Context mContext, String userName, String password, String advertisersName, String partnerName) {
		super(mContext,new EfunLoginImpl());
		listenerParameters.setUserName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		
		listenerParameters.setAdvertisersName(advertisersName);
		listenerParameters.setPartner(partnerName);
		
		this.loginType = EfunLoginType.LOGIN_TYPE_EFUN;
	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();		
		saveLoginReponse(mResponse);
	}
	
}
