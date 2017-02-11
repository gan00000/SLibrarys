package com.starpy.googlepay.util;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.res.SConfig;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.constants.GooglePayContant;

public class EfunPayHelper {
	
	public static String getPreferredUrl(BasePayActivity payActivity){
		String preferredUrl = payActivity.getPayPreferredUrl();
		if (SStringUtil.isEmpty(preferredUrl)) {
			preferredUrl = SConfig.getEfunPayPreferredUrl(payActivity);
		}
		return checkUrl(preferredUrl);
	}
	
	public static String getSpareUrl(BasePayActivity payActivity){
		String spareUrl = payActivity.getPaySpareUrl();
		if (SStringUtil.isEmpty(spareUrl)) {
			spareUrl = SConfig.getEfunPaySpareUrl(payActivity);
		}
		return checkUrl(spareUrl);
	}
	
	/**
	* <p>Title: checkUrl</p>
	* <p>Description: 判断是否为正确url</p>
	* @param url
	* @return
	*/
	public static String checkUrl(String url){
		if (TextUtils.isEmpty(url)) {
			return "";
		}
		try {
			URL checkUrl = new URL(url);//判断是否为正确url
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		}
		if (url.endsWith("/")) {
			return url;
		}else{
			url = url + "/";
		}
		return url;
	}
	
	
	public static void logCurrentVersion(){
		Log.d("efun", "pay version " + BasePayActivity.GOOGLE_PAY_VERSION);
		Log.d("efun", "changeLog: " + BasePayActivity.GOOGLE_PAY_VERSION_CHAGE_LOG);
	}
	
	/**
	* <p>Title: getVersionCode</p>
	* <p>Description: 获取游戏版本号</p>
	* @param context
	* @return
	*/
	public static String getVersionCode(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			String version = String.valueOf(info.versionCode);
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getVersionName(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void saveCurrentOrderId(Context ctx,String orderId){
		SPUtil.saveSimpleInfo(ctx, GooglePayContant.EFUNFILENAME, GooglePayContant.EFUN_CURRENT_ORDER_ID_KEY, orderId);
	}
	
	public static String getPreviousOrderId(Context ctx){
		return SPUtil.getSimpleString(ctx, GooglePayContant.EFUNFILENAME, GooglePayContant.EFUN_CURRENT_ORDER_ID_KEY);
	}
	
	/**
	 * <p>Description: 获取登录的sign</p>
	 * @param context
	 * @return
	 * @date 2016年2月24日
	 */
	public static String getLoginSign(Context context){
//		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, "EFUN_LOGIN_SIGN");
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, "EFUN_LOGIN_SIGN");
	}
}