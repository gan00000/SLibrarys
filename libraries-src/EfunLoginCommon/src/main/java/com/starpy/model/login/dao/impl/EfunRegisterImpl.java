///**
// *
// */
//package com.starpy.model.login.dao.impl;
//
//import java.util.Map;
//
//import com.core.base.exception.EfunException;
//import com.core.base.utils.EfunJSONUtil;
//import com.core.base.utils.EfunLogUtil;
//import com.starpy.model.login.constant.DomainSuffix;
//import com.starpy.model.login.efun.EfunParamsBuilder;
//
///**
// * <p>Title: EfunRegisterImpl</p>
// * <p>Description: </p>
// * <p>Company: EFun</p>
// * @author GanYuanrong
// * @date 2013年12月7日
// */
//public class EfunRegisterImpl extends EfunBaseLoginDao {
//
//	@Override
//	public String efunRequestServer() throws EfunException {
//		super.efunRequestServer();
//		if (parameters == null) {
//			return null;
//		}
//
//		EfunLogUtil.logD(parameters.toString());
//		if (this.checkUserAndPwd(parameters) != null) {
//			return this.checkUserAndPwd(parameters);
//		}
//		Map<String, String> nameValuePairs = EfunParamsBuilder.buildRegister(parameters);
//		if (nameValuePairs == null) {
//			return null;
//		}
//
//		return EfunJSONUtil.efunTransformToJSONStr(doRequest(DomainSuffix.URL_LOGIN_REGISTER, nameValuePairs));
//	}
//}
