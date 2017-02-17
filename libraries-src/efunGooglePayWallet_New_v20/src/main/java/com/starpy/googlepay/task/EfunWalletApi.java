package com.starpy.googlepay.task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.core.base.http.HttpRequest;
import com.starpy.base.cfg.ResConfig;
import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.utils.SLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.EfunGooglePayService;
import com.starpy.googlepay.bean.GoogleOrderBean;
import com.starpy.googlepay.constants.EfunDomainSite;
import com.starpy.googlepay.util.EfunPayHelper;
import com.starpy.util.Purchase;

import android.content.Context;
import android.text.TextUtils;

public class EfunWalletApi {
	
	/**
	* <p>Title: createGoogleOrder</p>
	* <p>Description: 请求生成订单</p>
	* @return 服务器返回的内容
	*/
	public static String createGoogleOrder(final BasePayActivity context, GoogleOrderBean orderBean) {
		
		if (orderBean  == null) {
			throw new RuntimeException("请先初始化OrderBean");
		}
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Map<String, String> params = new HashMap<String, String>();
		String userId = orderBean.getUserId();
		String creditId = orderBean.getCreditId();
		String goodsId = orderBean.getSku();
		String moneyType = orderBean.getMoneyType();
		String serverCode = orderBean.getServerCode();
		String gameCode = orderBean.getGameCode();
		String payFrom = orderBean.getPayFrom();
		String payType = orderBean.getPayType();
		
		SLogUtil.logD("orderBean hashCode:" + orderBean.hashCode() + ",userId:" + userId + ",creditId:" + creditId + ",sku:" + goodsId + ",moneyType:" +
		moneyType + ",serverCode:" + serverCode + ",gameCode:" + gameCode + ",payFrom:" + payFrom+ ",payType:" + payType);
		
		if (SStringUtil.isNotEmpty(userId)&& SStringUtil.isNotEmpty(creditId) && SStringUtil.isNotEmpty(goodsId)
				&& SStringUtil.isNotEmpty(moneyType) && SStringUtil.isNotEmpty(serverCode) && SStringUtil.isNotEmpty(gameCode)
				&& SStringUtil.isNotEmpty(payFrom)&& SStringUtil.isNotEmpty(payType)) {
			
			String localMacAddress = ApkInfoUtil.getMacAddress(context);
			String localImeiAddress = ApkInfoUtil.getImeiAddress(context);
			String localIpAddress = ApkInfoUtil.getLocalIpAddress(context);
			String localAndroidId = ApkInfoUtil.getAndroidId(context);
			
			params.put("mac", localMacAddress);
			params.put("imei", localImeiAddress);
			params.put("ip", localIpAddress);
			params.put("androidid", localAndroidId);
			
			params.put("userId", orderBean.getUserId());
			params.put("creditId", orderBean.getCreditId());
			params.put("goodsId", orderBean.getSku());
			params.put("moneyType", orderBean.getMoneyType());
			params.put("serverCode", orderBean.getServerCode());
			params.put("gameCode", orderBean.getGameCode());
			params.put("payFrom", orderBean.getPayFrom());
			params.put("payType", orderBean.getPayType());
			params.put("version", orderBean.getVersion());
			
			params.put("language", orderBean.getLanguage());
			params.put("remark", orderBean.getRemark());
			params.put("efunRole", orderBean.getEfunRole());
			params.put("efunLevel", orderBean.getEfunLevel());
			
			params.put("packageName", context.getPackageName());//添加包名参数
			params.put("versionCode", EfunPayHelper.getVersionCode(context));//添加包版本号参数
			params.put("gameVersion", EfunPayHelper.getVersionName(context));//添加包版本名称参数
			
			params.put("roleId", orderBean.getRoleId());
			params.put("googlepayversion", BasePayActivity.GOOGLE_PAY_VERSION);
			params.put("deviceSource", orderBean.getDeviceSource());
			params.put("activityCode", orderBean.getActivityCode());

			//SLogUtil.logI("玩家点击储值. params: " + params.toString());
			
			return doRequest(context, EfunDomainSite.EFUN_GOOGLE_PAY_CREATE_ORDER, params);
		} else {
			throw new RuntimeException("获取订单时设置的参数异常，请先设置好OrderBean参数");
		}
		
	}

