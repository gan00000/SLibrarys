package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.tools.EfunJSONUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;
import com.efun.platform.login.comm.utils.LoinStringUtil;

/**
* <p>Title: EfunThirdPlatImpl</p>
* <p>Description: 三方登陆接口</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年9月15日
*/

@Deprecated
public class EfunThirdPlatImpl extends EfunBaseLoginDao {
	
	
	@Override
	public String efunRequestServer() throws EfunException {
		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		EfunLogUtil.logD(parameters.toString());
		if (LoinStringUtil.isEmpty(parameters.getThirdPlateId())) {
			return this.emptyReturn();
		}
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildThirdPlat(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_THIRD_PART, nameValuePairs));
	}

}
