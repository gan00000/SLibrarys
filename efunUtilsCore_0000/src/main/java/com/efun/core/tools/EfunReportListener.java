package com.efun.core.tools;

import com.efun.core.callback.EfunCallBack;

public interface EfunReportListener extends EfunCallBack{
	/**
	 * 
	 * @param efunOrdeId efun订单号
	 * @param payMentSque  储值订单号
	 * @param creitedId   creitedId
	 * @param projectId   储值项ID
	 * @param projectPrice  储值项金额
	 * @param serverCode   服务器ID
	 * @param remark   remark
	 */
	public void efunWallet(String efunOrdeId,String payMentSque,String efunUserId,String crietedId,String serverCode,String projectId,String projectPrice,String remark);
	public void efunError();
}
