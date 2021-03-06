package com.starpy.pay.gp.bean.req;

import android.content.Context;

import com.starpy.pay.gp.constants.GooglePayContant;

public class GooglePayCreateOrderIdReqBean extends PayReqBean {

	private String payValue = "0";

	private String payType = GooglePayContant.GOOGLEPAYTYPE;

	public GooglePayCreateOrderIdReqBean(Context context) {
		super(context);
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


	public String getPayValue() {
		return payValue;
	}

	public void setPayValue(String payValue) {
		this.payValue = payValue;
	}
}
