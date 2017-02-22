package com.starpy.googlepay.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.core.base.callback.ISReqCallBack;
import com.core.base.request.SRequestAsyncTask;
import com.core.base.utils.PL;
import com.starpy.base.utils.SLogUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.GoogleExchangeReqBean;
import com.starpy.googlepay.bean.GooglePayCreateOrderIdReqBean;
import com.starpy.googlepay.constants.GooglePayDomainSite;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.util.IabHelper;
import com.starpy.googlepay.util.IabHelper.QueryInventoryFinishedListener;
import com.starpy.googlepay.util.IabResult;
import com.starpy.googlepay.util.Inventory;
import com.starpy.googlepay.util.PayHelper;
import com.starpy.googlepay.util.Purchase;
import com.starpy.googlepay.util.SkuDetails;

import android.text.TextUtils;

public class SAsyncPurchaseTask extends SRequestAsyncTask {

	private IabHelper mHelper;
	private BasePayActivity act;
	private GooglePayCreateOrderIdReqBean createOrderIdReqBean;
	private Prompt prompt;
//	private SkuDetails skuDetails;

	public SAsyncPurchaseTask(BasePayActivity basePayActivity) {
		this.act = basePayActivity;
		this.mHelper = basePayActivity.getHelper();
		this.createOrderIdReqBean = basePayActivity.getGoogleOrderBean();
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
		SLogUtil.logI("click stored value result with " + respone);
		try {
			final String sku = createOrderIdReqBean.getProductId();
			mHelper.efunQuerySkuDetails(sku, new QueryInventoryFinishedListener() {

				@Override
				public void onQueryInventoryFinished(IabResult result, Inventory inv) {
					if (result != null && result.isSuccess() && inv != null && inv.hasDetails(sku)) {
						SLogUtil.logD("SkuDetails:" + inv.getSkuDetails(sku).toString());
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
		SLogUtil.logD("respone return");
		return respone;
	}

	@Override
	protected void onPostExecute(String mResult) {
		super.onPostExecute(mResult);
		try {
			if (!TextUtils.isEmpty(mResult)) {
				final JSONObject json = new JSONObject(mResult);

				if (act != null) {
					act.getWalletBean().setEfunOrderId(json.optString("orderId", ""));
					act.getWalletBean().setProductId(createOrderIdReqBean.getProductId());
				}

				if ("1000".equals(json.optString("code", ""))) {
					launchPurchase(json);
					return;
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		act.getWalletBean().setErrorType(1);
		EndFlag.setEndFlag(true);
        prompt.dismissProgressDialog();
        prompt.complainCloseAct("create orderId error");
	}

	private void launchPurchase(JSONObject resultJson) {
		createOrderIdReqBean.setOrderId(resultJson.optString("orderId", ""));//efun订单号
		String paygpid = resultJson.optString("paygpid", "");

		JSONObject mjson = new JSONObject();
		try {
			mjson.put("orderId", createOrderIdReqBean.getOrderId());
			mjson.put("cpOrderId", createOrderIdReqBean.getCpOrderId());
			mjson.put("userId", createOrderIdReqBean.getUserId());
			mjson.put("gameCode", createOrderIdReqBean.getGameCode());
			mjson.put("productId", createOrderIdReqBean.getProductId());
			mjson.put("serverCode", createOrderIdReqBean.getServerCode());
			mjson.put("roleId", createOrderIdReqBean.getRoleId());

			mjson.put("paygpid", paygpid);

		} catch (JSONException e) {
			SLogUtil.logI("JSONException异常");
			e.printStackTrace();
		}
		String developerPayload = mjson.toString();
		if (developerPayload.length() > 256) {
			PL.i("developerPayload.length() > 256");
		}
		SLogUtil.logI("developerPayload: " + developerPayload + " developerPayload length:" + developerPayload.length());
		SLogUtil.logI("开始google购买流程launchPurchaseFlow");
		//developerPayload: optional argument to be sent back with the purchase information,最大256 characters.否则报错code:"IAB-DPTL" 
		mHelper.launchPurchaseFlow(act, createOrderIdReqBean.getProductId(),GooglePayContant.RC_REQUEST,
				new IabHelper.OnIabPurchaseFinishedListener() {
					public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {

						SLogUtil.logI("购买流程完毕并且回调onIabPurchaseFinished");
						if (prompt != null) {
							prompt.dismissProgressDialog();
						}

						if (purchase == null || result.isFailure()) {
							SLogUtil.logI("purchase is null.");
							EndFlag.setEndFlag(true);

							if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
								SLogUtil.logI("info: " + result.getMessage());
								if (act.isCloseActivityUserCancel()) {
									act.finish();
								}
								return;
							}else if (act.isCloseActivityUserCancel()) {
								prompt.complainCloseAct(result.getMessage());
							}else{
								prompt.complain(result.getMessage());
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
								exchangeReqBean.setPrice(skuDetails.getPrice());
							}

							GoogleExchangeReqTask googleExchangeReqTask = new GoogleExchangeReqTask(act,exchangeReqBean);
							googleExchangeReqTask.setReqCallBack(new ISReqCallBack() {
								@Override
								public void callBack(Object o, String rawResult) {
									// 消费
									if (mHelper != null) {
										mHelper.consumeAsync(purchase, mConsumeFinishedListener);
									}
								}
							});
							googleExchangeReqTask.excute();

						}

					/*
						//服务端订单验证失败（公密googleKey进行数据验证失败）
						if (result != null && result.getResponse() == IabHelper.IABHELPER_VERIFICATION_FAILED && null == result.getmEfunState()) {
							SLogUtil.logI("本次购买失败: " + result.getMessage());
							prompt.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							prompt.complainCloseAct(act.getEfunPayError().getGoogleBuyFailError());
							return;
						}
						//请求验证订单的时候服务器超时或者返回结果失败
						if (null != result.getmEfunState() && GooglePayContant.IAB_STATE .equals(result.getmEfunState()) && null != result.getMessage()) {
							SLogUtil.logI("msg : " + result.getMessage());
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
							SLogUtil.logI("本次购买失败...");
							EndFlag.setEndFlag(true);
							prompt.dismissProgressDialog();
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
				SLogUtil.logI("消费成功");
			} else {
				SLogUtil.logI("消费失败");
			}
			EndFlag.setEndFlag(true);
			if (prompt != null) {
				prompt.dismissProgressDialog();
			}
			if (act != null) {
				act.finish();
			}
		}
	};
}
