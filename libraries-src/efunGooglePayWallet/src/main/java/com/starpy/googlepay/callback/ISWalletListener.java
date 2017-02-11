package com.starpy.googlepay.callback;

import com.core.base.callback.ISCallBack;
import com.starpy.googlepay.bean.EfunBaseWalletBean;

/**
* <p>Title: ISWalletListener</p>
* <p>Description: google储值回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013年11月25日
*/
public interface ISWalletListener extends ISCallBack {
	
	public void efunWallet(EfunBaseWalletBean walletBean);
}
