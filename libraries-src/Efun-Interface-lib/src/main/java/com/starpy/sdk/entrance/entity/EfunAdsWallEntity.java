package com.starpy.sdk.entrance.entity;

public class EfunAdsWallEntity extends EfunBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean callAfterLogin = false;
	private boolean callBeforeLogin = true;//少瑜要求编写
	

	public boolean isCallBeforeLogin() {
		return callBeforeLogin;
	}


	public void setCallBeforeLogin(boolean callBeforeLogin) {
		this.callBeforeLogin = callBeforeLogin;
	}

	/**
	 * @return the callAfterLogin
	 */
	@Deprecated
	public boolean isCallAfterLogin() {
		return callAfterLogin;
	}



	/**
	 * @param callAfterLogin the callAfterLogin to set
	 */
	@Deprecated
	public void setCallAfterLogin(boolean callAfterLogin) {
		this.callAfterLogin = callAfterLogin;
	}



	public EfunAdsWallEntity() {
	}




}
