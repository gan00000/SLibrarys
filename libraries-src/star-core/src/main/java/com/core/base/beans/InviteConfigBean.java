/*
package com.core.base.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class InviteConfigBean implements Serializable{
	*/
/**
	 * 
	 *//*

	private static final long serialVersionUID = 1L;
	//新的接口使用code判断
	private String code = "-1";
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private String rawRespone = "";
	
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
	 * 活动开始时间（必返回参数）
	 *//*

	private long startTime;
	*/
/**
	 * 活动结束时间（必返回参数）
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
	 * 跳转链接
	 *//*

	private String jumpUrl;
	*/
/**
	 * 点赞链接（如果没有结果返回，则是空的）
	 *//*

	private String fbLikeUrl;
	*/
/**
	 * 分享链接（如果没有结果返回，则是空的）
	 *//*

	private String fbShareUrl;
	
	*/
/**
	 * fb分享内容
	 *//*

	private String fbShareContent;
	
	*/
/**
	 * fb邀请内容
	 *//*

	private String fbInviteContent;
	*/
/**
	 *  iCON
	*//*

	private String fbIconUrl;
	
	*/
/**
	 *  功能说明地址
	*//*

	private String explainUrl;
	
	
	private String activityName;
	
	
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
	 * 活动开始时间（必返回参数）
	 *//*

	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	*/
/**
	 * 活动结束时间（必返回参数）
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
	 * 跳转链接
	 *//*

	public String getJumpUrl() {
		return jumpUrl;
	}
	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	*/
/**
	 * 点赞链接（如果没有结果返回，则是空的）
	 *//*

	public String getFbLikeUrl() {
		return fbLikeUrl;
	}
	public void setFbLikeUrl(String fbLikeUrl) {
		this.fbLikeUrl = fbLikeUrl;
	}
	*/
/**
	 * 分享链接（如果没有结果返回，则是空的）
	 *//*

	public String getFbShareUrl() {
		return fbShareUrl;
	}
	public void setFbShareUrl(String fbShareUrl) {
		this.fbShareUrl = fbShareUrl;
	}
	
	public String getFbIconUrl() {
		return fbIconUrl;
	}
	public void setFbIconUrl(String fbIconUrl) {
		this.fbIconUrl = fbIconUrl;
	}
	public String getExplainUrl() {
		return explainUrl;
	}
	*/
/**
	 * <p>Description: 邀请说明连接</p>
	 * @param explainUrl
	 * @date 2016年3月7日
	 *//*

	public void setExplainUrl(String explainUrl) {
		this.explainUrl = explainUrl;
	}

	*/
/**
	 * 判断是否打开邀请
	 * @param appPlatform
	 * @param version
	 * @param packageName
	 * @return
	 *//*

	public boolean isOpen(String appPlatform, String version, String packageName, String efunUserName) {
		if (this.code.equals("1000")) {
			return true;
		}
		
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
	
	public String getFbShareContent() {
		return fbShareContent;
	}
	public void setFbShareContent(String fbShareContent) {
		this.fbShareContent = fbShareContent;
	}
	public String getFbInviteContent() {
		return fbInviteContent;
	}
	public void setFbInviteContent(String fbInviteContent) {
		this.fbInviteContent = fbInviteContent;
	}

	public String getRawRespone() {
		return rawRespone;
	}

	public void setRawRespone(String rawRespone) {
		this.rawRespone = rawRespone;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
}
*/
