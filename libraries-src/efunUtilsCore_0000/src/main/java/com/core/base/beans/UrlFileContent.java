package com.core.base.beans;

import java.io.Serializable;

import android.text.TextUtils;

public class UrlFileContent implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	String versionCode = "";
	String versionContentMd5 = "";
	String urlContent = "";
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionContentMd5() {
		return versionContentMd5;
	}
	public void setVersionContentMd5(String versionContentMd5) {
		this.versionContentMd5 = versionContentMd5;
	}
	public String getUrlContent() {
		return urlContent;
	}
	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}
	
	/**
	* <p>Title: hasFileEmpty</p>
	* <p>Description: 判断是否有属性为空值</p>
	* @return
	*/
	public boolean hasFiledEmpty(){
		return TextUtils.isEmpty(urlContent) || TextUtils.isEmpty(versionCode) || TextUtils.isEmpty(versionContentMd5);
	}
	
}
