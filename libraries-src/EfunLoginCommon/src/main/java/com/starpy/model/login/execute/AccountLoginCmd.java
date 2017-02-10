package com.starpy.model.login.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.AccountLoginRequest;
import com.starpy.model.login.constant.SLoginType;

import java.util.Map;

public class AccountLoginCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountLoginRequest accountLoginRequest;
	
	public AccountLoginCmd(Context mContext, String userName, String password) {
		super(mContext);

		accountLoginRequest = new AccountLoginRequest(mContext);
		baseRequest = accountLoginRequest;
		accountLoginRequest.setName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		accountLoginRequest.setPwd(password);

		accountLoginRequest.setCompleteUrl("http://10.10.10.110:8080/login/login");
		accountLoginRequest.setAppKey("test123");
		accountLoginRequest.setGameCode("test");
		accountLoginRequest.setGameLanguage("tw");

		accountLoginRequest.setSignature(SStringUtil.toMd5(accountLoginRequest.getAppKey() + accountLoginRequest.getTimestamp() +
				accountLoginRequest.getName() + accountLoginRequest.getPwd() + accountLoginRequest.getGameCode()));

	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();

		mResponse = doRequest(accountLoginRequest);
		saveLoginReponse(mResponse);
	}
	
}
