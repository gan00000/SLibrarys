package com.starpy.sdk.entrance.entity;

import com.starpy.sdk.entrance.constant.EfunPayType;

public final class EfunPayEntity extends EfunBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EfunPayType payType;
	
	private String productId;
	private String creditId;
	private String remark;
	
	
	private String payMoney;
	private String payStone;
	private String payCardData;
	/**
	 * @return the payType
	 */
	public EfunPayType getPayType() {
		return payType;
	}
	/**
	 * @param payType the payType to setScrollDuration
	 */
	public void setPayType(EfunPayType payType) {
		this.payType = payType;
	}
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to setScrollDuration
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the creditId
	 */
	public String getCreditId() {
		return creditId;
	}
	/**
	 * @param creditId the creditId to setScrollDuration
	 */
	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to setScrollDuration
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the payMoney
	 */
	public String getPayMoney() {
		return payMoney;
	}
	/**
	 * @param payMoney the payMoney to setScrollDuration
	 */
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	/**
	 * @return the payStone
	 */
	public String getPayStone() {
		return payStone;
	}
	/**
	 * @param payStone the payStone to setScrollDuration
	 */
	public void setPayStone(String payStone) {
		this.payStone = payStone;
	}
	/**
	 * @return the payCardData
	 */
	public String getPayCardData() {
		return payCardData;
	}
	/**
	 * @param payCardData the payCardData to setScrollDuration
	 */
	public void setPayCardData(String payCardData) {
		this.payCardData = payCardData;
	}
	
	
}
