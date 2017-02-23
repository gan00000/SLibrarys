/*
package com.starpy.pay.gp.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.SLogUtil;
import com.starpy.pay.gp.BasePayActivity;
import com.starpy.pay.gp.bean.EfunWalletBean;
import com.starpy.pay.gp.bean.QueryInventoryState;
import com.starpy.pay.gp.constants.GooglePayContant;
import com.starpy.pay.gp.util.IabHelper;
import com.starpy.pay.gp.util.IabHelper.OnIabPurchaseFinishedListener;
import com.starpy.pay.gp.util.IabResult;
import com.starpy.pay.gp.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

*/
/**
* <p>Title: VerifyTask</p>
* <p>Description: 请求发放砖石验证订单类</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013年11月25日
*//*

public class VerifyTask {
	
	private Handler mHandler;
	
	*/
/**
	* <p>Title: verifyOrder</p>
	* <p>Description: 购买商品的时候进行服务器端验证</p>
	* @param payActivity
	* @param mPurchaseListener 购买完成后的回调
	* @param purchaseData	google返回的purchaseData
	* @param dataSignature  google返回的dataSignature
	* @param mPurchasingItemType 内购商品的类型
	*//*

	public void verifyOrder(final BasePayActivity payActivity, final OnIabPurchaseFinishedListener mPurchaseListener,
							final String purchaseData, final String dataSignature, String mPurchasingItemType){
		try {
			final Purchase purchase = new Purchase(mPurchasingItemType, purchaseData, dataSignature);
			final String sku = purchase.getSku();

			EndFlag.setCanPurchase(false);//标识能否再次购买
	          if (mHandler == null) {
	        	  mHandler = new Handler();
				}
	          SLog.logD( "开始验证订单是否真实，商品sku:" + sku);
	          new Thread(new Runnable() {
				
				@Override
				public void run() {
					String result = EfunWalletApi.exchage(payActivity,purchaseData, dataSignature);//请求服务器验证购买的订单并且是否发放砖石
					if (result != null && SStringUtil.isNotEmpty(result)) {
						
						SLog.logD( "result返回：" + result);
						
						try {
							final JSONObject json = new JSONObject(result);
//							if (false) {
							*/
/*if (JsonUtil.efunVerificationRequest(json)) {
								
								serverBackProcess(payActivity, mPurchaseListener, purchase, sku, json);
								
							}else{//server not connect
								SLog.logW("do not clear local purchaseData,server not connect.");
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										SLog.logW("server internet is not connect.");
										//网络连接超时的回调
										IabResult iabResult = new IabResult(GooglePayContant.IAB_STATE, payActivity.getEfunPayError().getGoogleServerError());
										if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(iabResult, purchase);
									}
								});
							}*//*

						} catch (JSONException e) {
							SLog.logW( "EfunVerifyUtil json异常");
							e.printStackTrace();
						}
					}
				}
			}).start();
          
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
	}
	
	*/
/**
	* <p>Title: verifyQueryInventory</p>
	* <p>Description: 查询未消费商品的订单验证</p>
	* @param payActivity
	* @param purchaseData
	* @param dataSignature
	* @return
	*//*

	public boolean verifyQueryInventory(final BasePayActivity payActivity,String purchaseData, String dataSignature,String linkMark) {
		String result = null;
		
		if (SStringUtil.isEmpty(linkMark)) {
			result = EfunWalletApi.exchage(payActivity,purchaseData, dataSignature);
		} else {
			result = EfunWalletApi.exchage(payActivity,purchaseData, dataSignature, linkMark);
		}
		
		if (!TextUtils.isEmpty(result)) {
//			SLog.logI( "result返回：" + result);
			try {
				JSONObject json = new JSONObject(result);
				checkAndClearData(payActivity, json);
				if (!json.isNull("result") && json.optString("result", "").equals("0000")) {
					if ("true".equals(json.optString("isSign"))) {//判断订单是否验证成功
						SLog.logI( "验证订单是否真实>>>>>成功");
						SLog.logI( "此订单已经成功，并且发放砖石，began to consume");
						//	return true;
					}
				} else {
					SLog.logI( "发送钻石失败.");
					payActivity.getQueryInventoryState().setQueryFailState(QueryInventoryState.SEND_STONE_FAIL);
					//return false;
				}

			} catch (JSONException e) {
				SLog.logW( "query jsonexception...");
				e.printStackTrace();
				//return false;
			}

		}
		return true;
	}

	*/
