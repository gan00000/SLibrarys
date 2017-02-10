package com.core.base.res;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.utils.ResUtil;
import com.core.base.utils.SPUtil;
import com.core.base.utils.ApkInfoUtil;

public class SConfig {
	//此处静态变量原因如下：
	//由于需要在http请求中拼接此参数，但由于HttpRequest类接口中无上下文传进来，无法取得保存的这些数据，并且又需要对外接口保持防止大改动，故在一些特殊方法中获取一下值保存在这里的静态变量
	private static String region = "";
	private static String loginSign = "";
	private static String loginTimestamp = "";
	public static Context mContext;
	
	public static void clearLoginMsg(Context context) {
		region = "";
		loginSign = "";
		loginTimestamp = "";
		mContext = context.getApplicationContext();
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SIGN, "");// 广告启动（每次启动游戏）清除掉sign
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_TIMESTAMP, "");
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SERVER_RETURN_DATA, "");
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_USER_ID, "");
	}
	
	public static String getSDKLoginReturnData() {
		if (mContext == null) {
			return "";
		}
		return getSDKLoginReturnData(mContext);
	}
	
	public static String getSDKLoginReturnData(Context context) {
		if (context == null) {
			return "";
		}
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SERVER_RETURN_DATA);
	}
	
	/**
	 * @return the loginSign  用来http拼接
	 */
	public static String getLoginSign() {
		if (TextUtils.isEmpty(loginSign) && mContext != null) {
			return getSDKLoginSign(mContext);
		}
		return loginSign;
	}
	
	public static String getLoginTimestamp() {
		if (TextUtils.isEmpty(loginTimestamp) && mContext != null) {
			return getSDKLoginTimestamp(mContext);
		}
		return loginTimestamp;
	}

	/**
	 * @param loginSign the loginSign to set
	 */
	public static void setLoginSign(Context context,String loginSign) {
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SIGN, loginSign);
		SConfig.loginSign = loginSign;
	}

	public static void setRegion(Context context,String region){
		SConfig.region = region;
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_GAME_REGION, region);
	}
	
	public static String getRegion(Context context){
		if (TextUtils.isEmpty(region)) {
			region = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_GAME_REGION);
		}
		return region;
	}
	
	
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
		return efunGetString(context, "efunGameCode");
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
		return efunGetString(context, "efunAppKey");
	}

	/**
	 * 获取gameShortName
	 * 
	 * @param context
	 * @return
	 */
	public static String getGameShortName(Context context) {
		return efunGetString(context, "efunGameShortName");
	}

	public static String getApplicationId(Context context) {
		return efunGetString(context, "efunFBApplicationId");
	}

	/**
	 * 获取前缀
	 * 
	 * @param context
	 * @return
	 */
	public static String getPrefixName(Context context) {
		return efunGetString(context, "efunPrefixName");
	}

	/**
	 * 获取屏幕方向
	 * 
	 * @param context
	 * @return
	 */
	public static String getScreenOrientation(Context context) {
		return efunGetString(context, "efunScreenOrientation");
	}


	public static String getSDKLanguage(Context context) {
		String language = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_SDK_LANGUAGE);
		if (TextUtils.isEmpty(language)) {
			language =  efunGetString(context, "efunLanguage");
		}
		return language;
	}
	
	public static String getSDKLanguageLower(Context context){
		return getSDKLanguage(context).toLowerCase();
	}
	
	public static String getSDKLoginSign(Context context){
		loginSign = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SIGN);
		return loginSign;
	}
	
	public static String getSDKLoginTimestamp(Context context){
		loginTimestamp = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_TIMESTAMP);
		return loginTimestamp;
	}
	
	public static String getCurrentEfunUserId(Context context){
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_USER_ID);
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
		return efunGetConfigUrl(context, "efunLoginPreferredUrl");
	}

	public static String getLoginSpareUrl(Context context) {
		return efunGetConfigUrl(context, "efunLoginSpareUrl");
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
	
/*	public static String getEfunLuaSwitchUrl(Context context) {
		return efunGetConfigUrl(context, "efunLuaSwitchUrl");
	}*/
	
	
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
	
	public static String getEfunLoginListener(Context context) {
		return efunGetString(context, "efunLoginListenerName");
	}
	
	
	//<string name="reg_jp_efunAdsPreferredUrl">http://ad.efunjp.com/</string>
	public static String efunGetConfigUrl(Context context,String xmlSchemaName){
		mContext = context.getApplicationContext();
		String region = getRegion(context);
		String url = "";
		if (!TextUtils.isEmpty(region)) {
			String xmlSchemaNameTmp = region.toLowerCase() + "_" + xmlSchemaName;
			url = efunGetString(context, xmlSchemaNameTmp);
		}
		
		if (TextUtils.isEmpty(url)) {
			url = efunGetString(context, xmlSchemaName);
		}
		
		if (TextUtils.isEmpty(url)) {
			return "";
		}
		if (url.contains(".com") || url.contains("http") || url.contains("//")) {
			return url;
		}
		return "";
	}
	
	private static String efunGetString(Context context,String xmlSchemaName){
		mContext = context.getApplicationContext();
		getSDKLoginSign(context);//初始化一下sign值，为请求网络使用
		getSDKLoginTimestamp(context);
		ApkInfoUtil.getEfunUUid(context);
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
