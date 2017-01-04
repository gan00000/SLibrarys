package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class SwitchApplicationBean implements Serializable {

	private String code;
	private String url;
	private String isExternalOpen;
	private String rawdata;
	private String key;
	private String application;

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	// public SwitchApplicationBean(String rawdata,String key) {
	// this.rawdata = rawdata;
	// this.key = key;
	// init();
	// }
	public SwitchApplicationBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		if (!TextUtils.isEmpty(rawdata)) {
			try {
				JSONObject appliJson = new JSONObject(rawdata);
				if ("1000".equals(appliJson.optString("code", "")) ){
					setApplication(rawdata);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}
}
