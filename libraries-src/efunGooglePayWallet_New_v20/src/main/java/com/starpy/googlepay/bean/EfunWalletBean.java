package com.starpy.googlepay.bean;


public class EfunWalletBean extends EfunBaseWalletBean{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * itemName 商品id
	 */
	private String itemName;
	/**
	 * itemPrice 商品价格
	 */
	private String itemPrice;
	/**
	 * currencyType 货币类型
	 */
	private String currencyType;
	/**
	 * itemNum 商品的个数
	 */
	private String itemNum;
	
	/**
	 * sku 商品id
	 */
	private String skuId;
	
	/**
	 * googleOrderId google订单号
	 */
	private String googleOrderId;
	
	/**
	 * skuPrice 商品价格
	 */
	private String skuPrice;

	private String errorDesc = "";
	/**
	 * 1表示创单失败，2表示其他失败
	 */
	private int errorType = 0;

	private int googleResponeCode;

	@Deprecated
	public String getItemName() {
		return itemName;
	}
	@Deprecated
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@Deprecated
	public String getItemPrice() {
		return itemPrice;
	}
	@Deprecated
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	
	public String getGoogleOrderId() {
		return googleOrderId;
	}
	public void setGoogleOrderId(String googleOrderId) {
		this.googleOrderId = googleOrderId;
	}
	
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}
	public int getGoogleResponeCode() {
		return googleResponeCode;
	}
	public void setGoogleResponeCode(int googleResponeCode) {
		this.googleResponeCode = googleResponeCode;
	}

	/**
	 * <p>Description: 1表示创单失败，2表示其他失败</p>
	 * @return
	 * @date 2016年1月14日
	 */
	public int getErrorType() {
		return errorType;
	}
	/**
	 * <p>Description: 1表示创单失败，2表示其他失败</p>
	 * @param errorType
	 * @date 2016年1月14日
	 */
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}


	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
