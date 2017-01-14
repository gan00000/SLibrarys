package com.efun.sdk.entrance.entity;

import java.io.Serializable;

import com.efun.platform.login.comm.constant.EfunLoginType;

public class EfunUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EfunLoginType loginType;

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
	public EfunLoginType getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType
	 *            the loginType to set
	 */
	public void setLoginType(EfunLoginType loginType) {
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
	 *            the userId to set
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
	 *            the timestamp to set
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
	 *            the emailMsn to set
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
	 *            the sign to set
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
	 *            the region to set
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
	 *            the thirdPlateId to set
	 */
	public void setThirdPlateId(String thirdPlateId) {
		this.thirdPlateId = thirdPlateId;
	}

}
