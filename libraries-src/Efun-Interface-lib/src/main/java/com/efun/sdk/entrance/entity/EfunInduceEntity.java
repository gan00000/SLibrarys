package com.efun.sdk.entrance.entity;

public class EfunInduceEntity extends EfunBaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 *  诱导储值触发位置
	 */
	private String induce_scene;
	private String creditId;
	private String remark;

	public String getInduceScene() {
		return induce_scene;
	}

	public void setInduceScene(String induce_scene) {
		this.induce_scene = induce_scene;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
