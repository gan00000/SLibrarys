package com.efun.sdk.entrance.entity;

public final class EfunCustomerServiceEntity extends EfunBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String vipLevel;
	private String remark;
	
	/**
	 * @return the vipLevel
	 */
	public String getVipLevel() {
		return vipLevel;
	}
	/**
	 * @param vipLevel the vipLevel to set
	 */
	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
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
	
	
}
