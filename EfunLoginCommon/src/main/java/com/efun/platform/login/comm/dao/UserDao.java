package com.efun.platform.login.comm.dao;

import com.efun.core.exception.EfunException;
import com.efun.platform.login.comm.bean.ListenerParameters;

/**
 * Class Description：
 * @author Joe
 * @date 2013-7-11
 * @version 1.0
 */
@Deprecated
public interface UserDao {
	
	/**
	 * efunLogin Method
	 * Method Description :登入
	 * @param params
	 * @return
	 * @throws EfunException
	 * @return String
	 * @author Joe
	 * @date 2013-7-13
	 */
	public String efunLogin(ListenerParameters params,String url) throws EfunException;
	
	/**
	 * efunRegister Method
	 * Method Description :注册
	 * @param params
	 * @return
	 * @throws EfunException
	 * @return String
	 * @author Joe
	 * @date 2013-7-13
	 */
	public String efunRegister(ListenerParameters params,String url) throws EfunException;

	/**
	 * efunChangePwd Method
	 * Method Description : 修改密码
	 * @param params
	 * @return
	 * @throws EfunException
	 * @return String
	 * @author Joe
	 * @date 2013-7-13
	 */
	public String efunChangePwd(ListenerParameters params,String url) throws EfunException;
	
	/**
	 * efunForgetPwdByEmail Method
	 * Method Description :找回密码
	 * @param params
	 * @return
	 * @throws EfunException
	 * @return String
	 * @author Joe
	 * @date 2013-7-13
	 */
	public String efunForgetPwdByEmail(ListenerParameters params,String url) throws EfunException;	
	
	/**
	 * efunThirdPlatLoginOrReg Method
	 * Method Description :第三方登入或者註冊
	 * @param params
	 * @return
	 * @throws EfunException
	 * @return String
	 * @author Joe
	 * @date 2013-7-11
	 */
	public String efunThirdPlatLoginOrReg(ListenerParameters params,String url) throws EfunException;

	/**
	 * efunBind Method
	 * Method Description :绑定
	 * @param params
	 * @return
	 * @throws EfunException
	 * @return String
	 * @author Joe
	 * @date 2013-7-13
	 */
	public String efunBind(ListenerParameters params,String url) throws EfunException;
}
