package com.starpy.googlepay.bean;

import android.content.Context;

import com.starpy.googlepay.constants.GooglePayContant;

public class GooglePayReqBean extends BasePayReqBean {

	private String productId;
	private String orderId;
	private String payType = GooglePayContant.GOOGLEPAYTYPE;

	public GooglePayReqBean(Context context) {
		super(context);
	}


	/**
	 * @return the productId
	 */

	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
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
		setProductId("");
		setExtra("");
		setGameCode("");
		setOrderId("");
		setCpOrderId("");
		setServerCode("");
	}


}
