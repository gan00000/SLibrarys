package com.starpy.googlepay.bean;

import java.io.Serializable;

import com.starpy.googlepay.constants.GooglePayContant;

public class EfunBaseOrderBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String creditId;
	private String moneyType = GooglePayContant.MONEY_TYPE;
	private String serverCode;
	private String gameCode;
	private String payFrom = GooglePayContant.PAY_FROM;
	private String remark = "";
	private String efunRole = "";
	private String efunLevel = "";
	private String secretKey;
	
	private String language;
	
	private String cardData = "";
	
	private String roleId = "";
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the creditId
	 */
	public String getCreditId() {
		return creditId;
	}
	/**
	 * @param creditId the creditId to set
	 */
	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	/**
	 * @return the moneyType
	 */
	public String getMoneyType() {
		return moneyType;
	}
	/**
	 * @param moneyType the moneyType to set
	 */
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	/**
	 * @return the serverCode
	 */
	public String getServerCode() {
		return serverCode;
	}
	/**
	 * @param serverCode the serverCode to set
	 */
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	/**
	 * @return the gameCode
	 */
	public String getGameCode() {
		return gameCode;
	}
	/**
	 * @param gameCode the gameCode to set
	 */
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	/**
	 * @return the payFrom
	 */
	public String getPayFrom() {
		return payFrom;
	}
	/**
	 * @param payFrom the payFrom to set
	 */
	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the efunRole
	 */
	public String getEfunRole() {
		return efunRole;
	}
	/**
	 * @param efunRole the efunRole to set
	 */
	public void setEfunRole(String efunRole) {
		this.efunRole = efunRole;
	}
	/**
	 * @return the efunLevel
	 */
	public String getEfunLevel() {
		return efunLevel;
	}
	/**
	 * @param efunLevel the efunLevel to set
	 */
	public void setEfunLevel(String efunLevel) {
		this.efunLevel = efunLevel;
	}
	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}
	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EfunBaseOrderBean [userId=" + userId + ", creditId=" + creditId + ", moneyType=" + moneyType + ", serverCode=" + serverCode
				+ ", gameCode=" + gameCode + ", payFrom=" + payFrom + ", remark=" + remark + ", efunRole=" + efunRole + ", efunLevel=" + efunLevel
				+ ", secretKey=" + secretKey + ", language=" + language + ", cardData=" + cardData + ", roleId=" + roleId + "]";
	}
	
	public String getCardData() {
		return cardData;
	}
	public void setCardData(String cardData) {
		this.cardData = cardData;
	}
	
	
}
