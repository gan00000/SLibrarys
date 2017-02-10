package com.starpy.googlepay.efuntask;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.core.base.task.SRequestAsyncTask;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.EfunGooglePayService;
import com.starpy.googlepay.bean.EfunQueryInventoryState;
import com.starpy.googlepay.bean.EfunWalletBean;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.util.EfunPayHelper;
import com.starpy.util.IabHelper;
import com.starpy.util.IabResult;
import com.starpy.util.Purchase;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
* <p>Title: EfunVerifyTask</p>
* <p>Description: 请求发放砖石验证订单类</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013年11月25日
*/
public class EfunPayRequest {
	
	
	/**
	* <p>Title: verifyOrder</p>
	* <p>Description: 购买商品的时候进行服务器端验证</p>
	* @param payActivity
	* @param mPurchaseListener 购买完成后的回调
	* @param purchaseData	google返回的purchaseData
	* @param dataSignature  google返回的dataSignature
	* @param mPurchasingItemType 内购商品的类型
	*/
	public static void requestSendStone2222222(final Context context, final Purchase purchase, final IabHelper.OnIabPurchaseFinishedListener mPurchaseListener){

		  EfunLogUtil.logD( "start request send stone");
		  
		  new SRequestAsyncTask() {
			
			@Override
			protected String doInBackground(String... params) {/*
				if(EfunPayHelper.googlePayType(purchase.getOriginalJson()) == 1){
					return EfunWalletApi.sendStoneForPromoCode(context, purchase.getOriginalJson(), purchase.getSignature());
				}
				return EfunWalletApi.exchage(context, purchase.getOriginalJson(), purchase.getSignature());// 请求服务器验证购买的订单并且是否发放砖石
			*/
				return "";
				}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				EfunLogUtil.logD("result返回：" + result);
				if (!TextUtils.isEmpty(result)) {

					try {
						JSONObject json = new JSONObject(result);

						serverResponseProcess(context, mPurchaseListener, purchase, json);
						return;
					} catch (JSONException e) {
						EfunLogUtil.logW("EfunVerifyUtil json异常");
						e.printStackTrace();
					}
					
				} 

				// server not connect
				EfunLogUtil.logW("do not clear local purchaseData,server not connect.");
				EfunLogUtil.logW("server connect error.");
				// 网络连接超时的回调
				IabResult iabResult = new IabResult(GooglePayContant.EFUN_SERVER_RESPONE_FAIL,
						EfunGooglePayService.getPayActivity().getEfunPayError().getEfunGoogleServerError());
				if (mPurchaseListener != null)
					mPurchaseListener.onIabPurchaseFinished(iabResult, null);
			}
			
		}.asyncExcute();
		
	}
	
	public static boolean  requestSendStoneForQuery(final Context context, String purchaseData, String dataSignature){
		if(EfunPayHelper.googlePayType(purchaseData) == 1){
			return requestSendStoneForQueryInventoryOrder_PromoCode(context, purchaseData, dataSignature);
		}
		return requestSendStoneForQueryInventoryOrder(context, purchaseData, dataSignature);
	}
	
	/**
	* <p>Title: verifyQueryInventory</p>
	* <p>Description: 查询未消费商品的订单验证</p>
	* @param payActivity
	* @param purchaseData
	* @param dataSignature
	* @return
	*/
	private static boolean requestSendStoneForQueryInventoryOrder(final Context context, String purchaseData, String dataSignature) {

		Map<String, String> requestMap = new HashMap<String, String>();
		//添加包名参数，以备日后使用
		requestMap.put("packageName", context.getPackageName());
		requestMap.put("purchaseData", purchaseData);
		requestMap.put("dataSignature", dataSignature);
		requestMap.put("versionCode", EfunPayHelper.getVersionCode(context));
		requestMap.put("gameVersion", EfunPayHelper.getVersionName(context));
		
		String result = EfunWalletApi.sendStoneForNormalPurchase(context, requestMap);

		EfunLogUtil.logD("result返回：" + result);
		if (!TextUtils.isEmpty(result)) {
			try {
				JSONObject json = new JSONObject(result);
				checkAndClearData(context, json);
				if ("true".equals(json.optString("isSign"))) {// 判断订单是否验证成功
					EfunLogUtil.logD("query order sign:true");
				}
				if (json.optString("result", "").equals("0000")) {
					EfunLogUtil.logD("stone send success");
				}

			} catch (JSONException e) {
				EfunLogUtil.logW("query jsonexception...");
				e.printStackTrace();
			}

		}else {
			EfunLogUtil.logD("server connect timeout...");
			if (EfunGooglePayService.getPayActivity() != null) {
				EfunGooglePayService.getPayActivity().getQueryInventoryState().setQueryFailState(EfunQueryInventoryState.SERVER_TIME_OUT);
			}
		}
		return true;
	}
	

	/**
	 * <p>Description: 查询发送兑换码砖石</p>
	 * @param context
	 * @param purchaseData
	 * @param dataSignature
	 * @return
	 * @date 2016年1月14日
	 */
	public static boolean requestSendStoneForQueryInventoryOrder_PromoCode(final Context context,String purchaseData, String dataSignature) {
		String result =  EfunWalletApi.sendStoneForPromoCode(context, purchaseData, dataSignature,null);
		EfunLogUtil.logD( "result返回：" + result);
		if (!TextUtils.isEmpty(result)) {
			try {
				JSONObject json = new JSONObject(result);
				if (json.optString("resultCode", "").equals("0000")) {//判断订单是否验证成功
					EfunLogUtil.logD("query order sign:true;stone send success");
					return true;
				}else{
					EfunLogUtil.logD("error msg:" + json.optString("message", ""));
				}
			} catch (JSONException e) {
				EfunLogUtil.logW( "query jsonexception...");
				e.printStackTrace();
			}

		}else {
			EfunLogUtil.logD("server connect timeout...");
		}
		return false;
	}

	/**
	* <p>Title: checkAndClearData</p>
	* <p>Description: </p>
	* @param payActivity
	* @param json
	*/
	private static void checkAndClearData(final Context context, JSONObject json) {
		String efunPurchaseData = getPurchaseData(json);
		SharedPreferences preferences = context.getSharedPreferences(GooglePayContant.EFUNFILENAME, Context.MODE_PRIVATE);
		String localPurchaseData = preferences.getString(GooglePayContant.PURCHASE_DATA_ONE, "");
		EfunLogUtil.logD("efunPurchaseData:" + efunPurchaseData);
		EfunLogUtil.logD("localPurchaseData:" + localPurchaseData);
		if (efunPurchaseData.equals(localPurchaseData)) {
			EfunLogUtil.logD("query clear local purchaseData");
			preferences.edit().clear().commit();
		} else {
			EfunLogUtil.logD("query do not clear local purchaseData");
		}
	}

	private static String getPurchaseData(JSONObject json){
		return json.optString("purchaseData", "");
	}
	
	//服务端返回的数据如下
	// {"gamePay":0.99,"goodsId":"payone","isSign":"true","purchaseData":"{\"orderId\":\"12999763169054705758.1336697901909981\",\"packageName\":
	//\"com.ejoy.sg.efun\",\"productId\":\"payone\",\"purchaseTime\":1407921750854,\"purchaseState\":0,\"developerPayload\":\"{\\\"gameCode\\\":\\\"mmzb\\\",
	//\\\"ggmid\\\":\\\"3318412\\\",\\\"userId\\\":\\\"1\\\",\\\"serverCode\\\":\\\"999\\\",\\\"creditId\\\":\\\"2147583846\\\",\\\"moneyType\\\":\\\"USD\\\",
	//\\\"orderId\\\":\\\"MMZB1407921689177NUW\\\",\\\"payFrom\\\":\\\"efun\\\"}\",\"purchaseToken\":
	//\"kalbplcndghhdldogiicpkik.AO-J1Ox6pHtyVQlCjZ0h0VmdD1JM8nDpNyCobT-R-37WXHyICP64GIkF8esliE2MGdC8eZ1YHY01riQsZ_NSbBR6VlBjCI2E4kg7u1Waa1Plfw311tWP3q8\"}","result":"0000"}

	public static void setWalletBean(final Context context, final JSONObject json){
		
		if (EfunGooglePayService.getPayActivity() == null) {
			return;
		}
		EfunWalletBean walletBean = EfunGooglePayService.getPayActivity().getWalletBean();
		if (null != walletBean) {
			walletBean.setItemPrice(json.optString("gamePay", ""));//商品价格
			walletBean.setSkuPrice(json.optString("gamePay", ""));//商品价格
			walletBean.setPurchaseState(GooglePayContant.PURCHASESUCCESS);//购买是否成功标识
			walletBean.setItemName(json.optString("goodsId", ""));//商品ID
			walletBean.setSkuId(json.optString("goodsId", ""));//商品ID
			walletBean.setCurrencyType(GooglePayContant.MONEY_TYPE);//货币类型
			walletBean.setItemNum("1");//购买的商品个数
			walletBean.setGoogleResponeCode(IabHelper.BILLING_RESPONSE_RESULT_OK);
			String purchaseData = getPurchaseData(json);
			if (SStringUtil.isNotEmpty(purchaseData)) {
				//			EfunLogUtil.logD("purchaseData处理后：" + purchaseData);
				try {
					JSONObject purchaseDataJson = new JSONObject(purchaseData);
					walletBean.setGoogleOrderId(purchaseDataJson.optString("orderId", ""));//google订单号
					String efunDeveloperPayload = purchaseDataJson.optString("developerPayload");
					if (SStringUtil.isNotEmpty(efunDeveloperPayload)) {
						JSONObject developerPayloadJson = new JSONObject(efunDeveloperPayload);
						if (developerPayloadJson != null) {
							String orderId = developerPayloadJson.optString("orderId", "");//efun订单号
							EfunLogUtil.logD("orderId:" + orderId);
							walletBean.setEfunOrderId(orderId);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					EfunLogUtil.logW("purchaseData不能转化为jsonObject");
				}
			}
		}
	}

	/**
	* <p>Title: clearPurchaseData</p>
	* <p>Description: </p>
	* @param payActivity
	*/
	public static void clearPurchaseData(final Context context) {
		EfunLogUtil.logD("clear local wallet data");
		SharedPreferences preferences = context.getSharedPreferences(GooglePayContant.EFUNFILENAME, Context.MODE_PRIVATE);
		preferences.edit().clear().commit();
	}

	
	/**
	 * <p>Description: 处理服务端返回的json格式数据</p>
	 * @param context
	 * @param mPurchaseListener
	 * @param purchase
	 * @param responseJson
	 * @date 2016年1月14日
	 */
	private static void serverResponseProcess(final Context context, final IabHelper.OnIabPurchaseFinishedListener mPurchaseListener, final Purchase purchase,
											  final JSONObject responseJson) {

		// gamePay
		clearPurchaseData(context);//清空本地保存的订单数据

		//if ("true".equals(responseJson.optString("isSign",""))) {// 判断订单在服务器是否验证通过
		//	EfunLogUtil.logD("buy order server sign:true");
			if ("0000".equals(responseJson.optString("result","")) || "0000".equals(responseJson.optString("resultCode",""))) {// 判断服务器返回的result结果是否为发放砖石成功
				EfunLogUtil.logD("buy order server result:0000. send stone success");
				setWalletBean(context, responseJson);

				// 购买成功并且发放砖石回调
				EfunLogUtil.logD("order success,send stone,began to consume");
				IabResult result = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_OK, "Success");
				if (mPurchaseListener != null)
					mPurchaseListener.onIabPurchaseFinished(result, purchase);

			} else {//发送砖石失败

				EfunLogUtil.logW("result is not 0000.send stone fail");
				String message = responseJson.optString("message", "");
				if (TextUtils.isEmpty(message)) {
					message = responseJson.optString("msg", "");
				}
				EfunLogUtil.logD("error msg:" + message);
				// 服务器返回失败状态的回调
				IabResult iabResult = new IabResult(GooglePayContant.EFUN_SERVER_RESPONE_FAIL, message);
				if (mPurchaseListener != null)
					mPurchaseListener.onIabPurchaseFinished(iabResult, null);

			}
//		} else {
//			EfunLogUtil.logW("verify is false.");
//
//			// 订单验证失败的回调
//			IabResult iabResult = new IabResult(IabHelper.IABHELPER_VERIFICATION_FAILED,
//					"Signature verification failed for sku " + purchase.getSku());
//			if (mPurchaseListener != null)
//				mPurchaseListener.onIabPurchaseFinished(iabResult, purchase);
//
//		}
	}
}
