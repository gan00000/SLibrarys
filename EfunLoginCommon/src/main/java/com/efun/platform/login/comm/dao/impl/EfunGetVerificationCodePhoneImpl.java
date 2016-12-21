package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.tools.EfunJSONUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;

public class EfunGetVerificationCodePhoneImpl extends EfunBaseLoginDao {
	@Override
	public String efunRequestServer() throws EfunException {
		
		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		
		EfunLogUtil.logD(parameters.toString());
		
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildPhoneVerifictionCode(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_GET_PHONE_VERIFICATION_CODE, nameValuePairs));
	}
}
