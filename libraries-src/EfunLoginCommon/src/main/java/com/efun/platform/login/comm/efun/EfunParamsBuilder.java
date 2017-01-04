/**
 * 
 */
package com.efun.platform.login.comm.efun;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.bean.ListenerParameters;
import com.efun.platform.login.comm.constant.HttpParamsKey;

import android.text.TextUtils;

/**
 * <p>Title: Builder</p>
 * <p>Description: </p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */
@SuppressWarnings("deprecation")
public class EfunParamsBuilder {
	
	public static Map<String, String> buildLogin(ListenerParameters login) {
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();

		postParams.put(HttpParamsKey.mac, login.getMac());
		postParams.put(HttpParamsKey.imei, login.getImei());
		postParams.put(HttpParamsKey.androidid, login.getAndroidId());
		postParams.put(HttpParamsKey.advertiser, login.getAdvertisersName());
//		postParams.put(HttpParamsKey.partner, login.getPartner());
		postParams.put(HttpParamsKey.params, login.getPartner());//合作商
		postParams.put(HttpParamsKey.referrer, login.getReferrer());
		postParams.put(HttpParamsKey.loginName, login.getUserName());
		postParams.put(HttpParamsKey.loginPwd, login.getPassword());
		postParams.put(HttpParamsKey.timestamp, currentTime + "");
		postParams.put(HttpParamsKey.gameCode, login.getGameCode());
		postParams.put(HttpParamsKey.systemVersion, login.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, login.getDeviceType());
		postParams.put(HttpParamsKey.appPlatform, login.getAppPlatform());
		postParams.put(HttpParamsKey.language, login.getLanguage());
		addCommonParams(login, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(login.getAppKey() + 
						currentTime + login.getUserName() + 
						login.getPassword() + 
						login.getGameCode(), false));
		return postParams;
	}
	
	
	public static Map<String, String> buildRegister(ListenerParameters register) {
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.loginName, register.getUserName());
		postParams.put(HttpParamsKey.loginPwd, register.getPassword());
		postParams.put(HttpParamsKey.email, register.getEmail());
		postParams.put(HttpParamsKey.gameCode, register.getGameCode());
		postParams.put(HttpParamsKey.advertiser, register.getAdvertisersName());
		postParams.put(HttpParamsKey.mac, register.getMac());
		postParams.put(HttpParamsKey.imei, register.getImei());
		postParams.put(HttpParamsKey.androidid, register.getAndroidId());
		postParams.put(HttpParamsKey.ip, register.getIp());
		postParams.put(HttpParamsKey.systemVersion, register.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, register.getDeviceType());
		postParams.put(HttpParamsKey.params, register.getPartner() + "," + 
																register.getGameShortName() + "," + 
																register.getPlatForm());
		postParams.put(HttpParamsKey.timestamp, currentTime + "");
		postParams.put(HttpParamsKey.referrer, register.getReferrer());
		postParams.put(HttpParamsKey.phone, register.getPhoneNumber());
		postParams.put(HttpParamsKey.code, register.getCode());
		postParams.put(HttpParamsKey.appPlatform, register.getAppPlatform());
		postParams.put(HttpParamsKey.language, register.getLanguage());
		addCommonParams(register, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(
				register.getAppKey() 			+ 
				currentTime 					+ 
				register.getUserName() 			+ 
				register.getPassword() 			+ 
				register.getEmail() 			+ 
				register.getGameCode() 			+ 
				register.getAdvertisersName() 	+ 
				register.getPartner() + "," + 
				register.getGameCode() + "," + 
				register.getPlatForm(), false));
		return postParams;
	}
	
	public static Map<String, String> buildChagePwd(ListenerParameters parameters){
		
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.loginPwd, parameters.getPassword());
		postParams.put(HttpParamsKey.newPwd, parameters.getNewPassword());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + parameters.getUserName() + 
						parameters.getPassword() + 
						parameters.getNewPassword() + 
						parameters.getGameCode(), false));
		return postParams;
	}
	
	public static Map<String, String> buildForgetPwd(ListenerParameters parameters){
		Long currentTime = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpParamsKey.loginName, parameters.getUserName());
		params.put(HttpParamsKey.email, parameters.getEmail());
		params.put(HttpParamsKey.gameCode, parameters.getGameCode());
		params.put(HttpParamsKey.timestamp, currentTime+"");
		params.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		params.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, params);
		params.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + parameters.getUserName() + 
						parameters.getEmail()+ 
						parameters.getGameCode(), false));
		return params;
	}
	
	/**
	* <p>Title: buildBind</p>
	* <p>Description: 第三方绑定efun账号接口</p>
	* @param parameters
	* @return
	*/
	public static Map<String, String> buildBind(ListenerParameters parameters){
		String _params = parameters.getGameShortName()+","+parameters.getThirdPlate()+"," + parameters.getPlatForm();
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginid, parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.loginPwd, parameters.getPassword());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.email, parameters.getEmail());
		
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.params, _params);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + 
						parameters.getThirdPlateId() + 
						parameters.getUserName() +
						parameters.getPassword()+ 
						parameters.getGameCode() +
						_params, false));
		return postParams;
	}
	
	
	public static Map<String, String> buildBind_third_urgentLogin(ListenerParameters parameters){
		
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		//postParams.put(HttpParamsKey.loginid, parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.loginPwd, parameters.getPassword());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		
		postParams.put(HttpParamsKey.mac, parameters.getMac());
		postParams.put(HttpParamsKey.imei, parameters.getImei());
		postParams.put(HttpParamsKey.androidid, parameters.getAndroidId());
		
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		
		postParams.put(HttpParamsKey.systemVersion, parameters.getSystemVersion());
		postParams.put(HttpParamsKey.platForm, parameters.getPlatForm());
		
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.thirdPlate, parameters.getThirdPlate());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + 
						parameters.getUserName() 		+
						parameters.getPassword()		+ 
						parameters.getGameCode() 		+
						parameters.getPlatForm(), false));
		return postParams;
		
	}
	
	public static Map<String, String> buildBindEmail(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.loginPwd, parameters.getPassword());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.email, parameters.getEmail());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		return postParams;
	}

	/**
	 * 不再使用
	 * @param parameters
	 * @return
	 */
	@Deprecated
	public static Map<String, String> buildThirdPlat(ListenerParameters parameters){
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginid, parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.mac, parameters.getMac());
		postParams.put(HttpParamsKey.imei, parameters.getImei());
		postParams.put(HttpParamsKey.androidid, parameters.getAndroidId());
		postParams.put(HttpParamsKey.ip, parameters.getIp());
		postParams.put(HttpParamsKey.systemVersion, parameters.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, parameters.getDeviceType());
		
		postParams.put(HttpParamsKey.referrer, parameters.getReferrer());
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		postParams.put("unionId", parameters.getToken_for_business());
		postParams.put(HttpParamsKey.params, parameters.getGameCode()+","+parameters.getPartner()+","
				+parameters.getGameShortName()+","+parameters.getThirdPlate()+","+parameters.getPlatForm());
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + 
						parameters.getThirdPlateId() + 
						parameters.getAdvertisersName() + 
						parameters.getGameCode()+","+
						parameters.getPartner()+","
						+parameters.getGameShortName()+","+
						parameters.getThirdPlate()+","+
						parameters.getPlatForm(), false));

		if (!TextUtils.isEmpty(parameters.getCoveredThirdId())){
			postParams.put("coveredThirdId", parameters.getCoveredThirdId());
		}
		if (!TextUtils.isEmpty(parameters.getCoveredThirdPlate())){
			postParams.put("coveredThirdPlate", parameters.getCoveredThirdPlate());
		}
		return postParams;
	}

	/**
	* <p>Title: build_third_login</p>
	* <p>Description: 新第三方登陆接口，fb政策改变后使用的</p>
	* @param parameters
	* @return
	*/
	public static Map<String, String> buildThirdPlat2(ListenerParameters parameters){

		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put("thirdId", parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.mac, parameters.getMac());
		postParams.put(HttpParamsKey.imei, parameters.getImei());
		postParams.put(HttpParamsKey.androidid, parameters.getAndroidId());
		postParams.put(HttpParamsKey.ip, parameters.getIp());
		postParams.put(HttpParamsKey.systemVersion, parameters.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, parameters.getDeviceType());
		
		postParams.put(HttpParamsKey.referrer, parameters.getReferrer());
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		
		postParams.put(HttpParamsKey.platForm, parameters.getPlatForm());//android or ios
		postParams.put(HttpParamsKey.thirdPlate, parameters.getThirdPlate());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put("apps", parameters.getApps());
		postParams.put("unionId", parameters.getToken_for_business());
		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + 
						parameters.getThirdPlateId() 	+ 
						parameters.getThirdPlate()		+ 
						parameters.getGameCode()		+
						parameters.getPlatForm(),
						false));
		if (!TextUtils.isEmpty(parameters.getCoveredThirdId())){
			postParams.put("coveredThirdId", parameters.getCoveredThirdId());
		}
		if (!TextUtils.isEmpty(parameters.getCoveredThirdPlate())){
			postParams.put("coveredThirdPlate", parameters.getCoveredThirdPlate());
		}
		return postParams;		
	}
	
	/**
	* <p>Title: third_bindThird</p>
	* <p>Description:  第三方绑定第三方接口，thirdId不带前缀!</p>
	* @param parameters
	* @return
	*/
	public static Map<String, String> build_third_bindThird(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put("thirdId", parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.thirdPlate, parameters.getThirdPlate());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		
		postParams.put(HttpParamsKey.platForm, parameters.getPlatForm());
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		postParams.put("userId", parameters.getEfunUserId());
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime 						+ 
						parameters.getEfunUserId() 			+
						parameters.getThirdPlateId() 		+ 
						parameters.getThirdPlate() 			+
						parameters.getPartner() 			+
						parameters.getAppPlatform(),
						false));
		return postParams;
	}
	
	/**
	* <p>Title: build_third_unbindThird</p>
	* <p>Description: 第三方平台之间解绑定，thirdId不带前缀!</p>
	* @param parameters
	* @return
	*/
	public static Map<String, String> build_third_unbindThird(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put("thirdId", parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		postParams.put(HttpParamsKey.thirdPlate, parameters.getThirdPlate());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		
		postParams.put(HttpParamsKey.platForm, parameters.getPlatForm());
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put("userId", parameters.getEfunUserId());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime 						+ 
						parameters.getEfunUserId() 			+
						parameters.getThirdPlateId() 		+ 
						parameters.getThirdPlate() 			+
						parameters.getPartner() 			+
						parameters.getAppPlatform(),
						false));
		return postParams;
	}
	
	/**
	 * <p>Description: 添加判断是否绑定接口（此方法还没使用）</p>
	 * @param parameters
	 * @return
	 * @date 2014年12月10日
	 */
