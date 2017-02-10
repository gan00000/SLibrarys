package com.starpy.ads.call;

import com.core.base.res.SConfig;
import com.starpy.ads.activity.EfunAdsS2SService;
import com.starpy.ads.bean.AdsHttpParams;
import com.core.base.utils.SPUtil;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;


public class EfunAdsPlatform {
	
	/**
	* <p>Title: initEfunAdsS2S</p>
	* <p>Description: 启动广告，S2S和google分析，S2S等待google追踪广播的最长时间为25s，上报一次</p>
	* @param currentActivity 当前Activity实例对象
	*/
	public static void initEfunAdsS2S(Activity currentActivity){
		Log.i("efunLog", "initEfunAdsS2S 启动");
		SConfig.clearLoginMsg(currentActivity);
		EfunAdsManager.setActivity(currentActivity);
		currentActivity.startService(new Intent(currentActivity, EfunAdsS2SService.class));
	}
	
	public static void initEfunAdsS2S(Activity currentActivity,boolean runS2SFlag){
		Log.i("efunLog", "initEfunAdsS2S 启动");
		SConfig.clearLoginMsg(currentActivity);
		EfunAdsManager.setActivity(currentActivity);
		Intent intent = new Intent(currentActivity, EfunAdsS2SService.class);
		intent.putExtra(EfunAdsS2SService.EFUN_S2S_RUN_FLAG, runS2SFlag);
		currentActivity.startService(intent);
	}
	
	public static void initEfunAdsS2S(Activity currentActivity, AdsHttpParams adsHttpParams, boolean runS2SFlag){
		Log.i("efunLog", "initEfunAdsS2S 启动");
		SConfig.clearLoginMsg(currentActivity);
		EfunAdsManager.setActivity(currentActivity);
		Intent intent = new Intent(currentActivity, EfunAdsS2SService.class);
		intent.setPackage(currentActivity.getPackageName());
		intent.putExtra(EfunAdsS2SService.EFUN_S2S_RUN_FLAG, runS2SFlag);
		if (null != adsHttpParams) {
			intent.putExtra(EfunAdsS2SService.EFUN_ADSHTTP_PARAMS, adsHttpParams);
		}
		currentActivity.startService(intent);
	}
	
	
	/**
	* <p>Title: initEfunAdsWithPartner</p>
	* <p>Description: 只启动S2S广告，指定合作商和广告商（不走google分析），上报一次</p>
	* @param currentActivity 当前Activity实例对象
	* @param advertisersName 广告商
	* @param partner 合作商
	*/
	public static void initEfunAdsWithPartner(Activity currentActivity, String advertisersName, String partner){
		SPUtil.saveSimpleInfo(currentActivity, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SIGN, "");//广告启动（每次启动游戏）清除掉sign
		EfunAdsManager.setAdvertisersName(currentActivity, advertisersName);
		EfunAdsManager.setPartnerName(currentActivity, partner);
		initEfunAdsS2S(currentActivity);
	}
	
/*	public static void initEfunAdsPartner(Activity currentActivity, String advertisersName, String partner){
		Log.i("efunLog", "initEfunAdsPartner 启动");
		SPUtil.saveAdvertisersName(currentActivity, advertisersName);
		SPUtil.savePartnerName(currentActivity, partner);
		SharedPreferences settings = currentActivity.getSharedPreferences(ads_efun, Context.MODE_PRIVATE);
		
		String partnerIsSuccess = settings.getString(S2S_PARTNER_RESULT_KEY , null);
		
		if (partnerIsSuccess == null || !S2S_PARTNER_VALUE.equals(partnerIsSuccess)) {
			settings.edit().putString(S2S_PARTNER_RESULT_KEY, S2S_PARTNER_VALUE).commit();
			Log.i("efunLog", "ActivationAdvertisingService线程 启动");
			AdvertService.startAdsPost(currentActivity);
		}
	}*/
	
	/**
	* <p>Title: initEfunAdsSendTwoPost</p>
	* <p>Description: 同时发两次，一次google analytics,一次传递android params直接发送到服务器 [一次带advertisersName和partner的S2S，一次不带advertisersName和partner的S2S（实际上是{@link #initEfunAdsWithPartner} 
	* 和 {@link #initEfunAdsOnlyS2S}的组合）]</p>
	* @param currentActivity Activity实例对象
	* @param advertisersName 广告商
	* @param partner 合作商
	*/
/*	public static void initEfunAdsSendTwoPost(Activity currentActivity, String advertisersName, String partner){
		initEfunAdsWithPartner(currentActivity, advertisersName, partner);
		initEfunAdsOnlyS2S(currentActivity);
	}*/
	
	/**
	* <p>Title: initEfunAdsOnlyS2S</p>
	* <p>Description: 只发一次S2S（直接使用线程调接口，不走s2s服务和GA广播）</p>
	* @param currentActivity Activity实例对象
	*/
/*	private static void initEfunAdsOnlyS2S(Activity currentActivity) {
		Map<String, String> params = AdvertService.getInstance().initAdsPostParams(currentActivity);
		String adsPreferredUrl = SConfig.getAdsPreferredUrl(currentActivity);
		String adsSpareUrl = SConfig.getAdsSpareUrl(currentActivity);
		if (SStringUtil.isNotEmpty(adsPreferredUrl)) {
			adsPreferredUrl = adsPreferredUrl + EfunDomainSite.EFUN_ADS;
		}
		if (SStringUtil.isNotEmpty(adsSpareUrl)) {
			adsSpareUrl = adsSpareUrl + EfunDomainSite.EFUN_ADS;
		}
		AdvertisingService.getInstance().egActivationAdvertising(
				currentActivity,
				adsPreferredUrl,
				adsSpareUrl,
				params,
				new Object[] { SConfig.getGameCode(currentActivity),
					SConfig.getAppKey(currentActivity)});
	}*/
	

}
