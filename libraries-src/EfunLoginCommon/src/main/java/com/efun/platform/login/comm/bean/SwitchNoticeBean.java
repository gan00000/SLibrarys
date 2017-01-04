package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchNoticeBean implements Serializable {

	private String rawdata;
	private String code;
	private String notice;
	private String activityName;
	private String activityCode;
	private String gameUrl;
	private String activityNoticeType;

	private String serviceUrl;
	private String consultUrl;
	private String snsUrl;
	private String payUrl;

	public SwitchNoticeBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject notiJson = new JSONObject(rawdata);
			setCode(notiJson.optString("code", ""));
			setActivityCode(notiJson.optString("activityCode", ""));
			setActivityName(notiJson.optString("activityName", ""));
			setActivityNoticeType(notiJson.optString("", ""));
			setGameUrl(notiJson.optString("gameUrl", ""));
			setNotice(notiJson.optString("notice", ""));
			setServiceUrl(notiJson.optString("serviceUrl", ""));
			setConsultUrl(notiJson.optString("consultUrl", ""));
			setSnsUrl(notiJson.optString("snsUrl", ""));
			setPayUrl(notiJson.optString("payUrl", ""));

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getConsultUrl() {
		return consultUrl;
	}

	public void setConsultUrl(String consultUrl) {
		this.consultUrl = consultUrl;
	}

	public String getSnsUrl() {
		return snsUrl;
	}

	public void setSnsUrl(String snsUrl) {
		this.snsUrl = snsUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getGameUrl() {
		return gameUrl;
	}

	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}

	public String getActivityNoticeType() {
		return activityNoticeType;
	}

	public void setActivityNoticeType(String activityNoticeType) {
		this.activityNoticeType = activityNoticeType;
	}

	@Override
	public String toString() {
		return "SwitchNoticeBean,code : " + code + "   notice : " + notice + "  activityName : " + activityName
				+ "  activityCode : " + activityCode + "  gameUrl : " + gameUrl + "  activityNoticeType : "
				+ activityNoticeType;
	}
}
