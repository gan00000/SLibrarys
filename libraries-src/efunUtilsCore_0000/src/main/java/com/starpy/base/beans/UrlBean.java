package com.starpy.base.beans;

import java.io.Serializable;

import android.text.TextUtils;

public class UrlBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String efunLoginPreferredUrl;
	private String efunLoginSpareUrl;
	private String efunAdsPreferredUrl;
	private String efunAdsSpareUrl;
	private String efunPayPreferredUrl;
	private String efunPaySpareUrl;
	private String efunFbPreferredUrl;
	private String efunFbSpareUrl;
	private String efunGameSpareUrl;
	private String efunGamePreferredUrl;
	private String efunQuestionPreferredUrl;
	private String efunPlatformPreferredUrl;
//	private String efunLuaSwitchUrl;
	private boolean isQxdlSwitch = false;
	
	public boolean isEmpty() {
		return TextUtils.isEmpty(efunLoginPreferredUrl) || TextUtils.isEmpty(efunAdsPreferredUrl) || TextUtils.isEmpty(efunPayPreferredUrl);
	}
	
	public String getEfunLoginPreferredUrl() {
		return efunLoginPreferredUrl;
	}
	public void setEfunLoginPreferredUrl(String efunLoginPreferredUrl) {
		this.efunLoginPreferredUrl = efunLoginPreferredUrl;
	}
	public String getEfunLoginSpareUrl() {
		return efunLoginSpareUrl;
	}
	public void setEfunLoginSpareUrl(String efunLoginSpareUrl) {
		this.efunLoginSpareUrl = efunLoginSpareUrl;
	}
	public String getEfunAdsPreferredUrl() {
		return efunAdsPreferredUrl;
	}
	public void setEfunAdsPreferredUrl(String efunAdsPreferredUrl) {
		this.efunAdsPreferredUrl = efunAdsPreferredUrl;
	}
	public String getEfunAdsSpareUrl() {
		return efunAdsSpareUrl;
	}
	public void setEfunAdsSpareUrl(String efunAdsSpareUrl) {
		this.efunAdsSpareUrl = efunAdsSpareUrl;
	}
	public String getEfunPayPreferredUrl() {
		return efunPayPreferredUrl;
	}
	public void setEfunPayPreferredUrl(String efunPayPreferredUrl) {
		this.efunPayPreferredUrl = efunPayPreferredUrl;
	}
	public String getEfunPaySpareUrl() {
		return efunPaySpareUrl;
	}
	public void setEfunPaySpareUrl(String efunPaySpareUrl) {
		this.efunPaySpareUrl = efunPaySpareUrl;
	}
	public String getEfunFbPreferredUrl() {
		return efunFbPreferredUrl;
	}
	public void setEfunFbPreferredUrl(String efunFbPreferredUrl) {
		this.efunFbPreferredUrl = efunFbPreferredUrl;
	}
	public String getEfunFbSpareUrl() {
		return efunFbSpareUrl;
	}
	public void setEfunFbSpareUrl(String efunFbSpareUrl) {
		this.efunFbSpareUrl = efunFbSpareUrl;
	}
	public String getEfunGameSpareUrl() {
		return efunGameSpareUrl;
	}
	public void setEfunGameSpareUrl(String efunGameSpareUrl) {
		this.efunGameSpareUrl = efunGameSpareUrl;
	}
	public String getEfunGamePreferredUrl() {
		return efunGamePreferredUrl;
	}
	public void setEfunGamePreferredUrl(String efunGamePreferredUrl) {
		this.efunGamePreferredUrl = efunGamePreferredUrl;
	}
	public String getEfunQuestionPreferredUrl() {
		return efunQuestionPreferredUrl;
	}
	public void setEfunQuestionPreferredUrl(String efunQuestionPreferredUrl) {
		this.efunQuestionPreferredUrl = efunQuestionPreferredUrl;
	}
	public String getEfunPlatformPreferredUrl() {
		return efunPlatformPreferredUrl;
	}
	public void setEfunPlatformPreferredUrl(String efunPlatformPreferredUrl) {
		this.efunPlatformPreferredUrl = efunPlatformPreferredUrl;
	}
	
	public boolean isQxdlSwitch() {
		return isQxdlSwitch;
	}
	public void setQxdlSwitch(boolean isQxdlSwitch) {
		this.isQxdlSwitch = isQxdlSwitch;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UrlBean [efunLoginPreferredUrl=" + efunLoginPreferredUrl + ", efunLoginSpareUrl=" + efunLoginSpareUrl + ", efunAdsPreferredUrl="
				+ efunAdsPreferredUrl + ", efunAdsSpareUrl=" + efunAdsSpareUrl + ", efunPayPreferredUrl=" + efunPayPreferredUrl + ", efunPaySpareUrl="
				+ efunPaySpareUrl + ", efunFbPreferredUrl=" + efunFbPreferredUrl + ", efunFbSpareUrl=" + efunFbSpareUrl + ", efunGameSpareUrl="
				+ efunGameSpareUrl + ", efunGamePreferredUrl=" + efunGamePreferredUrl + ", efunQuestionPreferredUrl=" + efunQuestionPreferredUrl
				+ ", efunPlatformPreferredUrl=" + efunPlatformPreferredUrl  + ", isQxdlSwitch="
				+ isQxdlSwitch + "]";
	}

/*	public String getEfunLuaSwitchUrl() {
		return efunLuaSwitchUrl;
	}

	public void setEfunLuaSwitchUrl(String efunLuaSwitchUrl) {
		this.efunLuaSwitchUrl = efunLuaSwitchUrl;
	}*/
	
	
}
