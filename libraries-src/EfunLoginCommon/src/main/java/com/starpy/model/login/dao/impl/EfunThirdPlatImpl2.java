package com.starpy.model.login.dao.impl;

import java.util.Map;

import com.starpy.base.exception.EfunException;
import com.starpy.base.utils.EfunJSONUtil;
import com.starpy.base.utils.EfunLogUtil;
import com.starpy.base.utils.SStringUtil;
import com.starpy.model.login.constant.DomainSuffix;
import com.starpy.model.login.efun.EfunParamsBuilder;

/**
* <p>Title: EfunThirdLoginImpl</p>
* <p>Description: 新三方登陆接口，FB政策调整后使用的</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年9月15日
*/
public class EfunThirdPlatImpl2 extends EfunBaseLoginDao {
	
	@Override
	public String efunRequestServer() throws EfunException {
		super.efunRequestServer();
		
		if (parameters == null) {
			return null;
		}
		EfunLogUtil.logD(parameters.toString());
		if (SStringUtil.isEmpty(parameters.getThirdPlateId())) {
			return this.emptyReturn();
		}
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildThirdPlat2(parameters);
		if (nameValuePairs == null) {
			return null;
		}
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_THIRD_PART_2, nameValuePairs));
	}

}