/*	public static Map<String, String> build_hasBindEfun(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		postParams.put(HttpParamsKey.loginid, parameters.getThirdPlate());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		
		postParams.put(HttpParamsKey.platForm, parameters.getPlatForm());
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime 						+ 
						parameters.getThirdPlateId()		,
						false));
		return postParams;
	}*/
	
	/**
	 * <p>Description: 查询第三方帐号绑定状态(hasBindEfun)(適用於免註冊)检查免注册登陆次数，用来提示或者强制玩家绑定efun账号</p>
	 * @param parameters
	 * @return
	 * @date 2015年12月14日
	 */
	public static Map<String, String> buildHasBindBind(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginid, parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());	
		postParams.put(HttpParamsKey.partner, parameters.getPartner());	
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.platForm, parameters.getPlatForm());
		postParams.put(HttpParamsKey.mac, parameters.getMac());
		postParams.put(HttpParamsKey.imei, parameters.getImei());
		postParams.put(HttpParamsKey.systemVersion, parameters.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, parameters.getDeviceType());
		postParams.put(HttpParamsKey.thirdPlate, parameters.getThirdPlate());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
						currentTime + 
						parameters.getThirdPlateId(),
						false));
		return postParams;
	}
	
	/**
	 * <p>Description: 获取验证码</p>
	 * @param parameters
	 * @return
	 * @date 2015年6月8日
	 */
	public static Map<String, String> buildVerificationCode(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.loginPwd, parameters.getPassword());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.email, parameters.getEmail());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.phone, parameters.getPhoneNumber());
		addCommonParams(parameters, postParams);
		return postParams;
	}
	/**
	 * <p>Description:  通过验证码绑定邮箱或者手机号</p>
	 * @param parameters
	 * @return
	 * @date 2015年6月8日
	 */
	public static Map<String, String> buildAssistBind(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.code, parameters.getVerificationCode());//验证码
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.email, parameters.getEmail());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.phone, parameters.getPhoneNumber());
		addCommonParams(parameters, postParams);
		return postParams;
	}
	
	/**
	 * 通过邮箱或者手机号找回密码
	 * @param parameters
	 * @return
	 */
	public static Map<String, String> buildAssistRetrievePassword(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.loginName, parameters.getUserName());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.email, parameters.getEmail());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.phone, parameters.getPhoneNumber());
		postParams.put(HttpParamsKey.code, parameters.getVerificationCode());
		postParams.put(HttpParamsKey.auth, parameters.getAuth());
		addCommonParams(parameters, postParams);
		return postParams;
	}
	
	
	/** 获取账号绑定信息，手机号码信箱等
	 * @param parameters
	 * @return
	 */
	public static Map<String, String> buildUserBindMessager(ListenerParameters parameters) {
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.timestamp, currentTime + "");
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put("encode",parameters.getEncode());//encode=0时取明文
		if (TextUtils.isEmpty(parameters.getEfunUserId())) {
			postParams.put(HttpParamsKey.loginName, parameters.getUserName());
			postParams.put(HttpParamsKey.loginPwd, parameters.getPassword());
			postParams.put(HttpParamsKey.signature, 
					EfunStringUtil.toMd5(parameters.getAppKey() + 
							currentTime + parameters.getUserName() + 
							parameters.getPassword() + 
							parameters.getGameCode(), false));
		}else {
			postParams.put("userId", parameters.getEfunUserId());
			postParams.put(HttpParamsKey.signature, 
					EfunStringUtil.toMd5(parameters.getAppKey() + 
							currentTime + parameters.getEfunUserId() + 
							parameters.getGameCode(), false));
		}
		
		return postParams;
	}
	
	
	/** 统一开关接口
	 * @param parameters
	 * @return
	 */
	public static Map<String, String> buildSwitch(ListenerParameters parameters) {
		Map<String, String> postParams = new HashMap<String, String>();
		
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.typeNames, parameters.getTypeNames());
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put("packageName", parameters.getPackageName());
//		postParams.put("gameVersion", parameters.getVersionName());
		postParams.put("gameVersion", parameters.getVersionCode());
