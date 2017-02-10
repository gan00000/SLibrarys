/**
 * 
 */
package com.starpy.model.login.constant;

/**
 * <p>Title: DomainSuffix</p>
 * <p>Description: </p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */
public class DomainSuffix {

	public static final String ACTION_NAME = "standard_";
	public static final String URL_LOGIN_CHECK = ACTION_NAME + "login.shtml";
	public static final String URL_LOGIN_REGISTER = "login/register";
	public static final String URL_CHANGE_PWD = ACTION_NAME + "changePwd.shtml";
	public static final String URL_FINDPWD_EMAIL = ACTION_NAME + "findPwdByEmail.shtml";
	public static final String URL_THIRD_PART = ACTION_NAME + "thirdPlateLogin.shtml";
	public static final String URL_BIND = ACTION_NAME + "accountBind.shtml";
	public static final String URL_BIND_EMAIL = ACTION_NAME + "bindEmail.shtml";
	
	public static final String URL_ASSIST_LOADCODEWITHACCOUNT = "assist_loadCodeWithAccount.shtml";
	public static final String URL_ASSIST_RETRIEVEPASSWORD = "assist_retrievePassword.shtml";
	public static final String URL_ASSIST_BIND = "assist_bind.shtml";

	public static final String URL_THIRD_PART_2 = "third_thirdPlateLogin.shtml";
	public static final String URL_BIND_2 = "thirdLogin_accountBind.shtml";
	public static final String URL_BIND_URGENT_LOGIN = "third_urgentLogin.shtml";
	public static final String URL_THIRD_ACCOUNT_BIND_THIRD_ACCOUNT = "third_bindThird.shtml";
	public static final String URL_THIRD_ACCOUNT_UNBIND_THIRD_ACCOUNT = "third_unbindThird.shtml";
	
	public static final String URL_HAS_BIND_EFUN = "third_hasBindEfun.shtml";
	
	public static final String URL_ASSIST_BIND_EFUN = "assist_validateUserMessage.shtml";
	
//	public static final String URL_SWITCH_EFUN = "queryUnion";//统一开关
	public static final String URL_SWITCH_EFUN = "unifiedControl";//统一开关
	public static final String URL_GET_PHONE_VERIFICATION_CODE="assist_loadCode.shtml";//手机获取验证码
	
	public static final String URL_THIRD_ALIAS = "third_alias.shtml";//日本 生成引继账号
	
	public static final String URL_THIRD_IS_BIND_EFUN_ACCOUNT = "third_checkBind.shtml";//检查是否绑定了efun账号
	
}
