package com.starpy.googlepay.efuntask;

import org.json.JSONException;
import org.json.JSONObject;

import com.core.base.request.SRequestAsyncTask;
import com.core.base.utils.EfunJSONUtil;
import com.core.base.utils.EfunLogUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.GoogleOrderBean;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.util.IabHelper;
import com.starpy.util.IabHelper.QueryInventoryFinishedListener;
import com.starpy.util.IabResult;
import com.starpy.util.Inventory;
import com.starpy.util.Purchase;
import com.starpy.util.SkuDetails;

import android.text.TextUtils;

public class SAsyncPurchaseTask extends SRequestAsyncTask {

	private IabHelper mHelper;
	private BasePayActivity act;
	private GoogleOrderBean orderBean;
	private Prompt prompt;
//	private SkuDetails skuDetails;

	public SAsyncPurchaseTask(BasePayActivity basePayActivity) {
		this.act = basePayActivity;
		this.mHelper = basePayActivity.getHelper();
		this.orderBean = basePayActivity.get_orderBean();
		this.prompt = basePayActivity.getPrompt();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		prompt.showProgressDialog();
	}

	@Override
	protected String doInBackground(String... params) {
		String respone = EfunWalletApi.pay(act);
		EfunLogUtil.logI("click stored value result with " + respone);
		try {
			final String sku = orderBean.getSku();
			mHelper.efunQuerySkuDetails(sku, new QueryInventoryFinishedListener() {

				@Override
				public void onQueryInventoryFinished(IabResult result, Inventory inv) {
					if (result != null && result.isSuccess() && inv != null && inv.hasDetails(sku)) {
						EfunLogUtil.logD("SkuDetails:" + inv.getSkuDetails(sku).toString());
						SkuDetails skuDetails = inv.getSkuDetails(sku);
						if (skuDetails != null) {
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
			if (!TextUtils.isEmpty(mResult)) {
				final JSONObject json = new JSONObject(mResult);
				/*if (EfunJSONUtil.efunVerificationRequest(json)) {
					if ("0000".equals(json.optString("result", ""))) {
						launchPurchase(orderBean, json);
					} else {
						EndFlag.setEndFlag(true);
						prompt.dismissProgressDialog();
						String msg = json.optString("msg", "");
						if (!TextUtils.isEmpty(msg)) {
							prompt.complainCloseAct(msg);
						}
					}
				} else {
					EndFlag.setEndFlag(true);
					prompt.dismissProgressDialog();
					act.showGoogleServiceErrorMessage();
				}*/
				
				if (act != null) {
					act.getWalletBean().setEfunOrderId(json.optString("orderId", ""));
					act.getWalletBean().setErrorDesc(json.optString("ErrorDesc", ""));
					act.getWalletBean().setSkuId(orderBean.getSku());
				}
				if (EfunJSONUtil.efunVerificationRequest(json) && "0000".equals(json.optString("result", ""))) {
					launchPurchase(orderBean, json);
					return;
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		act.getWalletBean().setErrorType(1);
		EndFlag.setEndFlag(true);
        prompt.dismissProgressDialog();
        prompt.complain("create orderId error");
	}

	private void launchPurchase(GoogleOrderBean extraOrderBean, JSONObject resultJson) {
		extraOrderBean.setGgmid(resultJson.optString("ggmid", ""));//数据库记录
		extraOrderBean.setOrderId(resultJson.optString("orderId", ""));//efun订单号

		JSONObject mjson = new JSONObject();
		try {
			mjson.put("orderId", extraOrderBean.getOrderId());
			mjson.put("ggmid", extraOrderBean.getGgmid());
			mjson.put("userId", extraOrderBean.getUserId());
			mjson.put("payFrom", extraOrderBean.getPayFrom());
			mjson.put("payType", extraOrderBean.getPayType());
//			mjson.put("creditId", extraOrderBean.getCreditId());
//			mjson.put("moneyType", extraOrderBean.getMoneyType());
//			mjson.put("serverCode", extraOrderBean.getServerCode());
//			mjson.put("gameCode", extraOrderBean.getGameCode());
//			mjson.put("remark", extraOrderBean.getRemark());

//			if (skuDetails != null) {
//				mjson.put("priceCurrencyCode", skuDetails.getPrice_currency_code());
//				mjson.put("priceAmountMicros", skuDetails.getPrice_amount_micros());
//				mjson.put("price", skuDetails.getPrice());
//				mjson.put("productId", skuDetails.getSku());
//			}
			
		} catch (JSONException e) {
			EfunLogUtil.logI("JSONException异常");
			e.printStackTrace();
		}
		String developerPayload = mjson.toString();
		if (developerPayload.length() > 256) {
			EfunLogUtil.logW("developerPayload length > 256");
			developerPayload = developerPayload.substring(0, 256);
		}
		EfunLogUtil.logI("developerPayload: " + developerPayload + " developerPayload length:" + developerPayload.length());
		EfunLogUtil.logI("开始google购买流程launchPurchaseFlow");
		//developerPayload: optional argument to be sent back with the purchase information,最大256 characters.否则报错code:"IAB-DPTL" 
		mHelper.launchPurchaseFlow(act, extraOrderBean.getSku(),GooglePayContant.RC_REQUEST,
				new IabHelper.OnIabPurchaseFinishedListener() {
					public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {

						EfunLogUtil.logI("购买流程完毕并且回调onIabPurchaseFinished");
						if (purchase == null) {
							EfunLogUtil.logI("purchase is null.");
							EndFlag.setEndFlag(true);
							prompt.dismissProgressDialog();
							if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
								EfunLogUtil.logI("info: " + result.getMessage());
								if (act.isCloseActivityUserCancel()) {
									act.finish();
								}
								return;
							}
							/*if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED && 
									result.getMessage().contains("Unable to buy item")) {
								Log.i("efunLog",result.getMessage() + "");
								mHelper.flagEndAsync();
								EndFlag.setEndFlag(true);
								prompt.complain("This item is temporarily unavailable,please choose other items "
										+ "or try to clear GooglePay application cache data before you buy this item");
								return;
							}*/
							//prompt.complainCloseAct(result.getMessage());
							if (act.isCloseActivityUserCancel()) {
								prompt.complainCloseAct(result.getMessage());
							}else{
								prompt.complain(result.getMessage());
							}
							return;
						}
						//服务端订单验证失败（公密googleKey进行数据验证失败）
						if (result != null && result.getResponse() == IabHelper.IABHELPER_VERIFICATION_FAILED && null == result.getmEfunState()) {
							EfunLogUtil.logI("本次购买失败: " + result.getMessage());
							prompt.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							prompt.complainCloseAct(act.getEfunPayError().getEfunGoogleBuyFailError());
							return;
						}
						//请求验证订单的时候服务器超时或者返回结果失败
						if (null != result.getmEfunState() && GooglePayContant.IAB_STATE .equals(result.getmEfunState()) && null != result.getMessage()) {
							EfunLogUtil.logI("msg : " + result.getMessage());
							prompt.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							prompt.complainCloseAct(result.getMessage());
							return;
						}
						//购买成功开始消费
						if (result != null && result.isSuccess() && purchase.getPurchaseState() == 0) {
							// 消费
							mHelper.consumeAsync(purchase, mConsumeFinishedListener);
						} else {
							EfunLogUtil.logI("本次购买失败...");
							EndFlag.setEndFlag(true);
							prompt.dismissProgressDialog();
							if (act != null) {
								act.finish();
							}
						}
					}
				}, developerPayload);

	}

	private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (result.isSuccess()) {
				EfunLogUtil.logI("消费成功");
			} else {
				EfunLogUtil.logI("消费失败");
			}
			EndFlag.setEndFlag(true);
			prompt.dismissProgressDialog();
			if (act != null) {
				act.finish();
			}
		}
	};
}
