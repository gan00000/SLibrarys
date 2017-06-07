package com.starpy.base.cfg;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.utils.PL;
import com.core.base.utils.ResUtil;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.StarPyUtil;

public class ResConfig {

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
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_GameCode())){
			return StarPyUtil.getSdkCfg(context).getS_GameCode();
		}
		return getResStringByName(context, "star_game_code");
	}
	

	/**
	 * 获取秘钥
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppKey(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_AppKey())){
			return StarPyUtil.getSdkCfg(context).getS_AppKey();
		}
		return getResStringByName(context, "star_app_key");
	}

	public static String getApplicationId(Context context) {
		return getResStringByName(context, "facebook_app_id");
	}


	public static String getGameLanguage(Context context) {
		String language = SPUtil.getSimpleString(context, StarPyUtil.STAR_PY_SP_FILE, StarPyUtil.STARPY_GAME_LANGUAGE);
//		if (TextUtils.isEmpty(language)) {
//			language =  getResStringByName(context, "star_game_language");
//		}
		return language;
	}

	public static void saveGameLanguage(Context context,String language) {
		if (TextUtils.isEmpty(language)) {
			return;
		}
		SPUtil.saveSimpleInfo(context, StarPyUtil.STAR_PY_SP_FILE, StarPyUtil.STARPY_GAME_LANGUAGE,language);
	}
	
	public static String getGameLanguageLower(Context context){
		return getGameLanguage(context).toLowerCase();
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
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_Login_Pre_Url())){
			return StarPyUtil.getSdkCfg(context).getS_Login_Pre_Url();
		}
		return getConfigUrl(context, "star_py_login_pre_url");
	}

	public static String getLoginSpareUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_Login_Spa_Url())){
			return StarPyUtil.getSdkCfg(context).getS_Login_Spa_Url();
		}
		return getConfigUrl(context, "star_py_login_spa_url");
	}

	/**
	 * <p>Description: 获取储值域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getPayPreferredUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_Pay_Pre_Url())){
			return StarPyUtil.getSdkCfg(context).getS_Pay_Pre_Url();
		}
		return getConfigUrl(context, "star_py_pay_pre_url");
	}
	public static String getPaySpareUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_Pay_Spa_Url())){
			return StarPyUtil.getSdkCfg(context).getS_Pay_Spa_Url();
		}
		return getConfigUrl(context, "star_py_pay_spa_url");
	}

	public static String getCsPreferredUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_CS_Pre_Url())){
			return StarPyUtil.getSdkCfg(context).getS_CS_Pre_Url();
		}
		return getConfigUrl(context, "star_py_cs_pre_url");
	}
	public static String getCsSpareUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_CS_Spa_Url())){
			return StarPyUtil.getSdkCfg(context).getS_CS_Spa_Url();
		}
		return getConfigUrl(context, "star_py_cs_spa_url");
	}

	public static String getActivityPreferredUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_Act_Pre_Url())){
			return StarPyUtil.getSdkCfg(context).getS_Act_Pre_Url();
		}
		return getConfigUrl(context, "star_py_act_pre_url");
	}
	public static String getActivitySpareUrl(Context context) {
		if (StarPyUtil.getSdkCfg(context) != null && !TextUtils.isEmpty(StarPyUtil.getSdkCfg(context).getS_Act_Spa_Url())){
			return StarPyUtil.getSdkCfg(context).getS_Act_Spa_Url();
		}
		return getConfigUrl(context, "star_py_act_spa_url");
	}

	public static String getCdnPreferredUrl(Context context) {
		return getConfigUrl(context, "star_py_cdn_pre_url");
	}
	public static String getCdnSpareUrl(Context context) {
		return getConfigUrl(context, "star_py_cdn_spa_url");
	}

	public static boolean isInfringement(Context context){
		return SStringUtil.isEqual(getResStringByName(context, "star_infringement"),"true");
	}

	public static String getPayThirdMethod(Context context) {
		return getResStringByName(context, "star_pay_third_method");
	}



	/**
	 * <p>Description: 获取广告域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getAdsPreferredUrl(Context context) {
		return getConfigUrl(context, "efunAdsPreferredUrl");
	}
	
	public static String getAdsSpareUrl(Context context) {
		return getConfigUrl(context, "efunAdsSpareUrl");
	}
	
	/**
	 * <p>Description: 获取game工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getGamePreferredUrl(Context context) {
		return getConfigUrl(context, "efunGamePreferredDomainUrl");
	}
	
	public static String getGameSpareUrl(Context context) {
		return getConfigUrl(context, "efunGameSpareDomainUrl");
	}
	
	/**
	 * <p>Description: 获取FB工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getFBPreferredUrl(Context context) {
		return getConfigUrl(context, "efunFbPreferredUrl");
	}
	
	public static String getFBSpareUrl(Context context) {
		return getConfigUrl(context, "efunFbSpareUrl");
	}
	/**
	 * <p>Description: 获取FB工程域名</p>
	 * @param context
	 * @return
	 * @date 2015年2月5日
	 */
	public static String getQuestionPreferredUrl(Context context) {
		return getConfigUrl(context, "efunQuestionPreUrl");
	}
	
	public static String getQuestionSpareUrl(Context context) {
		return getConfigUrl(context, "efunQuestionSpaUrl");
	}
	
	
	public static String getPushServerPreferredUrl(Context context) {
		return getConfigUrl(context, "efunPushPreferredUrl");
	}
	
	public static String getPushServerSpareUrl(Context context) {
		return getConfigUrl(context, "efunPushSpareUrl");
	}

//===========================================域名获取end===============================================	
//===========================================域名获取end===============================================	
//===========================================域名获取end===============================================	
	
	public static String getGoogleAnalyticsTrackingId(Context context) {
		return getResStringByName(context, "efunGoogleAnalyticsTrackingId");
	}
	
	public static String getS2SListenerName(Context context) {
		return getResStringByName(context, "efunS2SListenerName");
	}
	
	public static String getGAListenerName(Context context) {
		return getResStringByName(context, "efunGAListenerName");
	}
	
	//<string name="reg_jp_efunAdsPreferredUrl">http://ad.efunjp.com/</string>
	public static String getConfigUrl(Context context, String xmlSchemaName){
		String url = getResStringByName(context, xmlSchemaName);
		
		if (TextUtils.isEmpty(url)) {
			return "";
		}
		if (url.contains(".com") || url.contains("http") || url.contains("//")) {
			return url;
		}
		return "";
	}
	
	private static String getResStringByName(Context context, String xmlSchemaName){

		String xmlSchemaContent = "";
		try {
			xmlSchemaContent = context.getResources().getString(ResUtil.findStringIdByName(context, xmlSchemaName));
		} catch (Exception e) {
			PL.w("String not find:" + xmlSchemaName);
			e.printStackTrace();
			return "";
		}
		return xmlSchemaContent;
	}

}
