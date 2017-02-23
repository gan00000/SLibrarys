package com.starpy.pay.gp.bean;

import com.starpy.pay.gp.constants.GooglePayContant;

public class WebOrderBean extends EfunBaseOrderBean{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String time;
	
	private String vh;
	private String payType = GooglePayContant.THIRDPAYTYPE;
	private String simOperator;
	private String appPlatFrom;
	
	private String phoneNumber = "";
	/**
	 * DCLversionCode 动态商品列表页面版本
	 */
	private String DCLversionCode = "MA3.0";
	
	/**
	 * 级别类型，此参数设定是否使用级别类型，比如有些游戏内置储值需要达到级别才可以看到第三方储值金流；
	 *	0：游戏内置储值跳转使用级别限制，根据等级控制显示储值金流；
	 *	1：游戏内置储值跳转不使用级别限制，仅看到第三方储值
	 */
	private String levelType = "0";
	
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the vh
	 */
	public String getVh() {
		return vh;
	}
	/**
	 * @param vh the vh to set
	 */
	public void setVh(String vh) {
		this.vh = vh;
	}
	/**
	 * @return the payType
	 */
	public String getPayType() {
		return payType;
	}
	/**
	 * @param payType the payType to set
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	/**
	 * @return the simOperator
	 */
	public String getSimOperator() {
		return simOperator;
	}
	/**
	 * @param simOperator the simOperator to set
	 */
	public void setSimOperator(String simOperator) {
		this.simOperator = simOperator;
	}
	public String getAppPlatFrom() {
		return appPlatFrom;
	}
	public void setAppPlatFrom(String appPlatFrom) {
		this.appPlatFrom = appPlatFrom;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getDCLversionCode() {
		return DCLversionCode;
	}
	public void setDCLversionCode(String dCLversionCode) {
		DCLversionCode = dCLversionCode;
	}
	
	public String getLevelType() {
		return levelType;
	}
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}
	
	@Override
	public String toString() {
		
		return "WebOrderBean [time=" + time + ", vh=" + vh + ", payType=" + payType + ", simOperator=" + simOperator + ", appPlatFrom=" + appPlatFrom
				+ ", phoneNumber=" + phoneNumber + ", DCLversionCode=" + DCLversionCode + ", levelType=" + levelType
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
