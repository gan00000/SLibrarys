package com.efun.core.beans;

import java.io.Serializable;

/**
 * HTTP-GET 参数bean
 * @author 李小健
 *
 */
public class EfunGetParamBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String paramKey;
	private String paramValue;
	
	public EfunGetParamBean(String paramKey, String paramValue) {
		super();
		this.paramKey = paramKey;
		this.paramValue = paramValue;
	}
	/**
	 * 获取键值
	 * @return
	 */
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	/**
	 * 获取内容值
	 * @return
	 */
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	/**
	 * 参数片段
	 * @param paramKey 键值
	 * @param paramValue 内容值
	 * @return
	 */
	public String getParamCombine(){
		return this.paramKey+"="+this.paramValue;
	}
	
}
