package com.efun.googlepay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import com.efun.core.tools.EfunLogUtil;
import com.efun.googlepay.bean.WebOrderBean;
import com.efun.googlepay.callback.EfunWalletListener;
import com.efun.googlepay.constants.GooglePayContant;
import com.efun.googlepay.efuntask.EfunPayUtil;
import com.efun.googlepay.efuntask.EndFlag;

public abstract class BaseBillActivity extends BasePayActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*if (_skus == null || _skus.isEmpty() || _skus.size() == 0) {
			throw new RuntimeException("请先初始化sku集合");
		}
		if (_skus.contains(null) || _skus.contains("")) {
			throw new RuntimeException("sku不能包含null或者\"\"");
		}*/
		this.efunHelperSetUp();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EfunLogUtil.logI("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		handlerActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}


	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// very important:
		if (mHelper != null){
			EfunLogUtil.logI("mHelper.dispose");
			mHelper.dispose();
		}
		if (null != this.prompt) {
			prompt.dismissProgressDialog();
		}
		mHelper = null;
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);
		try{
			if (!openGW && null != walletListeners && !walletListeners.isEmpty() && null != walletBean) {
				EfunLogUtil.logI("walletListeners size:" + walletListeners.size());
				if(walletBean.getPurchaseState() != GooglePayContant.PURCHASESUCCESS){
					walletBean.setPurchaseState(GooglePayContant.PURCHASEFAILURE);
					if (walletBean.getErrorType() != 1) {
						walletBean.setErrorType(2);
						walletBean.setErrorDesc("取消支付");
					}
				}
				for (EfunWalletListener walletListener : walletListeners) {
					if (walletListener != null) {
						walletListener.efunWallet(walletBean);
					}
				}
			} else {
				EfunLogUtil.logI("不回调");
			}
		}catch(Exception e){
			Log.i("efun", e.getMessage() + "");
			e.printStackTrace();
		}finally{
			if (null != walletListeners) {
				walletListeners.clear();
			}
		}
	}
	
	@Override
	protected List<String> initSku() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void handlerActivityResult(int requestCode, int resultCode,Intent data) {
		if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			EfunLogUtil.logI("onActivityResult handled by IABUtil. the result was related to a purchase flow and was handled");
		} else {
			EfunLogUtil.logI("onActivityResult handled by IABUtil.the result was not related to a purchase");
			EndFlag.setEndFlag(true);
			prompt.dismissProgressDialog();
			prompt.complainCloseAct(efunPayError.getEfunGoogleBuyFailError());
		}
	}
	
	
	/**
	* <p>Title: startWebClient</p>
	* <p>Description: 启动更多储值支付页面</p>
	*/
	protected void startWebClient() {
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startOtherWallet(this, webOrderBean, "");
		openGW = true;
		this.finish();
	}
	
	
	
	protected void startWebGW(){
		/*Intent GWPayIntent = new Intent(GooglePayContant.BillAction.EFUN_PAY_ACTIVITY_GW + _gameCode);
		startGW(GWPayIntent);*/
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startGWWallet(this, webOrderBean, "");
		openGW = true;
		this.finish();
	}

}
