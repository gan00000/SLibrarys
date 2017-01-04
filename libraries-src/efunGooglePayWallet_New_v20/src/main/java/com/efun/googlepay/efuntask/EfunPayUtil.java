package com.efun.googlepay.efuntask;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.efun.core.db.EfunDatabase;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunResourceUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.googlepay.BasePayActivity;
import com.efun.googlepay.bean.WebOrderBean;
import com.efun.googlepay.constants.GooglePayContant;
import com.efun.googlepay.util.EfunPayHelper;

public class EfunPayUtil {

	private static final int SERCET_KEY = 50001;
	private static final int SERCET_KEY_GW = 50002;
	private static final int SERCET_KEY_GOODLIST = 50003;
	
	public static void startOtherWallet(Context context,WebOrderBean webOrderBean){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY);
		startOtherWallet(context, webOrderBean, GooglePayContant.BillAction.EFUN_PAY_ACTIVITY + webOrderBean.getGameCode());
	}
	
	public static void startOtherWallet(Context context,WebOrderBean webOrderBean, String intentAction){
		Intent otherPayIntent;
		checkWebOrderBean(context, webOrderBean, SERCET_KEY);
		if (EfunStringUtil.isEmpty(intentAction)) {
			otherPayIntent = new Intent(GooglePayContant.BillAction.EFUN_PAY_ACTIVITY + webOrderBean.getGameCode());
		} else {
			otherPayIntent = new Intent(intentAction);
		}
		doOtherWallet(context, webOrderBean, otherPayIntent);
	}
	
	public static void startOtherWallet(Context context,WebOrderBean webOrderBean, Intent intent){
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
	private static void doOtherWallet(Context context, WebOrderBean webOrderBean, Intent otherPayIntent) {
		String otherParams = buildWebPayUrlParams(context, webOrderBean, SERCET_KEY);
		otherPayIntent.putExtra(GooglePayContant.ExtraOtherKey, otherParams);
		EfunLogUtil.logD("otherPayParams:" + otherParams);
		otherPayIntent.putExtra(GooglePayContant.ExtraOtherWebOrderBean, webOrderBean);
		context.startActivity(otherPayIntent);
	}
	
	
	
	public static void startGWWallet(Context context, WebOrderBean webOrderBean){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GW);
		startGWWallet(context, webOrderBean, GooglePayContant.BillAction.EFUN_PAY_ACTIVITY_GW + webOrderBean.getGameCode());
	}
	
	public static void startGWWallet(Context context, WebOrderBean webOrderBean,String intentAction){
		Intent GWPayIntent;
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GW);
		if (EfunStringUtil.isEmpty(intentAction)) {
			GWPayIntent = new Intent(GooglePayContant.BillAction.EFUN_PAY_ACTIVITY_GW + webOrderBean.getGameCode());
		} else {
			GWPayIntent = new Intent(intentAction);
		}
		doGWWallet(context, webOrderBean, GWPayIntent);
	}
	public static void startGWWallet(Context context, WebOrderBean webOrderBean,Intent intent){
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
	private static void doGWWallet(Context context, WebOrderBean webOrderBean, Intent GWPayIntent) {
		if ( null == GWPayIntent) {
			throw new RuntimeException("Intent is null");
		}
		String url = buildWebPayUrlParams(context, webOrderBean,SERCET_KEY_GW);
		GWPayIntent.putExtra(GooglePayContant.ExtraGWKey, url);
		GWPayIntent.putExtra(GooglePayContant.ExtraGWWebOrderBean, webOrderBean);
		EfunLogUtil.logD("GWPayParams:" + url);
		context.startActivity(GWPayIntent);
	}
	
	public static String buildGoogleGoodsUrl(Context context, WebOrderBean webOrderBean){
		checkWebOrderBean(context, webOrderBean, SERCET_KEY_GOODLIST);
		String url = buildWebPayUrlParams(context, webOrderBean,SERCET_KEY_GOODLIST);
		EfunLogUtil.logD("gw url:" + url);
		return url;
	}

	private static String buildWebPayUrlParams(Context context, WebOrderBean webOrderBean, int urlMark) {
		if (null == webOrderBean) {
			throw new NullPointerException("webOrderBean is null");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("gameCode=" + webOrderBean.getGameCode());
		sb.append("&serverCode=" + webOrderBean.getServerCode());
		sb.append("&creditId=" + webOrderBean.getCreditId());
		sb.append("&userId=" + webOrderBean.getUserId());
		sb.append("&payFrom=" + webOrderBean.getPayFrom());
		sb.append("&time=" + webOrderBean.getTime());
		sb.append("&language=").append(webOrderBean.getLanguage());//tw
		sb.append("&vh=").append(webOrderBean.getVh());
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
		sb.append("&cardData=" + webOrderBean.getCardData());
		
		appendLocalInfo(context, sb);
		
		appendMd5Str(webOrderBean, sb,urlMark);
		
		return sb.toString();
	}

	private static void appendMd5Str(WebOrderBean webOrderBean, StringBuffer sb,int urlMark) {
		if (SERCET_KEY_GOODLIST == urlMark || SERCET_KEY_GW == urlMark) {
			sb.append("&md5Str="
					+ EfunStringUtil.toMd5( webOrderBean.getGameCode() + 
							webOrderBean.getServerCode() + 
							webOrderBean.getEfunRole() + 
							webOrderBean.getEfunLevel() + 
							webOrderBean.getCreditId()+ 
							webOrderBean.getUserId() + 
							webOrderBean.getPayFrom() + 
							webOrderBean.getTime() + 
							webOrderBean.getSecretKey(), false));
			
			EfunLogUtil.logD("md5Str:" + webOrderBean.getGameCode() + 
					webOrderBean.getServerCode() + 
					webOrderBean.getEfunRole() +
					webOrderBean.getEfunLevel() + 
					webOrderBean.getCreditId()+ 
					webOrderBean.getUserId() + 
					webOrderBean.getPayFrom() + 
					webOrderBean.getTime() + 
					webOrderBean.getSecretKey());
		} else if(SERCET_KEY == urlMark){
			sb.append("&payMethod=other");
			sb.append("&md5Str="
					+ EfunStringUtil.toMd5(webOrderBean.getGameCode() + 
							webOrderBean.getServerCode() + 
							webOrderBean.getCreditId() + 
							webOrderBean.getUserId() + 
							webOrderBean.getPayFrom() + 
							webOrderBean.getTime() + 
							webOrderBean.getSecretKey(), false));
		}
	}

	private static void appendLocalInfo(Context context, StringBuffer sb) {
		String localMacAddress = (null == EfunLocalUtil.getLocalMacAddress(context)?"":EfunLocalUtil.getLocalMacAddress(context));
		String localImeiAddress = (null == EfunLocalUtil.getLocalImeiAddress(context)?"":EfunLocalUtil.getLocalImeiAddress(context));
		String localIpAddress = (null == EfunLocalUtil.getLocalIpAddress(context)?"":EfunLocalUtil.getLocalIpAddress(context));
		String localAndroidId = (null == EfunLocalUtil.getLocalAndroidId(context)?"":EfunLocalUtil.getLocalAndroidId(context));
		
		sb.append("&mac=").append(localMacAddress).
		append("&imei=").append(localImeiAddress).
		append("&ip=").append(localIpAddress).
		append("&androidid=").append(localAndroidId).
		append("&packageName=").append(context.getPackageName()).
		append("&versionCode=").append(EfunPayHelper.getVersionCode(context)).
		append("&versionName=").append(EfunPayHelper.getVersionName(context));
	}
	
	/**
	* <p>Title: checkWebOrderBean</p>
	* <p>Description: </p>
	* @param context
	* @param webOrderBean
	* @param isGW 是否为官网
	*/
	private static void checkWebOrderBean(Context context,WebOrderBean webOrderBean,int pageMark){
		if (EfunStringUtil.isEmpty(webOrderBean.getGameCode())) {
			String gameCode = EfunResConfiguration.getGameCode(context);
			if (EfunStringUtil.isEmpty(gameCode)) {
				throw new RuntimeException("请先配置好gamecode");
			}
			webOrderBean.setGameCode(gameCode);
		}

		if (EfunStringUtil.isEmpty(webOrderBean.getLanguage())) {
			webOrderBean.setLanguage(EfunResConfiguration.getLanguage(context));
		}
		if (EfunStringUtil.isEmpty(webOrderBean.getVh())) {
			String vh = EfunResConfiguration.getScreenOrientation(context);
			if (EfunStringUtil.isEmpty(vh)) {
				vh = "";
			}
			webOrderBean.setVh(vh);
		}
		
		if (EfunStringUtil.isEmpty(webOrderBean.getTime())) {
			webOrderBean.setTime(System.currentTimeMillis() + "");;
		}
		if (EfunStringUtil.isEmpty(webOrderBean.getSimOperator())) {
			String simOperator = "";
			if (EfunLocalUtil.isWifiAvailable(context)) {
				simOperator = GooglePayContant.WIFI;
			} else {
				TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				simOperator = telephonyManager.getSimOperator();
				if (EfunStringUtil.isEmpty(simOperator)) {
					simOperator = "";
				}
			}
			webOrderBean.setSimOperator(simOperator);
		}
		//添加对应的加密key
		checkSecretKey(webOrderBean, pageMark);
		//平台标识
		if (EfunStringUtil.isEmpty(webOrderBean.getAppPlatFrom())) {
			try {
				String appPlatform = context.getResources().getString(EfunResourceUtil.findStringIdByName(context, "efunAppPlatform"));
				webOrderBean.setAppPlatFrom(appPlatform);
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(BasePayActivity.TAG, "appPlatform is not find");
				webOrderBean.setAppPlatFrom("");
			}
		}
		/*if (EfunStringUtil.isEmpty(webOrderBean.getPhoneNumber())) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = tm.getLine1Number();
			if (EfunStringUtil.isNotEmpty(phoneNumber)) {
				webOrderBean.setPhoneNumber(phoneNumber);
			}
		}*/
		EfunLogUtil.logD("webOrderBean:" + webOrderBean.toString());
	}

	private static void checkSecretKey(WebOrderBean webOrderBean, int pageMark) {
		if (EfunStringUtil.isEmpty(webOrderBean.getSecretKey())) {
			if (SERCET_KEY_GOODLIST == pageMark) {
				webOrderBean.setSecretKey(GooglePayContant.SERCET_KEY_GOODLIST);
			} else if(SERCET_KEY_GW == pageMark){
				webOrderBean.setSecretKey(GooglePayContant.SERCET_KEY_GW);
			}else if(SERCET_KEY == pageMark){
				webOrderBean.setSecretKey(GooglePayContant.SERCET_KEY);
			}
		}
	}
	
	
	public static void saveListSkus(Context context,List<String> skus){
		if (skus == null || skus.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String string : skus) {
			sb.append(string + ";");
		}
		String skuString = sb.substring(0, sb.length());
		EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, BasePayActivity.EFUN_GOOGLE_PAY_LIST_SKUS, skuString);
	}
	
	public static List<String> getListSkus(Context context){
		ArrayList<String> a = new ArrayList<String>();
		String skuString = EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, BasePayActivity.EFUN_GOOGLE_PAY_LIST_SKUS);
		if (!TextUtils.isEmpty(skuString)) {
			String[] mSku = skuString.split(";");
			for (int i = 0; i < mSku.length; i++) {
				a.add(mSku[i]);
			}
		
		}
		return a;
	}
}
