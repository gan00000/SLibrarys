package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class SwitchManagementBean implements Serializable {

	private String rawdata;
	private String code;
	private String officialWebsite;
	private String fbWebsite;
	private String bahaWebsite;

	public SwitchManagementBean(String rawdata) {
		this.rawdata = rawdata;
		initdata();
	}

	private void initdata() {
		try {
			JSONObject manaJson = new JSONObject(rawdata);
			setCode(manaJson.optString("code", ""));
			if (!TextUtils.isEmpty(manaJson.optString("officialWebsite", ""))) {
				setOfficialWebsite(new JSONObject(manaJson.optString("officialWebsite", "")).optString("url", ""));
			}
			if (!TextUtils.isEmpty(manaJson.optString("fbWebsite", ""))) {
				setFbWebsite(new JSONObject(manaJson.optString("fbWebsite", "")).optString("url", ""));
			}
			if (!TextUtils.isEmpty(manaJson.optString("bahaWebsite", ""))) {
				setBahaWebsite(new JSONObject(manaJson.optString("bahaWebsite", "")).optString("url", ""));
			}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOfficialWebsite() {
		return officialWebsite;
	}

	public void setOfficialWebsite(String officialWebsite) {
		this.officialWebsite = officialWebsite;
	}

	public String getFbWebsite() {
		return fbWebsite;
	}

	public void setFbWebsite(String fbWebsite) {
		this.fbWebsite = fbWebsite;
	}

	public String getBahaWebsite() {
		return bahaWebsite;
	}

	public void setBahaWebsite(String bahaWebsite) {
		this.bahaWebsite = bahaWebsite;
	}

	@Override
	public String toString() {
		return "SwitchManagementBean  code : " + code + "  officialWebsite : " + officialWebsite + "  fbWebsite : "
				+ fbWebsite + "  bahaWebsite : " + bahaWebsite;
	}
}
