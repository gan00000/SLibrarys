package com.starpy.ads.event;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.core.base.http.HttpRequest;
import com.starpy.base.cfg.SConfig;
import com.core.base.request.SCommandAsyncTask;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.GoogleUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class EfunSdkEventsLogger {
	
	static String postUrl = "";
	static String advertisingId;
	
	/**
	 * <p>Description: sdk事件统计分析</p>
	 * @param context
	 * @param sdkEvent 事件对象
	 * @date 2015年11月10日
	 */
	public static void logEvent(Context context,SdkEvent sdkEvent){
		logEvent(context, sdkEvent,null);
	}
	

	/**
	 * <p>Description: sdk事件统计分析</p>
	 * @param context
	 * @param sdkEvent 事件对象
	 * @date 2015年11月10日
	 */
	public static void logEvent(final Context context,SdkEvent sdkEvent,HashMap<String, String> extraEvent){
		
		final Map<String, String> m = new HashMap<String, String>();
		m.put("type", "efunsdk");
		m.put("uid", sdkEvent.getUserId());
		m.put("serverCode", sdkEvent.getServerCode());
		m.put("roleId", sdkEvent.getRoleId());
		m.put("roleName", sdkEvent.getRoleName());
		m.put("roleLevel", sdkEvent.getRoleLevel());
		m.put("parentEvent", sdkEvent.getParentEvent());
		m.put("childEvent", sdkEvent.getChildEvent());
		if (TextUtils.isEmpty(sdkEvent.getGameCode())) {
			m.put("gameCode", SConfig.getGameCode(context));
		}else{
			m.put("gameCode", sdkEvent.getGameCode());
		}
		m.put("mac", ApkInfoUtil.getMacAddress(context));
		m.put("imei", ApkInfoUtil.getImeiAddress(context));
		m.put("androidid", ApkInfoUtil.getAndroidId(context));
		m.put("osVersion", ApkInfoUtil.getOsVersion());
		m.put("phoneModel", ApkInfoUtil.getDeviceType());
		m.put("wifi", ApkInfoUtil.isWifiAvailable(context) ? "yes" : "no");
		m.put("language", Locale.getDefault().getLanguage());
		//m.put("userAgent", new WebView(context).getSettings().getUserAgentString());
		m.put("sign", SConfig.getSDKLoginSign(context));
		m.put("loginTimestamp", SConfig.getSDKLoginTimestamp(context));
		
		
		if (extraEvent != null && !extraEvent.isEmpty()) {
			for (Map.Entry<String, String> entry : extraEvent.entrySet()) {
				if (!m.containsKey(entry.getKey())) {
					m.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		if (TextUtils.isEmpty(postUrl)) {
			String adsUrl = SConfig.getAdsPreferredUrl(context);
			if (TextUtils.isEmpty(adsUrl)) {
				adsUrl = SConfig.getAdsSpareUrl(context);
			}
			if (TextUtils.isEmpty(adsUrl)) {
				return;
			}
			adsUrl = SStringUtil.checkUrl(adsUrl);
			postUrl = adsUrl + "ads_events.shtml";
		}
		
		SCommandAsyncTask.getSdkExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				if (TextUtils.isEmpty(advertisingId)) {
					advertisingId = GoogleUtil.getAdvertisingId(context);
				}
				m.put("advertising_id", advertisingId);
				String result = HttpRequest.post(postUrl, m);
				Log.d("EfunSdkEventsLogger", "logger result=" + result);
			}
		});
	}

}
