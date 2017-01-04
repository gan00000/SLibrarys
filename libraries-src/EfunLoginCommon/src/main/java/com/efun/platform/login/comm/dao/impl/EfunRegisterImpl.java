/**
 * 
 */
package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.tools.EfunJSONUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;

/**
 * <p>Title: EfunRegisterImpl</p>
 * <p>Description: </p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */
public class EfunRegisterImpl extends EfunBaseLoginDao {

	@Override
	public String efunRequestServer() throws EfunException {
		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		
		EfunLogUtil.logD(parameters.toString());
		if (this.checkUserAndPwd(parameters) != null) {
			return this.checkUserAndPwd(parameters);
		}
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildRegister(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_LOGIN_REGISTER, nameValuePairs));
	}
}
