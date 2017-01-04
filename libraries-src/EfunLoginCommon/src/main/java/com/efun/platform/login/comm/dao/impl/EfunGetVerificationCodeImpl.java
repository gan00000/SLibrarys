/**
 * 
 */
package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.tools.EfunJSONUtil;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.efun.EfunParamsBuilder;


/**
* <p>Title: EfunGetVerificationCodeImpl</p>
* <p>Description: 获取验证码</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年6月8日
*/
public class EfunGetVerificationCodeImpl extends EfunBaseLoginDao {
	
	
	@Override
	public String efunRequestServer() throws EfunException {
		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		if (this.checkUserAndPwd(parameters) != null) {
			return this.checkUserAndPwd(parameters);
		}
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildVerificationCode(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_ASSIST_LOADCODEWITHACCOUNT, nameValuePairs));
	
	}
	
}
