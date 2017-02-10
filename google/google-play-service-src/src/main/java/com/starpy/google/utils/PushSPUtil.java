package com.starpy.google.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.efun.core.db.EfunDatabase;

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
	private static final String pullicon = "pullicon";
	private static final String PUSH_DISPATHER_GOOGLE_FIREBASE_CLASS_KEY = "PUSH_DISPATHER_GOOGLE_FIREBASE_CLASS_KEY";
	

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
	
	
	public static void saveDispatherClassName(Context context, String name){
		EfunDatabase.saveSimpleInfo(context, efun_pull_notification, PUSH_DISPATHER_GOOGLE_FIREBASE_CLASS_KEY, name);
	}
	
	public static String getDispatherClassName(Context context){
		return EfunDatabase.getSimpleString(context, efun_pull_notification, PUSH_DISPATHER_GOOGLE_FIREBASE_CLASS_KEY);
	}
}
