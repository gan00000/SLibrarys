package com.efun.googlepay.bean;

import java.io.Serializable;

import android.content.Context;

import com.efun.core.res.EfunResConfiguration;

public class EfunPayError implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
//	private Context context;
	
	private String efunGoogleServerError = "";
	private String efunGoogleBuyFailError = "";
	private String efunGoogleStoreError = "";
	
	public EfunPayError(Context context) {
//		this.context = context;
		efunGoogleServerError = EfunResConfiguration.getGoogleServiceError(context);
		efunGoogleBuyFailError = EfunResConfiguration.getGoogleBuyFailError(context);
		efunGoogleStoreError = EfunResConfiguration.getGoogleStoreError(context);
	}
	
	
	public String getEfunGoogleServerError() {
		return efunGoogleServerError;
	}
	public void setEfunGoogleServerError(String efunGoogleServerError) {
		this.efunGoogleServerError = efunGoogleServerError;
	}
	public String getEfunGoogleBuyFailError() {
		return efunGoogleBuyFailError;
	}
	public void setEfunGoogleBuyFailError(String efunGoogleBuyFailError) {
		this.efunGoogleBuyFailError = efunGoogleBuyFailError;
	}
	public String getEfunGoogleStoreError() {
		return efunGoogleStoreError;
	}
	public void setEfunGoogleStoreError(String efunGoogleStoreError) {
		this.efunGoogleStoreError = efunGoogleStoreError;
	}


	@Override
	public String toString() {
		return "EfunPayError [efunGoogleServerError=" + efunGoogleServerError + ", efunGoogleBuyFailError=" + efunGoogleBuyFailError
				+ ", efunGoogleStoreError=" + efunGoogleStoreError + "]";
	}

	
}
