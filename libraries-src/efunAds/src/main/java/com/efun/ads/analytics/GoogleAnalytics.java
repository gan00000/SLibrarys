package com.efun.ads.analytics;

import com.efun.core.res.EfunResConfiguration;
import com.efun.google.EfunGoogleProxy;

import android.content.Context;

public class GoogleAnalytics {

	public  static void tarckerEvent(Context ctx, String referrer){
		try {
			Class p = Class.forName("com.efun.google.EfunGoogleProxy");
			if (p != null) {
				//google分析事件追踪
				EfunGoogleProxy.EfunGoogleAnalytics.initDefaultTracker(ctx, EfunResConfiguration.getGoogleAnalyticsTrackingId(ctx)).setReferrer(referrer);
				EfunGoogleProxy.EfunGoogleAnalytics.trackEvent("app_install", "app_install", EfunResConfiguration.getGameCode(ctx) + "_install");
				return;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GoogleAnalyticsV1.tarckerEvent(ctx, referrer);
	
	}
	
	
}
