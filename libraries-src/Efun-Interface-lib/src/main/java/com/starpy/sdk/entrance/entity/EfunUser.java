package com.starpy.sdk.entrance.entity;


import com.starpy.base.bean.SLoginType;

import java.io.Serializable;

public class EfunUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SLoginType loginType;

	/**
	 * efun uid
	 */
	private String userId;
	/**
	 * 时间戳
	 */
	private String timestamp;
	/**
	 * 邮箱
	 */
	private String emailMsn;
	/**
	 * 签名
	 */
	private String sign;

	/**
	 * area 游戏区域
	 */
	private String region;

	/**
	 * 第三方登入的三方id
	 */
	private String thirdPlateId;

	/**
	 * @return the loginType
	 */
	public SLoginType getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType
	 *            the loginType to setScrollDuration
	 */
	public void setLoginType(SLoginType loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to setScrollDuration
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to setScrollDuration
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the emailMsn
	 */
	public String getEmailMsn() {
		return emailMsn;
	}

	/**
	 * @param emailMsn
	 *            the emailMsn to setScrollDuration
	 */
	public void setEmailMsn(String emailMsn) {
		this.emailMsn = emailMsn;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to setScrollDuration
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to setScrollDuration
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the thirdPlateId
	 */
	public String getThirdPlateId() {
		return thirdPlateId;
	}

	/**
	 * @param thirdPlateId
	 *            the thirdPlateId to setScrollDuration
	 */
	public void setThirdPlateId(String thirdPlateId) {
		this.thirdPlateId = thirdPlateId;
	}

}
