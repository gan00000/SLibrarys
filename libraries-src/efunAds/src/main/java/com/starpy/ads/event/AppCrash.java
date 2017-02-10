package com.starpy.ads.event;

import com.core.base.res.SConfig;

import android.content.Context;

public class AppCrash {
	
	public static void init(Context context,String gameCode){

		CrashHandler.getInstance().register(context,gameCode);
		setReportCrashUrl(SConfig.getAdsPreferredUrl(context));
	}
	
	public static void setReportCrashUrl(String reportCrashUrl){
		CrashHandler.reportCrashUrl = reportCrashUrl;
	}
	public static void reportCrash(String crashContent){
		CrashHandler.getInstance().reportCrash(crashContent);
	}
}
