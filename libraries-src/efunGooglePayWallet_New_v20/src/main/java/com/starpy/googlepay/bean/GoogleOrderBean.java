package com.starpy.pay.gp.bean;

import com.starpy.pay.gp.constants.GooglePayContant;

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
	private String deviceSource = "SDK";
	private String activityCode = "";

	public String getDeviceSource() {
		return deviceSource;
	}

	public void setDeviceSource(String deviceSource) {
		this.deviceSource = deviceSource;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	/**
	 * @return the goodsId

	 */
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
	/**
	 * @param googleOrderId the googleOrderId to set
	 */
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
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
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
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public GoogleOrderBean cloneGoogleOrderBean(){
		try {
			return (GoogleOrderBean)clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public String toString() {
		return "GoogleOrderBean{" +
				"ggmid='" + ggmid + '\'' +
				", sku='" + sku + '\'' +
				", orderId='" + orderId + '\'' +
				", version='" + version + '\'' +
				", payType='" + payType + '\'' +
				", deviceSource='" + deviceSource + '\'' +
				", activityCode='" + activityCode + '\'' +
				"} " + super.toString();
	}
}
