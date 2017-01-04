package com.efun.googlepay.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.efun.core.db.EfunDatabase;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunStringUtil;
import com.efun.googlepay.BasePayActivity;
import com.efun.googlepay.EfunGooglePayService;
import com.efun.googlepay.constants.GooglePayContant;

public class EfunPayHelper {
	
	public static String getPreferredUrl(Context context){
		String preferredUrl = "";
		if (EfunGooglePayService.getPayActivity() != null) {
			preferredUrl = EfunGooglePayService.getPayActivity().getPayPreferredUrl();
		}
		if (EfunStringUtil.isEmpty(preferredUrl)) {
			preferredUrl = EfunResConfiguration.getEfunPayPreferredUrl(context);
		}
		return checkUrl(preferredUrl);
	}
	
	public static String getSpareUrl(Context context){
		String spareUrl = "";
		if (EfunGooglePayService.getPayActivity() != null) {
			spareUrl = EfunGooglePayService.getPayActivity().getPaySpareUrl();
		}
		if (EfunStringUtil.isEmpty(spareUrl)) {
			spareUrl = EfunResConfiguration.getEfunPaySpareUrl(context);
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
		Log.d(BasePayActivity.TAG, "new pay version " + BasePayActivity.GOOGLE_PAY_VERSION);
//		LLog.d(BasePayActivity.TAG, "v2 2.2.1 -> 更改google正常储值和兑换码储值的判断方式,fix bug");
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
		EfunDatabase.saveSimpleInfo(ctx, GooglePayContant.EFUNFILENAME, GooglePayContant.EFUN_CURRENT_ORDER_ID_KEY, orderId);
	}
	
	public static String getPreviousOrderId(Context ctx){
		return EfunDatabase.getSimpleString(ctx, GooglePayContant.EFUNFILENAME, GooglePayContant.EFUN_CURRENT_ORDER_ID_KEY);
	}
	
	/**
	 * <p>Description: 判断是正常储值还是促销码兑换</p>
	 * @param data 0标识正常充值，此类形充值会有google orderId返回；1标识兑换促销码，无orderId返回
	 * @return
	 * @date 2016年1月13日
	 */
	public static int googlePayType(String data){
		try {
			if (TextUtils.isEmpty(data)) {
				return 0;
			}
			if (!data.contains("developerPayload")) {//若无developerPayload字段，则判断为兑换码,若有，则判断为正常储值行为
				return 1;
			}
			JSONObject j = new JSONObject(data);
			if (TextUtils.isEmpty(j.optString("developerPayload", ""))) {
				return 1;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
}