	public static String sendStoneForPromoCode(Context context, String purchaseData, String dataSignature,GoogleOrderBean orderBean) {

		Map<String, String> pay = new HashMap<String, String>();
		//添加包名参数，以备日后使用
		pay.put("purchaseData", purchaseData);
		pay.put("dataSignature", dataSignature);
		
		pay.put("packageName", context.getPackageName());
		pay.put("versionCode", EfunPayHelper.getVersionCode(context));
		pay.put("gameVersion", EfunPayHelper.getVersionName(context));
		pay.put("googlepayversion", BasePayActivity.GOOGLE_PAY_VERSION);
		
		String userId = "";
		String gameCode = "";
		String serverCode = "";
		String creditId = "";
		String efunLevel = "";
		String efunName = "";
		
		if (orderBean != null) {
			userId = orderBean.getUserId();
			gameCode = orderBean.getGameCode();
			serverCode = orderBean.getServerCode();
			creditId = orderBean.getCreditId();
			efunLevel = orderBean.getEfunLevel();
			efunName = orderBean.getEfunRole();
			
		}else if (EfunGooglePayService.googleOrderBean != null) {
			userId = EfunGooglePayService.googleOrderBean.getUserId();
			gameCode = EfunGooglePayService.googleOrderBean.getGameCode();
			serverCode =  EfunGooglePayService.googleOrderBean.getServerCode();
			creditId = EfunGooglePayService.googleOrderBean.getRoleId();
			efunLevel = EfunGooglePayService.googleOrderBean.getEfunLevel();
			efunName = EfunGooglePayService.googleOrderBean.getEfunRole();
			
		}else{
			SLogUtil.logD("EfunGooglePayService.googleOrderBean == null");
			return "";
		}
		
		if (TextUtils.isEmpty(gameCode)) {
			gameCode = ResConfig.getGameCode(context);
		}
		
		if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(serverCode) || TextUtils.isEmpty(creditId) || TextUtils.isEmpty(gameCode)) {
			SLogUtil.logE("userId、serverCode、roleId不能为空");
			return "";
		}
		pay.put("userId", userId);
		pay.put("gameCode", gameCode);
		pay.put("serverCode", serverCode);
		pay.put("creditId", creditId);//角色id
		pay.put("efunLevel", efunLevel);
		pay.put("efunName", efunName);
		
		pay.put("uniqueKey", UUID.randomUUID().toString());
		
		SLogUtil.logD("兑换码发币");
		return doRequest(context, "dynamicPay_googleRedeem.shtml", pay);
		
	}

	/**
	* <p>Title: sendStoneForNormalPurchase 正常购买发游戏币</p>
	* <p>Description: 请求订单验证并发放游戏币</p>
	* 		String in JSON format similar to '{"orderId":"12999763169054705758.1371079406387615",
	* 		"packageName":"com.example.app", "productId":"exampleSku", "purchaseTime":1345678900000, 
	*  		"purchaseToken" : "122333444455555", "developerPayload":"example developer payload" }'
	* @return 服务器返回的内容
	*/
	public static String sendStoneForNormalPurchase(Context context,Map<String, String> requestMap) {
		
		if (requestMap == null || requestMap.isEmpty()) {
			return "";
		}
		requestMap.put("googlepayversion", BasePayActivity.GOOGLE_PAY_VERSION);
		requestMap.put("versionCode", EfunPayHelper.getVersionCode(context));
		requestMap.put("gameVersion", EfunPayHelper.getVersionName(context));
		requestMap.put("packageName", context.getPackageName());
		return doRequest(context, EfunDomainSite.EFUN_GOOGLE_PAY_PAY_STONE, requestMap);
	}
	
	
	/**
	* <p>Title: reportRefund</p>
	* <p>Description: 上报退款</p>
	* @param context
	* @param purchase
	* @return
	*/
	public static String reportRefund(final Context context, final Purchase purchase) {
		if (purchase == null) {
			return "";
		}
		//List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("dataSignature", purchase.getSignature());
		postParams.put("purchaseData", purchase.getOriginalJson());
		postParams.put("packageName", context.getPackageName());
		// http://pay.efuntw.com/googlePlay_logPint.shtml
		
		String efunResponse = doRequest(context, EfunDomainSite.EFUN_REPORT_REFUND, postParams);
		SLogUtil.logD("efun", "efunResponse:" + efunResponse);
		return efunResponse;

	}
	
	/**
	* <p>Title: doRequest</p>
	* <p>Description: 实际网络请求</p>
	* @param context
	* @param efunDomainSite
	* @param requestParams
	* @return
	*/
	public static String doRequest(final Context context, final String efunDomainSite,Map<String, String> requestParams){
		
		String preferredUrl = EfunPayHelper.getPreferredUrl(context);
		String spareUrl = EfunPayHelper.getSpareUrl(context);
		String efunResponse = "";
		if (SStringUtil.isNotEmpty(preferredUrl)) {
			preferredUrl = preferredUrl + efunDomainSite;
			SLogUtil.logD("preferredUrl:" + preferredUrl);
			efunResponse = HttpRequest.post(preferredUrl, requestParams);
			SLogUtil.logD("preferredUrl response: " + efunResponse);
		}
		if (SStringUtil.isEmpty(efunResponse) && SStringUtil.isNotEmpty(spareUrl)) {
			spareUrl = spareUrl + efunDomainSite;
			SLogUtil.logD("spareUrl Url: " + spareUrl);
			efunResponse = HttpRequest.post(spareUrl, requestParams);
			SLogUtil.logD("spareUrl response: " + efunResponse);
		}
		return efunResponse;
	}

}
