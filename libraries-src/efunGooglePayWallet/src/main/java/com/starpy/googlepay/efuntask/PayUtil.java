package com.starpy.googlepay.efuntask;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.ResUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.SConfig;
import com.starpy.base.utils.SLogUtil;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.googlepay.bean.WebPayReqBean;
import com.starpy.googlepay.constants.GooglePayContant;

public class PayUtil {

	private static final int SERCET_KEY = 50001;
	private static final int SERCET_KEY_GW = 50002;
	private static final int SERCET_KEY_GOODLIST = 50003;
	
	public static void startOtherWallet(Context context,WebPayReqBean webOrderBean){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY);
		startOtherWallet(context, webOrderBean, GooglePayContant.BillAction.EFUN_PAY_ACTIVITY + webOrderBean.getGameCode());
	}
	
	public static void startOtherWallet(Context context, WebPayReqBean webOrderBean, String intentAction){
		Intent otherPayIntent;
		checkWebOrderBean(context, webOrderBean, SERCET_KEY);
		if (SStringUtil.isEmpty(intentAction)) {
			otherPayIntent = new Intent(GooglePayContant.BillAction.EFUN_PAY_ACTIVITY + webOrderBean.getGameCode());
		} else {
			otherPayIntent = new Intent(intentAction);
		}
		doOtherWallet(context, webOrderBean, otherPayIntent);
	}
	
	public static void startOtherWallet(Context context, WebPayReqBean webOrderBean, Intent intent){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY);
		doOtherWallet(context, webOrderBean, intent);
	}

	
	/**
	* <p>Title: doOtherWallet</p>
	* <p>Description: </p>
	* @param context
	* @param webOrderBean
	* @param otherPayIntent
	*/
	private static void doOtherWallet(Context context, WebPayReqBean webOrderBean, Intent otherPayIntent) {
		String otherParams = buildWebPayUrlParams(context, webOrderBean, SERCET_KEY);
		otherPayIntent.putExtra(GooglePayContant.ExtraOtherKey, otherParams);
		SLogUtil.logD("otherPayParams:" + otherParams);
		otherPayIntent.putExtra(GooglePayContant.ExtraOtherWebOrderBean, webOrderBean);
		context.startActivity(otherPayIntent);
	}
	
	
	
	public static void startGWWallet(Context context, WebPayReqBean webOrderBean){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GW);
		startGWWallet(context, webOrderBean, GooglePayContant.BillAction.EFUN_PAY_ACTIVITY_GW + webOrderBean.getGameCode());
	}
	
	public static void startGWWallet(Context context, WebPayReqBean webOrderBean, String intentAction){
		Intent GWPayIntent;
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GW);
		if (SStringUtil.isEmpty(intentAction)) {
			GWPayIntent = new Intent(GooglePayContant.BillAction.EFUN_PAY_ACTIVITY_GW + webOrderBean.getGameCode());
		} else {
			GWPayIntent = new Intent(intentAction);
		}
		doGWWallet(context, webOrderBean, GWPayIntent);
	}
	public static void startGWWallet(Context context, WebPayReqBean webOrderBean, Intent intent){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GW);
		doGWWallet(context, webOrderBean, intent);
	}

	/**
	* <p>Title: doGWWallet</p>
	* <p>Description: </p>
	* @param context
	* @param webOrderBean
	* @param GWPayIntent
	*/
	private static void doGWWallet(Context context, WebPayReqBean webOrderBean, Intent GWPayIntent) {
		if ( null == GWPayIntent) {
			throw new RuntimeException("Intent is null");
		}
		String url = buildWebPayUrlParams(context, webOrderBean,SERCET_KEY_GW);
		GWPayIntent.putExtra(GooglePayContant.ExtraGWKey, url);
		GWPayIntent.putExtra(GooglePayContant.ExtraGWWebOrderBean, webOrderBean);
		SLogUtil.logD("GWPayParams:" + url);
		context.startActivity(GWPayIntent);
	}
	
	public static String buildGoogleGoodsUrl(Context context, WebPayReqBean webOrderBean){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GOODLIST);
		String url = buildWebPayUrlParams(context, webOrderBean,SERCET_KEY_GOODLIST);
		SLogUtil.logD("gw url:" + url);
		return url;
	}

	private static String buildWebPayUrlParams(Context context, WebPayReqBean webOrderBean, int urlMark) {
		if (null == webOrderBean) {
			throw new NullPointerException("webOrderBean is null");
		}
		StringBuffer sb = new StringBuffer();
		/*sb.append("gameCode=" + webOrderBean.getGameCode());
		sb.append("&serverCode=" + webOrderBean.getServerCode());
		sb.append("&userId=" + webOrderBean.getUserId());
		sb.append("&payFrom=" + webOrderBean.getPayFrom());
		sb.append("&time=" + webOrderBean.getTime());
		sb.append("&language=").append(webOrderBean.getGameLanguage());//tw
		sb.append("&appPlatFrom=").append(webOrderBean.getAppPlatFrom());
		
		sb.append("&remark=" + webOrderBean.getRemark());
		sb.append("&efunLevel=" + webOrderBean.getEfunLevel());
		sb.append("&efunRole=" + URLEncoder.encode(webOrderBean.getEfunRole()));
		sb.append("&roleId=" + webOrderBean.getRoleId());
		
		sb.append("&payType=").append(webOrderBean.getPayType());
		sb.append("&simOperator=" + webOrderBean.getSimOperator());
		sb.append("&phoneNumber=" + webOrderBean.getPhoneNumber());
		sb.append("&DCLversionCode=" + webOrderBean.getDCLversionCode());
		sb.append("&levelType=" + webOrderBean.getLevelType());
		sb.append("&cardData=" + webOrderBean.getCardData());*/
		
		appendMd5Str(webOrderBean, sb,urlMark);
		
		return sb.toString();
	}

	public static WebPayReqBean buildWebPayBean(Context context, String cpOrderId, String roleLevel, String extra, String url){
		WebPayReqBean webPayReqBean = new WebPayReqBean(context);
		webPayReqBean.setCompleteUrl(url);

		if (SStringUtil.isEmpty(webPayReqBean.getSimOperator())) {
			String simOperator = "";
			if (ApkInfoUtil.isWifiAvailable(context)) {
				simOperator = GooglePayContant.WIFI;
			} else {
				simOperator = ApkInfoUtil.getSimOperator(context);
			}
			webPayReqBean.setSimOperator(simOperator);
		}

		webPayReqBean.setUserId(StarPyUtil.getUid(context));
		webPayReqBean.setCpOrderId(cpOrderId);
		webPayReqBean.setRoleLevel(roleLevel);
		webPayReqBean.setExtra(extra);

		return webPayReqBean;

	}

	private static void appendMd5Str(WebPayReqBean webOrderBean, StringBuffer sb, int urlMark) {
		if (SERCET_KEY_GOODLIST == urlMark || SERCET_KEY_GW == urlMark) {
			sb.append("&md5Str="
					+ SStringUtil.toMd5( webOrderBean.getGameCode() +
							webOrderBean.getServerCode() + 
							webOrderBean.getRoleName() +
							webOrderBean.getRoleLevel() +
							webOrderBean.getRoleId()+
							webOrderBean.getUserId() + 
							webOrderBean.getPayFrom() + 
							webOrderBean.getTime() + 
							webOrderBean.getSecretKey(), false));
			
			SLogUtil.logD("md5Str:" + webOrderBean.getGameCode() +
					webOrderBean.getServerCode() +
					webOrderBean.getRoleName() +
					webOrderBean.getRoleLevel() +
					webOrderBean.getRoleId()+
					webOrderBean.getUserId() + 
					webOrderBean.getPayFrom() + 
					webOrderBean.getTime() + 
					webOrderBean.getSecretKey());
		} else if(SERCET_KEY == urlMark){
			sb.append("&payMethod=other");
			sb.append("&md5Str="
					+ SStringUtil.toMd5(webOrderBean.getGameCode() +
							webOrderBean.getServerCode() + 
							webOrderBean.getRoleId() +
							webOrderBean.getUserId() + 
							webOrderBean.getPayFrom() + 
							webOrderBean.getTime() + 
							webOrderBean.getSecretKey(), false));
		}
	}


	/**
	* <p>Title: checkWebOrderBean</p>
	* <p>Description: </p>
	* @param context
	* @param webOrderBean
	*/
	private static void checkWebOrderBean(Context context, WebPayReqBean webOrderBean, int pageMark){
		if (SStringUtil.isEmpty(webOrderBean.getGameCode())) {
			String gameCode = SConfig.getGameCode(context);
			if (SStringUtil.isEmpty(gameCode)) {
				throw new RuntimeException("请先配置好gamecode");
			}
			webOrderBean.setGameCode(gameCode);
		}

		if (SStringUtil.isEmpty(webOrderBean.getGameLanguage())) {
			webOrderBean.setGameLanguage(SConfig.getGameLanguage(context));
		}

		if (SStringUtil.isEmpty(webOrderBean.getSimOperator())) {
			String simOperator = "";
			if (ApkInfoUtil.isWifiAvailable(context)) {
				simOperator = GooglePayContant.WIFI;
			} else {
				simOperator = ApkInfoUtil.getSimOperator(context);
			}
			webOrderBean.setSimOperator(simOperator);
		}
		//添加对应的加密key
		checkSecretKey(webOrderBean, pageMark);
		//平台标识
		if (SStringUtil.isEmpty(webOrderBean.getAppPlatFrom())) {
			try {
				String appPlatform = context.getResources().getString(ResUtil.findStringIdByName(context, "efunAppPlatform"));
				webOrderBean.setAppPlatFrom(appPlatform);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("efun", "appPlatform is not find");
				webOrderBean.setAppPlatFrom("");
			}
		}
		SLogUtil.logD("webOrderBean:" + webOrderBean.toString());
	}

	private static void checkSecretKey(WebPayReqBean webOrderBean, int pageMark) {
		if (SStringUtil.isEmpty(webOrderBean.getSecretKey())) {
			if (SERCET_KEY_GOODLIST == pageMark) {
				webOrderBean.setSecretKey(GooglePayContant.SERCET_KEY_GOODLIST);
			} else if(SERCET_KEY_GW == pageMark){
				webOrderBean.setSecretKey(GooglePayContant.SERCET_KEY_GW);
			}else if(SERCET_KEY == pageMark){
				webOrderBean.setSecretKey(GooglePayContant.SERCET_KEY);
			}
		}
	}
	
}
