package com.starpy.model.login.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.AccountLoginRequest;
import com.starpy.model.login.constant.SLoginType;

public class EfunUserLoginCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountLoginRequest accountLoginRequest;
	
	public EfunUserLoginCmd(Context mContext, String userName, String password) {
		super(mContext);
		accountLoginRequest.setName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		accountLoginRequest.setPwd(password);
	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();

		saveLoginReponse(mResponse);
	}
	
}
