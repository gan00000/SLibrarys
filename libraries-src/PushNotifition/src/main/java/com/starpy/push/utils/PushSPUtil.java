package com.starpy.push.utils;

import com.starpy.base.utils.SPUtil;

import android.content.Context;
import android.content.SharedPreferences;

/**
* <p>Title: SPUtil</p>
* <p>Description: 文件保全工具类</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013-7-19
*/
public class PushSPUtil {
	
	public static final String efun_pull_notification = "efun.pullnotifition";
	public static final int FILE_MODEL = Context.MODE_PRIVATE;
	
	private static final int saveCount = 3;
	private static final String installTime = "installTime";
	private static final String lastLoginTime = "lastLoginTime";
	private static final String pullicon = "pullicon";
	private static final String PULLTIME = "pulltime";
	private static final String EFUN_GAME_CODE = "EFUN_GAMECODE";
	private static final String EFUN_PUSH_PREURL = "EFUN_PUSH_PREURL";
	private static final String EFUN_PUSH_SPAREURL = "EFUN_PUSH_SPAREURL";
	
	private static final String PUSH_APP_PLATFORM_KEY = "PUSH_APP_PLATFORM_KEY";
	private static final String PUSH_DISPATHER_CLASS_KEY = "PUSH_DISPATHER_CLASS_KEY";
	
	public static void saveAppPlatform(Context context,String appPlatform){
		SPUtil.saveSimpleInfo(context, efun_pull_notification, PUSH_APP_PLATFORM_KEY, appPlatform);
	}
	
	public static final String takeAppPlatform(Context context){
		return SPUtil.getSimpleString(context, efun_pull_notification, PUSH_APP_PLATFORM_KEY);
	}
	
	public static void saveSpareUrl(Context context, String preUrl){
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		sp.edit().putString(PushSPUtil.EFUN_PUSH_SPAREURL, preUrl).commit();
	}
	
	public static String takeSpareUrl(Context context,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		return sp.getString(PushSPUtil.EFUN_PUSH_SPAREURL, defValue);
	}
	
	public static void savePreUrl(Context context, String preUrl){
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		sp.edit().putString(PushSPUtil.EFUN_PUSH_PREURL, preUrl).commit();
	}
	
	public static String takePreUrl(Context context,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		return sp.getString(PushSPUtil.EFUN_PUSH_PREURL, defValue);
	}
	
	public static void saveGameCode(Context context, String gameCode){
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		sp.edit().putString(PushSPUtil.EFUN_GAME_CODE, gameCode).commit();
	}
	
	public static String takeGameCode(Context context,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		return sp.getString(PushSPUtil.EFUN_GAME_CODE, defValue);
	}
	
	public static void savePullTime(Context context, long time) {
		
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putLong(PushSPUtil.PULLTIME, time).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
	public static Long takePullTime(Context context,long defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		return sp.getLong(PushSPUtil.PULLTIME, defValue);
	}
	
	
	public static void savePullIcon(Context context, int icon) {
		
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putInt(PushSPUtil.pullicon, icon).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
	public static int takePullIcon(Context context,int defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
	
		return sp.getInt(PushSPUtil.pullicon, defValue);
	}
	
	
	public static void saveLastLoginTime(Context context, long mLastLoginTime) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putLong(PushSPUtil.lastLoginTime, mLastLoginTime).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
	public static Long takeLastLoginTime(Context context,Long defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		if (defValue == null) {
			return sp.getLong(PushSPUtil.lastLoginTime, 0);
		}
		return sp.getLong(PushSPUtil.lastLoginTime, defValue);
	}
	
	
	public static void saveInstallTime(Context context, long mInstallTime) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		//如果保存失败，尝试3此保存
		for (int i = 0; i < saveCount ; i++) {
			boolean saveSuccess = sp.edit().putLong(PushSPUtil.installTime, mInstallTime).commit();
			if (saveSuccess) {
				break;
			}
		}
	}
	
	public static Long takeInstallTime(Context context,Long defValue) {
		SharedPreferences sp = context.getSharedPreferences(efun_pull_notification, FILE_MODEL);
		if (defValue == null) {
			return sp.getLong(PushSPUtil.installTime, 0);
		}
		return sp.getLong(PushSPUtil.installTime, defValue);
	}
	
	public static void saveDispatherClassName(Context context, String name){
		SPUtil.saveSimpleInfo(context, efun_pull_notification, PUSH_DISPATHER_CLASS_KEY, name);
	}
	
	public static String getDispatherClassName(Context context){
		return SPUtil.getSimpleString(context, efun_pull_notification, PUSH_DISPATHER_CLASS_KEY);
	}
}
