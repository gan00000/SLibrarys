package com.starpy.googlepay.task;

import com.starpy.googlepay.BasePayActivity;

public class PurchaseFlow{

    public static void startPurchase(BasePayActivity payActivity, String sku) {
            if (payActivity != null) {
				if (!payActivity.isSupportGooglePlay()) {
					EndFlag.setEndFlag(true);
					payActivity.showGoogleStoreErrorMessage();
					return;
				}
				payActivity.getGoogleOrderBean().setProductId(sku);
				new SAsyncPurchaseTask(payActivity).asyncExcute();
			}       
    }  
}
