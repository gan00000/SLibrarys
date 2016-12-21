package com.efun.platform.login.comm.bean;

import java.io.Serializable;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchInviteBean implements Serializable {

	private String rawdata;
	private String fbShareUrl;
	private String fbIconUrl;
	private String jumpUrl;
	private String fbInviteContent;
	private String explainUrl;
	private String fbShareContent;
	private String fbLikeUrl;
	private String activityNoticeType;
	private String code;


	public SwitchInviteBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject invJson = new JSONObject(rawdata);
			setCode(invJson.optString("code", ""));
			setFbIconUrl(invJson.optString("fbIconUrl", ""));
			setFbInviteContent(URLDecoder.decode(invJson.optString("fbInviteContent", "")));
			setFbLikeUrl(invJson.optString("fbLikeUrl", ""));
			setFbShareContent(URLDecoder.decode(invJson.optString("fbShareContent", "")));
			setFbShareUrl(invJson.optString("fbShareUrl", ""));
			setJumpUrl(invJson.optString("jumpUrl", ""));
			setExplainUrl(invJson.optString("explainUrl", ""));
			setActivityNoticeType(invJson.optString("activityNoticeType", ""));

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isOpen() {
		if ("1000".equals(code)) {
			return true;
		}
		return false;
	}

	
	
	public String getActivityNoticeType() {
		return activityNoticeType;
	}

	public void setActivityNoticeType(String activityNoticeType) {
		this.activityNoticeType = activityNoticeType;
	}

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}

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

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	public String getFbInviteContent() {
		return fbInviteContent;
	}

	public void setFbInviteContent(String fbInviteContent) {
		this.fbInviteContent = fbInviteContent;
	}

	public String getExplainUrl() {
		return explainUrl;
	}

	public void setExplainUrl(String explainUrl) {
		this.explainUrl = explainUrl;
	}

	public String getFbShareContent() {
		return fbShareContent;
	}

	public void setFbShareContent(String fbShareContent) {
		this.fbShareContent = fbShareContent;
	}

	public String getFbLikeUrl() {
		return fbLikeUrl;
	}

	public void setFbLikeUrl(String fbLikeUrl) {
		this.fbLikeUrl = fbLikeUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		
		return "SwitchInviteBean,  code : " + code + "  fbShareUrl : " + fbShareUrl + "  fbIconUrl: " + fbIconUrl
				+ "  jumpUrl : " + jumpUrl + "  fbInviteContent : " + fbInviteContent + "  explainUrl : "+ explainUrl+ 
				"  fbShareContent : " + fbShareContent+ "  fbLikeUrl: " + fbLikeUrl;
		
	}
}
