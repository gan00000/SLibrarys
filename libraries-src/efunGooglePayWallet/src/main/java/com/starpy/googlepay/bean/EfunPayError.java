package com.starpy.googlepay.bean;

import java.io.Serializable;

import android.content.Context;

public class EfunPayError implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
//	private Context context;
	
	private String googleServerError = "";
	private String googleBuyFailError = "";
	private String googleStoreError = "";
	
	public EfunPayError(Context context) {
//		this.context = context;
//		googleServerError = ResConfig.getGoogleServiceError(context);
//		googleBuyFailError = ResConfig.getGoogleBuyFailError(context);
//		googleStoreError = ResConfig.getGoogleStoreError(context);
		googleServerError = "server error,please try again";
		googleBuyFailError = "An error occurred,please try again";
		googleStoreError = googleBuyFailError;
	}
	
	
	public String getGoogleServerError() {
		return googleServerError;
	}
	public void setGoogleServerError(String googleServerError) {
		this.googleServerError = googleServerError;
	}
	public String getGoogleBuyFailError() {
		return googleBuyFailError;
	}
	public void setGoogleBuyFailError(String googleBuyFailError) {
		this.googleBuyFailError = googleBuyFailError;
	}
	public String getGoogleStoreError() {
		return googleStoreError;
	}
	public void setGoogleStoreError(String googleStoreError) {
		this.googleStoreError = googleStoreError;
	}


	@Override
	public String toString() {
		return "EfunPayError [googleServerError=" + googleServerError + ", googleBuyFailError=" + googleBuyFailError
				+ ", googleStoreError=" + googleStoreError + "]";
	}

	
}
