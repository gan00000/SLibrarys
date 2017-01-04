package com.efun.ads.event;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.efun.core.http.HttpRequest;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.task.EfunCommandAsyncTask;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.core.tools.GoogleAdUtil;

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
			m.put("gameCode", EfunResConfiguration.getGameCode(context));
		}else{
			m.put("gameCode", sdkEvent.getGameCode());
		}
		m.put("mac", EfunLocalUtil.getLocalMacAddress(context));
		m.put("imei", EfunLocalUtil.getLocalImeiAddress(context));
		m.put("androidid", EfunLocalUtil.getLocalAndroidId(context));
		m.put("osVersion", EfunLocalUtil.getOsVersion());
		m.put("phoneModel", EfunLocalUtil.getDeviceType());
		m.put("wifi", EfunLocalUtil.isWifiAvailable(context) ? "yes" : "no");
		m.put("language", Locale.getDefault().getLanguage());
		//m.put("userAgent", new WebView(context).getSettings().getUserAgentString());
		m.put("sign", EfunResConfiguration.getSDKLoginSign(context));
		m.put("loginTimestamp", EfunResConfiguration.getSDKLoginTimestamp(context));
		
		
		if (extraEvent != null && !extraEvent.isEmpty()) {
			for (Map.Entry<String, String> entry : extraEvent.entrySet()) {
				if (!m.containsKey(entry.getKey())) {
					m.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		if (TextUtils.isEmpty(postUrl)) {
			String adsUrl = EfunResConfiguration.getAdsPreferredUrl(context);
			if (TextUtils.isEmpty(adsUrl)) {
				adsUrl = EfunResConfiguration.getAdsSpareUrl(context);
			}
			if (TextUtils.isEmpty(adsUrl)) {
				return;
			}
			adsUrl = EfunStringUtil.checkUrl(adsUrl);
			postUrl = adsUrl + "ads_events.shtml";
		}
		
		EfunCommandAsyncTask.getSdkExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				if (TextUtils.isEmpty(advertisingId)) {
					advertisingId = GoogleAdUtil.getAdvertisingId(context);
				}
				m.put("advertising_id", advertisingId);
				String result = HttpRequest.post(postUrl, m);
				Log.d("EfunSdkEventsLogger", "logger result=" + result);
			}
		});
	}

}
