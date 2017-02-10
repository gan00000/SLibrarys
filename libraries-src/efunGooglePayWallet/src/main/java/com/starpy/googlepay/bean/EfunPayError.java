package com.starpy.googlepay.bean;

import java.io.Serializable;

import android.content.Context;

import com.core.base.res.SConfig;

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
		efunGoogleServerError = SConfig.getGoogleServiceError(context);
		efunGoogleBuyFailError = SConfig.getGoogleBuyFailError(context);
		efunGoogleStoreError = SConfig.getGoogleStoreError(context);
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
