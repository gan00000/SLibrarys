package com.starpy.sdk.ads.analytics;

import com.starpy.base.cfg.ResConfig;
import com.core.base.utils.SStringUtil;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Context;

public class GoogleAnalyticsV1 {

	public  static void tarckerEvent(Context ctx, String referrer){

		// GA v1.x
		GoogleAnalyticsTracker mtracker = GoogleAnalyticsTracker.getInstance();
		String tracking_id = ResConfig.getGoogleAnalyticsTrackingId(ctx);
		if (SStringUtil.isEmpty(tracking_id)) {
			//throw new RuntimeException("tracking_id为空或者配置不正确");
			return;
		}
	//	mtracker.startNewSession(tracking_id, 20, ctx);

		mtracker.setReferrer(referrer);
		/* GAServiceManager.getInstance().dispatch(); */
		//mtracker.dispatch();
		String gameCode = ResConfig.getGameCode(ctx);
		String TRACK_PAGE_VIEW_NAME = "/ADS_" + gameCode.toUpperCase();
		mtracker.trackPageView(TRACK_PAGE_VIEW_NAME);
	//	mtracker.
	
	}
	
	
}
