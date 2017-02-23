package com.starpy.pay.gp.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.core.base.callback.ISReqCallBack;
import com.core.base.request.SRequestAsyncTask;
import com.core.base.utils.PL;
import com.starpy.base.utils.SLog;
import com.starpy.pay.gp.BasePayActivity;
import com.starpy.pay.gp.bean.req.GoogleExchangeReqBean;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.constants.GooglePayDomainSite;
import com.starpy.pay.gp.constants.GooglePayContant;
import com.starpy.pay.gp.util.IabHelper;
import com.starpy.pay.gp.util.IabHelper.QueryInventoryFinishedListener;
import com.starpy.pay.gp.util.IabResult;
import com.starpy.pay.gp.util.Inventory;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.pay.gp.util.Purchase;
import com.starpy.pay.gp.util.SkuDetails;

import android.text.TextUtils;

public class SAsyncPurchaseTask extends SRequestAsyncTask {

	private IabHelper mHelper;
	private BasePayActivity act;
	private GooglePayCreateOrderIdReqBean createOrderIdReqBean;
	private PayDialog payDialog;
//	private SkuDetails skuDetails;

	public SAsyncPurchaseTask(BasePayActivity basePayActivity) {
		this.act = basePayActivity;
		this.mHelper = basePayActivity.getHelper();
		this.createOrderIdReqBean = basePayActivity.getGoogleOrderBean();
		this.payDialog = basePayActivity.getPayDialog();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		payDialog.showProgressDialog();
	}

	@Override
	protected String doInBackground(String... params) {
		String respone = EfunWalletApi.pay(act,createOrderIdReqBean);
		SLog.logI("click stored value result with " + respone);
		try {
			final String sku = createOrderIdReqBean.getProductId();
			mHelper.efunQuerySkuDetails(sku, new QueryInventoryFinishedListener() {

				@Override
				public void onQueryInventoryFinished(IabResult result, Inventory inv) {
					if (result != null && result.isSuccess() && inv != null && inv.hasDetails(sku)) {
						SLog.logD("SkuDetails:" + inv.getSkuDetails(sku).toString());
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
		SLog.logD("respone return");
		return respone;
	}

	@Override
	protected void onPostExecute(String mResult) {
		super.onPostExecute(mResult);
		try {
			if (!TextUtils.isEmpty(mResult)) {
				final JSONObject json = new JSONObject(mResult);

				if ("1000".equals(json.optString("code", ""))) {
					launchPurchase(json);
					return;
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		EndFlag.setEndFlag(true);
        payDialog.dismissProgressDialog();
        payDialog.complainCloseAct("create orderId error");
	}

	private void launchPurchase(JSONObject resultJson) {
		String orderId = resultJson.optString("orderId", "");//efun订单号
		String paygpid = resultJson.optString("paygpid", "");

		JSONObject mjson = new JSONObject();
		try {
			mjson.put("orderId", orderId);
			mjson.put("cpOrderId", createOrderIdReqBean.getCpOrderId());
			mjson.put("userId", createOrderIdReqBean.getUserId());
			mjson.put("gameCode", createOrderIdReqBean.getGameCode());
//			mjson.put("productId", createOrderIdReqBean.getProductId());
			mjson.put("serverCode", createOrderIdReqBean.getServerCode());
			mjson.put("roleId", createOrderIdReqBean.getRoleId());

			mjson.put("paygpid", paygpid);

		} catch (JSONException e) {
			SLog.logI("JSONException异常");
			e.printStackTrace();
		}
		String developerPayload = mjson.toString();
		if (developerPayload.length() > 256) {
			PL.i("developerPayload.length() > 256");
		}
		SLog.logI("developerPayload: " + developerPayload + " developerPayload length:" + developerPayload.length());
		SLog.logI("开始google购买流程launchPurchaseFlow");
		//developerPayload: optional argument to be sent back with the purchase information,最大256 characters.否则报错code:"IAB-DPTL" 
		mHelper.launchPurchaseFlow(act, createOrderIdReqBean.getProductId(),GooglePayContant.RC_REQUEST,
				new IabHelper.OnIabPurchaseFinishedListener() {
					public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {

						SLog.logI("购买流程完毕并且回调onIabPurchaseFinished");

						if (purchase == null || result.isFailure()) {

							if (payDialog != null) {
								payDialog.dismissProgressDialog();
							}

							SLog.logI("purchase is null.");
							EndFlag.setEndFlag(true);

							if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
								SLog.logI("info: " + result.getMessage());
								if (act.isCloseActivityUserCancel()) {
									act.finish();
								}
								return;
							}else if (act.isCloseActivityUserCancel()) {
								payDialog.complainCloseAct(result.getMessage());
							}else{
								payDialog.complain(result.getMessage());
							}
							return;

						}else {
							GoogleExchangeReqBean exchangeReqBean = new GoogleExchangeReqBean(act);
							exchangeReqBean.setDataSignature(purchase.getSignature());
							exchangeReqBean.setPurchaseData(purchase.getOriginalJson());

							exchangeReqBean.setRequestUrl(PayHelper.getPreferredUrl(act));
							exchangeReqBean.setRequestMethod(GooglePayDomainSite.google_send);

							SkuDetails skuDetails = act.getSkuDetails();
							if (skuDetails != null) {
								exchangeReqBean.setPriceCurrencyCode(skuDetails.getPrice_currency_code());
								exchangeReqBean.setPriceAmountMicros(skuDetails.getPrice_amount_micros());
								exchangeReqBean.setProductPrice(skuDetails.getPrice());
							}

							GoogleExchangeReqTask googleExchangeReqTask = new GoogleExchangeReqTask(act,exchangeReqBean);
							googleExchangeReqTask.setReqCallBack(new ISReqCallBack() {
								@Override
								public void success(Object o, String rawResult) {

									PL.i("exchange callback");
									// 消费
									if (mHelper != null) {
										PL.i("google pay consumeAsync");
										mHelper.consumeAsync(purchase, mConsumeFinishedListener);
									}else{
										if (payDialog != null) {
											payDialog.dismissProgressDialog();
										}

									}
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

					/*
						//服务端订单验证失败（公密googleKey进行数据验证失败）
						if (result != null && result.getResponse() == IabHelper.IABHELPER_VERIFICATION_FAILED && null == result.getmEfunState()) {
							SLog.logI("本次购买失败: " + result.getMessage());
							payDialog.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							payDialog.complainCloseAct(act.getEfunPayError().getGoogleBuyFailError());
							return;
						}
						//请求验证订单的时候服务器超时或者返回结果失败
						if (null != result.getmEfunState() && GooglePayContant.IAB_STATE .equals(result.getmEfunState()) && null != result.getMessage()) {
							SLog.logI("msg : " + result.getMessage());
							payDialog.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							payDialog.complainCloseAct(result.getMessage());
							return;
						}
						//购买成功开始消费
						if (result != null && result.isSuccess() && purchase.getPurchaseState() == 0) {
							// 消费
							mHelper.consumeAsync(purchase, mConsumeFinishedListener);
						} else {
							SLog.logI("本次购买失败...");
							EndFlag.setEndFlag(true);
							payDialog.dismissProgressDialog();
							if (act != null) {
								act.finish();
							}
						}

						*/
					}
				}, developerPayload);

	}

	private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (result.isSuccess()) {
				SLog.logI("消费成功");
			} else {
				SLog.logI("消费失败");
			}
			EndFlag.setEndFlag(true);
			if (payDialog != null) {
				payDialog.dismissProgressDialog();
			}
			if (act != null) {
				act.finish();
			}
		}
	};
}
