package com.starpy.googlepay.efuntask;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.starpy.base.task.SRequestAsyncTask;
import com.starpy.base.utils.EfunLogUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.GoogleOrderBean;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.util.EfunPayHelper;
import com.starpy.util.IabHelper;
import com.starpy.util.IabHelper.QueryInventoryFinishedListener;
import com.starpy.util.IabResult;
import com.starpy.util.Inventory;
import com.starpy.util.Purchase;
import com.starpy.util.SkuDetails;

import android.content.Context;
import android.text.TextUtils;

public class SAsyncPurchaseTask extends SRequestAsyncTask {

	private IabHelper mHelper;
	private BasePayActivity act;
	private GoogleOrderBean orderBean;
	private PayPrompt prompt;
	SkuDetails skuDetails;

	public SAsyncPurchaseTask(BasePayActivity basePayActivity, IabHelper iabHelper, GoogleOrderBean gob) {
		this.act = basePayActivity;
		this.mHelper = iabHelper;
		this.orderBean = gob;
		this.prompt = basePayActivity.getPayPrompt();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.mHelper == null) {
			throw new NullPointerException("mHelper is null,must not be null");
		}
		if (act != null) {
			act.setLaunching(true);
		}
		act.getLaunchPurchaseDialog().showProgressDialog();
	}

	@Override
	protected String doInBackground(String... params) {
		String respone = EfunWalletApi.createGoogleOrder(act,orderBean);
		if (act == null || act.isPurchaseCancel()) {
			return "";
		}
		EfunLogUtil.logD("click stored value result with " + respone);
		try {
			final String sku = orderBean.getSku();
			mHelper.efunQuerySkuDetails(sku, new QueryInventoryFinishedListener() {

				@Override
				public void onQueryInventoryFinished(IabResult result, Inventory inv) {
					if (result != null && result.isSuccess() && inv != null && inv.hasDetails(sku)) {
						EfunLogUtil.logD("SkuDetails:" + inv.getSkuDetails(sku).toString());
						skuDetails = inv.getSkuDetails(sku);
						if (act != null && skuDetails != null) {
							act.setSkuDetails(skuDetails);
						}
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		EfunLogUtil.logD("respone return");
		return respone;
	}

	@Override
	protected void onPostExecute(String mResult) {
		super.onPostExecute(mResult);
		
		try {
			if (act != null && !act.isPurchaseCancel() && !TextUtils.isEmpty(mResult) && orderBean != null) {
				JSONObject json = new JSONObject(mResult);
				if ("0000".equals(json.optString("result", ""))) {
					orderBean.setGgmid(json.optString("ggmid", ""));// 数据库记录
					orderBean.setOrderId(json.optString("orderId", ""));// efun订单号

					JSONObject developerPayloadJson = new JSONObject();
					
					developerPayloadJson.put("orderId", orderBean.getOrderId());
					developerPayloadJson.put("ggmid", orderBean.getGgmid());
					developerPayloadJson.put("userId", orderBean.getUserId());
					developerPayloadJson.put("payType", orderBean.getPayType());
					// mjson.put("creditId", extraOrderBean.getCreditId());
					developerPayloadJson.put("serverCode", orderBean.getServerCode());
					developerPayloadJson.put("gameCode", orderBean.getGameCode());
					String developerPayload = developerPayloadJson.toString();
					// developerPayload: optional argument to be sent back with the purchase
					// information,最大256 characters.否则报错code:"IAB-DPTL"
					if (developerPayload.length() > 256) {// developerPayload长度不能大于256
						EfunLogUtil.logW("developerPayload length > 256");
						developerPayload = developerPayload.substring(0, 256);
					}
					EfunLogUtil.logD("developerPayload: " + developerPayload + " developerPayload length:" + developerPayload.length());
					
					if (!TextUtils.isEmpty(orderBean.getSku()) && !TextUtils.isEmpty(orderBean.getOrderId())) {
						launchPurchase(orderBean.getSku(), orderBean.getOrderId(),developerPayload);// 開始購買
						return;
					}
				}
			}
		} catch (JSONException e) {
			EfunLogUtil.logD(e.getStackTrace().toString());
			e.printStackTrace();
		}
		if (act == null || act.isPurchaseCancel()) {
			return;
		}
		
		if (act != null) {
			act.setLaunching(false);
			act.getLaunchPurchaseDialog().dismissProgressDialog();
			prompt.complain("create orderId error");
		}
	}

	private void launchPurchase(String purchaseSku,String efunOrderId,String developerPayload) {
	
	
		EfunLogUtil.logD("开始google购买流程launchPurchaseFlow");
		
		if (act == null || act.isPurchaseCancel()) {
			return;
		}
		
		mHelper.launchPurchaseFlow(act, purchaseSku, efunOrderId, GooglePayContant.RC_REQUEST, new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {
				if (act != null) {
					act.getLaunchPurchaseDialog().dismissProgressDialog();
					act.setLaunching(false);
				}
				EfunLogUtil.logD("购买流程完毕并且回调onIabPurchaseFinished");
				if (purchase == null) {
					EfunLogUtil.logD("purchase is null.");
					prompt.dismissProgressDialog();
					if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {//用户取消购买
						EfunLogUtil.logD("IABHELPER_USER_CANCELLED: " + result.getMessage());
						if (act != null) {
							act.determineCloseActivity();//判断是否需要关闭activity
						}
						return;
					} 
					if (act != null) {
						act.processPayCallBack(result.getResponse());// 储值回调
					}
					EfunLogUtil.logD("iabResult:" + result.getMessage());
					if (TextUtils.isEmpty(result.getMessage())) {
						prompt.complainCloseAct("error");
					}else{
						prompt.complainCloseAct(result.getMessage());
					}
					return;
				}
				
				
				// 服务端订单验证失败（公密googleKey进行数据验证失败）
				else if (result != null && result.getResponse() == IabHelper.IABHELPER_VERIFICATION_FAILED) {
					EfunLogUtil.logD("本次购买失败: " + result.getMessage());
					prompt.dismissProgressDialog();
					prompt.complainCloseAct(act.getEfunPayError().getEfunGoogleBuyFailError());
					return;
				}

				// 购买成功开始消费
				else if (result != null && result.isSuccess() && purchase.getPurchaseState() == 0) {
					// 消费
					//mHelper.consumeAsync(purchase, mConsumeFinishedListener);
					requestSendStone2(act, purchase, orderBean);
				} else {
					EfunLogUtil.logD("本次购买失败...");
					prompt.dismissProgressDialog();
					if (act != null) {
						act.finish();
					}
				}
			}
		}, developerPayload);

	}
	
	public void requestSendStone2(final Context context,final Purchase purchase,GoogleOrderBean googleOrderBean){

		  EfunLogUtil.logD( "start request send stone");
		  
		  new SRequestAsyncTask() {
			
			@Override
			protected String doInBackground(String... params) {
				//请求两次，防止网络问题调单
				if(EfunPayHelper.googlePayType(purchase.getOriginalJson()) == 1){//促销码兑换发币
					String respone = EfunWalletApi.sendStoneForPromoCode(context, purchase.getOriginalJson(), purchase.getSignature(),orderBean);
					if (TextUtils.isEmpty(respone)) {
						respone = EfunWalletApi.sendStoneForPromoCode(context, purchase.getOriginalJson(), purchase.getSignature(),orderBean);
					}
					return respone;
				}else{//正常储值
					
					
					Map<String, String> requestMap = new HashMap<String, String>();
					//添加包名参数，以备日后使用
					
					requestMap.put("purchaseData", purchase.getOriginalJson());
					requestMap.put("dataSignature", purchase.getSignature());
					
					if (null != orderBean) {
						requestMap.put("language", orderBean.getLanguage());
						requestMap.put("version", orderBean.getVersion());

						requestMap.put("creditId", orderBean.getCreditId());
						requestMap.put("moneyType", orderBean.getMoneyType());
						requestMap.put("serverCode", orderBean.getServerCode());
						requestMap.put("gameCode", orderBean.getGameCode());
						requestMap.put("remark", orderBean.getRemark());
						requestMap.put("roleId", orderBean.getRoleId());

					}
					if (skuDetails != null) {
						requestMap.put("priceCurrencyCode", skuDetails.getPrice_currency_code());
						requestMap.put("priceAmountMicros", skuDetails.getPrice_amount_micros());
						requestMap.put("price", skuDetails.getPrice());
						//requestMap.put("productId", skuDetails.getSku()));
					}
					//请求两次，防止网络问题调单
					String respone = EfunWalletApi.sendStoneForNormalPurchase(context, requestMap);// 请求服务器验证购买的订单并且是否发放砖石
					if (TextUtils.isEmpty(respone)) {
						 respone = EfunWalletApi.sendStoneForNormalPurchase(context, requestMap);// 请求服务器验证购买的订单并且是否发放砖石
					}
					return respone;
				}
			}
			
			@Override
			protected void onPostExecute(String respone) {
				// TODO Auto-generated method stub
				super.onPostExecute(respone);
				EfunLogUtil.logD("respone返回：" + respone);
				EfunLogUtil.logD("orderBean:" + orderBean.toString());
				orderBean = null;
				if (!TextUtils.isEmpty(respone)) {

					try {
						JSONObject responseJson = new JSONObject(respone);
						EfunPayRequest.clearPurchaseData(act);
						if ("0000".equals(responseJson.optString("result","")) || "0000".equals(responseJson.optString("resultCode",""))) {// 判断服务器返回的result结果是否为发放砖石成功
							EfunLogUtil.logD("buy order server result:0000. send stone success");
							// 发送游戏币成功回调
							EfunPayRequest.setWalletBean(act, responseJson);
							EfunLogUtil.logD("order success,send stone,began to consume");
							if (mHelper != null) {
								mHelper.consumeAsync(purchase, mConsumeFinishedListener);
								return;
							}
						} else {//发送砖石失败

							EfunLogUtil.logW("result is not 0000.send stone fail");
							String message = responseJson.optString("message", "");
							if (TextUtils.isEmpty(message)) {
								message = responseJson.optString("msg", "");
								prompt.complainCloseAct(message);
								return;
							}
							EfunLogUtil.logD("error msg:" + message);

						}
					} catch (JSONException e) {
						EfunLogUtil.logW("请求发币server response json异常");
						e.printStackTrace();
					}

				}else{
					EfunLogUtil.logW("网络超时");
					prompt.complainCloseAct("Network timeout");
					return;
				}
				
				prompt.dismissProgressDialog();
				if (act != null) {
					act.finish();
				}
			}
			
		}.asyncExcute();
		
	}
	

	private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			
			if (result.isSuccess()) {
				EfunLogUtil.logD("消费成功");
			} else {
				EfunLogUtil.logD("消费失败");
			}
			prompt.dismissProgressDialog();
			if (act != null) {
				act.finish();
			}
		}
	};
}
