package com.starpy.googlepay.bean;

import android.content.Context;

import com.starpy.googlepay.constants.GooglePayContant;

public class GooglePayReqBean extends BasePayReqBean {

	private String ggmid;
	private String sku;
	private String orderId;
	private String payType = GooglePayContant.GOOGLEPAYTYPE;

	public GooglePayReqBean(Context context) {
		super(context);
	}

	/**
	 * @return the ggmid
	 */
	public String getGgmid() {
		return ggmid;
	}
	/**
	 * @param ggmid the ggmid to set
	 */
	public void setGgmid(String ggmid) {
		this.ggmid = ggmid;
	}

	/**
	 * @return the sku
	 */

	public String getSku() {
		return sku;
	}
	/**
	 * @param sku the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set  订单号
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	public void clear(){
		setSku("");
		setExtra("");
		setGameCode("");
		setOrderId("");
		setCpOrderId("");
		setServerCode("");
	}


}
