package com.starpy.thirdlib.google;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 */
public class SGoogleFirebaseProxy {

	private FirebaseAnalytics mFirebaseAnalytics;

	private Activity activity;

	public SGoogleFirebaseProxy(Activity activity) {
		this.activity = activity;

		// Obtain the FirebaseAnalytics instance.
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity.getApplicationContext());
		//mFirebaseAnalytics.logEvent("starpy_install",null);
	}

	public void logEvent(String event){
		if (mFirebaseAnalytics != null){
			mFirebaseAnalytics.logEvent(event,null);
		}
	}

	public void logEvent(String event, Bundle bundle){
		if (mFirebaseAnalytics != null){
			mFirebaseAnalytics.logEvent(event,bundle);
		}
	}
}
