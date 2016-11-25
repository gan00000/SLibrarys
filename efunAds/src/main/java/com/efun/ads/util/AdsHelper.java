package com.efun.ads.util;

import java.util.Locale;

import com.efun.ads.bean.AdsHttpParams;
import com.efun.core.db.EfunDatabase;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunResourceUtil;
import com.efun.core.tools.EfunStringUtil;

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
					EfunDatabase.saveSimpleInfo(context, SPUtil.EFUN_FILE, SPUtil.EFUN_GOOGLE_ADVERTISING_ID, advertisingId);
					adsHttpParams.setAdvertising_id(advertisingId);
				}
			}
		} catch (Exception e) {
			Log.d("efun", "Exception:" + e.getMessage());
			e.printStackTrace();
		}
		if (EfunStringUtil.isEmpty(adsHttpParams.getGameCode())) {
			String gameCode = EfunResConfiguration.getGameCode(context);
			if (EfunStringUtil.isEmpty(gameCode )) {
				throw new NullPointerException("please configure the gameCode in xml file,must not be null or “”");
			}
			adsHttpParams.setGameCode(gameCode);
		}
		if (EfunStringUtil.isEmpty(adsHttpParams.getAppKey())) {
			String appKey = EfunResConfiguration.getAppKey(context);
			if (EfunStringUtil.isEmpty(appKey)) {
				throw new NullPointerException("please configure the appKey in xml file,must not be null or “”");
			}
			adsHttpParams.setAppKey(appKey);
		}
		if (EfunStringUtil.isEmpty(adsHttpParams.getAppPlatform())) {
			String appPlatform = context.getResources().getString(EfunResourceUtil.findStringIdByName(context, "efunAppPlatform"));
			if (EfunStringUtil.isEmpty(appPlatform )) {
				throw new NullPointerException("please configure the efunAppPlatform in xml file,must not be null or “”");
			}
			adsHttpParams.setAppPlatform(appPlatform);
		}
		
		if (EfunStringUtil.isEmpty(adsHttpParams.getAdvertiser())) {
			adsHttpParams.setAdvertiser(SPUtil.takeAdvertisersName(context, ""));
		}
		if (EfunStringUtil.isEmpty(adsHttpParams.getPartner())) {
			adsHttpParams.setPartner(SPUtil.takePartnerName(context, ""));
		}
		if (EfunStringUtil.isEmpty(adsHttpParams.getReferrer())) {
			adsHttpParams.setReferrer(SPUtil.takeReferrer(context, ""));
		}
		adsHttpParams.setPackageName(context.getPackageName());//添加包名
		adsHttpParams.setVersionCode(getVersionCode(context));
		adsHttpParams.setVersionName(getVersionName(context));
		
		String signature = EfunStringUtil.toMd5(adsHttpParams.getAppKey() + 
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
		String localAndroidId = (null == EfunLocalUtil.getLocalAndroidId(context) ? "" : EfunLocalUtil.getLocalAndroidId(context));
		String localMacAddress = (null == EfunLocalUtil.getLocalMacAddress(context) ? "" : EfunLocalUtil.getLocalMacAddress(context));
		String localImeiAddress = (null == EfunLocalUtil.getLocalImeiAddress(context) ? "" : EfunLocalUtil.getLocalImeiAddress(context));
		String localIpAddress = (null == EfunLocalUtil.getLocalIpAddress(context) ? "" : EfunLocalUtil.getLocalIpAddress(context));
		adsHttpParams.setOs_language(Locale.getDefault().getLanguage());
		adsHttpParams.setLocalAndroidId(localAndroidId);
		adsHttpParams.setLocalMacAddress(localMacAddress);
		adsHttpParams.setLocalImeiAddress(localImeiAddress);
		adsHttpParams.setLocalIpAddress(localIpAddress);
		adsHttpParams.setOsVersion(EfunLocalUtil.getOsVersion());
		adsHttpParams.setPhoneModel(EfunLocalUtil.getDeviceType());
		adsHttpParams.setWifi(EfunLocalUtil.isWifiAvailable(context) ? "yes" : "no");
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
		SharedPreferences settings = context.getSharedPreferences(SPUtil.ads_efun, Context.MODE_PRIVATE);
		String advertisersResult = settings.getString(SPUtil.ADVERTISERS_S2S_KEY, null);
		if (null != advertisersResult && advertisersResult.equals(SPUtil.ADVERTISERS_S2S_RESULT)) {
			EfunLogUtil.logD( "has old local data--ADVERTISERS_SUCCESS_200...Efun.ads");
			SPUtil.saveResponeCode(context, "1000");
			return true;
		}
		advertisersResult = context.getSharedPreferences(SPUtil.ads_efun_older, Context.MODE_PRIVATE).getString(SPUtil.ADVERTISERS_S2S_KEY, null);
		if (null != advertisersResult && (advertisersResult.equals(SPUtil.ADVERTISERS_S2S_RESULT))) {
			EfunLogUtil.logD( "has old local data--ADVERTISERS_SUCCESS_200...ads.efun");
			SPUtil.saveResponeCode(context, "1000");
			return true;
		}
		String localCode = SPUtil.takeResponeCode(context,"");
		if (EfunStringUtil.isNotEmpty(localCode) && EfunStringUtil.isNotEmpty(localCode.trim()) && !"null".equals(localCode)) {
			Log.d("efunLog", "ads already  has local ads code:" + localCode);
			return true;
		}
		return false;
	}
}
