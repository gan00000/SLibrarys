package com.efun.googlepay.efuntask;

import java.util.List;

import com.efun.core.tools.EfunLogUtil;
import com.efun.googlepay.BasePayActivity;
import com.efun.googlepay.EfunGooglePayService;
import com.efun.util.IabHelper;
import com.efun.util.IabHelper.QueryInventoryFinishedListener;
import com.efun.util.IabResult;
import com.efun.util.Inventory;
import com.efun.util.Purchase;

import android.content.Context;

public class QueryInventoryFinishedListenerImpl implements QueryInventoryFinishedListener {
	private IabHelper mHelper;

	/**
	 * service上下文或者BasePayActivity
	 */
	private Context context;
	
	private BasePayActivity payActivity;
	private PayPrompt prompt;
	
	private int setUpTiming = 0;
	
	public QueryInventoryFinishedListenerImpl(Context context,int timing) {
		this.context = context;
		this.setUpTiming = timing;
		mHelper = EfunGooglePayService.getIabHelper();
		if (context instanceof BasePayActivity) {
			this.payActivity = (BasePayActivity)context;
			prompt = this.payActivity.getPayPrompt();
		}
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
		List<Purchase> purchases = null;
		if (result.isFailure()) {
			EfunLogUtil.logD("onQueryInventoryFinished result is failure");
			EfunLogUtil.logD("Failed to query inventory: " + result.getMessage());
		} else {
			EfunLogUtil.logD("query inventory was successful.");
			//Log.d(BasePayActivity.TAG, "need to consume:" + inventory.getAllOwnedSkus());
			purchases = inventory.getAllPurchases();

			EfunLogUtil.logD("purchases size: " + purchases.size());
			if (null != purchases && purchases.size() == 1) {
				EfunLogUtil.logD("mConsumeFinishedListener. 消费一个");
				mHelper.consumeAsync(purchases.get(0), mConsumeFinishedListener);
				return;
			} else if (null != purchases && purchases.size() > 1) {
				EfunLogUtil.logD("mConsumeMultiFinishedListener.消费多个");
				mHelper.consumeAsync(purchases, mConsumeMultiFinishedListener);
				return;
			}
		}
		
		disDialog();
		if (payActivity != null && payActivity.getQueryItemListener() != null) {//通過activity是否為null來判斷是否是啟動遊戲檢查還是購買檢查
			payActivity.getQueryItemListener().onQueryFinish();
			return;
		}
		if (payActivity != null) {
			payActivity.determineCloseActivity();
		}    
	}
	

	private void disDialog(){
		if (prompt != null) {
			prompt.dismissProgressDialog();//如果prompt不等於null,標識是頁面打開購買
		}
	}

	/**
	 * 多个未消费
	 */
	private IabHelper.OnConsumeMultiFinishedListener mConsumeMultiFinishedListener = new IabHelper.OnConsumeMultiFinishedListener() {

		@Override
		public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {
			EfunLogUtil.logD("Consume Multiple finished.");
			disDialog();
			for (int i = 0; i < purchases.size(); i++) {
				if (results.get(i).isSuccess()) {
					EfunLogUtil.logD("sku: " + purchases.get(i).getSku() + " Consume finished success");
				} else {
					EfunLogUtil.logD("sku: " + purchases.get(i).getSku() + " Consume finished fail");
					EfunLogUtil.logD(purchases.get(i).getSku() + "consumption is not success, yet to be consumed.");
				}
			}
			EfunLogUtil.logD("End consumption flow.");

			if (payActivity != null && payActivity.getQueryItemListener() != null) {
				payActivity.getQueryItemListener().onQueryFinish();
			} else if (payActivity != null) {
				payActivity.determineCloseActivity();
			}
		}
	};

		/**
		 * 只有一个未消费
		 */
	private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			disDialog();
			EfunLogUtil.logD("Consumption finished");
			if (null != purchase) {
				EfunLogUtil.logD("Purchase: " + purchase.toString() + ", result: " + result);
			} else {
				EfunLogUtil.logD("Purchase is null");
			}

			if (result.isSuccess()) {
				EfunLogUtil.logD("Consumption successful.");
				if (purchase != null) {
					EfunLogUtil.logD("sku: " + purchase.getSku() + " Consume finished success");
				}
			} else {
				EfunLogUtil.logD("consumption is not success, yet to be consumed.");
			}
			EfunLogUtil.logD("End consumption flow.");
			if (payActivity != null && payActivity.getQueryItemListener() != null) {
				payActivity.getQueryItemListener().onQueryFinish();
			}else if (payActivity != null) {
				payActivity.determineCloseActivity();
			}
		}
	};
	    
}
