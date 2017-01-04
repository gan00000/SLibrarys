/**
 * 
 */
package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;

/**
 * <p>Title: EfunBindImpl</p>
 * <p>Description: 检查第三方是否绑定了efun账号</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */

public class EfunCheckThirdIsBindEfunAccountImpl extends EfunBaseLoginDao {
	
	
	@Override
	public String efunRequestServer() throws EfunException {
		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildThirdIsBindEfunAccount(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		return doRequest(DomainSuffix.URL_THIRD_IS_BIND_EFUN_ACCOUNT, nameValuePairs);
	}
}
