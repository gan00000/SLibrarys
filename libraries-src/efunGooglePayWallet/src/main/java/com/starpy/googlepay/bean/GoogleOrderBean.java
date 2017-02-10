package com.starpy.googlepay.bean;

import com.starpy.googlepay.constants.GooglePayContant;

public class GoogleOrderBean extends EfunBaseOrderBean{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
//	private String goodsId;
	private String ggmid;
//	private String googleOrderId;
	private String sku;
//	private String stone;
	private String orderId;
	private String version = "20130522";
	private String payType = GooglePayContant.GOOGLEPAYTYPE;
	
	/**
	 * @return the goodsId
	 */
//	public String getGoodsId() {
//		return goodsId;
//	}
//	/**
//	 * @param goodsId the goodsId to set
//	 */
//	public void setGoodsId(String goodsId) {
//		this.goodsId = goodsId;
//	}
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
	 * @return the googleOrderId
	 */
/*	public String getGoogleOrderId() {
		return googleOrderId;
	}
	*//**
	 * @param googleOrderId the googleOrderId to set
	 *//*
	public void setGoogleOrderId(String googleOrderId) {
		this.googleOrderId = googleOrderId;
	}*/
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
	/**
	 * @return the stone
	 */
//	public String getStone() {
//		return stone;
//	}
//	/**
//	 * @param stone the stone to set
//	 */
//	public void setStone(String stone) {
//		this.stone = stone;
//	}
	/**
	 * @return the orderId  efun订单号
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set  efun订单号
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	@Override
	public String toString() {
		return "GoogleOrderBean [ggmid=" + ggmid + ", sku=" + sku + ", orderId=" + orderId + ", version=" + version + ", toString()="
				+ super.toString() + "]";
	}
	
	
	public void clear(){
		setSku("");
		setRemark("");
		setGameCode("");
		setCreditId("");
		setOrderId("");
		setServerCode("");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	
	
}
