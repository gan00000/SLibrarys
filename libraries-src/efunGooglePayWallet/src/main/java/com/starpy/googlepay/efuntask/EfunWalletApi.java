package com.starpy.googlepay.efuntask;

import java.util.HashMap;
import java.util.Map;

import com.core.base.http.HttpRequest;
import com.core.base.utils.EfunJSONUtil;
import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.SLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.GoogleOrderBean;
import com.starpy.googlepay.constants.EfunDomainSite;
import com.starpy.googlepay.util.EfunPayHelper;
import com.starpy.util.Purchase;
import com.starpy.util.SkuDetails;

public class EfunWalletApi {
	


	/**
	* <p>Title: pay</p>
	* <p>Description: 请求生成订单</p>
	* @param payActivity
	* @return 服务器返回的内容
	*/
	public static String pay(final BasePayActivity payActivity) {
		GoogleOrderBean orderBean = payActivity.get_orderBean();
		if (orderBean  == null) {
			throw new RuntimeException("请先初始化OrderBean");
		}
	//	List<NameValuePair> params = new ArrayList<NameValuePair>();
		Map<String, String> params = new HashMap<String, String>();
		String userId = orderBean.getUserId();
		String creditId = orderBean.getCreditId();
		String goodsId = orderBean.getSku();
		String moneyType = orderBean.getMoneyType();
		String serverCode = orderBean.getServerCode();
		String gameCode = orderBean.getGameCode();
		String payFrom = orderBean.getPayFrom();
		String payType = orderBean.getPayType();
		
		SLogUtil.logD("GoogleOrderBean hashCode:" + orderBean.hashCode() + ",userId:" + userId + ",creditId:" + creditId + ",sku:" + goodsId + ",moneyType:" +
		moneyType + ",serverCode:" + serverCode + ",gameCode:" + gameCode + ",payFrom:" + payFrom+ ",payType:" + payType);
		
		if (SStringUtil.isNotEmpty(userId)&& SStringUtil.isNotEmpty(creditId) && SStringUtil.isNotEmpty(goodsId)
				&& SStringUtil.isNotEmpty(moneyType) && SStringUtil.isNotEmpty(serverCode) && SStringUtil.isNotEmpty(gameCode)
				&& SStringUtil.isNotEmpty(payFrom)&& SStringUtil.isNotEmpty(payType)) {
			
			String localMacAddress = (null == ApkInfoUtil.getMacAddress(payActivity)?"": ApkInfoUtil.getMacAddress(payActivity));
			String localImeiAddress = (null == ApkInfoUtil.getImeiAddress(payActivity)?"": ApkInfoUtil.getImeiAddress(payActivity));
			String localIpAddress = (null == ApkInfoUtil.getLocalIpAddress(payActivity)?"": ApkInfoUtil.getLocalIpAddress(payActivity));
			String localAndroidId = (null == ApkInfoUtil.getAndroidId(payActivity)?"": ApkInfoUtil.getAndroidId(payActivity));
			
			params.put("mac", localMacAddress);
			params.put("imei", localImeiAddress);
			params.put("ip", localIpAddress);
			params.put("androidid", localAndroidId);
			
			params.put("userId", orderBean.getUserId());
			//params.put("creditId", orderBean.getCreditId());
			params.put("sku", orderBean.getSku());//google商品id
//			params.put("moneyType", orderBean.getMoneyType());
			params.put("serverCode", orderBean.getServerCode());
			params.put("gameCode", orderBean.getGameCode());
			params.put("payFrom", orderBean.getPayFrom());//web android
//			params.put("payType", orderBean.getPayType());//支付类型
//			params.put("version", orderBean.getVersion());

			params.put("language", orderBean.getLanguage());
			params.put("extra", orderBean.getRemark());
			params.put("roleName", orderBean.getEfunRole());
			params.put("roleLevel", orderBean.getEfunLevel());
			params.put("roleId", orderBean.getRoleId());
			params.put("cpOrderId", orderBean.getRoleId());//cp 订单号

			params.put("packageName", payActivity.getPackageName());//添加包名参数
			params.put("versionCode", EfunPayHelper.getVersionCode(payActivity));//添加包版本号参数
			params.put("versionName", EfunPayHelper.getVersionName(payActivity));//添加包版本名称参数

//			params.put("googlepayversion", BasePayActivity.GOOGLE_PAY_VERSION);
			params.put("accessToken", EfunPayHelper.getLoginSign(payActivity));
			
			SLogUtil.logI("玩家点击储值. params: " + params.toString());
			
			return EfunJSONUtil.efunTransformToJSONStr(doRequest(payActivity, EfunDomainSite.EFUN_GOOGLE_PAY_CREATE_ORDER, params));
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
		GoogleOrderBean googleOrderBean = payActivity.get_orderBean();
		
		//添加包名参数，以备日后使用
		verifyParams.put("packageName", payActivity.getPackageName());

		verifyParams.put("purchaseData", purchaseData);
		verifyParams.put("dataSignature", dataSignature);
		verifyParams.put("versionCode", EfunPayHelper.getVersionCode(payActivity));
		verifyParams.put("versionName", EfunPayHelper.getVersionName(payActivity));
//		verifyParams.put("googlepayversion", BasePayActivity.GOOGLE_PAY_VERSION);
		if (null != googleOrderBean) {
			verifyParams.put("language", googleOrderBean.getLanguage());
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
		

//		verifyParams.put("roleId", googleOrderBean.getRoleId());
		verifyParams.put("sign", EfunPayHelper.getLoginSign(payActivity));
		
		SLogUtil.logI("purchaseData: " + purchaseData);
		SLogUtil.logI("dataSignature: " + dataSignature);
		SLogUtil.logI("exchage params: " + verifyParams.toString());
		
//		return EfunJSONUtil.efunTransformToJSONStr(doRequest(payActivity, EfunDomainSite.EFUN_GOOGLE_PAY_SEND_STONE, verifyParams));
		return EfunJSONUtil.efunTransformToJSONStr(doRequest(payActivity, EfunDomainSite.EFUN_GOOGLE_PAY_PAY_STONE, verifyParams));
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
