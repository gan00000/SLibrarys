package com.starpy.googlepay.efuntask;

import com.core.base.http.HttpRequest;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.SLogUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.GooglePayReqBean;
import com.starpy.googlepay.constants.EfunDomainSite;
import com.starpy.googlepay.util.EfunPayHelper;
import com.starpy.util.Purchase;
import com.starpy.util.SkuDetails;

import java.util.HashMap;
import java.util.Map;

public class EfunWalletApi {
	
	/**
	* <p>Title: pay</p>
	* <p>Description: 请求生成订单</p>
	* @param payActivity
	* @return 服务器返回的内容
	*/
	public static String pay(final BasePayActivity payActivity) {
		GooglePayReqBean orderBean = payActivity.get_orderBean();
		if (orderBean  == null) {
			throw new RuntimeException("请先初始化OrderBean");
		}

		Map<String, String> params = new HashMap<String, String>();
		String userId = orderBean.getUserId();
		String goodsId = orderBean.getSku();
		String serverCode = orderBean.getServerCode();
		String gameCode = orderBean.getGameCode();
		String payFrom = orderBean.getPayFrom();
		String payType = orderBean.getPayType();

		if (SStringUtil.isNotEmpty(userId) && SStringUtil.isNotEmpty(goodsId)
				&& SStringUtil.isNotEmpty(serverCode) && SStringUtil.isNotEmpty(gameCode)
				&& SStringUtil.isNotEmpty(payFrom)&& SStringUtil.isNotEmpty(payType)) {

			params.put("userId", orderBean.getUserId());
			params.put("sku", orderBean.getSku());//google商品id
			params.put("serverCode", orderBean.getServerCode());
			params.put("gameCode", orderBean.getGameCode());
			params.put("payFrom", orderBean.getPayFrom());//web android
			params.put("payType", orderBean.getPayType());//支付类型

			params.put("language", orderBean.getGameLanguage());
			params.put("extra", orderBean.getExtra());
			params.put("roleName", orderBean.getRoleName());
			params.put("roleLevel", orderBean.getRoleLevel());
			params.put("roleId", orderBean.getRoleId());
			params.put("cpOrderId", orderBean.getCpOrderId());//cp 订单号


			SLogUtil.logI("玩家点击储值. params: " + params.toString());
			
			return doRequest(payActivity, EfunDomainSite.EFUN_GOOGLE_PAY_CREATE_ORDER, params);
		} else {
			throw new RuntimeException("获取订单时设置的参数异常，请先设置好OrderBean参数");
		}
		
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
//		List<NameValuePair> verifyParams = new ArrayList<NameValuePair>();
		Map<String, String> verifyParams = new HashMap<String, String>();
		GooglePayReqBean googleOrderBean = payActivity.get_orderBean();
		
		//添加包名参数，以备日后使用
		verifyParams.put("packageName", payActivity.getPackageName());

		verifyParams.put("purchaseData", purchaseData);
		verifyParams.put("dataSignature", dataSignature);
//		verifyParams.put("googlepayversion", BasePayActivity.GOOGLE_PAY_VERSION);
		if (null != googleOrderBean) {
			verifyParams.put("language", googleOrderBean.getGameLanguage());
//			verifyParams.put("version", googleOrderBean.getVersion());
			
//			verifyParams.put("creditId", googleOrderBean.getCreditId());
//			verifyParams.put("moneyType", googleOrderBean.getMoneyType());
//			verifyParams.put("serverCode", googleOrderBean.getServerCode());
//			verifyParams.put("gameCode", googleOrderBean.getGameCode());
			//verifyParams.put("payFrom", googleOrderBean.getPayFrom());
//			verifyParams.put("remark", googleOrderBean.getRemark());

		}
		
		SkuDetails skuDetails = payActivity.getSkuDetails();
		if (skuDetails != null) {
			verifyParams.put("priceCurrencyCode", skuDetails.getPrice_currency_code());
			verifyParams.put("priceAmountMicros", skuDetails.getPrice_amount_micros());
			verifyParams.put("price", skuDetails.getPrice());
			//verifyParams.put("productId", skuDetails.getSku()));
		}
		
		SLogUtil.logI("purchaseData: " + purchaseData);
		SLogUtil.logI("dataSignature: " + dataSignature);
		SLogUtil.logI("exchage params: " + verifyParams.toString());
		
//		return JsonUtil.efunTransformToJSONStr(doRequest(payActivity, EfunDomainSite.EFUN_GOOGLE_PAY_SEND_STONE, verifyParams));
		return doRequest(payActivity, EfunDomainSite.EFUN_GOOGLE_PAY_PAY_STONE, verifyParams);
	}
	
	
	/**
	* <p>Title: reportRefund</p>
	* <p>Description: 上报退款</p>
	* @param payActivity
	* @param purchase
	* @return
	*/
	public static String reportRefund(final BasePayActivity payActivity, final Purchase purchase) {
		if (purchase == null) {
			return "";
		}
//		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("dataSignature", purchase.getSignature());
		postParams.put("purchaseData", purchase.getOriginalJson());
		postParams.put("packageName", payActivity.getPackageName());
		// http://pay.efuntw.com/googlePlay_logPint.shtml
		
		String efunResponse = doRequest(payActivity, EfunDomainSite.EFUN_REPORT_REFUND, postParams);
		SLogUtil.logD("efun", "efunResponse:" + efunResponse);
		return efunResponse;

	}
	
	/**
	* <p>Title: doRequest</p>
	* <p>Description: 实际网络请求</p>
	* @param payActivity
	* @param efunDomainSite
	* @param requestParams
	* @return
	*/
	public static String doRequest(final BasePayActivity payActivity, final String efunDomainSite,Map<String, String> requestParams){
		
		String preferredUrl = EfunPayHelper.getPreferredUrl(payActivity);
		String spareUrl = EfunPayHelper.getSpareUrl(payActivity);
		String efunResponse = "";
		if (SStringUtil.isNotEmpty(preferredUrl)) {
			preferredUrl = preferredUrl + efunDomainSite;
			SLogUtil.logD("preferredUrl:" + preferredUrl);
			//efunResponse = EfunHttpUtil.efunExecutePostRequest(preferredUrl, requestParams);
			efunResponse = HttpRequest.post(preferredUrl,requestParams);
			SLogUtil.logD("preferredUrl response: " + efunResponse);
		}
		if (SStringUtil.isEmpty(efunResponse) && SStringUtil.isNotEmpty(spareUrl)) {
			spareUrl = spareUrl + efunDomainSite;
			SLogUtil.logD("spareUrl Url: " + spareUrl);
			//efunResponse = EfunHttpUtil.efunExecutePostRequest(spareUrl, requestParams);
			efunResponse = HttpRequest.post(spareUrl,requestParams);
			SLogUtil.logD("spareUrl response: " + efunResponse);
		}
		return efunResponse;
	}

}
