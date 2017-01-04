package com.efun.push.client;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.efun.core.db.EfunDatabase;
import com.efun.core.http.HttpRequest;
import com.efun.core.task.EfunRequestAsyncTask;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.push.client.utils.PushHelper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class PushRequest{
	
	public static final String SEND_UUID_SUCCESS_KEY = "SEND_UUID_SUCCESS_KEY";
	public static final String SEND_UUID_SUCCESS_VALUE = "SEND_UUID_SUCCESS_VALUE";
	
	public static final String GAMECODE = "gameCode";
	public static final String VERSIONCODE = "versionCode";
	public static final String MAC = "mac";
	public static final String IMEI = "imei";
	public static final String APPPLATFORM = "appPlatform";
	public static final String PACKAGENAME = "packageName";
	public static final String TOKEN = "token";
	public static final String ORGUUID = "orgUUID";
	public static final String MESSAGEID = "messageId";
	
	private final static String PUSH_RECEIVEPROPELL = "push_receivePropell.shtml";
	private static final String TAG = "efun_PushRequest";
	private static final String ADVERTISER = "advertiser";
	
	private String preUrl = "";
	private String spaUrl = "";
	
	private String gameCode;
//	private String versionCode;
//	private String mac;
//	private String imei;
	private String appPlatform;
//	private String packageName;
//	private String token;
//	private String orgUUID;
	
	private String messageId;
	private String advertiser;
	

	Map<String, String> paramsMap = new HashMap<String, String>();
	
	public void sendUUIDToServer(final Context context){
		
		/*String sendUUIDSuccess = EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, SEND_UUID_SUCCESS_KEY);
		if (sendUUIDSuccess.equals(SEND_UUID_SUCCESS_VALUE)) {
			Log.d(TAG, "SEND_UUID_SUCCESS_VALUE:" + sendUUIDSuccess);
			return;
		}*/
		
		String mac = EfunLocalUtil.getLocalMacAddress(context);
		String imei = EfunLocalUtil.getLocalImeiAddress(context);
		paramsMap.put(GAMECODE, gameCode);
		paramsMap.put(VERSIONCODE, EfunLocalUtil.getVersionName(context));
		paramsMap.put(MAC, mac);
		paramsMap.put(IMEI, imei);
		paramsMap.put(APPPLATFORM, appPlatform);
		paramsMap.put(PACKAGENAME, context.getPackageName());
		paramsMap.put(TOKEN, "");
		paramsMap.put(ADVERTISER, advertiser);
		paramsMap.put(ORGUUID, PushHelper.generateUUID(context));
		paramsMap.put("eid", EfunLocalUtil.getEfunUUid(context));
		new EfunRequestAsyncTask() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (!TextUtils.isEmpty(result) && "1000".equals(result)) {
					EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, SEND_UUID_SUCCESS_KEY, SEND_UUID_SUCCESS_VALUE);
				}
			}

			@Override
			protected String doInBackground(String... params) {
				String result = HttpRequest.postIn2Url(preUrl, spaUrl, PUSH_RECEIVEPROPELL, paramsMap);
				EfunLogUtil.logD("PushRequest:" + result);
				//{"code":"1000","message":"token或者orgUUID已保存過"}
				if (TextUtils.isEmpty(result)) {
					return "";
				}
				try {
					JSONObject resultJson = new JSONObject(result);
					String code = resultJson.optString("code", "");
					return code;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "";
			}
		}.asyncExcute();
		
	}

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public String getSpaUrl() {
		return spaUrl;
	}

	public void setSpaUrl(String spaUrl) {
		this.spaUrl = spaUrl;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}


	public String getAppPlatform() {
		return appPlatform;
	}

	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}


	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	/**
	 * @return the advertiser
	 */
	public String getAdvertiser() {
		return advertiser;
	}

	/**
	 * @param advertiser the advertiser to set
	 */
	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

}
