package com.starpy.pay.gp.task;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.PL;
import com.starpy.base.utils.SLog;
import com.starpy.pay.gp.BasePayActivity;
import com.starpy.pay.gp.bean.req.GoogleExchangeReqBean;
import com.starpy.pay.gp.bean.QueryInventoryState;
import com.starpy.pay.gp.constants.GooglePayDomainSite;
import com.starpy.pay.gp.util.IabHelper;
import com.starpy.pay.gp.util.IabHelper.QueryInventoryFinishedListener;
import com.starpy.pay.gp.util.IabResult;
import com.starpy.pay.gp.util.Inventory;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.pay.gp.util.Purchase;

public class QueryInventoryFinished implements QueryInventoryFinishedListener {
	private BasePayActivity payActivity;
	private PayDialog payDialog;
	private IabHelper mHelper;

	
	public QueryInventoryFinished(BasePayActivity basePayActivity) {
		this.payActivity = basePayActivity;
		this.payDialog = basePayActivity.getPayDialog();
		this.mHelper = basePayActivity.getHelper();
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

		if (result.isFailure()) {
			payDialog.dismissProgressDialog();
			PL.i("query result:" + result.getMessage());
			if (null != payActivity.getQueryInventoryState()
					&& payActivity.getQueryInventoryState().getQueryFailState() == QueryInventoryState.SEND_STONE_FAIL) {
				SLog.logD("getQueryInventoryState is 1");
				//payDialog.complain("Failed to query inventory: SENDIND_FAIL");
			} else if (null != payActivity.getQueryInventoryState()
					&& payActivity.getQueryInventoryState().getQueryFailState() == QueryInventoryState.SERVER_TIME_OUT) {
				SLog.logD("getQueryInventoryState is 2");
				//payActivity.showGoogleServiceErrorMessage();
			} else {
				SLog.logD("getQueryInventoryState is 3");
			//	payDialog.complain("Failed to query inventory: " + result.getMessage());
			}
			SLog.logD("getQueryInventoryState is null");
			/*if (payActivity != null && payActivity.isCloseActivityUserCancel()) {
				payActivity.finish();
			}*/
			onQueryProcessFinish();
			return;
		}
        
//        sendLocalToServer();
		SLog.logD( "Query inventory was successful.");
		List<Purchase> purchaseList = inventory.getAllPurchases();

		if (null == purchaseList || purchaseList.isEmpty()) {

			payDialog.dismissProgressDialog();
			SLog.logD( "purchases is empty");
			onQueryProcessFinish();

		}else {

			SLog.logD("purchases size: " + purchaseList.size());
			for (Purchase mPurchase : purchaseList) {
				SLog.logI("purchases sku: " + mPurchase.getSku());
				if (mPurchase.getPurchaseState() == 2) {//退款订单
					PL.i("refunded:属于退款订单");
				} else {

					GoogleExchangeReqBean exchangeReqBean = new GoogleExchangeReqBean(payActivity);
					exchangeReqBean.setDataSignature(mPurchase.getSignature());
					exchangeReqBean.setPurchaseData(mPurchase.getOriginalJson());

					exchangeReqBean.setRequestUrl(PayHelper.getPreferredUrl(payActivity));
					exchangeReqBean.setRequestMethod(GooglePayDomainSite.google_send);

					GoogleExchangeReqTask googleExchangeReqTask = new GoogleExchangeReqTask(payActivity, exchangeReqBean);
					googleExchangeReqTask.setReqCallBack(new ISReqCallBack() {
						@Override
						public void success(Object o, String rawResult) {
							PL.i("exchange callback");
							// 消费
						/*if (mHelper != null) {
							PL.i("google pay consumeAsync");
							mHelper.consumeAsync(mPurchase, mConsumeFinishedListener);
						}*/
						}

						@Override
						public void timeout(String code) {

						}

						@Override
						public void noData() {

						}
					});
					googleExchangeReqTask.excute();
				}

			}

			if (purchaseList.size() == 1) {
				SLog.logD("mConsumeFinishedListener. 消费一个");
				mHelper.consumeAsync(purchaseList.get(0), mConsumeFinishedListener);
			} else if (purchaseList.size() > 1) {
				SLog.logD("mConsumeMultiFinishedListener.消费多个");
				mHelper.consumeAsync(purchaseList, mConsumeMultiFinishedListener);
			}
		}
    
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
	/*private void sendLocalFileToServer() {
		SharedPreferences preferences = payActivity.getSharedPreferences(GooglePayContant.EFUNFILENAME, Context.MODE_PRIVATE);
        final String purchaseInfo = preferences.getString(GooglePayContant.PURCHASE_DATA_ONE, "");
        final String purchaseSign = preferences.getString(GooglePayContant.PURCHASE_SIGN_ONE, "");
        
        if (SStringUtil.isNotEmpty(purchaseInfo) && SStringUtil.isNotEmpty(purchaseSign)) {
        	final Handler handler = new Handler();
        	new Thread(new Runnable() {
				
				@Override
				public void run() {
					SLog.logD("send the local purchase data to server");
					VerifyTask verifyTask = new VerifyTask();
					if (verifyTask.verifyQueryInventory(payActivity, purchaseInfo, purchaseSign, "isSharedPreferences")) {
						SLog.logD("send the local purchase data to server,server return success");
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
						SLog.logD("send the local purchase data to server,server return fail");
						//if (isTaskFinish) {
							payActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									
									payDialog.dismissProgressDialog();
									//isTaskFinish = true;
								}
							});
						//}
					}
				}
			}).start();
		} else {
			//if (isTaskFinish) {
				payDialog.dismissProgressDialog();
			//}
			//isTaskFinish = true;
		}
	}*/

	/**
	 * 多个未消费
	 */
	private IabHelper.OnConsumeMultiFinishedListener mConsumeMultiFinishedListener = new IabHelper.OnConsumeMultiFinishedListener() {
			
			@Override
			public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {
				SLog.logD( "Consume Multiple finished.");
				//sendLocalFileToServer();
				//if (isTaskFinish) {
					payDialog.dismissProgressDialog();
				//}
				for (int i = 0; i < purchases.size(); i++) {
					if (results.get(i).isSuccess()) {
						SLog.logD( "sku: " + purchases.get(i).getSku() + " Consume finished success");
					} else  {
						SLog.logD( "sku: " + purchases.get(i).getSku() + " Consume finished fail");
						SLog.logD( purchases.get(i).getSku() + "consumption is not success, yet to be consumed.");
					}
				}
				SLog.logD( "End consumption flow.");
				//isTaskFinish = true;
				onQueryProcessFinish();
			}
		};

		/**
		 * 只有一个未消费
		 */
		private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
	        public void onConsumeFinished(Purchase purchase, IabResult result) {
	        	SLog.logD("Consumption finished");
	        	if ( null != purchase) {
					SLog.logD("Purchase: " + purchase.toString() + ", result: " + result);
				} else {
					SLog.logD("Purchase is null");
				}
				//sendLocalFileToServer();
	    		//if (isTaskFinish) {
	    			payDialog.dismissProgressDialog();
	    		//}
	    		if (result.isSuccess()) {
	    			SLog.logD( "Consumption successful.");
	    			if (purchase != null) {
	    				SLog.logD( "sku: " + purchase.getSku() + " Consume finished success");
	    			}
	    		} else {
	    			SLog.logD( "consumption is not success, yet to be consumed.");
	    		}
	    		SLog.logD( "End consumption flow.");
	    		
	    		onQueryProcessFinish();
	        }
	    };
	    
	    /**
	     * 文件中记录的订单进行消费
	     */
	    private IabHelper.OnConsumeFinishedListener mConsumeFinishedCallback = new IabHelper.OnConsumeFinishedListener() {
	    	public void onConsumeFinished(Purchase purchase, IabResult result) {
	    		SLog.logD("Consumption finished");
	        	if ( null != purchase) {
					SLog.logD("Purchase: " + purchase.toString() + ", result: " + result);
				} else {
					SLog.logD("Purchase is null");
				}
	    	//	if (isTaskFinish) {
	    			payDialog.dismissProgressDialog();
	    		//}
	    		//isTaskFinish = true;
	    		if (result.isSuccess()) {
	    			SLog.logD( "Consumption successful.");
	    			
	    			if (purchase == null) {
	    				return;
	    			}
	    			SLog.logD( "sku: " + purchase.getSku() + " Consume finished success");
	    		} else {
	    			SLog.logD( "consumption is not success, yet to be consumed.");
	    		}
	    		SLog.logD( "End consumption flow.");
	    		onQueryProcessFinish();
	    	}
	    };
}
