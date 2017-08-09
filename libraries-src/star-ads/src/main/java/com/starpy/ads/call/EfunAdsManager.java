package com.starpy.sdk.ads.call;

import android.app.Activity;
import android.content.Context;

import com.starpy.sdk.ads.util.SPUtil;

public class EfunAdsManager {
	private static Activity activity;

	/**
	* <p>Title: getAdvertisersName</p>
	* <p>Description: Retrieve a String value from the preferences.</p>
	* @param context context object
	* @param defValue  Value to return if this preference does not exist.
	* @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
	*/
	public static String getAdvertisersName(Context context, String defValue){
		return SPUtil.takeAdvertisersName(context, defValue);
	}
	
	public static void setAdvertisersName(Context context, String advertisers){
		SPUtil.saveAdvertisersName(context, advertisers);
	}

	
	/**
	* <p>Title: getPartnerName</p>
	* <p>Description: Retrieve a String value from the preferences.</p>
	* @param context context object
	* @param defValue  Value to return if this preference does not exist. 
	* @return the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
	*/
	public static String getPartnerName(Context context, String defValue){
		return SPUtil.takePartnerName(context, defValue);
	}
	public static void setPartnerName(Context context, String partnerName){
		SPUtil.savePartnerName(context, partnerName);
	}
	
	public static void setActivity(Activity activity){
		EfunAdsManager.activity = activity;
	}
	
	public static Activity getActivity(){
		return activity;
	}
	
	public static String getLevel(Context context, String level, String defValue) {
		return SPUtil.takeLevel(context, level, defValue);
	}
	
	public static void saveLevel(Context context, String level){
		SPUtil.saveLevel(context, level);
	}
	
//	public static void saveInstallTime(Context context, Long installTime) {
//		 SPUtil.saveInstallTime(context, installTime);
//	}
//	
//	public static Long takeInstallTime(Context context,Long defValue) {
//		return SPUtil.takeInstallTime(context, defValue);
//	}
//	
//	public static void saveLastLoginTime(Context context, Long mLastLoginTime) {
//		SPUtil.saveLastLoginTime(context, mLastLoginTime);
//	}
//	
//	public static Long takeLastLoginTime(Context context,Long defValue) {
//		return SPUtil.takeLastLoginTime(context, defValue);
//	}
	
}
