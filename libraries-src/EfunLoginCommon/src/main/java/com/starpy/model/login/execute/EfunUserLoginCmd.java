package com.starpy.model.login.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.starpy.base.utils.SStringUtil;
import com.starpy.model.login.constant.SLoginType;

public class EfunUserLoginCmd extends EfunBaseCmd{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunUserLoginCmd(Context mContext, String userName, String password, String advertisersName, String partnerName) {
		super(mContext);
		listenerParameters.setUserName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(SStringUtil.toMd5(password, false));
		
		listenerParameters.setAdvertisersName(advertisersName);
		listenerParameters.setPartner(partnerName);
		
		this.loginType = SLoginType.LOGIN_TYPE_EFUN;
	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();		
		saveLoginReponse(mResponse);
	}
	
}
