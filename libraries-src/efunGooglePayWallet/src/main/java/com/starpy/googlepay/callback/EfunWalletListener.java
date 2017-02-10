package com.starpy.googlepay.callback;

import com.starpy.base.callback.EfunCallBack;
import com.starpy.googlepay.bean.EfunBaseWalletBean;

/**
* <p>Title: EfunWalletListener</p>
* <p>Description: google储值回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013年11月25日
*/
public interface EfunWalletListener extends EfunCallBack {
	
	public void efunWallet(EfunBaseWalletBean walletBean);
}
