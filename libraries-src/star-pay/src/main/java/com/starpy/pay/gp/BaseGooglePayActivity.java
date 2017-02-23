package com.starpy.pay.gp;

import android.content.Intent;
import android.os.Bundle;

import com.starpy.base.utils.SLog;
import com.starpy.pay.gp.task.EndFlag;

public abstract class BaseGooglePayActivity extends BasePayActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SLog.logI("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		handlerActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// very important:
		if (mHelper != null){
			SLog.logI("mHelper.dispose");
			mHelper.dispose();
		}
		mHelper = null;
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);

		if (null != this.payDialog) {
			payDialog.dismissProgressDialog();
			//payDialog = null;
		}

	}
	

	private void handlerActivityResult(int requestCode, int resultCode,Intent data) {
		payDialog.dismissProgressDialog();
		if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			SLog.logI("onActivityResult handled by IABUtil. the result was related to a purchase flow and was handled");
		} else {
			SLog.logI("onActivityResult handled by IABUtil.the result was not related to a purchase");
			EndFlag.setEndFlag(true);
			payDialog.complainCloseAct(efunPayError.getGoogleBuyFailError());
		}
	}
	
	

}
