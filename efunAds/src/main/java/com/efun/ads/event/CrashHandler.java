package com.efun.ads.event;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.efun.core.db.EfunDatabase;
import com.efun.core.http.HttpRequest;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.task.EfunRequestAsyncTask;
import com.efun.core.tools.EfunFileUtil;
import com.efun.core.tools.EfunLocalUtil;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

/**
 *
 * 崩溃日志收集
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private static CrashHandler sInstance = null;
	private static Object lock = new Object();
	//private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
	public static String fileName;
	public static String reportCrashUrl;
	
	static JSONObject  crashObj;
	private static UncaughtExceptionHandler mDefaultExceptionHandler;

	private CrashHandler() {
		
	}

	public static CrashHandler getInstance() {
		if (sInstance == null) {
			synchronized (lock) {
				if (sInstance == null)
					sInstance = new CrashHandler();
			}
		}
		return sInstance;
	}

	public void register(Context context,String gameCode) {
		mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		
		CrashHandler.fileName = context.getFilesDir().getAbsolutePath() + "/efuncrash/crash.log";
		if (crashObj == null) {
			crashObj = new JSONObject();
			try {
				crashObj.put("mac", EfunLocalUtil.getLocalMacAddress(context));
				crashObj.put("imei", EfunLocalUtil.getLocalImeiAddress(context));
				crashObj.put("androidid", EfunLocalUtil.getLocalAndroidId(context));
				crashObj.put("osVersion", EfunLocalUtil.getOsVersion());
				crashObj.put("phoneModel", EfunLocalUtil.getDeviceType());

				crashObj.put("language", EfunLocalUtil.getLocaleLanguage());
				crashObj.put("packageName", context.getPackageName());
				crashObj.put("versionCode", EfunLocalUtil.getVersionCode(context));
				crashObj.put("gameVersion", EfunLocalUtil.getVersionName(context));
				
				if(TextUtils.isEmpty(gameCode)) {
					crashObj.put("gameCode", EfunResConfiguration.getGameCode(context));
				}else{
					crashObj.put("gameCode", gameCode);
				}
				String efunUserId = EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_LOGIN_USER_ID);
				crashObj.put("userid", efunUserId);//需要取得UID
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		new EfunRequestAsyncTask() {
			
			@Override
			protected String doInBackground(String... params) {
				try {
					String fileContent = EfunFileUtil.readFile(fileName);
					if (TextUtils.isEmpty(fileContent)) {
						Log.d(TAG, "crash fileContent is empty");
						return "";
					}
					
					reportCrash(fileContent);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
		}.asyncExcute();
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		
		saveCashContent(ex);
		ex.printStackTrace();
		Log.e(TAG, ex.getMessage());
		if(Looper.myLooper() != Looper.getMainLooper()){
			Log.d(TAG, "not main looper:" + Thread.currentThread().getName());
			try {
				String crashContent = EfunFileUtil.readFile(fileName);
				if (!TextUtils.isEmpty(crashContent)) {
					reportCrash(crashContent);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			saveCashContent(ex);
		}
		excuteDefalseHandle(thread, ex);
		
	}
	
	public static void reportCrash(String crashContent){
		if (crashObj != null && !TextUtils.isEmpty(crashContent) && !TextUtils.isEmpty(reportCrashUrl)) {
			try {
				crashObj.put("crashContent", crashContent);
				String result = HttpRequest.postJsonObject(reportCrashUrl + "ad/ads_sdkLog.shtml", crashObj);
				Log.d(TAG, "crash result=" + result);
				if (result.contains("1000")) {
					EfunFileUtil.writeFileData(null, EfunFileUtil.createFile(fileName), "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void excuteDefalseHandle(Thread thread, Throwable ex){
		if (mDefaultExceptionHandler != null) {
			mDefaultExceptionHandler.uncaughtException(thread, ex);
		}else{
			Process.killProcess(Process.myPid());
		}
	}
	
	public void saveCashContent(Throwable ex){

		try {
			if (TextUtils.isEmpty(fileName)) {
				return ;
			}
			
			EfunFileUtil.createFile(fileName);
			
			PrintStream err = new PrintStream(fileName);
			ex.printStackTrace(err);
			err.close();
			Log.d(TAG, "saveCashContent finish");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}