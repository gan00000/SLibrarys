package com.starpy.googlepay.util;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.cfg.ResConfig;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.bean.WebPayReqBean;
import com.starpy.googlepay.constants.GooglePayContant;

public class PayHelper {
	
	public static String getPreferredUrl(BasePayActivity payActivity){
		String preferredUrl = payActivity.getPayPreferredUrl();
		if (SStringUtil.isEmpty(preferredUrl)) {
			preferredUrl = ResConfig.getPayPreferredUrl(payActivity);
		}
		return checkUrl(preferredUrl);
	}
	
	public static String getSpareUrl(BasePayActivity payActivity){
		String spareUrl = payActivity.getPaySpareUrl();
		if (SStringUtil.isEmpty(spareUrl)) {
			spareUrl = ResConfig.getPaySpareUrl(payActivity);
		}
		return checkUrl(spareUrl);
	}

	public static WebPayReqBean buildWebPayBean(Context context, String cpOrderId, String roleLevel, String extra){
		WebPayReqBean webPayReqBean = new WebPayReqBean(context);

		if (SStringUtil.isEmpty(webPayReqBean.getSimOperator())) {
			String simOperator = "";
			if (ApkInfoUtil.isWifiAvailable(context)) {
				simOperator = GooglePayContant.WIFI;
			} else {
				simOperator = ApkInfoUtil.getSimOperator(context);
			}
			webPayReqBean.setSimOperator(simOperator);
		}

		webPayReqBean.setUserId(StarPyUtil.getUid(context));
		webPayReqBean.setCpOrderId(cpOrderId);
		webPayReqBean.setRoleLevel(roleLevel);
		webPayReqBean.setExtra(extra);

		return webPayReqBean;

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
