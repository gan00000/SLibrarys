package com.efun.core.js;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.efun.core.component.EfunWebView;
import com.efun.core.db.EfunDatabase;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLocalUtil;

public class Native2JS {

	Context context;
	String commonString = "";
	Map<String, String> map;
	EfunWebView efunWebView;
	
	
	public Native2JS(Context context) {
		this.context = context;
	}
	
	public Native2JS(Context context,EfunWebView efunWebView) {
		this.context = context;
		this.efunWebView = efunWebView;
	}

	/**
	 * @return the commonString
	 */
	@JavascriptInterface
	public String getCommonString() {
		return commonString;
	}
	
	@JavascriptInterface
	public String getCommonString(String key) {
		if (!TextUtils.isEmpty(key) && map != null && map.containsKey(key)) {
			return map.get(key);
		}
		return "";
	}
	
	
	/**
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	/**
	 * @param commonString the commonString to set
	 */
	public void setCommonString(String commonString) {
		this.commonString = commonString;
	}

	
	@JavascriptInterface
	public String getPackageName(){
		return context.getPackageName();
	}
	
	@JavascriptInterface
	public String getVersionCode(){
		return EfunLocalUtil.getVersionCode(context);
	}
	
	@JavascriptInterface
	public String getDeviceType(){
		return EfunLocalUtil.getDeviceType();
	}
	@JavascriptInterface
	public String getAndroidId(){
		return EfunLocalUtil.getLocalAndroidId(context);
	}
	
	@JavascriptInterface
	public String getImei(){
		return EfunLocalUtil.getLocalImeiAddress(context);
	}
	
	@JavascriptInterface
	public String getIp(){
		return EfunLocalUtil.getLocalIpAddress(context);
	}
	
	@JavascriptInterface
	public String getMac(){
		return EfunLocalUtil.getLocalMacAddress(context);
	}
	
	@JavascriptInterface
	public String getOsVersion(){
		return EfunLocalUtil.getOsVersion();
	}
	
	@JavascriptInterface
	public String getVersionName(){
		return EfunLocalUtil.getVersionName(context);
	}
	
	@JavascriptInterface
	public String getUserName(){
		return EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_LOGIN_USERNAME);
	}
	@JavascriptInterface
	public String getPassword(){
		return EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_LOGIN_PASSWORD);
	}
	
	@JavascriptInterface
	public String getUserId(){
		return EfunResConfiguration.getCurrentEfunUserId(context);
	}
	
	@JavascriptInterface
	public String getTimesStamp(){
		return EfunResConfiguration.getSDKLoginTimestamp(context);
	}
	
	@JavascriptInterface
	public String getSign(){
		return EfunResConfiguration.getSDKLoginSign(context);
	}
	
	@JavascriptInterface
	public String getRoleId(){
		return "";
	}
	
	@JavascriptInterface
	public String getRoleName(){
		return "";
	}
	
	@JavascriptInterface
	public String getServerCode(){
		return "";
	}
	
	@JavascriptInterface
	public String getLanguage(){
		return EfunResConfiguration.getSDKLanguage(context);
	}
	
	@JavascriptInterface
	public String getLoginType(){
		return "";
	}
	
	@JavascriptInterface
	public String getGameCode(){
		return EfunResConfiguration.getGameCode(context);
	}
	
	@JavascriptInterface
	public void finishActivity(){
		if (context != null && context instanceof Activity) {
			Log.d("efun", "activity finish");
			((Activity)context).finish();
		}
	}
	
	@JavascriptInterface
	public void close(){
		finishActivity();
	}
	
//	gameInfo：{"userid":"1","serverCode":"1","roleLevel":"1","gameCode":"sehzw","roleId":"EFUN_1"}
//	gameLoginInfo：{"userid":"1","sign":"CB7AEE4499008034774E75586C6F24BD","timestamp":"1428048053685","partner":"efun","loginType":"efun"}
//	deviceInfo：{"systemVersion":"4.3.1","mac":"d4:6e:5c:41:d1:2f","deviceType":"HUAWEI G700-T00","imei":"860457020317674","ip":"172.16.80.159"}
	
	@JavascriptInterface
	public String getDeviceInfo(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("systemVersion", EfunLocalUtil.getOsVersion());
			jsonObject.put("mac", EfunLocalUtil.getLocalMacAddress(context));
			jsonObject.put("deviceType", EfunLocalUtil.getDeviceType());
			jsonObject.put("imei", EfunLocalUtil.getLocalImeiAddress(context));
			jsonObject.put("ip", EfunLocalUtil.getLocalIpAddress(context));
			jsonObject.put("androidid", EfunLocalUtil.getLocalAndroidId(context));
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	@JavascriptInterface
	public void setSDKMsg(String msg){
		final String m = msg;
		if (efunWebView != null) {
			efunWebView.getHandler().post(new Runnable() {
				
				@Override
				public void run() {
					efunWebView.jsCallBack(m);
				}
			});
			
		}
	}
	
}
