package com.starpy.base.js;

import org.json.JSONObject;

import com.starpy.base.component.SWebView;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class PlatNative2JS extends Native2JS {

	Handler handler = new Handler();
	public PlatNative2JS(Context context) {
		super(context);
	}

	public PlatNative2JS(Context context, SWebView efunWebView) {
		super(context, efunWebView);
		
	}
//	"{'key':'platformInfo'，'callback':'window.xxx.syncToCache'}
	/*platformInfo
	gameInfo
	deviceInfo*/
	
	@JavascriptInterface
	public String getSdkInfo(String jsJson) {//callback
		Log.d("efun", "keyJson:" + jsJson);
		if (TextUtils.isEmpty(jsJson)) {
			return "";
		}
		try {
			JSONObject jsonObject = new JSONObject(jsJson);
			String keyValue = jsonObject.optString("key", "");
			String func = jsonObject.optString("callback", "");
			if (TextUtils.isEmpty(keyValue)) {
				return "";
			}
			
			if (map != null && map.containsKey(keyValue) && !TextUtils.isEmpty(func)) {
				/*if (keyValue.equals("platformInfo")) {
					String js = func + "(" + map.get(keyValue) + ")";
					sWebView.executeJavascript(js);
					return "";
				} else if (keyValue.equals("gameInfo")) {
					String js = func + "(" + map.get(keyValue) + ")";
					sWebView.executeJavascript(js);
				} else if (keyValue.equals("deviceInfo")) {
					String js = func + "(" + map.get(keyValue) + ")";
					sWebView.executeJavascript(js);
				}*/
				final String js = func + "(" + map.get(keyValue) + ")";
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						
						sWebView.executeJavascript(js);
					}
				});
				return map.get(keyValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