/**
	* <p>Title: checkAndClearData</p>
	* <p>Description: </p>
	* @param payActivity
	* @param json
	*//*

	private void checkAndClearData(final BasePayActivity payActivity, JSONObject json) {
		String efunPurchaseData = getPurchaseData(json);
		SharedPreferences preferences = payActivity.getSharedPreferences(GooglePayContant.EFUNFILENAME, Context.MODE_PRIVATE);
		String localPurchaseData = preferences.getString(GooglePayContant.PURCHASE_DATA_ONE, "");
		SLog.logI("efunPurchaseData:" + efunPurchaseData);
		SLog.logI("localPurchaseData:" + localPurchaseData);
		if (efunPurchaseData.equals(localPurchaseData)) {
			SLog.logI("query clear local purchaseData");
			preferences.edit().clear().commit();
		} else {
			SLog.logI("query do not clear local purchaseData");
		}
	}

	private String getPurchaseData(JSONObject json){
		return json.optString("purchaseData", "");
	}
	
	//服务端返回的数据如下
	// {"gamePay":0.99,"goodsId":"payone","isSign":"true","purchaseData":"{\"orderId\":\"12999763169054705758.1336697901909981\",\"packageName\":
	//\"com.ejoy.sg.efun\",\"productId\":\"payone\",\"purchaseTime\":1407921750854,\"purchaseState\":0,\"developerPayload\":\"{\\\"gameCode\\\":\\\"mmzb\\\",
	//\\\"ggmid\\\":\\\"3318412\\\",\\\"userId\\\":\\\"1\\\",\\\"serverCode\\\":\\\"999\\\",\\\"creditId\\\":\\\"2147583846\\\",\\\"moneyType\\\":\\\"USD\\\",
	//\\\"orderId\\\":\\\"MMZB1407921689177NUW\\\",\\\"payFrom\\\":\\\"efun\\\"}\",\"purchaseToken\":
	//\"kalbplcndghhdldogiicpkik.AO-J1Ox6pHtyVQlCjZ0h0VmdD1JM8nDpNyCobT-R-37WXHyICP64GIkF8esliE2MGdC8eZ1YHY01riQsZ_NSbBR6VlBjCI2E4kg7u1Waa1Plfw311tWP3q8\"}","result":"0000"}

	private void setWalletBean(final BasePayActivity payActivity, final JSONObject json){
		
		//EfunWalletBean walletBean = payActivity.getWalletBean();
		if (null != walletBean) {
			walletBean.setItemPrice(json.optString("gamePay", ""));//商品价格
			walletBean.setSkuPrice(json.optString("gamePay", ""));//商品价格
			walletBean.setPurchaseState(GooglePayContant.PURCHASESUCCESS);//购买是否成功标识
			walletBean.setItemName(json.optString("goodsId", ""));//商品ID
			walletBean.setProductId(json.optString("goodsId", ""));//商品ID
			walletBean.setCurrencyType(GooglePayContant.MONEY_TYPE);//货币类型
			walletBean.setItemNum("1");//购买的商品个数
			String purchaseData = getPurchaseData(json);
			if (SStringUtil.isNotEmpty(purchaseData)) {
				//			SLog.logI("purchaseData处理后：" + purchaseData);
				try {
					JSONObject purchaseDataJson = new JSONObject(purchaseData);
					walletBean.setGoogleOrderId(purchaseDataJson.optString("orderId", ""));//google订单号
					String efunDeveloperPayload = purchaseDataJson.optString("developerPayload");
					if (SStringUtil.isNotEmpty(efunDeveloperPayload)) {
						JSONObject developerPayloadJson = new JSONObject(efunDeveloperPayload);
						if (developerPayloadJson != null) {
							String orderId = developerPayloadJson.optString("orderId", "");//efun订单号
							SLog.logD("orderId:" + orderId);
							walletBean.setEfunOrderId(orderId);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					SLog.logW("purchaseData不能转化为jsonObject");
				}
			}
		}
	}

	*/
/**
	* <p>Title: clearPurchaseData</p>
	* <p>Description: </p>
	* @param payActivity
	*//*

	private void clearPurchaseData(final BasePayActivity payActivity) {
		SLog.logD("clear local wallet data");
		SharedPreferences preferences = payActivity.getSharedPreferences(GooglePayContant.EFUNFILENAME, Context.MODE_PRIVATE);
		preferences.edit().clear().commit();
	}

	*/
/**
	* <p>Title: serverBackProcess</p>
	* <p>Description: </p>
	* @param payActivity
	* @param mPurchaseListener
	* @param purchase
	* @param sku
	* @param json
	*//*

	private void serverBackProcess(final BasePayActivity payActivity, final OnIabPurchaseFinishedListener mPurchaseListener,
			final Purchase purchase, final String sku, final JSONObject json) {
		
		//gamePay
		clearPurchaseData(payActivity);
		
		if("true".equals(json.optString("isSign"))){//判断订单在服务器是否验证通过
			SLog.logD( "验证订单是否真实--->成功");
			
			if ("0000".equals(json.optString("result"))){//判断服务器返回的result结果是否为发放砖石成功
				
				setWalletBean(payActivity , json);
				if (mHandler != null) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//购买成功并且发放砖石回调
							IabResult result = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_OK, "Success");
							if (mPurchaseListener != null)
								mPurchaseListener.onIabPurchaseFinished(result, purchase);
						}
					});
				}
			}else{
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						SLog.logW("result is not 0000.");
						//服务器返回失败状态的回调
						IabResult iabResult = new IabResult(GooglePayContant.IAB_STATE, json.optString("msg", null));
						if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(iabResult, purchase);
					}
				});
			}
		}else {
			SLog.logW("verify is false.");
			
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					//订单验证失败的回调
					IabResult iabResult = new IabResult(IabHelper.IABHELPER_VERIFICATION_FAILED, "Signature verification failed for sku " + sku);
					if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(iabResult, purchase);
				}
			});
		}
	}
}
*/
