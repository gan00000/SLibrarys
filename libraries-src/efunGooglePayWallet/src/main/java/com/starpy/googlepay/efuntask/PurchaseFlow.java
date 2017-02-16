package com.starpy.googlepay.efuntask;

import com.starpy.googlepay.BasePayActivity;

public class PurchaseFlow{

    public static void startPurchase(BasePayActivity payActivity, String sku) {
            if (payActivity != null) {
				if (!payActivity.isSupportGooglePlay()) {
					EndFlag.setEndFlag(true);
					payActivity.showGoogleStoreErrorMessage();
					return;
				}
				payActivity.get_orderBean().setProductId(sku);
				new SAsyncPurchaseTask(payActivity).asyncExcute();
			}       
    }  
}
