package com.core.base.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import com.core.base.SBaseWebView;
import com.core.base.js.Native2JS;
import com.core.base.js.PlatNative2JS;
import com.core.base.utils.EfunJSONUtil;

import org.json.JSONException;

import java.util.Map;

public class SWebView extends SBaseWebView {

	private static final String AndroidNativeJs = "AndroidNativeJs";
	private static final String ESDK = "ESDK";
	
	Native2JS native2js;
	PlatNative2JS platNative2JS;
	private Native2JS jsObject;
	
	/**
	 * @param jsObject the jsObject to set
	 */
	public void setJsObject(Native2JS jsObject) {
		this.jsObject = jsObject;
	}

	public SWebView(Context context) {
		super(context);
		initJavaScript();
	}

	@SuppressLint("NewApi")
	public SWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initJavaScript();
	}

	public SWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initJavaScript();
	}

	public SWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initJavaScript();
	}

	public void send2JS(Map<String, String> map) {
		
		try {
			String mapString = EfunJSONUtil.map2jsonString(map);
			map.put("all", mapString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		native2js.setMap(map);
		platNative2JS.setMap(map);
		if (jsObject != null) {
			jsObject.setMap(map);
		}
	}
	
	public void send2JS(String jsStrng){
		native2js.setCommonString(jsStrng);
		platNative2JS.setCommonString(jsStrng);
	}
	
	public void executeJavascript(String scriptName){
		this.loadUrl("javascript:" + scriptName);
	}

	
	@SuppressLint("SetJavaScriptEnabled")
	private void initJavaScript(){
		native2js = new Native2JS(getContext());
		platNative2JS = new PlatNative2JS(getContext(), this);
	}


	public void jsCallBack(String msg){
		
	}
	

	@Override
	public void loadUrl(String url) {
		this.addJavascriptInterface(native2js, AndroidNativeJs);
		if (jsObject == null) {
			this.addJavascriptInterface(platNative2JS, ESDK);
		}else {
			this.addJavascriptInterface(jsObject, ESDK);
		}
		super.loadUrl(url);
	}
	
	@Override
	public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
		this.addJavascriptInterface(native2js, AndroidNativeJs);
		if (jsObject == null) {
			this.addJavascriptInterface(platNative2JS, ESDK);
		}else {
			this.addJavascriptInterface(jsObject, ESDK);
		}
		super.loadUrl(url, additionalHttpHeaders);
	}
	
	@Override
	public void loadData(String data, String mimeType, String encoding) {
		this.addJavascriptInterface(native2js, AndroidNativeJs);
		if (jsObject == null) {
			this.addJavascriptInterface(platNative2JS, ESDK);
		}else {
			this.addJavascriptInterface(jsObject, ESDK);
		}
		super.loadData(data, mimeType, encoding);
	}
}
