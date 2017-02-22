package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;

public class AccountLoginRequestTask extends BaseRequestTask {

	private com.starpy.data.login.request.AccountLoginRequest accountLoginRequest;
	
	public AccountLoginRequestTask(Context mContext, String userName, String password) {
		super(mContext);

		userName = userName.toLowerCase();
		password = password.toLowerCase();

		accountLoginRequest = new com.starpy.data.login.request.AccountLoginRequest(mContext);
		baseRequest = accountLoginRequest;
		accountLoginRequest.setName(userName);
		password = SStringUtil.toMd5(password);
		accountLoginRequest.setPwd(password);

		accountLoginRequest.setRequestMethod("login");


	}


	@Override
	public BaseReqeustBean onHttpRequest() {
		super.onHttpRequest();

		accountLoginRequest.setSignature(SStringUtil.toMd5(accountLoginRequest.getAppKey() + accountLoginRequest.getTimestamp() +
				accountLoginRequest.getName() + accountLoginRequest.getPwd() + accountLoginRequest.getGameCode()));

		return  accountLoginRequest;
	}
}
