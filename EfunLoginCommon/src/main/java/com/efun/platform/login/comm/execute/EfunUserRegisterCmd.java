package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.constant.EfunLoginType;
import com.efun.platform.login.comm.dao.impl.EfunRegisterImpl;

public class EfunUserRegisterCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EfunUserRegisterCmd(Context context, String userName, String password, String email, String advertisersName, String partnerName, String platForm) {
		super(context,new EfunRegisterImpl());
		
		listenerParameters.setUserName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		listenerParameters.setEmail(email);
		listenerParameters.setPartner(partnerName);
		listenerParameters.setPlatForm(platForm);
		listenerParameters.setAdvertisersName(advertisersName);
		this.loginType = EfunLoginType.LOGIN_TYPE_EFUN;
	}
	public EfunUserRegisterCmd(Context context, String userName, String password, String email, String advertisersName, String partnerName, String platForm, String phoneNumber, String phoneCode) {
		super(context,new EfunRegisterImpl());
		
		listenerParameters.setUserName(userName);
		if (password.length() >= 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		listenerParameters.setEmail(email);
		listenerParameters.setPartner(partnerName);
		listenerParameters.setPlatForm(platForm);
		listenerParameters.setAdvertisersName(advertisersName);
		listenerParameters.setPhoneNumber(phoneNumber);
		listenerParameters.setCode(phoneCode);
		this.loginType = EfunLoginType.LOGIN_TYPE_EFUN;
	}
	@Override
	public void execute() throws Exception {
		super.execute();	
		saveLoginReponse(mResponse);
	}

}
