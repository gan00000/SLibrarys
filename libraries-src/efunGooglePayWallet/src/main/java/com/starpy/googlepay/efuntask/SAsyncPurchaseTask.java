package com.starpy.googlepay.efuntask;

import org.json.JSONException;
import org.json.JSONObject;

import com.core.base.request.SRequestAsyncTask;
import com.starpy.base.utils.SLogUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.GooglePayReqBean;
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
	private GooglePayReqBean orderBean;
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
		SLogUtil.logI("click stored value result with " + respone);
		try {
			final String sku = orderBean.getProductId();
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
					act.getWalletBean().setErrorDesc(json.optString("ErrorDesc", ""));
					act.getWalletBean().setSkuId(orderBean.getProductId());
				}
				if ("0000".equals(json.optString("result", ""))) {
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

	private void launchPurchase(GooglePayReqBean extraOrderBean, JSONObject resultJson) {
		extraOrderBean.setOrderId(resultJson.optString("orderId", ""));//efun订单号

		JSONObject mjson = new JSONObject();
		try {
			mjson.put("orderId", extraOrderBean.getOrderId());
			mjson.put("cpOrderId", extraOrderBean.getCpOrderId());
			mjson.put("userId", extraOrderBean.getUserId());
			mjson.put("gameCode", extraOrderBean.getGameCode());
			mjson.put("sku", extraOrderBean.getProductId());
			mjson.put("serverCode", extraOrderBean.getServerCode());
			mjson.put("roleId", extraOrderBean.getRoleId());

		} catch (JSONException e) {
			SLogUtil.logI("JSONException异常");
			e.printStackTrace();
		}
		String developerPayload = mjson.toString();
		if (developerPayload.length() > 256) {
			SLogUtil.logW("developerPayload length > 256");
			developerPayload = developerPayload.substring(0, 256);
		}
		SLogUtil.logI("developerPayload: " + developerPayload + " developerPayload length:" + developerPayload.length());
		SLogUtil.logI("开始google购买流程launchPurchaseFlow");
		//developerPayload: optional argument to be sent back with the purchase information,最大256 characters.否则报错code:"IAB-DPTL" 
		mHelper.launchPurchaseFlow(act, extraOrderBean.getProductId(),GooglePayContant.RC_REQUEST,
				new IabHelper.OnIabPurchaseFinishedListener() {
					public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {

						SLogUtil.logI("购买流程完毕并且回调onIabPurchaseFinished");
						if (purchase == null) {
							SLogUtil.logI("purchase is null.");
							EndFlag.setEndFlag(true);
							prompt.dismissProgressDialog();
							if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
								SLogUtil.logI("info: " + result.getMessage());
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
			prompt.dismissProgressDialog();
			if (act != null) {
				act.finish();
			}
		}
	};
}
