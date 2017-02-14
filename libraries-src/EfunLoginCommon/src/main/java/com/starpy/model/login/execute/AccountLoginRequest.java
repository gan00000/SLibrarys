package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.utils.SStringUtil;

public class AccountLoginRequest extends BaseRequest {

	private com.starpy.model.login.bean.request.AccountLoginRequest accountLoginRequest;
	
	public AccountLoginRequest(Context mContext, String userName, String password) {
		super(mContext);

		accountLoginRequest = new com.starpy.model.login.bean.request.AccountLoginRequest(mContext);
		baseRequest = accountLoginRequest;
		accountLoginRequest.setName(userName);
		password = SStringUtil.toMd5(password);
		accountLoginRequest.setPwd(password);

		accountLoginRequest.setCompleteUrl("http://10.10.10.110:8080/login/login");
		accountLoginRequest.setAppKey("test123");
		accountLoginRequest.setGameCode("test");
		accountLoginRequest.setGameLanguage("tw");

		accountLoginRequest.setSignature(SStringUtil.toMd5(accountLoginRequest.getAppKey() + accountLoginRequest.getTimestamp() +
				accountLoginRequest.getName() + accountLoginRequest.getPwd() + accountLoginRequest.getGameCode()));

	}




}
