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
* <p>Title: EfunAssistBindImpl</p>
* <p>Description: 通过验证码绑定邮箱或者手机号</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年6月8日
*/
public class EfunAssistBindImpl extends EfunBaseLoginDao {
	
	
	public EfunAssistBindImpl() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public String efunRequestServer() throws EfunException {

		super.efunRequestServer();
		if (parameters == null) {
			return null;
		}
		
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildAssistBind(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_ASSIST_BIND, nameValuePairs));
	
	}
}
