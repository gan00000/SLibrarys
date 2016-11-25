package com.efun.ads.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
* <p>Title: SPUtil</p>
* <p>Description: 文件保全工具类</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013-7-19
*/
public class SPUtil {

	public static final String EFUN_FILE = "Efun.db";
	private static final String ADS_ADVERTISERS_NAME = "ADS_ADVERTISERS_NAME";
	private static final String ADS_PARTNER_NAME = "ADS_PARTNER_NAME";
	private static final String ADS_EFUN_REFERRER = "ADS_EFUN_REFERRER";
	private static final String ADS_EFUN_RESPONE_CODE = "ADS_EFUN_RESPONE_CODE";
	private static final String ADS_EFUN_SOURCE = "ADS_EFUN_SOURCE";
	private static final String ADS_EFUN_REGION = "ADS_EFUN_REGION";
	private static final int FILE_MODEL = Context.MODE_PRIVATE;
	private static final int saveCount = 3;
	private static final String installTime = "installTime";
	private static final String lastLoginTime = "lastLoginTime";
	
	public static final String ADS_ONLYONCE_KEY = "ADS_ONLYONCE_KEY";
	public static final String ADS_ONLYONCE_CODE = "ADS_ONLYONCE_CODE";
	public static final String EFUN_GOOGLE_ADVERTISING_ID = "EFUN_GOOGLE_ADVERTISING_ID";
	
	public static final String ADVERTISERS_S2S_KEY = "efunAdvertisersKey";
	public static final String ADVERTISERS_S2S_RESULT = "ADVERTISERS_SUCCESS_200";
	public static final String ads_efun = "Efun.ads";
	public static final String ads_efun_older = "ads.efun";
	
	public static void adsCurrentVersion(){
		Log.d("efunlog", "current version 2.6,修改google追踪方式，使用新的google-play-service-9256000");
	}
	
	public static void saveLastLoginTime(Context context, long mLastLoginTime) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putLong(SPUtil.lastLoginTime, mLastLoginTime).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
	public static Long takeLastLoginTime(Context context,Long defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		if (defValue == null) {
			return sp.getLong(SPUtil.lastLoginTime, 0);
		}
		return sp.getLong(SPUtil.lastLoginTime, defValue);
	}
	
	
	public static void saveInstallTime(Context context, long mInstallTime) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putLong(SPUtil.installTime, mInstallTime).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
	public static Long takeInstallTime(Context context,Long defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		if (defValue == null) {
			return sp.getLong(SPUtil.installTime, 0);
		}
		return sp.getLong(SPUtil.installTime, defValue);
	}
	
	public static void saveAdvertisersName(Context context, String advertisers) {
		save(context,SPUtil.ADS_ADVERTISERS_NAME, advertisers);
	}
	
	public static String takeAdvertisersName(Context context,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		if (defValue == null) {
			return sp.getString(SPUtil.ADS_ADVERTISERS_NAME, "");
		}
		return sp.getString(SPUtil.ADS_ADVERTISERS_NAME, defValue);
	}
	
	
	public static void saveRegion(Context context, String region) {
		save(context,SPUtil.ADS_EFUN_REGION, region);
	}
	
	public static String takeRegion(Context context,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		if (defValue == null) {
			return sp.getString(SPUtil.ADS_EFUN_REGION, "");
		}
		return sp.getString(SPUtil.ADS_EFUN_REGION, defValue);
	}
	
	
	public static void saveReferrer(Context context, String referrer){
		save(context, SPUtil.ADS_EFUN_REFERRER, referrer);
	}
	
	public static String takeReferrer(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		if (defValue == null) {
			return sp.getString(SPUtil.ADS_EFUN_REFERRER, "");
		}
		return sp.getString(SPUtil.ADS_EFUN_REFERRER, defValue);
	}
	
	public static void savePartnerName(Context context, String partnerName){
		save(context, SPUtil.ADS_PARTNER_NAME, partnerName);
	}
	
	public static String takePartnerName(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		if (defValue == null) {
			return sp.getString(SPUtil.ADS_PARTNER_NAME, "");
		}
		return sp.getString(SPUtil.ADS_PARTNER_NAME, defValue);
	}
	
	public static void saveLevel(Context context, String level){
		save(context, "efun_" + level, level);
	}
	
	public static String takeLevel(Context context, String level, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		return sp.getString("efun_" + level, defValue);
	}	
	
	public static void saveResponeCode(Context context, String code){
		save(context, ADS_EFUN_RESPONE_CODE, code);
	}
	
	public static String takeResponeCode(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		return sp.getString(ADS_EFUN_RESPONE_CODE, defValue);
	}	
	
	public static void saveEfunAdsThirdPlat(Context context, String efunSource){
		save(context, ADS_EFUN_SOURCE, efunSource);
	}
	
	public static String takeEfunAdsThirdPlat(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		return sp.getString(ADS_EFUN_SOURCE, defValue);
	}	
	
	public static void saveAdsOnlyFlag(Context context){
		save(context, ADS_ONLYONCE_KEY, ADS_ONLYONCE_CODE);
	}
	
	public static String takeAdsOnlyFlag(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		return sp.getString(ADS_ONLYONCE_KEY, defValue);
	}	
	
	private static void save(Context context,String key, String value){
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putString(key, value).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
}
