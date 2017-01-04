package com.efun.core.beans;

import java.io.Serializable;

public class DynamicVersionBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String versionCode;
	private String signValue;
	private String qxdlSwitch;
	
	public String getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	public String getQxdlSwitch() {
		return qxdlSwitch;
	}
	public void setQxdlSwitch(String qxdlSwitch) {
		this.qxdlSwitch = qxdlSwitch;
	}
	
	public boolean isHasNull(){
		return versionCode == null || signValue == null;
	}
}
