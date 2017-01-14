package com.efun.sdk.entrance.entity;

public final class EfunLoginEntity extends EfunEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isShowNotice = true;
	private boolean isAutoLogin = false;
	/**
	 * @return the isShowNotice
	 */
	public boolean getShowNotice() {
		return isShowNotice;
	}
	/**
	 * @param isShowNotice the isShowNotice to set
	 */
	public void setShowNotice(boolean isShowNotice) {
		this.isShowNotice = isShowNotice;
	}
	/**
	 * @return the isAutoLogin
	 */
	public boolean getAutoLogin() {
		return isAutoLogin;
	}
	/**
	 * @param isAutoLogin the isAutoLogin to set
	 */
	public void setAutoLogin(boolean isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}
	
}
