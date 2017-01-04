package com.efun.googlepay.callback;

import com.efun.core.callback.EfunCallBack;
import com.efun.googlepay.bean.EfunBaseWalletBean;

/**
* <p>Title: EfunWalletListener</p>
* <p>Description: google储值回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013年11月25日
*/
public interface EfunWalletListener extends EfunCallBack{
	
	public void efunWallet(EfunBaseWalletBean walletBean);
}
