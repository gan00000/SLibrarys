/**
 * 
 */
package com.starpy.model.login.efun;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.starpy.base.utils.SStringUtil;
import com.starpy.model.login.bean.ListenerParameters;
import com.starpy.model.login.constant.HttpParamsKey;

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
		postParams.put(HttpParamsKey.systemVersion, login.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, login.getDeviceType());
//		postParams.put(HttpParamsKey.partner, login.getPartner());
//		postParams.put(HttpParamsKey.params, login.getPartner());//合作商
		postParams.put(HttpParamsKey.referrer, login.getReferrer());

		postParams.put(HttpParamsKey.loginName, login.getUserName());
		postParams.put(HttpParamsKey.loginPwd, login.getPassword());
		postParams.put(HttpParamsKey.timestamp, currentTime + "");
		postParams.put(HttpParamsKey.gameCode, login.getGameCode());
//		postParams.put(HttpParamsKey.appPlatform, login.getAppPlatform());
		postParams.put(HttpParamsKey.language, login.getLanguage());
		addCommonParams(login, postParams);

		postParams.put(HttpParamsKey.signature, SStringUtil.toMd5(login.getAppKey() +
						currentTime + login.getUserName() +
						login.getPassword() + 
						login.getGameCode(), true));
		return postParams;
	}
	
	
	public static Map<String, String> buildRegister(ListenerParameters register) {
		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.loginName, register.getUserName());
		postParams.put(HttpParamsKey.loginPwd, register.getPassword());
//		postParams.put(HttpParamsKey.email, register.getEmail());
		postParams.put(HttpParamsKey.gameCode, register.getGameCode());
		postParams.put(HttpParamsKey.advertiser, register.getAdvertisersName());
		postParams.put(HttpParamsKey.mac, register.getMac());
		postParams.put(HttpParamsKey.imei, register.getImei());
		postParams.put(HttpParamsKey.androidid, register.getAndroidId());
//		postParams.put(HttpParamsKey.ip, register.getIp());
		postParams.put(HttpParamsKey.systemVersion, register.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, register.getDeviceType());
//		postParams.put(HttpParamsKey.params, register.getPartner() + "," +
//																register.getGameShortName() + "," +
//																register.getPlatForm());
		postParams.put(HttpParamsKey.timestamp, currentTime + "");
		postParams.put(HttpParamsKey.referrer, register.getReferrer());
//		postParams.put(HttpParamsKey.phone, register.getPhoneNumber());
//		postParams.put(HttpParamsKey.code, register.getCode());
//		postParams.put(HttpParamsKey.appPlatform, register.getAppPlatform());
		postParams.put(HttpParamsKey.language, register.getLanguage());
		addCommonParams(register, postParams);
		postParams.put(HttpParamsKey.signature, 
				SStringUtil.toMd5(
				register.getAppKey() 			+ 
				currentTime 					+ 
				register.getUserName() 			+ 
				register.getPassword() 			+ 
//				register.getEmail() 			+
				register.getGameCode()
				 ,true));
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
				SStringUtil.toMd5(parameters.getAppKey() +
						currentTime + parameters.getUserName() + 
						parameters.getPassword() + 
						parameters.getNewPassword() + 
						parameters.getGameCode(), false));
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
		postParams.put("thirdPlatId", parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.mac, parameters.getMac());
		postParams.put(HttpParamsKey.imei, parameters.getImei());
		postParams.put(HttpParamsKey.androidid, parameters.getAndroidId());
		postParams.put(HttpParamsKey.ip, parameters.getIp());
		postParams.put(HttpParamsKey.systemVersion, parameters.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, parameters.getDeviceType());
		
		postParams.put(HttpParamsKey.referrer, parameters.getReferrer());
//		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);
		
		postParams.put(HttpParamsKey.operatingSystem, parameters.getPlatForm());//android or ios
		postParams.put(HttpParamsKey.registPlatform, parameters.getThirdPlate());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
		postParams.put("apps", parameters.getApps());
		postParams.put("tokenBusiness", parameters.getToken_for_business());
//		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		postParams.put(HttpParamsKey.signature, 
				SStringUtil.toMd5(parameters.getAppKey() +
						currentTime + 
						parameters.getThirdPlateId() 	+ 
						parameters.getThirdPlate()		+ 
						parameters.getGameCode()		+
						parameters.getPlatForm(),
						true));
//		if (!TextUtils.isEmpty(parameters.getCoveredThirdId())){
//			postParams.put("coveredThirdId", parameters.getCoveredThirdId());
//		}
//		if (!TextUtils.isEmpty(parameters.getCoveredThirdPlate())){
//			postParams.put("coveredThirdPlate", parameters.getCoveredThirdPlate());
//		}
		return postParams;		
	}


	/**
	 * <p>Title: build_third_login</p>
	 * <p>Description: 新第三方登陆接口，fb政策改变后使用的</p>
	 * @param parameters
	 * @return
	 */
	public static Map<String, String> buildMacLogin(ListenerParameters parameters){

		Long currentTime = System.currentTimeMillis();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put(HttpParamsKey.timestamp, currentTime+"");
		postParams.put("uniqueId", parameters.getThirdPlateId());
		postParams.put(HttpParamsKey.advertiser, parameters.getAdvertisersName());
		postParams.put(HttpParamsKey.mac, parameters.getMac());
		postParams.put(HttpParamsKey.imei, parameters.getImei());
		postParams.put(HttpParamsKey.androidid, parameters.getAndroidId());
		postParams.put(HttpParamsKey.ip, parameters.getIp());
		postParams.put(HttpParamsKey.systemVersion, parameters.getSystemVersion());
		postParams.put(HttpParamsKey.deviceType, parameters.getDeviceType());

		postParams.put(HttpParamsKey.referrer, parameters.getReferrer());
//		postParams.put(HttpParamsKey.appPlatform, parameters.getAppPlatform());
		postParams.put(HttpParamsKey.language, parameters.getLanguage());
		addCommonParams(parameters, postParams);

		postParams.put(HttpParamsKey.operatingSystem, parameters.getPlatForm());//android or ios
//		postParams.put(HttpParamsKey.registPlatform, parameters.getThirdPlate());
		postParams.put(HttpParamsKey.gameCode, parameters.getGameCode());
//		postParams.put(HttpParamsKey.partner, parameters.getPartner());
		postParams.put(HttpParamsKey.signature,
				SStringUtil.toMd5(parameters.getAppKey() +
								currentTime +
								parameters.getThirdPlateId() 	+
								parameters.getGameCode()		+
								parameters.getPlatForm(),
						true));
		return postParams;
	}


	private static void addCommonParams(ListenerParameters parameters, Map<String, String> map) {
//		map.put("region", parameters.getRegion());
//		map.put("userArea", parameters.getUserArea());//用户地区

		map.put("packageName", parameters.getPackageName());
		map.put("versionCode", parameters.getVersionCode());
		map.put("versionName", parameters.getVersionName());
		map.put("advertising_id", parameters.getAdvertisingId());

		if (!parameters.isEmpty()) {
			for (Entry<String, String> entry : parameters.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		
	}

}
