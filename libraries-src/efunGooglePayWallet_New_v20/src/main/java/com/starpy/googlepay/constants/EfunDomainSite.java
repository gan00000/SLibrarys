package com.starpy.googlepay.constants;

public class EfunDomainSite {
	public final static String EFUN_GOOGLE_PAY_CREATE_ORDER = "googlePlay_credit.shtml";
	/**
	 *  旧接口使用该接口名称请求发放钻石
	 */
	public final static String EFUN_GOOGLE_PAY_SEND_STONE = "googlePlay_sendStone.shtml";
	/**
	 *  新接口使用该接口名称请求发放钻石 （DeveloperPayload不在传递太多字段数据）
	 */
	public final static String EFUN_GOOGLE_PAY_PAY_STONE = "googlePlay_payStone.shtml";
	
	public final static String EFUN_MORE_OTHER_PAY = "payForward_toPay.shtml?";
	public final static String EFUN_MORE_GW_PAY = "payForward_toPayGW.shtml?";
	public final static String EFUN_GAME_LISTPRICE = "game_listProductId.shtml";
	public final static String EFUN_GOODSLIS_TO_PAYLIST = "payForward_toPayList.shtml?";
//	public final static String EFUN_GOODSLIS_TO_PAYLIST = "goodslist/index.html?";
	public final static String EFUN_REPORT_REFUND = "googlePlay_logPint.shtml";
}
