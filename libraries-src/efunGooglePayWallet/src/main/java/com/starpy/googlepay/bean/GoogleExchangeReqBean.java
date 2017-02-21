package com.starpy.googlepay.bean;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.googlepay.constants.GooglePayContant;

public class GoogleExchangeReqBean extends BaseReqeustBean {


	private String purchaseData;
	private String dataSignature;

	private String priceCurrencyCode;
	private String priceAmountMicros;
	private String price;

	private String gameLanguage;
	private String accessToken;

	private String timestamp = System.currentTimeMillis() + "";
	private String signature = "";

	public String getPurchaseData() {
		return purchaseData;
	}

	public void setPurchaseData(String purchaseData) {
		this.purchaseData = purchaseData;
	}

	public String getDataSignature() {
		return dataSignature;
	}

	public void setDataSignature(String dataSignature) {
		this.dataSignature = dataSignature;
	}

	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}

	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}

	public String getPriceAmountMicros() {
		return priceAmountMicros;
	}

	public void setPriceAmountMicros(String priceAmountMicros) {
		this.priceAmountMicros = priceAmountMicros;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}


	public GoogleExchangeReqBean(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		accessToken = StarPyUtil.getSdkAccessToken(context);
		gameLanguage = ResConfig.getGameLanguage(context);
		signature = SStringUtil.toMd5(ResConfig.getAppKey(context) + ResConfig.getGameCode(context) + timestamp);
	}



	public String getGameLanguage() {
		return gameLanguage;
	}

	public void setGameLanguage(String gameLanguage) {
		this.gameLanguage = gameLanguage;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
