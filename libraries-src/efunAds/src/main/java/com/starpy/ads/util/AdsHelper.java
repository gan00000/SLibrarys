package com.starpy.ads.util;

import java.util.Locale;

import com.starpy.base.cfg.ResConfig;
import com.starpy.ads.bean.AdsHttpParams;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.SPUtil;
import com.starpy.base.utils.SLogUtil;
import com.core.base.utils.ResUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.google.utils.GoogleUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

public class AdsHelper {
	
	public static AdsHttpParams initParams(Context context,AdsHttpParams adsHttpParams) {
		
		if (adsHttpParams == null) {
			return null;
		}
		initLocalInfo(context, adsHttpParams);
		try {
			if (existClass("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {//判断google-play-server.jar是否存在
				String advertisingId = GoogleUtil.getAdvertisingId(context);
				if (!TextUtils.isEmpty(advertisingId)) {
					SPUtil.saveSimpleInfo(context, com.starpy.ads.util.SPUtil.EFUN_FILE, com.starpy.ads.util.SPUtil.EFUN_GOOGLE_ADVERTISING_ID, advertisingId);
					adsHttpParams.setAdvertising_id(advertisingId);
				}
			}
		} catch (Exception e) {
			Log.d("efun", "Exception:" + e.getMessage());
			e.printStackTrace();
		}
		if (SStringUtil.isEmpty(adsHttpParams.getGameCode())) {
			String gameCode = ResConfig.getGameCode(context);
			if (SStringUtil.isEmpty(gameCode )) {
				throw new NullPointerException("please configure the gameCode in xml file,must not be null or “”");
			}
			adsHttpParams.setGameCode(gameCode);
		}
		if (SStringUtil.isEmpty(adsHttpParams.getAppKey())) {
			String appKey = ResConfig.getAppKey(context);
			if (SStringUtil.isEmpty(appKey)) {
				throw new NullPointerException("please configure the appKey in xml file,must not be null or “”");
			}
			adsHttpParams.setAppKey(appKey);
		}
		if (SStringUtil.isEmpty(adsHttpParams.getAppPlatform())) {
			String appPlatform = context.getResources().getString(ResUtil.findStringIdByName(context, "efunAppPlatform"));
			if (SStringUtil.isEmpty(appPlatform )) {
				throw new NullPointerException("please configure the efunAppPlatform in xml file,must not be null or “”");
			}
			adsHttpParams.setAppPlatform(appPlatform);
		}
		
		if (SStringUtil.isEmpty(adsHttpParams.getAdvertiser())) {
			adsHttpParams.setAdvertiser(com.starpy.ads.util.SPUtil.takeAdvertisersName(context, ""));
		}
		if (SStringUtil.isEmpty(adsHttpParams.getPartner())) {
			adsHttpParams.setPartner(com.starpy.ads.util.SPUtil.takePartnerName(context, ""));
		}
		if (SStringUtil.isEmpty(adsHttpParams.getReferrer())) {
			adsHttpParams.setReferrer(com.starpy.ads.util.SPUtil.takeReferrer(context, ""));
		}
		adsHttpParams.setPackageName(context.getPackageName());//添加包名
		adsHttpParams.setVersionCode(getVersionCode(context));
		adsHttpParams.setVersionName(getVersionName(context));
		
		String signature = SStringUtil.toMd5(adsHttpParams.getAppKey() +
				adsHttpParams.getTimeStamp() + 
				adsHttpParams.getLocalMacAddress() + 
				adsHttpParams.getGameCode() + 
				adsHttpParams.getLocalImeiAddress() + 
				adsHttpParams.getLocalIpAddress() + 
				adsHttpParams.getLocalAndroidId(),
				false);
		adsHttpParams.setSignature(signature);
		

		return adsHttpParams;
	}

	private static void initLocalInfo(Context context, AdsHttpParams adsHttpParams) {
		String localAndroidId = (null == ApkInfoUtil.getAndroidId(context) ? "" : ApkInfoUtil.getAndroidId(context));
		String localMacAddress = (null == ApkInfoUtil.getMacAddress(context) ? "" : ApkInfoUtil.getMacAddress(context));
		String localImeiAddress = (null == ApkInfoUtil.getImeiAddress(context) ? "" : ApkInfoUtil.getImeiAddress(context));
		String localIpAddress = (null == ApkInfoUtil.getLocalIpAddress(context) ? "" : ApkInfoUtil.getLocalIpAddress(context));
		adsHttpParams.setOs_language(Locale.getDefault().getLanguage());
		adsHttpParams.setLocalAndroidId(localAndroidId);
		adsHttpParams.setLocalMacAddress(localMacAddress);
		adsHttpParams.setLocalImeiAddress(localImeiAddress);
		adsHttpParams.setLocalIpAddress(localIpAddress);
		adsHttpParams.setOsVersion(ApkInfoUtil.getOsVersion());
		adsHttpParams.setPhoneModel(ApkInfoUtil.getDeviceType());
		adsHttpParams.setWifi(ApkInfoUtil.isWifiAvailable(context) ? "yes" : "no");
	}
	
	
	private static String getVersionCode(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			String version = String.valueOf(info.versionCode);
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static String getVersionName(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
/*	public static String getUserAgent(Context context){
		WebView webView = new WebView(context);
		return webView.getSettings().getUserAgentString();
	}*/
	
	private static boolean existClass(String className){
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log.w("efun", "not include google-play-services.jar");
			return false;
		}		
		return true;
	}
	
	public static boolean existLocalResponeCode(Context context) {
		SharedPreferences settings = context.getSharedPreferences(com.starpy.ads.util.SPUtil.ads_efun, Context.MODE_PRIVATE);
		String advertisersResult = settings.getString(com.starpy.ads.util.SPUtil.ADVERTISERS_S2S_KEY, null);
		if (null != advertisersResult && advertisersResult.equals(com.starpy.ads.util.SPUtil.ADVERTISERS_S2S_RESULT)) {
			SLogUtil.logD( "has old local data--ADVERTISERS_SUCCESS_200...Efun.ads");
			com.starpy.ads.util.SPUtil.saveResponeCode(context, "1000");
			return true;
		}
		advertisersResult = context.getSharedPreferences(com.starpy.ads.util.SPUtil.ads_efun_older, Context.MODE_PRIVATE).getString(com.starpy.ads.util.SPUtil.ADVERTISERS_S2S_KEY, null);
		if (null != advertisersResult && (advertisersResult.equals(com.starpy.ads.util.SPUtil.ADVERTISERS_S2S_RESULT))) {
			SLogUtil.logD( "has old local data--ADVERTISERS_SUCCESS_200...ads.efun");
			com.starpy.ads.util.SPUtil.saveResponeCode(context, "1000");
			return true;
		}
		String localCode = com.starpy.ads.util.SPUtil.takeResponeCode(context,"");
		if (SStringUtil.isNotEmpty(localCode) && SStringUtil.isNotEmpty(localCode.trim()) && !"null".equals(localCode)) {
			Log.d("efunLog", "ads already  has local ads code:" + localCode);
			return true;
		}
		return false;
	}
}
