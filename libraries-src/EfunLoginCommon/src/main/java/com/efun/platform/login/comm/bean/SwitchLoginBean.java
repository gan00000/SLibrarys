package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class SwitchLoginBean implements Serializable {
	private String rawdata;
	private String code;
	private String baha;
	private String twitter;
	private String vk;
	private String fb;
	private String efun;
	private String mac;
	private String google;
	private String userSwitchEnable;

	public SwitchLoginBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject loginJson = new JSONObject(rawdata);
			setCode(loginJson.optString("code", ""));
			setBaha(loginJson.optString("baha", ""));
			setEfun(loginJson.optString("efun", ""));
			setFb(loginJson.optString("fb", ""));
			setGoogle(loginJson.optString("google", ""));
			setMac(loginJson.optString("mac", ""));
			setTwitter(loginJson.optString("twitter", ""));
			setVk(loginJson.optString("vk", ""));
			setUserSwitchEnable(loginJson.optString("userSwitchEnable", ""));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getLoginways() {
		if ("1000".equals(code)) {
			String loginWays = ",";
					loginWays = loginWays + (TextUtils.isEmpty(getMac()) ? "" : "mac,");
					loginWays = loginWays + (TextUtils.isEmpty(getFb()) ? "" : "fb,");
					loginWays = loginWays + (TextUtils.isEmpty(getGoogle()) ? "" : "google,");
					loginWays = loginWays + (TextUtils.isEmpty(getBaha()) ? "" : "baha,");
//					loginWays = loginWays + (TextUtils.isEmpty(getEfun()) ? "" : "efun,");
//					loginWays = loginWays + (TextUtils.isEmpty(getTwitter()) ? "" : "twitter,");
//					loginWays = loginWays + (TextUtils.isEmpty(getVk()) ? "" : "vk,");
			return loginWays;
		}
		return "";
	}

	public String getUserSwitchEnable() {
		return userSwitchEnable;
	}

	public void setUserSwitchEnable(String userSwitchEnable) {
		this.userSwitchEnable = userSwitchEnable;
	}

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBaha() {
		return baha;
	}

	public void setBaha(String baha) {
		this.baha = baha;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getVk() {
		return vk;
	}

	public void setVk(String vk) {
		this.vk = vk;
	}

	public String getFb() {
		return fb;
	}

	public void setFb(String fb) {
		this.fb = fb;
	}

	public String getEfun() {
		return efun;
	}

	public void setEfun(String efun) {
		this.efun = efun;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getGoogle() {
		return google;
	}

	public void setGoogle(String google) {
		this.google = google;
	}

	@Override
	public String toString() {
		return "SwitchLoginBean  code : " + code + "  baha: " + baha + "  twitter: " + twitter + "  vk: " + vk
				+ "  fb: " + fb + "  efun: " + efun + "   mac: " + mac + "   google: " + google;
	}
}