//		postParams.put("region", parameters.getRegion());
		postParams.put("multiLanguage", parameters.getMultiLanguage());
		
		return postParams;
	}
	/**
	 * 手机获取验证码
	 * @param parameters
	 * @return
	 */
	public static Map<String,String> buildPhoneVerifictionCode(ListenerParameters parameters){
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());	
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
	    postParams.put(HttpParamsKey.phone, parameters.getPhoneNumber());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + currentTime + parameters.getGameCode()+parameters.getPhoneNumber(),
						false));
		return postParams;

	}
	
	
	
	public static Map<String, String> buildThirdAliasParams(ListenerParameters parameters){
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(parameters.getAppKey() + 
				parameters.getEfunUserId() + 
				parameters.getGameCode()+
				currentTime +
				parameters.getAppPlatform(), false));
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.userId, parameters.getEfunUserId());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		return postParams;
	}
	
	private static void addCommonParams(ListenerParameters parameters, Map<String, String> map) {
		map.put("region", parameters.getRegion());
		map.put("packageName", parameters.getPackageName());
		map.put("versionCode", parameters.getVersionCode());
		map.put("gameVersion", parameters.getVersionName());
		map.put("advertising_id", parameters.getAdvertisingId());
		map.put("eid", parameters.getEid());
		map.put("userArea", parameters.getUserArea());//用户地区
		
		if (!parameters.isEmpty()) {
			for (Entry<String, String> entry : parameters.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		
	}


	public static Map<String, String> buildThirdIsBindEfunAccount(ListenerParameters parameters) {
		
		Map<String, String> postParams = new HashMap<String, String>();
		Long currentTime = System.currentTimeMillis();
		postParams.put(HttpParamsKey.timestamp, currentTime + "");
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		postParams.put(HttpParamsKey.userId, parameters.getEfunUserId());
		addCommonParams(parameters, postParams);
		postParams.put(HttpParamsKey.signature, EfunStringUtil.toMd5(
				parameters.getAppKey() + currentTime + parameters.getEfunUserId() + parameters.getGameCode(), false));
		return postParams;
	}
	

}
