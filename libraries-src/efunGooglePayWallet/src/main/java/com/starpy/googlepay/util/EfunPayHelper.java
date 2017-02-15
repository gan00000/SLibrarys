package com.starpy.googlepay.util;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.starpy.base.cfg.SConfig;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.StarPyUtil;
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

	
	public static void saveCurrentOrderId(Context ctx,String orderId){
		SPUtil.saveSimpleInfo(ctx, GooglePayContant.EFUNFILENAME, GooglePayContant.EFUN_CURRENT_ORDER_ID_KEY, orderId);
	}
	
	public static String getPreviousOrderId(Context ctx){
		return SPUtil.getSimpleString(ctx, GooglePayContant.EFUNFILENAME, GooglePayContant.EFUN_CURRENT_ORDER_ID_KEY);
	}
	
}
