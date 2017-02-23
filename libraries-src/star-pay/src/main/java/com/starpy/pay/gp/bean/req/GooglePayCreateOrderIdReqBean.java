package com.starpy.pay.gp.bean.req;

import android.content.Context;

import com.starpy.pay.gp.constants.GooglePayContant;

public class GooglePayCreateOrderIdReqBean extends BasePayReqBean {

	private String productId;
	private String payType = GooglePayContant.GOOGLEPAYTYPE;

	public GooglePayCreateOrderIdReqBean(Context context) {
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
		setCpOrderId("");
		setServerCode("");
	}


}
