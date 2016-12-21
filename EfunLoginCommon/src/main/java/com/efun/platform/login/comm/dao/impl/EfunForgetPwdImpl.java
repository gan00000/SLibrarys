package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.tools.EfunJSONUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;
import com.efun.platform.login.comm.utils.LoinStringUtil;

public class EfunForgetPwdImpl extends EfunBaseLoginDao {
	
	private int interfaceType = 0;
	
	public EfunForgetPwdImpl(){}
	
	public EfunForgetPwdImpl(int type){
		this.interfaceType = type;
	}
	
	@Override
	public String efunRequestServer() throws EfunException {
		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		EfunLogUtil.logD(parameters.toString());
		/*if (LoinStringUtil.isAllEmpty(parameters.getUserName(),parameters.getEmail())) {
			return this.emptyReturn();
		}*/
		
		if (interfaceType == 100) {
			Map<String, String> nameValuePairs = EfunParamsBuilder.buildAssistRetrievePassword(parameters);
			if (nameValuePairs == null) {
				return null;
			}
			return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_ASSIST_RETRIEVEPASSWORD, nameValuePairs));
		}else{

			Map<String, String> nameValuePairs = EfunParamsBuilder.buildForgetPwd(parameters);
			if (nameValuePairs == null) {
				return null;
			}
			return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_FINDPWD_EMAIL, nameValuePairs));
		
		}
	}
}
