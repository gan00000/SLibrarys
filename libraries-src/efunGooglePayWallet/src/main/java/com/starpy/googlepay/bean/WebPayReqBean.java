package com.starpy.googlepay.bean;

import android.content.Context;

import com.starpy.googlepay.constants.GooglePayContant;

public class WebPayReqBean extends BasePayReqBean {

	private String time = System.currentTimeMillis() + "";
	
	private String payType = GooglePayContant.THIRDPAYTYPE;

	private String simOperator;

	private String appPlatFrom;

	private String secretKey;


	/**
	 * 级别类型，此参数设定是否使用级别类型，比如有些游戏内置储值需要达到级别才可以看到第三方储值金流；
	 *	0：游戏内置储值跳转使用级别限制，根据等级控制显示储值金流；
	 *	1：游戏内置储值跳转不使用级别限制，仅看到第三方储值
	 */
	private String levelType = "0";

	public WebPayReqBean(Context context) {
		super(context);
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
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


	public String getLevelType() {
		return levelType;
	}
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}