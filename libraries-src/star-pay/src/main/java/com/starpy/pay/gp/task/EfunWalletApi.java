package com.starpy.pay.gp.task;

import android.app.Activity;

import com.core.base.http.HttpRequest;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.SLog;
import com.starpy.pay.gp.BasePayActivity;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.constants.GooglePayDomainSite;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.pay.gp.util.Purchase;
import com.starpy.pay.gp.util.SkuDetails;

import java.util.HashMap;
import java.util.Map;

public class EfunWalletApi {
	
	/**
	* <p>Title: pay</p>
	* <p>Description: 请求生成订单</p>
	* @param payActivity
	* @return 服务器返回的内容
	*/
	public static String pay(final Activity payActivity,GooglePayCreateOrderIdReqBean googlePayCreateOrderIdReqBean) {
		if (googlePayCreateOrderIdReqBean  == null) {
			SLog.logE("请先初始化OrderBean");
			return "";
		}
		return doRequest(payActivity, GooglePayDomainSite.google_order_create, googlePayCreateOrderIdReqBean.fieldValueToMap());

		
	}

	public static String exchage(final BasePayActivity payActivity,String purchaseData, String dataSignature) {
		return exchage(payActivity, purchaseData, dataSignature, null);
	}
	
	/**
	* <p>Title: exchage</p>
	* <p>Description: 请求订单验证并发放游戏币</p>
	* @param payActivity Activity实例
	* @param purchaseData 购买成功返回的信息  
	* 		String in JSON format similar to '{"orderId":"12999763169054705758.1371079406387615",
	* 		"packageName":"com.example.app", "productId":"exampleSku", "purchaseTime":1345678900000, 
	*  		"purchaseToken" : "122333444455555", "developerPayload":"example developer payload" }'
	* @param dataSignature RSA签名信息
	* @param isSharedPreferencesMark 是否是保存本地的订单信息
	* @return 服务器返回的内容
	*/
	public static String exchage(final BasePayActivity payActivity,String purchaseData, String dataSignature, String isSharedPreferencesMark) {

		Map<String, String> verifyParams = new HashMap<String, String>();
		GooglePayCreateOrderIdReqBean googleOrderBean = payActivity.getGoogleOrderBean();
		
		//添加包名参数，以备日后使用
		verifyParams.put("packageName", payActivity.getPackageName());

		verifyParams.put("purchaseData", purchaseData);
		verifyParams.put("dataSignature", dataSignature);
		if (null != googleOrderBean) {
			verifyParams.put("language", googleOrderBean.getGameLanguage());

		}
		SkuDetails skuDetails = payActivity.getSkuDetails();
		if (skuDetails != null) {
			verifyParams.put("priceCurrencyCode", skuDetails.getPrice_currency_code());
			verifyParams.put("priceAmountMicros", skuDetails.getPrice_amount_micros());
			verifyParams.put("price", skuDetails.getPrice());
		}
		
		SLog.logI("purchaseData: " + purchaseData);
		SLog.logI("dataSignature: " + dataSignature);
		SLog.logI("exchage params: " + verifyParams.toString());
		
//		return JsonUtil.efunTransformToJSONStr(doRequest(payActivity, GooglePayDomainSite.EFUN_GOOGLE_PAY_SEND_STONE, verifyParams));
		return doRequest(payActivity, GooglePayDomainSite.EFUN_GOOGLE_PAY_PAY_STONE, verifyParams);
	}
	
	
	/**
	* <p>Title: reportRefund</p>
	* <p>Description: 上报退款</p>
	* @param payActivity
	* @param purchase
	* @return
	*/
	public static String reportRefund(final Activity payActivity, final Purchase purchase) {
		if (purchase == null) {
			return "";
		}
//		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("dataSignature", purchase.getSignature());
		postParams.put("purchaseData", purchase.getOriginalJson());
		postParams.put("packageName", payActivity.getPackageName());
		// http://pay.efuntw.com/googlePlay_logPint.shtml
		
		String efunResponse = doRequest(payActivity, GooglePayDomainSite.EFUN_REPORT_REFUND, postParams);
		SLog.logD("efun", "efunResponse:" + efunResponse);
		return efunResponse;

	}
	
	/**
	* <p>Title: doRequest</p>
	* <p>Description: 实际网络请求</p>
	* @param efunDomainSite
	* @param requestParams
	* @return
	*/
	public static String doRequest(final Activity activity, final String efunDomainSite, Map<String, String> requestParams){
		
		String preferredUrl = PayHelper.getPreferredUrl(activity);
		String spareUrl = PayHelper.getSpareUrl(activity);
		String sResponse = "";
		if (SStringUtil.isNotEmpty(preferredUrl)) {
			preferredUrl = preferredUrl + efunDomainSite;
			SLog.logD("preferredUrl:" + preferredUrl);
			//efunResponse = EfunHttpUtil.efunExecutePostRequest(preferredUrl, requestParams);
			sResponse = HttpRequest.post(preferredUrl,requestParams);
			SLog.logD("preferredUrl response: " + sResponse);
		}
		if (SStringUtil.isEmpty(sResponse) && SStringUtil.isNotEmpty(spareUrl)) {
			spareUrl = spareUrl + efunDomainSite;
			SLog.logD("spareUrl Url: " + spareUrl);
			//efunResponse = EfunHttpUtil.efunExecutePostRequest(spareUrl, requestParams);
			sResponse = HttpRequest.post(spareUrl,requestParams);
			SLog.logD("spareUrl response: " + sResponse);
		}
		return sResponse;
	}

}
