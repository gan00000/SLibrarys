package com.efun.platform.login.comm.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchTransferBean implements Serializable {
	private String rawdata;
	private String bindnotice;
	private String downloadlink;
	private String code;
	private String downloadnotice;
	
	public SwitchTransferBean(String rawdata) {
		this.rawdata = rawdata;
		init();
	}

	private void init() {
		try {
			JSONObject transJson = new JSONObject(rawdata);
			setCode(transJson.optString("code", ""));
			setBindnotice(transJson.optString("bindnotice", ""));
			setDownloadlink(transJson.optString("downloadlink", ""));
			setDownloadnotice(transJson.optString("downloadnotice", ""));
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

	public String getBindnotice() {
		return bindnotice;
	}

	public void setBindnotice(String bindnotice) {
		this.bindnotice = bindnotice;
	}

	public String getDownloadlink() {
		return downloadlink;
	}

	public void setDownloadlink(String downloadlink) {
		this.downloadlink = downloadlink;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDownloadnotice() {
		return downloadnotice;
	}

	public void setDownloadnotice(String downloadnotice) {
		this.downloadnotice = downloadnotice;
	}

	@Override
	public String toString() {
		return "SwitchTransferBean  code : " + code + "   bindnotice : " + bindnotice + "   downloadlink : " + downloadlink
				+ "   downloadnotice : " + downloadnotice;
	}
}
