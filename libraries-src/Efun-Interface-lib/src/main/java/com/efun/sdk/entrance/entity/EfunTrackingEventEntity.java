package com.efun.sdk.entrance.entity;

import android.os.Bundle;

public class EfunTrackingEventEntity extends EfunBaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parentEvent = "";
	private String childEvent = "";
	
	private Bundle bundle;

	public EfunTrackingEventEntity() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>Description: 获取父事件</p>
	 * @return
	 * @date 2015年11月12日
	 */
	public String getEvent() {
		return parentEvent;
	}

	/**
	 * <p>Description: 设置父事件</p>
	 * @param event
	 * @date 2015年11月12日
	 */
	public void setEvent(String event) {
		this.parentEvent = event;
	}
	
	/**
	 * <p>Description: 获取父事件与getEvent相同</p>
	 * @return
	 * @date 2015年11月12日
	 */
	public String getParentEvent() {
		return parentEvent;
	}

	
	/**
	 * <p>Description: 设置父事件与setEvent相同</p>
	 * @param event
	 * @date 2015年11月12日
	 */
	public void setParentEvent(String event) {
		this.parentEvent = event;
	}
	

	/**
	 * <p>Description: 获取子事件</p>
	 * @return
	 * @date 2015年11月12日
	 */
	public String getChildEvent() {
		return childEvent;
	}

	/**
	 * <p>Description: 设置子事件</p>
	 * @param childEvent
	 * @date 2015年11月12日
	 */
	public void setChildEvent(String childEvent) {
		this.childEvent = childEvent;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}



	
}
