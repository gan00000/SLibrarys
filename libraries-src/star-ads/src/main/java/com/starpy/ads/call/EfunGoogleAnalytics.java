package com.starpy.sdk.ads.call;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class EfunGoogleAnalytics {
	
	private static GoogleAnalyticsTracker tracker;
	
	public static void startNewSession(Context context,String analyticsAccount){
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession(analyticsAccount,context);
	}
	
	public static void trackEvent(String category, String action, String label) {
		if (null != tracker) {
			tracker.trackEvent(category, // Category
					action, // Action
					label, // Label
					77); // Value
			tracker.dispatch();
		}
	}
	
	public static void stopSession(){
		 if (null != tracker) {
			tracker.stopSession();
		}
	}
	
	//新版google分析
/*	private static Tracker mTracker;
	  *//**
	   * @return tracker
	   *//*
	synchronized public Tracker getDefaultTracker(Context context, String trackingId) {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
			mTracker = analytics.newTracker(trackingId);
		}
		return mTracker;
	}

	public static void trackEvent(String category, String action, String label) {

		if (null != mTracker) {
			mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
		}
	}
	
	public static void stopSession(){
		
	}*/
}
