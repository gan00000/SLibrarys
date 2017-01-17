package com.efun.google.utils;

import java.io.IOException;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

public class GoogleAdUtil {
	
	public static String getAdvertisingId(Context mContext){
		String advertisingId = "";
		try {
			Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
			advertisingId = adInfo.getId();
//			boolean isLAT = adInfo.isLimitAdTrackingEnabled();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GooglePlayServicesRepairableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return advertisingId;
	}
}
