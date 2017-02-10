package com.starpy.base.utils;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class EfunLogUtil {
	

	private static final String TAG = "efunLog";

	private static final int CAN_LOG = 2;
	private static final int CAN_NOT_LOG = 3;
	
	private static boolean mDebugLog = false;
	private static boolean mInfo = false;
	public static int logFlag = 1;
	public static String can_log_debug = "";
	public static String can_log_info = "";
	
	private static boolean canToast = false;
	private static boolean isInit = false;
	
	public static void enableDebug(boolean debug){
		EfunLogUtil.mDebugLog = debug;
	}
	
	public static void enableInfo(boolean mInfo){
		EfunLogUtil.mInfo = mInfo;
	}
	
	public static  void logD(String msg) {
        if (mDebugLog) Log.d(TAG, msg + "");
        efunDebug("", msg + "");
    }
	
	public static  void logD(String tag, String msg) {
        if (mDebugLog) Log.d(tag, msg);
        efunDebug(tag, msg);
    }
	
	public static void logI(String msg) {
		if (mInfo) Log.i(TAG, msg + "");
		efunInfo("", msg + "");
	}
	
	public static void logI(String tag, String msg) {
		if (mInfo) Log.i(tag, msg);
		efunInfo(tag, msg);
	}
	
	public static void logE(String msg) {
		Log.e(TAG,  msg);
	}
	
	public static void logW(String msg) {
		Log.w(TAG, msg);
	}
	
	public static void logUrl(final String url, final Map<String, String> params){
		if (params == null || params.isEmpty()) {
			return;
		}
		
		String urlString = url + "?" + SStringUtil.map2strData(params);
		
		if (mDebugLog) {
			EfunLogUtil.logD("http url:" + urlString);
		}else{
			efunDebug("", "http url:" + urlString);
		}
	}
	
	private static void efunDebug(String tag,String msg) {
		if (mDebugLog) {
			return;
		}
		if (logFlag == 1) {
			initLog();
		}
		if (logFlag == CAN_LOG && can_log_debug.equals("true")) {
			if (TextUtils.isEmpty(tag)) {
				Log.d(TAG, msg);
			}else{
				Log.d(tag, msg);
			}
		}
	}
	private static void efunInfo(String tag,String msg) {
		if (mInfo) {
			return;
		}
		if (logFlag == 1) {
			initLog();
		}
		if (logFlag == CAN_LOG && can_log_info.equals("true")) {
			if (TextUtils.isEmpty(tag)) {
				Log.i(TAG, msg);
			}else{
				Log.i(tag, msg);
			}
		}
	}
	
	public static void efunToast(Context context,String message){
		//Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		if (context == null || message == null ) {
			return;
		}
		if (!isInit) {
			initLog();
		}
		if (canToast) {
			Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		}
	}
	
	private static void initLog() {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "efunlog" + File.separator
						+ "efunlog.properties";
				Properties prop = EfunFileUtil.readProterties(logPath);
				if (null != prop) {
					if (prop.getProperty("debug", "").equals("true")) {
						can_log_debug = "true";
					}
					if (prop.getProperty("info", "").equals("true")) {
						can_log_info = "true";
					}
					if(prop.getProperty("toast", "").equals("true")){
						canToast = true;
					}
					if (can_log_debug.equals("true") || can_log_info.equals("true")) {
						logFlag = CAN_LOG;
						//return;
					}else{
						logFlag = CAN_NOT_LOG;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logFlag = CAN_NOT_LOG;
		}
		
		//logFlag = CAN_NOT_LOG;
		isInit = true;
	}
	
}
