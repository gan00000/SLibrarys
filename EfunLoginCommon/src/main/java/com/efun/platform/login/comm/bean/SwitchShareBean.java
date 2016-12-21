package com.efun.platform.login.comm.bean;

import java.io.Serializable;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchShareBean implements Serializable {

	private String shareImgUrl;
	private String shareTitle;
	private String code;
	private String shareContent;
	// private Long endTime;
	// private Long startTime;
	private String likeUrl;
	private String rawdata;

	public SwitchShareBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject kakaoJson = new JSONObject(rawdata);
			setCode(kakaoJson.optString("code", ""));
			setShareTitle(URLDecoder.decode(kakaoJson.optString("shareTitle", "")));
			setShareContent(URLDecoder.decode(kakaoJson.optString("shareContent", "")));
			setShareImgUrl(kakaoJson.optString("shareImgUrl", ""));
			setLikeUrl(kakaoJson.optString("likeUrl", ""));

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}

	public String getShareImgUrl() {
		return shareImgUrl;
	}

	public void setShareImgUrl(String shareImgUrl) {
		this.shareImgUrl = shareImgUrl;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getLikeUrl() {
		return likeUrl;
	}

	public void setLikeUrl(String likeUrl) {
		this.likeUrl = likeUrl;
	}

	@Override
	public String toString() {
		return "SwitchShareBean, code : " + code + "   shareImgUrl : " + shareImgUrl + "   shareTitle : " + shareTitle
				+ "   shareContent : " + shareContent + "   likeUrl : " + likeUrl;
	}

}
