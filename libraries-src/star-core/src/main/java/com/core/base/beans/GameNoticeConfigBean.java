/*
package com.core.base.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GameNoticeConfigBean implements Serializable{
	*/
/**
	 * 
	 *//*

	private static final long serialVersionUID = 1L;
	
	private String rawRespone = "";
	
	//新的接口使用code判断
	private String code = "-1";
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private String userSwitchEnable =  "";
	
	
	*/
/**
	 * 平台标志，如果有多个使用 “|”分割，单包含配置中的平台标志的时候，就不显示系统公告
	 *//*

	private String appPlatform;
	*/
/**
	 * 版本标志，如果有多个使用 “|”分割，单包含配置中的版本标志的时候，就不显示系统公告
	 *//*

	private String version;
	*/
/**
	 * 包标志，如果有多个使用 “|”分割，单包含配置中的包标志的时候，就不显示系统公告
	 *//*

	private String packageName;
	*/
/**
	 * 系统公告开始时间（必返回参数）
	 *//*

	private long startTime;
	*/
/**
	 * 系统公告结束时间（必返回参数）
	 *//*

	private long endTime;
	*/
/**
	 * 通过header获取到的当前时间
	 *//*

	private long currentTime; 
	*/
/**
	 * 白名单,使用登陆sdk时候的使用名作为白名单，多个使用“|”分割
	 *//*

	private String whiteListNames;
	*/
/**
	 * 个人中心url
	 *//*

	private String snsUrl;
	*/
/**
	 * 客服url 
	 *//*

	private String serviceUrl;
	*/
/**
	 * 游戏url 
	 *//*

	private String gameUrl;
	*/
/**
	 * 储值url 
	 *//*

	private String payUrl;
	*/
/**
	 * 咨询url
	 *//*

	private String consultUrl;
	
	
	*/
/**
	 * 完整包系统公告地址(如果是完整包，则忽略那五个返回的url)
	 *//*

	private String noticeUrl;
	
	
	public String getNoticeUrl() {
		return noticeUrl;
	}
	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}
	*/
/**
	 * 平台标志，如果有多个使用 “|”分割，单包含配置中的平台标志的时候，就不显示系统公告
	 *//*

	public String getAppPlatform() {
		return appPlatform;
	}
	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}
	*/
/**
	 * 版本标志，如果有多个使用 “|”分割，单包含配置中的版本标志的时候，就不显示系统公告
	 *//*

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	*/
/**
	 * 包标志，如果有多个使用 “|”分割，单包含配置中的包标志的时候，就不显示系统公告
	 *//*

	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	*/
/**
	 * 系统公告开始时间（必返回参数）
	 *//*

	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	*/
/**
	 * 系统公告结束时间（必返回参数）
	 *//*

	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	*/
/**
	 * 通过header获取到的当前时间
	 *//*

	public long getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	*/
/**
	 * 白名单,使用登陆sdk时候的使用名作为白名单，多个使用“|”分割
	 *//*

	public String getWhiteListNames() {
		return whiteListNames;
	}
	public void setWhiteListNames(String whiteListNames) {
		this.whiteListNames = whiteListNames;
	}
	*/
/**
	 * 个人中心url
	 *//*

	public String getSnsUrl() {
		return snsUrl;
	}
	public void setSnsUrl(String snsUrl) {
		this.snsUrl = snsUrl;
	}
	*/
/**
	 * 客服url 
	 *//*

	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	*/
/**
	 * 游戏url 
	 *//*

	public String getGameUrl() {
		return gameUrl;
	}
	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}
	*/
/**
	 * 储值url 
	 *//*

	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	*/
/**
	 * 咨询url 
	 *//*

	public String getConsultUrl() {
		return consultUrl;
	}
	public void setConsultUrl(String consultUrl) {
		this.consultUrl = consultUrl;
	}
	*/
/**
	 * 判断是否打开公告
	 * @param appPlatform
	 * @param version
	 * @param packageName
	 * @return
	 *//*

	public boolean isOpen(String appPlatform, String version, String packageName, String efunUserName) {
		if (this.code.equals("1000")) {//新接口
			return true;
		}
		//兼容旧接口
		if(efunUserName!=null&&this.whiteListNames!=null&&!"".equals(whiteListNames.trim())&&this.whiteListNames.contains(efunUserName)){
			return true;
		}
		if(isContains(this.appPlatform, appPlatform)){
			return false;
		}
		if(isContains(this.version, version)){
			return false;
		}
		if(isContains(this.packageName, packageName)){
			return false;
		}
		if(currentTime>0l&&startTime>0l&&endTime>0l&&currentTime>=startTime&&currentTime<endTime){
			return true;
		}
		return false;
	}
	private boolean isContains(String longStr, String targetStr){
		if(longStr!=null&&!"".equals(longStr.trim())&&targetStr!=null&&!"".equals(targetStr.trim())){
			String[] longStrArray = longStr.split("\\|");
			if(longStrArray!=null&&longStrArray.length>0){
				List<String> longStrList = Arrays.asList(longStrArray);
				return longStrList!=null&&longStrList.contains(targetStr);					
			}
		}
		return false;
	}

	public String getUserSwitchEnable() {
		return userSwitchEnable;
	}

	public void setUserSwitchEnable(String userSwitchEnable) {
		this.userSwitchEnable = userSwitchEnable;
	}

	public String getRawRespone() {
		return rawRespone;
	}

	public void setRawRespone(String rawRespone) {
		this.rawRespone = rawRespone;
	}
	
}
*/
