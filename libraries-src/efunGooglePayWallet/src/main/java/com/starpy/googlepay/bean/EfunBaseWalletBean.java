package com.starpy.googlepay.bean;

import java.io.Serializable;

import com.starpy.googlepay.constants.GooglePayContant;

public class EfunBaseWalletBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * purchaseState 储值成功失败状态
	 */
	private int purchaseState;
	
	/**
	 * orderId efun订单号
	 */
	private String orderId;
	
	public int getPurchaseState() {
		return purchaseState;
	}

	public void setPurchaseState(int purchaseState) {
		this.purchaseState = purchaseState;
	}
	@Deprecated
	public String getOrderId() {
		return orderId;
	}
	@Deprecated
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getEfunOrderId() {
		return orderId;
	}

	public void setEfunOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public boolean isPurchaseSuccess(){
		return this.getPurchaseState() == GooglePayContant.PURCHASESUCCESS;
	}
	
	@Override
	public String toString() {
		return "EfunBaseWalletBean [purchaseState=" + purchaseState + ", orderId=" + orderId + "]";
	}
	
	
}
