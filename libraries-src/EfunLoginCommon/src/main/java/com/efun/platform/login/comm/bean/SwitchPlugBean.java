package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchPlugBean implements Serializable {

	private String code;
	private String plugPackageName;
	private String plugUrl;
	private String plugVersionCode;
	private String plugActionName;
	private String rawdata;

	public SwitchPlugBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject plugJson = new JSONObject(rawdata);
			setCode(plugJson.optString("code", ""));
			setPlugActionName(plugJson.optString("plugActionName", ""));
			setPlugPackageName(plugJson.optString("plugPackageName", ""));
			setPlugUrl(plugJson.optString("plugUrl", ""));
			setPlugVersionCode(plugJson.optString("plugVersionCode", ""));

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

	public String getPlugPackageName() {
		return plugPackageName;
	}

	public void setPlugPackageName(String plugPackageName) {
		this.plugPackageName = plugPackageName;
	}

	public String getPlugUrl() {
		return plugUrl;
	}

	public void setPlugUrl(String plugUrl) {
		this.plugUrl = plugUrl;
	}

	public String getPlugVersionCode() {
		return plugVersionCode;
	}

	public void setPlugVersionCode(String plugVersionCode) {
		this.plugVersionCode = plugVersionCode;
	}

	public String getPlugActionName() {
		return plugActionName;
	}

	public void setPlugActionName(String plugActionName) {
		this.plugActionName = plugActionName;
	}

	@Override
	public String toString() {
		return "SwitchPlugBean,code : " + code + " plugPackageName : " + plugPackageName + " plugUrl : " + plugUrl
				+ " plugVersionCode : " + plugVersionCode + " plugActionName : " + plugActionName;
	}
}
