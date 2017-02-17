package com.starpy.googlepay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.utils.SLogUtil;
import com.starpy.googlepay.callback.ISWalletListener;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.task.EndFlag;

public abstract class BaseGooglePayActivity extends BasePayActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SLogUtil.logI("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		handlerActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// very important:
		if (mHelper != null){
			SLogUtil.logI("mHelper.dispose");
			mHelper.dispose();
		}
		mHelper = null;
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);

		if (null != this.prompt) {
			prompt.dismissProgressDialog();
			//prompt = null;
		}

		try {
			if (null != walletListeners && !walletListeners.isEmpty()) {
				SLogUtil.logI("walletListeners size:" + walletListeners.size());
				if (walletBean != null && walletBean.getPurchaseState() != GooglePayContant.PURCHASESUCCESS) {
					walletBean.setPurchaseState(GooglePayContant.PURCHASEFAILURE);
				}
				for (ISWalletListener walletListener : walletListeners) {
					if (walletListener != null) {
						walletListener.efunWallet(walletBean);
					}
				}
			} else {
				SLogUtil.logI("不回调");
			}
		} catch (Exception e) {
			Log.i("efun", e.getMessage() + "");
			e.printStackTrace();
		} finally {
			if (null != walletListeners) {
				walletListeners.clear();
			}
		}
	}
	

	private void handlerActivityResult(int requestCode, int resultCode,Intent data) {
		prompt.dismissProgressDialog();
		if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			SLogUtil.logI("onActivityResult handled by IABUtil. the result was related to a purchase flow and was handled");
		} else {
			SLogUtil.logI("onActivityResult handled by IABUtil.the result was not related to a purchase");
			EndFlag.setEndFlag(true);
			prompt.complainCloseAct(efunPayError.getGoogleBuyFailError());
		}
	}
	
	

}
