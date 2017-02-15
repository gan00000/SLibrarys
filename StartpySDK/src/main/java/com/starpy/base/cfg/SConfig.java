package com.starpy.base.cfg;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.utils.ResUtil;

public class SConfig {

	//===========================================参数配置start===============================================
	//===========================================每個遊戲每個渠道都可能不一樣=======================================
	//===========================================参数配置start================================================

	
	/**
	 * 获取gameCode
	 * 
	 * @param context
	 * @return
	 */
	public static String getGameCode(Context context) {
		return efunGetString(context, "star_game_code");
	}
	
	public static String getAppPlatform(Context context) {
		return efunGetString(context, "efunAppPlatform");
	}

	/**
	 * 获取秘钥
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppKey(Context context) {
		return efunGetString(context, "star_app_key");
	}

	public static String getApplicationId(Context context) {
		return efunGetString(context, "facebook_app_id");
	}


	public static String getGameLanguage(Context context) {
		//String language = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_SDK_LANGUAGE);
//		if (TextUtils.isEmpty(language)) {
//			language =  efunGetString(context, "efunLanguage");
//		}
		String language =  efunGetString(context, "star_game_language");
		return language;
	}
	
	public static String getGameLanguageLower(Context context){
		return getGameLanguage(context).toLowerCase();
	}

	public static String getEfunAppPlatform(Context context) {
		return efunGetString(context, "efunAppPlatform");
	}
	
	//===========================================参数配置end===============================================	
	//===========================================参数配置end===============================================	
	//===========================================参数配置end===============================================	
	
	
	
	
	
	
	
	//===========================================域名获取start===============================================	
	//=========================================== 根据地区改变，同一地区的游戏不变===================================
	//===========================================域名获取start===============================================	

	/**
	 * 获取登录域名地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLoginPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "star_py_login_pre_url");
	}

	public static String getLoginSpareUrl(Context context) {
		return efunGetConfigUrl(context, "star_py_login_spa_url");
	}

	/**
	 * 获取三方登录域名地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getPlatformLoginPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunPlatformLoginPreferredUrl");
	}

	public static String getPlatformLoginSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunPlatformLoginSpareUrl");
	}
	/**
	 * <p>Description: 获取储值域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getEfunPayPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunPayPreferredUrl");
	}
	public static String getEfunPaySpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunPaySpareUrl");
	}

	/**
	 * <p>Description: 获取广告域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getAdsPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunAdsPreferredUrl");
	}
	
	public static String getAdsSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunAdsSpareUrl");
	}
	
	/**
	 * <p>Description: 获取game工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getGamePreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunGamePreferredDomainUrl");
	}
	
	public static String getGameSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunGameSpareDomainUrl");
	}
	
	/**
	 * <p>Description: 获取动态域名工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getDynamicPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunDynamicPreUrl");
	}
	
	public static String getDynamicSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunDynamicSpaUrl");
	}
	
	/**
	 * <p>Description: 获取FB工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getFBPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunFbPreferredUrl");
	}
	
	public static String getFBSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunFbSpareUrl");
	}
	/**
	 * <p>Description: 获取FB工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getQuestionPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunQuestionPreUrl");
	}
	
	public static String getQuestionSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunQuestionSpaUrl");
	}
	
	
	public static String getPushServerPreferredUrl(Context context) {
		return efunGetConfigUrl(context, "efunPushPreferredUrl");
	}
	
	public static String getPushServerSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunPushSpareUrl");
	}

//===========================================域名获取end===============================================	
//===========================================域名获取end===============================================	
//===========================================域名获取end===============================================	
	
	/**
	 * 获取google play 服务错误提示
	 * 
	 * @param context
	 * @return
	 */
	public static String getGoogleServiceError(Context context) {
		return efunGetString(context, "efunGoogleServerError");
	}

	/**
	 * 获取google play 购买错误提示
	 * 
	 * @param context
	 * @return
	 */
	public static String getGoogleBuyFailError(Context context) {
		return efunGetString(context, "efunGoogleBuyFailError");
	}

	/**
	 * 获取google play 地区错误提示
	 * 
	 * @param context
	 * @return
	 */
	public static String getGoogleStoreError(Context context) {
		return efunGetString(context, "efunGoogleStoreError");
	}

	public static String getGoogleAnalyticsTrackingId(Context context) {
		return efunGetString(context, "efunGoogleAnalyticsTrackingId");
	}
	
	public static String getS2SListenerName(Context context) {
		return efunGetString(context, "efunS2SListenerName");
	}
	
	public static String getGAListenerName(Context context) {
		return efunGetString(context, "efunGAListenerName");
	}
	
	//<string name="reg_jp_efunAdsPreferredUrl">http://ad.efunjp.com/</string>
	public static String efunGetConfigUrl(Context context,String xmlSchemaName){
		String url = efunGetString(context, xmlSchemaName);
		
		if (TextUtils.isEmpty(url)) {
			return "";
		}
		if (url.contains(".com") || url.contains("http") || url.contains("//")) {
			return url;
		}
		return "";
	}
	
	private static String efunGetString(Context context,String xmlSchemaName){

		String xmlSchemaContent = "";
		try {
			xmlSchemaContent = context.getResources().getString(ResUtil.findStringIdByName(context, xmlSchemaName));
		} catch (Exception e) {
			Log.w("efun", "String not find:" + xmlSchemaName);
			e.printStackTrace();
			return "";
		}
		return xmlSchemaContent;
	}

}
