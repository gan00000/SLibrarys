package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchKRplatformBean implements Serializable {

	private String code;
	private String url;
	private String audited;
	private String artificial;
	private String rawdata;

	public SwitchKRplatformBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject serJson = new JSONObject(rawdata);

			setCode(serJson.optString("code", ""));
			setUrl(serJson.optString("url", ""));
			setAudited(serJson.optString("audited", ""));
			setArtificial(serJson.optString("artificial", ""));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getArtificial() {
		return artificial;
	}

	public void setArtificial(String artificial) {
		this.artificial = artificial;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAudited() {
		return audited;
	}

	public void setAudited(String audited) {
		this.audited = audited;
	}

	@Override
	public String toString() {
		return "SwitchKRplatformBean,code : " + code + " url : " + url + " audited : " + audited + " artificial : " + artificial ;
	}

}
