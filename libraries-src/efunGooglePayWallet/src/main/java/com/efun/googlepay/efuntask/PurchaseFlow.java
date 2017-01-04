package com.efun.googlepay.efuntask;

import com.efun.googlepay.BasePayActivity;

public class PurchaseFlow{

    public static void startPurchase(BasePayActivity payActivity, String sku) {
            if (payActivity != null) {
				if (!payActivity.isSupportGooglePlay()) {
					EndFlag.setEndFlag(true);
					payActivity.showGoogleStoreErrorMessage();
					return;
				}
				payActivity.get_orderBean().setSku(sku);
				new EfunAsyncPurchaseTask(payActivity).asyncExcute();
			}       
    }  
}
