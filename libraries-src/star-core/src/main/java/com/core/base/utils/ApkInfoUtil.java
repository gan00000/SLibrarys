package com.core.base.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 本地信息帮助类
 */
public class ApkInfoUtil {
	
	private static String customizedUniqueId = "";

	public static String getApplicationName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo = getApplicationInfo(context);
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 获取当前的包信息
	 *
	 * @param context 上下文
	 * @return packageInfo
	 */
	public static ApplicationInfo getApplicationInfo(@NonNull Context context) {

		try {
			return context.getApplicationInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getNavigationBarHeight(Context context) {
	    Resources resources = context.getResources();
	    int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
	    int height = 0 ;
	    if(resourceId > 0){//2.4添加，过滤没有导航栏的设备
	    	height = resources.getDimensionPixelSize(resourceId);
	    }
		PL.i( "Navi height:" + height);
	    return height;
	}
	
	/**
	 * <p>Description: </p>
	 * @param ctx  
	 * @return
	 * @date 2015年10月12日
	 * 
	 */
	
	public static String getMacAddress(Context ctx) {

		String macTmp = "";
		try {
			WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			macTmp = info.getMacAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macTmp;
	}

	public static  String getCustomizedUniqueIdOrAndroidId(Context ctx){
		String s = getCustomizedUniqueId(ctx);
		if (TextUtils.isEmpty(s)){
			s = getAndroidId(ctx);
		}
		return s;
	}

	/**
	 * <p>Description: 获取植入SD卡的uuid</p>
	 * @param ctx
	 * @return
	 * @date 2015年10月12日
	 */
	private static synchronized String getCustomizedUniqueId(Context ctx) {

		if (!TextUtils.isEmpty(customizedUniqueId) && customizedUniqueId.length() >= 30) {
			return customizedUniqueId;
		}
		if (SdcardUtil.isExternalStorageExist()) {
			String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			String dataPath = sdcardPath + File.separator + "Android" + File.separator + "data" + File.separator;
			
			String dataTempPathDir = dataPath + "stardata" + File.separator;
			File dir = new File(dataTempPathDir);
			if (!dir.exists()) {
				if(!dir.mkdirs()){
					PL.w("没有添加android.permission.WRITE_EXTERNAL_STORAGE权限?");
				}
			}
			
			if (!dir.exists() || !dir.isDirectory()) {
				return "";
			}

			String dataTempPath = dataTempPathDir +"stardata-uuid.txt";

			try {
				customizedUniqueId = FileUtil.readFile(dataTempPath);
				if (!TextUtils.isEmpty(customizedUniqueId)) {
					return customizedUniqueId;
				}
				String uuid = UUID.randomUUID().toString();
				if (FileUtil.writeFileData(ctx, dataTempPath, uuid)) {
					customizedUniqueId = uuid;
				}
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return customizedUniqueId;

		}
		return "";
	}
	
	/**
	 * getImeiAddress Method
	 * Method Description : imei Address
	 * @author Joe
	 * @date 2013-1-23
	 */
	public static String getImeiAddress(Context ctx) {
		String imei = "";
		try {
			if (PermissionUtil.hasSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE)) {
				TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
				imei = telephonyManager.getDeviceId();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei;
	}

	/**
	 * getLocalIpAddress Method
	 * Method Description : Ip Address
	 * @author Joe
	 * @date 2013-1-23
	 */
	public static String getLocalIpAddress(Context ctx){
		WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
	    WifiInfo info = wifi.getConnectionInfo();
	    int ipInt = info.getIpAddress();
		String ipTmp = String.format("%d.%d.%d.%d", (ipInt & 0xff), (ipInt >> 8 & 0xff), (ipInt >> 16 & 0xff), (ipInt >> 24 & 0xff));

	/*    if (TextUtils.isEmpty(ipTmp) || ipTmp.equals("0.0.0.0")) {
	    	Log.d("efunLog", "getLocalIpAddress()");
	    	ipTmp = getLocalIpAddress();
		}*/

	    return ipTmp;

	}
	
	/**
	 * getAndroidId Method
	 * Method Description :get Android sys ID.
	 * @param ctx
	 * @author Joe
	 * @date 2013-1-25
	 */
	public static String getAndroidId(Context ctx) {
		String mAndroidId = "";
		try {
			mAndroidId = android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			if (TextUtils.isEmpty(mAndroidId)) {
				mAndroidId = android.provider.Settings.System.getString(ctx.getContentResolver(), android.provider.Settings.System.ANDROID_ID);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mAndroidId;
	}
	
	/**
	 * isNetworkAvaiable Method
	 * Method Description : wifi or cellular is Open.
	 * @return boolean
	 * @author Joe
	 * @date 2013-1-25
	 */
	public static boolean isNetworkAvaiable(Context ctx){  
	    ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
	    return (info != null && info.isConnected());
	}

	
	/**
	* <p>Title: isWifiAvailable</p>
	* <p>Description: 判断wifi是否可用</p>
	* @param context
	* @return
	*/
	public static boolean isWifiAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
		if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
			return false;
		} else {
			// NetworkInfo不为null开始判断是网络类型
			int netType = mobNetInfoActivity.getType();
			Log.i("efunLog", "netType:" + netType);
			if (netType == ConnectivityManager.TYPE_WIFI) {
				// wifi net处理
				return true;
			}
		}
		return false;
	}
	
	/**
	* <p>Title: getDeviceType</p>
	* <p>Description: 获取手机设备厂商和型号</p>
	* @return
	*/
	public static String getDeviceType(){
		String manufacturer = android.os.Build.MANUFACTURER;
		String modle = android.os.Build.MODEL;
		if (manufacturer == null) {
			manufacturer = "";
		}
		if (modle == null) {
			modle = "";
		}
		String deviceType =  manufacturer + "@@" + modle;
		
		return deviceType;
	}
	
	/**
	* <p>Title: getOsVersion</p>
	* <p>Description: 获取手机系统版本</p>
	* @return
	*/
	public static String getOsVersion(){
		String systemVersion = android.os.Build.VERSION.RELEASE;
		return systemVersion == null ? "" : systemVersion;
	}
	
	public static String getVersionCode(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			String version = String.valueOf(info.versionCode);
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getVersionName(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static int checkFastMobileNetwork(Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return NetworkType.NET_TYPE_2G; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return NetworkType.NET_TYPE_2G; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return NetworkType.NET_TYPE_2G; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return NetworkType.NET_TYPE_3G; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return NetworkType.NET_TYPE_3G; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return NetworkType.NET_TYPE_2G; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return NetworkType.NET_TYPE_3G; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return NetworkType.NET_TYPE_3G; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return NetworkType.NET_TYPE_3G; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return NetworkType.NET_TYPE_3G; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return NetworkType.NET_TYPE_3G; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return NetworkType.NET_TYPE_3G; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return NetworkType.NET_TYPE_3G; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return NetworkType.NET_TYPE_2G; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return NetworkType.NET_TYPE_4G; // ~ 10+ Mbps         4g
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return NetworkType.NET_TYPE_UNKNOW;
		default:
			return NetworkType.NET_TYPE_UNKNOW;

		}
	}
	
	public static class NetworkType{
		public static final int NET_TYPE_UNKNOW = 0;
		public static final int NET_TYPE_WIFI = 1;
		public static final int NET_TYPE_2G = 2;
		public static final int NET_TYPE_3G = 3;
		public static final int NET_TYPE_4G = 4;
	}
	/***
	 * 判断Network具体类型（wifi,2g,3g,4g）
	 * 
	 * */
	public static int getNetworkType(Context mContext) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
		if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
			return NetworkType.NET_TYPE_UNKNOW;
		}
		// NetworkInfo不为null开始判断是网络类型
		int netType = mobNetInfoActivity.getType();
		String netTypeName = mobNetInfoActivity.getTypeName();
		if (netType == ConnectivityManager.TYPE_WIFI && netTypeName.equalsIgnoreCase("WIFI")) {
			// wifi net处理
			return NetworkType.NET_TYPE_WIFI;
		} 
		try {
			if (netType == ConnectivityManager.TYPE_MOBILE) {
				int type_mobile = checkFastMobileNetwork(mContext);
				switch (type_mobile) {
				case NetworkType.NET_TYPE_2G:
					return NetworkType.NET_TYPE_2G;
				case NetworkType.NET_TYPE_3G:
					return NetworkType.NET_TYPE_3G;
				case NetworkType.NET_TYPE_4G:
					return NetworkType.NET_TYPE_4G;
				default:
					return NetworkType.NET_TYPE_UNKNOW;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NetworkType.NET_TYPE_UNKNOW;
	}


	public static String getSimOperator(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSimOperator();
	}

	
	public static String getLocaleLanguage(){
		return Locale.getDefault().getLanguage();
	}
	
	public static String getOsLanguage(){
		return getLocaleLanguage();
	}

}