package com.starpy.model.login.bean.request;

import android.content.Context;

import com.core.base.utils.EfunLogUtil;

/**
* <p>Title: LoginBaseRequest</p>
* <p>Description: 接口请求参数实体</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年8月22日
*/
public class MacLoginRegRequest extends LoginBaseRequest{

	public MacLoginRegRequest(Context context) {
		super(context);
	}

	private String appKey;

	private String userName;
	private String password;
	private String newPassword;

	private String gameCode;
	/**
	 * registPlatform 第三方登陆平台的标识符
	 */
	private String thirdPlate;
	/**
	 * thirdPlateId 第三方登陆平台id
	 */
	private String thirdPlateId;
	private String platForm = "android";
	private String partner = "";
	private String advertisersName;
	private String language = "";

	private String referrer;
	private String imei;
	private String mac;
	private String ip;
	private String androidId;
	private String systemVersion;
	private String deviceType;
	private String appPlatform;
	private String packageName = "";
	private String versionCode = "";
	private String versionName = "";

	/**
	 * apps 关联应用的FB ID
	 */
	private String apps = "";
	private String advertisingId = "";

	private String token_for_business = "";//编码（标志明文密文）


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


	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
		EfunLogUtil.logD(this.gameCode);
	}


	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
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


	public String getToken_for_business() {
		return token_for_business;
	}

	public void setToken_for_business(String token_for_business) {
		this.token_for_business = token_for_business;
	}
	
	

}
