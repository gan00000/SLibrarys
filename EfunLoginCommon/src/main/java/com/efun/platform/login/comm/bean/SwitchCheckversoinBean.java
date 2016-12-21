package com.efun.platform.login.comm.bean;

import java.io.Serializable;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class SwitchCheckversoinBean implements Serializable {

	private String rawdata;
	private String code;
	private String audited;
	private String isforce;
	private String auditversion;
	private String downloadUrl;
	private String version;
	private String versionName;
	private String updateDesc;

	public SwitchCheckversoinBean(String rawdata) {
		setRawdata(rawdata);
		init();
	}

	private void init() {
		if (!TextUtils.isEmpty(rawdata)) {
			try {
				JSONObject cbJson = new JSONObject(rawdata);
				setCode(cbJson.optString("code", ""));
				setAudited(cbJson.optString("audited", ""));
				setAuditversion(cbJson.optString("auditversion", ""));
				setDownloadUrl(cbJson.optString("downloadUrl", ""));
				setIsforce(cbJson.optString("isforce", ""));
				setUpdateDesc(URLDecoder.decode(cbJson.optString("updateDesc", "")));
				setVersion(cbJson.optString("version", ""));
				setVersionName(cbJson.optString("versionName", ""));

			} catch (JSONException e) {
				e.printStackTrace();
			}catch (Exception e) {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAudited() {
		return audited;
	}

	public void setAudited(String audited) {
		this.audited = audited;
	}

	public String getIsforce() {
		return isforce;
	}

	public void setIsforce(String isforce) {
		this.isforce = isforce;
	}

	public String getAuditversion() {
		return auditversion;
	}

	public void setAuditversion(String auditversion) {
		this.auditversion = auditversion;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUpdateDesc() {
		return updateDesc;
	}

	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}

	@Override
	public String toString() {

		return "SwitchCheckversoinBean,  code : " + code + "  audited : " + audited + "  isforce: " + isforce
				+ "  auditversion : " + auditversion + "  downloadUrl : " + downloadUrl + "  version : " + version
				+ "  versionName : " + versionName + "  updateDesc: " + updateDesc;
	}
}
