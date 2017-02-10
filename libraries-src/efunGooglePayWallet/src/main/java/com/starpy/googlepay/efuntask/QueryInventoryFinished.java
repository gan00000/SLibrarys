package com.starpy.googlepay.efuntask;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import org.json.JSONException;

import com.starpy.base.utils.EfunLogUtil;
import com.starpy.base.utils.SStringUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.EfunQueryInventoryState;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.util.IabHelper;
import com.starpy.util.IabHelper.QueryInventoryFinishedListener;
import com.starpy.util.IabResult;
import com.starpy.util.Inventory;
import com.starpy.util.Purchase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

public class QueryInventoryFinished implements QueryInventoryFinishedListener {
	private BasePayActivity payActivity;
	private Prompt prompt;
	private IabHelper mHelper;
	//private List<String> skuList;
//	private volatile boolean isTaskFinish = false;
	
	
	public QueryInventoryFinished(BasePayActivity basePayActivity) {
		this.payActivity = basePayActivity;
		this.prompt = basePayActivity.getPrompt();
		this.mHelper = basePayActivity.getHelper();
		//this.skuList = basePayActivity.get_skus();
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

		if (result.isFailure()) {
			prompt.dismissProgressDialog();
			Log.d("efun", "query result:" + result.getMessage());
			if (null != payActivity.getQueryInventoryState()
					&& payActivity.getQueryInventoryState().getQueryFailState() == EfunQueryInventoryState.SEND_STONE_FAIL) {
				EfunLogUtil.logD("getQueryInventoryState is 1");
				//prompt.complain("Failed to query inventory: SENDIND_FAIL");
			} else if (null != payActivity.getQueryInventoryState()
					&& payActivity.getQueryInventoryState().getQueryFailState() == EfunQueryInventoryState.SERVER_TIME_OUT) {
				EfunLogUtil.logD("getQueryInventoryState is 2");
				//payActivity.showGoogleServiceErrorMessage();
			} else {
				EfunLogUtil.logD("getQueryInventoryState is 3");
			//	prompt.complain("Failed to query inventory: " + result.getMessage());
			}
			EfunLogUtil.logD("getQueryInventoryState is null");
			/*if (payActivity != null && payActivity.isCloseActivityUserCancel()) {
				payActivity.finish();
			}*/
			onQueryProcessFinish();
			return;
		}
        
//        sendLocalToServer();
		EfunLogUtil.logD( "Query inventory was successful.");
     /*   List<Purchase> purchases = new ArrayList<Purchase>();
        for (int i=0;i<skuList.size(); i++) {
        	if (inventory.hasPurchase(skuList.get(i))) {
        		EfunLogUtil.logD( "We have " + skuList.get(i) + " need to consume,Consuming it.");
        		final Purchase mPurchase = inventory.getPurchase(skuList.get(i));
        		purchases.add(mPurchase);
        		Log.d("efun", "query inventory PurchaseState:" + mPurchase.getPurchaseState());
        		if(mPurchase.getPurchaseState() == 2){//退款订单
        			Log.d("efun", "refunded:属于退款订单");
        			startReportRefund(mPurchase);
        		}
        	}
        }*/
		
		List<Purchase> purchases = inventory.getAllPurchases();
        for (Purchase mPurchase : purchases) {
        	Log.d("efun_QueryInventoryFinished","purchases sku: " + mPurchase.getSku());
        	if (mPurchase.getPurchaseState() == 2) {//退款订单
				Log.d("efun", "refunded:属于退款订单");
				startReportRefund(mPurchase);
			}
		}
	        
		EfunLogUtil.logD("purchases size: " + purchases.size());
        if (null == purchases || purchases.isEmpty()) {
        	//sendLocalFileToServer();
			prompt.dismissProgressDialog();
			EfunLogUtil.logD( "purchases is empty.purchases size: " + purchases.size());
		//	isTaskFinish = true;

			onQueryProcessFinish();
		}else  if (purchases.size() == 1) {
        	EfunLogUtil.logD("mConsumeFinishedListener. 消费一个");
        	mHelper.consumeAsync(purchases.get(0), mConsumeFinishedListener);
		} else if (purchases.size() > 1){
			EfunLogUtil.logD("mConsumeMultiFinishedListener.消费多个");
			mHelper.consumeAsync(purchases, mConsumeMultiFinishedListener);
		}
    
        /*if (purchases == null || purchases.isEmpty()) {
			if (payActivity != null && payActivity.getQueryItemListener() != null) {
    			payActivity.getQueryItemListener().onQueryFinish();
    		}
		}*/
	}

	private void onQueryProcessFinish() {
		if (payActivity != null && payActivity.getQueryItemListener() != null) {
			payActivity.getQueryItemListener().onQueryFinish();
		}
	}

