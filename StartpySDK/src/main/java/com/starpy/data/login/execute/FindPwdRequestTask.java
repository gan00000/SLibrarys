package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;

public class FindPwdRequestTask extends BaseRequestTask {

	private com.starpy.data.login.request.FindPwdRequest findPwdRequest;

	public FindPwdRequestTask(Context mContext, String userName, String email) {
		super(mContext);

		userName = userName.toLowerCase();

		findPwdRequest = new com.starpy.data.login.request.FindPwdRequest(mContext);
		baseRequest = findPwdRequest;
		findPwdRequest.setName(userName);
		findPwdRequest.setEmail(email);

		findPwdRequest.setRequestMethod("findPwd");


	}


	@Override
	public BaseReqeustBean createRequestBean() {
		super.createRequestBean();

		findPwdRequest.setSignature(SStringUtil.toMd5(findPwdRequest.getAppKey() + findPwdRequest.getTimestamp() +
				findPwdRequest.getName() + findPwdRequest.getGameCode()));

		return  findPwdRequest;
	}
}
