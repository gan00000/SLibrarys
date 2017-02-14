package com.core.base.utils;

import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

public class GoogleUtil {
	
	static String advertisingId = "";

	/**
	 * 非UI线程调用，UI线程调用造成anr
	 * @param mContext
	 * @return
	 */
	public static String getAdvertisingId(Context mContext){

		if (!ClassUtil.existClass("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {//判断google-play-server.jar是否存在
			return "";
		}
		if (!TextUtils.isEmpty(advertisingId)) {
			return advertisingId;
		}
		try {
			Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
			advertisingId = adInfo.getId();
//			boolean isLAT = adInfo.isLimitAdTrackingEnabled();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (GooglePlayServicesRepairableException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return advertisingId;
	}
}