	/**
	* <p>Title: startReportRefund</p>
	* <p>Description: 开启线程上报退款</p>
	* @param mPurchase
	*/
	private void startReportRefund(final Purchase mPurchase) {
		Thread thread = new Thread(){
			public void run() {
				EfunWalletApi.reportRefund(payActivity, mPurchase);
			};
		};
		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				ex.printStackTrace();
			}
		});
		thread.start();
	}

	/**
	* <p>Title: sendLocalFileToServer</p>
	* <p>Description: </p>
	*/
	private void sendLocalFileToServer() {
		SharedPreferences preferences = payActivity.getSharedPreferences(GooglePayContant.EFUNFILENAME, Context.MODE_PRIVATE);
        final String purchaseInfo = preferences.getString(GooglePayContant.PURCHASE_DATA_ONE, "");
        final String purchaseSign = preferences.getString(GooglePayContant.PURCHASE_SIGN_ONE, "");
        
        if (SStringUtil.isNotEmpty(purchaseInfo) && SStringUtil.isNotEmpty(purchaseSign)) {
        	final Handler handler = new Handler();
        	new Thread(new Runnable() {
				
				@Override
				public void run() {
					EfunLogUtil.logD("send the local purchase data to server");
					EfunVerifyTask efunVerifyTask = new EfunVerifyTask();
					if (efunVerifyTask.verifyQueryInventory(payActivity, purchaseInfo, purchaseSign, "isSharedPreferences")) {
						EfunLogUtil.logD("send the local purchase data to server,server return success");
						try {
							final Purchase mPurchase = new Purchase(IabHelper.ITEM_TYPE_INAPP, purchaseInfo, purchaseSign);
							if (mPurchase != null && mHelper != null) {
								
								handler.post(new Runnable() {
									
									@Override
									public void run() {
										mHelper.consumeAsync(mPurchase, mConsumeFinishedCallback);
									}
								});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else {
						EfunLogUtil.logD("send the local purchase data to server,server return fail");
						//if (isTaskFinish) {
							payActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									
									prompt.dismissProgressDialog();
									//isTaskFinish = true;
								}
							});
						//}
					}
				}
			}).start();
		} else {
			//if (isTaskFinish) {
				prompt.dismissProgressDialog();
			//}
			//isTaskFinish = true;
		}
	}

	/**
	 * 多个未消费
	 */
	private IabHelper.OnConsumeMultiFinishedListener mConsumeMultiFinishedListener = new IabHelper.OnConsumeMultiFinishedListener() {
			
			@Override
			public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {
				EfunLogUtil.logD( "Consume Multiple finished.");
				//sendLocalFileToServer();
				//if (isTaskFinish) {
					prompt.dismissProgressDialog();
				//}
				for (int i = 0; i < purchases.size(); i++) {
					if (results.get(i).isSuccess()) {
						EfunLogUtil.logD( "sku: " + purchases.get(i).getSku() + " Consume finished success");
					} else  {
						EfunLogUtil.logD( "sku: " + purchases.get(i).getSku() + " Consume finished fail");
						EfunLogUtil.logD( purchases.get(i).getSku() + "consumption is not success, yet to be consumed.");
					}
				}
				EfunLogUtil.logD( "End consumption flow.");
				//isTaskFinish = true;
				onQueryProcessFinish();
			}
		};

		/**
		 * 只有一个未消费
		 */
		private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
	        public void onConsumeFinished(Purchase purchase, IabResult result) {
	        	EfunLogUtil.logD("Consumption finished");
	        	if ( null != purchase) {
					EfunLogUtil.logD("Purchase: " + purchase.toString() + ", result: " + result);
				} else {
					EfunLogUtil.logD("Purchase is null");
				}
				//sendLocalFileToServer();
	    		//if (isTaskFinish) {
	    			prompt.dismissProgressDialog();
	    		//}
	    		if (result.isSuccess()) {
	    			EfunLogUtil.logD( "Consumption successful.");
	    			if (purchase != null) {
	    				EfunLogUtil.logD( "sku: " + purchase.getSku() + " Consume finished success");
	    			}
	    		} else {
	    			EfunLogUtil.logD( "consumption is not success, yet to be consumed.");
	    		}
	    		EfunLogUtil.logD( "End consumption flow.");
	    		
	    		onQueryProcessFinish();
	        }
	    };
	    
	    /**
	     * 文件中记录的订单进行消费
	     */
	    private IabHelper.OnConsumeFinishedListener mConsumeFinishedCallback = new IabHelper.OnConsumeFinishedListener() {
	    	public void onConsumeFinished(Purchase purchase, IabResult result) {
	    		EfunLogUtil.logD("Consumption finished");
	        	if ( null != purchase) {
					EfunLogUtil.logD("Purchase: " + purchase.toString() + ", result: " + result);
				} else {
					EfunLogUtil.logD("Purchase is null");
				}
	    	//	if (isTaskFinish) {
	    			prompt.dismissProgressDialog();
	    		//}
	    		//isTaskFinish = true;
	    		if (result.isSuccess()) {
	    			EfunLogUtil.logD( "Consumption successful.");
	    			
	    			if (purchase == null) {
	    				return;
	    			}
	    			EfunLogUtil.logD( "sku: " + purchase.getSku() + " Consume finished success");
	    		} else {
	    			EfunLogUtil.logD( "consumption is not success, yet to be consumed.");
	    		}
	    		EfunLogUtil.logD( "End consumption flow.");
	    		onQueryProcessFinish();
	    	}
	    };
}
