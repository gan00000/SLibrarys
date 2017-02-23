package com.starpy.base.utils;

import android.util.Log;

public class SLog {
	

	private static final String S_LOG_TAG = "S_LOG";

	private static boolean mDebugLog = false;
	private static boolean mInfo = false;

	public static void enableDebug(boolean debug){
		SLog.mDebugLog = debug;
	}
	
	public static void enableInfo(boolean mInfo){
		SLog.mInfo = mInfo;
	}
	
	public static  void logD(String msg) {
        if (mDebugLog) Log.i(S_LOG_TAG, msg + "");
    }
	
	public static  void logD(String tag, String msg) {
        if (mDebugLog) Log.i(tag, msg);
    }
	
	public static void logI(String msg) {
		if (mInfo) Log.i(S_LOG_TAG, msg + "");
	}
	
	public static void logI(String tag, String msg) {
		if (mInfo) Log.i(tag, msg);
	}
	
	public static void logE(String msg) {
		Log.e(S_LOG_TAG,  msg);
	}
	
	public static void logW(String msg) {
		Log.w(S_LOG_TAG, msg);
	}
	


/*	public static void efunToast(Context context,String message){
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
				String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "star" + File.separator
						+ "efunlog.properties";
				Properties prop = FileUtil.readProterties(logPath);
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
	}*/
	
}
