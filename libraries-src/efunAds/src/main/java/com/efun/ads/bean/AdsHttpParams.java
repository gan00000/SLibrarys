package com.efun.ads.bean;

import java.io.Serializable;

public class AdsHttpParams implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String preferredUrl;
	private String spareUrl;
	private String domainSite;
	
	private String signature;
	private String timeStamp = System.currentTimeMillis()/1000 + "";
	
	private String localMacAddress = "";
	private String localImeiAddress = "";
	private String localIpAddress = "";
	private String localAndroidId = "";
	private String osVersion = "";
	private String phoneModel = "";
	
	private String flage = "iSNew_20130130";

	private String gameCode = "";
	private String appKey = "";
//	private String efunThirdPlat = "";
	
	private String advertiser = "";
	private String partner = "";
	private String referrer = "";
	private String appPlatform = "";
	
	private String adstartTime = System.currentTimeMillis() + "";
	private String adendTime = "";
	
	private String region = "";
	
	private String packageName = "";
	
	private String versionCode = "";
	private String versionName = "";
	
	private String advertising_id = "";
	
	private String wifi = "";
	private String userAgent = "";
	private String os_language = "";
	
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getLocalMacAddress() {
		return localMacAddress;
	}
	public void setLocalMacAddress(String localMacAddress) {
		this.localMacAddress = localMacAddress;
	}
	public String getLocalImeiAddress() {
		return localImeiAddress;
	}
	public void setLocalImeiAddress(String localImeiAddress) {
		this.localImeiAddress = localImeiAddress;
	}
	public String getLocalIpAddress() {
		return localIpAddress;
	}
	public void setLocalIpAddress(String localIpAddress) {
		this.localIpAddress = localIpAddress;
	}
	public String getLocalAndroidId() {
		return localAndroidId;
	}
	public void setLocalAndroidId(String localAndroidId) {
		this.localAndroidId = localAndroidId;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getPhoneModel() {
		return phoneModel;
	}
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	public String getFlage() {
		return flage;
	}
	public void setFlage(String flage) {
		this.flage = flage;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getAdvertiser() {
		return advertiser;
	}
	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getReferrer() {
		return referrer;
	}
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	public String getAppPlatform() {
		return appPlatform;
	}
	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}
/*	public String getEfunThirdPlat() {
		return efunThirdPlat;
	}
	public void setEfunThirdPlat(String efunThirdPlat) {
		this.efunThirdPlat = efunThirdPlat;
	}*/
	public String getPreferredUrl() {
		return preferredUrl;
	}
	public void setPreferredUrl(String preferredUrl) {
		this.preferredUrl = preferredUrl;
	}
	public String getSpareUrl() {
		return spareUrl;
	}
	public void setSpareUrl(String spareUrl) {
		this.spareUrl = spareUrl;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAdstartTime() {
		return adstartTime;
	}
	public void setAdstartTime(String adstartTime) {
		this.adstartTime = adstartTime;
	}
	public String getAdendTime() {
		return adendTime;
	}
	public void setAdendTime(String adendTime) {
		this.adendTime = adendTime;
	}
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDomainSite() {
		return domainSite;
	}
	public void setDomainSite(String domainSite) {
		this.domainSite = domainSite;
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
	
	public String getAdvertising_id() {
		return advertising_id;
	}
	public void setAdvertising_id(String advertising_id) {
		this.advertising_id = advertising_id;
	}
	
	
	public String getWifi() {
		return wifi;
	}
	public void setWifi(String wifi) {
		this.wifi = wifi;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	@Override
	public String toString() {
		return "AdsHttpParams [signature=" + signature + ", timeStamp=" + timeStamp + ", localMacAddress=" + localMacAddress + ", localImeiAddress="
				+ localImeiAddress + ", localIpAddress=" + localIpAddress + ", localAndroidId=" + localAndroidId + ", osVersion=" + osVersion
				+ ", phoneModel=" + phoneModel + ", flage=" + flage + ", gameCode=" + gameCode + ", appKey=" + appKey + ", advertiser=" + advertiser
				+ ", partner=" + partner + ", referrer=" + referrer + ", appPlatform=" + appPlatform + ", adstartTime=" + adstartTime
				+ ", adendTime=" + adendTime + ", region=" + region + ", packageName=" + packageName + ", versionCode=" + versionCode
				+ ", versionName=" + versionName + ", advertising_id=" + advertising_id + ", wifi=" + wifi + ", userAgent=" + userAgent + "]";
	}
	public String getOs_language() {
		return os_language;
	}
	public void setOs_language(String os_language) {
		this.os_language = os_language;
	}
	
	
}
