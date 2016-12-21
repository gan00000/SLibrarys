package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.tools.EfunLogUtil;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;

public class EfunThirdAliasImpl extends EfunBaseLoginDao {

	@Override
	public String efunRequestServer() throws EfunException {
		// TODO Auto-generated method stub
		if (parameters == null) {
			return null;
		}
		EfunLogUtil.logD(parameters.toString());
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildThirdAliasParams(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		
		return doRequest(DomainSuffix.URL_THIRD_ALIAS, nameValuePairs);
	}

	
	
}
