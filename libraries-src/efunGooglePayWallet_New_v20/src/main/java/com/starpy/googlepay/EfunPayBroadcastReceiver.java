package com.starpy.googlepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EfunPayBroadcastReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d(BasePayActivity.TAG, "EfunPayBroadcastReceiver onReceive");
		if (EfunGooglePayService.googleOrderBean != null) {
			EfunGooglePay.setUpGooglePay(context, EfunGooglePayService.googleOrderBean.getUserId(),
					EfunGooglePayService.googleOrderBean.getGameCode(), EfunGooglePayService.googleOrderBean.getServerCode(),
					EfunGooglePayService.googleOrderBean.getRoleId(), EfunGooglePayService.googleOrderBean.getEfunRole(),
					EfunGooglePayService.googleOrderBean.getEfunLevel());
		}
	}

}
