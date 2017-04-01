package com.starpy.pay.gp.constants;


public class GooglePayContant {
	
	public static final int RC_REQUEST = 10001;
	
	/**
	 * PAY_FROM payform 参数默认值
	 */
	public static final String PAY_FROM = "starpy";
	/**
	 * WIFI wifi参数值
	 */
	public static final String WIFI = "-111111";
	/**
	 * THIRDPAYTYPE 内嵌第三方paytype参数默认值
	 */
	public static final String THIRDPAYTYPE = "mobile";
	/**
	 * GOOGLEPAYTYPE google paytype参数默认值
	 */
	public static final String GOOGLEPAYTYPE = "SDK";
	
	/**
	 * PURCHASESUCCESS 回调购买成功标识
	 */
	public static final int PURCHASESUCCESS = 100000;
	/**
	 * PURCHASEFAILURE 回调购买失败标识
	 */
	public static final int PURCHASEFAILURE = -100000;
	
	/**
	 * EFUNFILENAME 储值数据保存文件
	 */
	public static final String EFUNFILENAME = "EFUN_WALLET.db";
//	public static final String PURCHASESKU = "PURCHASESKU";
	/**
	 * PURCHASE_DATA_ONE 保存最后google储值的一笔订单data
	 */
	public static final String PURCHASE_DATA_ONE = "EFUN_PURCHASE_DATA_ONE";
	/**
	 * PURCHASE_SIGN_ONE  保存最后google储值的一笔订单sign
	 */
	public static final String PURCHASE_SIGN_ONE = "EFUN_PURCHASE_SIGN_ONE";
//	public static final String PURCHASE_DATA_TWO = "EFUN_PURCHASE_DATA_TWO";
//	public static final String PURCHASE_SIGN_TWO = "EFUN_PURCHASE_SIGN_TWO";
//	public static final String PURCHASE_DATA_THREE = "EFUN_PURCHASE_DATA_THREE";
//	public static final String PURCHASE_SIGN_THREE = "EFUN_PURCHASE_SIGN_THREE";
//	public static final String PURCHASE_SKU = "EFUN_PURCHASE_SKU";//表示本次保存于哪一个key
//	public static final String EFUN_SKUS_KEY = "EFUN_SKUS";//用于保存后台获取的skus json 数据key
//	public static final String EFUN_SKUS_TIME_KEY = "EFUN_SKUS_TIME";//用于保存后台获取的skus json 数据key
	
	public static final String EFUN_CURRENT_ORDER_ID_KEY = "EFUN_CURRENT_ORDER_ID_KEY";//用于保存后台获取的skus json 数据key
	

}
