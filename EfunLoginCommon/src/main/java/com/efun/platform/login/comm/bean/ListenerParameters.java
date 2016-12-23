package com.efun.platform.login.comm.bean;

import java.io.Serializable;
import java.util.HashMap;

import com.efun.core.tools.EfunLogUtil;

/**
* <p>Title: ListenerParameters</p>
* <p>Description: 接口请求参数实体</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年8月22日
*/
public class ListenerParameters extends HashMap<String, String> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String appKey;

	private String userName;
	private String password;
	private String newPassword;
	private String email;
	
	private String gameCode;
	private String gameShortName;
	/**
	 * thirdPlate 第三方登陆平台的标识符
	 */
	private String thirdPlate;
	/**
	 * thirdPlateId 第三方登陆平台id
	 */
	private String thirdPlateId;
	private String platForm = "android";
	private String partner = "";
	private String advertisersName;
	private String referrer;
	
	private String imei;
	private String mac;
	private String ip;
	private String androidId;
	private String systemVersion;
	private String deviceType;
	private String appPlatform;
	private String code;//用於註冊時驗證
	private String language = "";
	private String region = "";
	private String packageName = "";
	private String versionCode = "";
	private String versionName = "";
	private String auth="";
	/**
	 * apps 关联应用的FB ID
	 */
	private String apps = "";
	private String advertisingId = "";
	private String efunUserId = "";
	
	
	private String phoneNumber = "";
	private String verificationCode = "";
	
	private String eid = "";
	private String userArea = "";
	private String encode = "";//编码（标志明文密文）
	private String token_for_business = "";//编码（标志明文密文）
	private String coveredThirdId = "";//被覆盖的第三方id（引继账号登入的时候有效，传免注册登入时的thirdId）
	private String coveredThirdPlate = "";//被覆盖的第三方登入标识（引继账号登入的时候有效，传免注册登入时的登入标识

	public String getCoveredThirdId() {
		return coveredThirdId;
	}

	public void setCoveredThirdId(String coveredThirdId) {
		this.coveredThirdId = coveredThirdId;
	}

	public String getCoveredThirdPlate() {
		return coveredThirdPlate;
	}

	public void setCoveredThirdPlate(String coveredThirdPlate) {
		this.coveredThirdPlate = coveredThirdPlate;
	}

	/**
	 * 统一开关标志：获取哪一些开关
	 */
	private String typeNames = "";
	
	/**
	 * 是否多语言游戏
	 */
	private String multiLanguage = "";
	
	/**
	 * 日本地区 继承码接口使用     efun userid
	 */
//	private String userId;
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}

	public String getMultiLanguage() {
		return multiLanguage;
	}

	public void setMultiLanguage(String multiLanguage) {
		this.multiLanguage = multiLanguage;
	}

	public String getTypeNames() {
		return typeNames;
	}

	public void setTypeNames(String typeNames) {
		this.typeNames = typeNames;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * @return the eid
	 */
	public String getEid() {
		return eid;
	}

	/**
	 * @param eid the eid to set
	 */
	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
		EfunLogUtil.logD(this.gameCode);
	}

	
	
	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getGameShortName() {
		return gameShortName;
	}

	public void setGameShortName(String gameShortName) {
		this.gameShortName = gameShortName;
	}

	public String getThirdPlateId() {
		return thirdPlateId;
	}

	public void setThirdPlateId(String thirdPlateId) {
		this.thirdPlateId = thirdPlateId;
	}

	public String getThirdPlate() {
		return thirdPlate;
	}

	public void setThirdPlate(String thirdPlate) {
		this.thirdPlate = thirdPlate;
	}

	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}

	public String getAdvertisersName() {
		return advertisersName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAdvertisersName(String advertisersName) {
		this.advertisersName = advertisersName;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAndroidId() {
		return androidId;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	/**
	 * @return the appPlatform
	 */
	public String getAppPlatform() {
		return appPlatform;
	}

	/**
	 * @param appPlatform the appPlatform to set
	 */
	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getApps() {
		return apps;
	}

	public void setApps(String apps) {
		this.apps = apps;
	}

	public String getAdvertisingId() {
		return advertisingId;
	}

	public void setAdvertisingId(String advertisingId) {
		this.advertisingId = advertisingId;
	}


	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}


	public String getEfunUserId() {
		return efunUserId;
	}

	public void setEfunUserId(String efunUserId) {
		this.efunUserId = efunUserId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getUserArea() {
		return userArea;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	public String getToken_for_business() {
		return token_for_business;
	}

	public void setToken_for_business(String token_for_business) {
		this.token_for_business = token_for_business;
	}
	
	

/*	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListenerParameters [appKey=");
		builder.append(appKey);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", newPassword=");
		builder.append(newPassword);
		builder.append(", email=");
		builder.append(email);
		builder.append(", gameCode=");
		builder.append(gameCode);
		builder.append(", gameShortName=");
		builder.append(gameShortName);
		builder.append(", thirdPlate=");
		builder.append(thirdPlate);
		builder.append(", thirdPlateId=");
		builder.append(thirdPlateId);
		builder.append(", platForm=");
		builder.append(platForm);
		builder.append(", partner=");
		builder.append(partner);
		builder.append(", advertisersName=");
		builder.append(advertisersName);
		builder.append(", referrer=");
		builder.append(referrer);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", mac=");
		builder.append(mac);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", androidId=");
		builder.append(androidId);
		builder.append(", systemVersion=");
		builder.append(systemVersion);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", appPlatform=");
		builder.append(appPlatform);
		builder.append(", language=");
		builder.append(language);
		builder.append(", region=");
		builder.append(region);
		builder.append(", packageName=");
		builder.append(packageName);
		builder.append(", versionCode=");
		builder.append(versionCode);
		builder.append(", versionName=");
		builder.append(versionName);
		builder.append(", apps=");
		builder.append(apps);
		builder.append(", advertisingId=");
		builder.append(advertisingId);
		builder.append(", efunUserId=");
		builder.append(efunUserId);
		builder.append("]");
		return builder.toString();
	}
*/
	
}
