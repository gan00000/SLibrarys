package com.starpy.sdk.ads.analytics;

import com.starpy.base.cfg.ResConfig;
import com.starpy.google.SGoogleProxy;

import android.content.Context;

public class GoogleAnalytics {

	public  static void tarckerEvent(Context ctx, String referrer){
		try {
			Class p = Class.forName("com.efun.google.SGoogleProxy");
			if (p != null) {
				//google分析事件追踪
				SGoogleProxy.SGoogleAnalytics.initDefaultTracker(ctx, ResConfig.getGoogleAnalyticsTrackingId(ctx)).setReferrer(referrer);
				SGoogleProxy.SGoogleAnalytics.trackEvent("app_install", "app_install", ResConfig.getGameCode(ctx) + "_install");
				return;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GoogleAnalyticsV1.tarckerEvent(ctx, referrer);
	
	}
	
	
}
