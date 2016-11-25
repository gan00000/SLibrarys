package com.efun.core.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONObject;

import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunStringUtil;

import android.content.Context;
import android.text.TextUtils;

public class HttpRequest {
	
	public HttpRequest() {
		// TODO Auto-generated constructor stub
	}
	

	public static String get(String urlStr,Map<String, String> dataMap) {
		return getWithAccessToken(urlStr, dataMap, true);
	}
	
	private static String getWithAccessToken(String urlStr,Map<String, String> dataMap,boolean accessToken) {
		if (TextUtils.isEmpty(urlStr)) {
			return "";
		}
		HttpRequestCore requestCore = new HttpRequestCore();
		if (dataMap != null) {
			dataMap = putCommonParams(dataMap);
		}else{
			try {//拼接accessToken请求
				if (accessToken && !TextUtils.isEmpty(EfunResConfiguration.getSDKLoginReturnData()) && !urlStr.contains("&accessToken=") && urlStr.contains("?")) {
					String appendAccessTokenUrl = urlStr + "&accessToken=" + URLEncoder.encode(EfunResConfiguration.getSDKLoginReturnData(), "UTF-8");
					HttpResponse mHttpResponse = requestCore.excuteGetRequest(appendAccessTokenUrl, null);
					if (mHttpResponse.getHttpResponseCode() == 200 && !TextUtils.isEmpty(mHttpResponse.getResult())) {
						return mHttpResponse.getResult();
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		HttpResponse hr = requestCore.excuteGetRequest(urlStr, dataMap);
		return hr.getResult();
	}
	
	public static String get(String urlStr) {
		return get(urlStr, null);
	}
	
	public static String get(String urlStr,boolean accessToken) {
		return getWithAccessToken(urlStr, null, accessToken);
	}
	
	public static String getIn2Url(String preUrl,String sprUrl) {
		return getIn2Url(preUrl, sprUrl,true);
	}
	
	public static String getIn2Url(String preUrl,String sprUrl,boolean accessToken) {
		String preResult = get(preUrl,accessToken);
		if (TextUtils.isEmpty(preResult)) {
			return get(sprUrl,accessToken);
		}
		return preResult;
	}
	
	public static String getVerticallineAccessToken(Context context, String urlStr) {
		if (!TextUtils.isEmpty(urlStr) && urlStr.contains("|") && !TextUtils.isEmpty(EfunResConfiguration.getSDKLoginReturnData(context))) {
			urlStr = urlStr + "|" + EfunResConfiguration.getSDKLoginReturnData(context);
			HttpRequestCore requestCore = new HttpRequestCore();
			return requestCore.excuteGetRequest(urlStr).getResult();
		}
		return "";
	}
	
	public HttpResponse getReuqest(String urlStr) {
		HttpRequestCore requestCore = new HttpRequestCore();
		return requestCore.excuteGetRequest(urlStr);
	}
	
	public HttpResponse getReuqestIn2Url(String preUrl,String sprUrl) {
		HttpResponse s = getReuqest(preUrl);
		if (s != null && TextUtils.isEmpty(s.getResult())) {
			return getReuqest(preUrl);
		}
		return s;
	}
	
	
	/**
	 * <p>Description: 发送post请求</p>
	 * @param urlStr 请求地址
	 * @return  请求的结果
	 * @date 2015年10月9日
	 */
	public static String post(String urlStr,Map<String, String> dataMap) {
		dataMap = putCommonParams(dataMap);
		HttpRequestCore requestCore = new HttpRequestCore();
		HttpResponse hr = requestCore.excutePostRequest(urlStr, dataMap);
		if (hr != null) {
			return hr.getResult();
		}else{
			return "";
		}
	}
	
	public  HttpResponse postReuqest(String urlStr,Map<String, String> dataMap) {
		dataMap = putCommonParams(dataMap);
		HttpRequestCore requestCore = new HttpRequestCore();
		return requestCore.excutePostRequest(urlStr, dataMap);
	}
	
	
	public static String post(String urlStr,byte[] dataByte) {
		HttpRequestCore requestCore = new HttpRequestCore();
		return requestCore.postByteData(urlStr, dataByte);
	}
	
	
	public static String postIn2Url(String preUrl,String sprUrl,Map<String, String> dataMap) {
		String result = post(preUrl, dataMap);
		if (TextUtils.isEmpty(result)) {
			result = post(sprUrl, dataMap);
		}
		return result;
	}
	
	public static String postIn2Url(String preUrl,String sprUrl,String mInterfaceName,Map<String, String> dataMap) {
		preUrl = EfunStringUtil.checkUrl(preUrl) + mInterfaceName;
		String result = post(preUrl, dataMap);
		
		if (TextUtils.isEmpty(result)) {//备用域名请求
			sprUrl = EfunStringUtil.checkUrl(sprUrl) + mInterfaceName;
			result = post(sprUrl, dataMap);
		}
		return result;
	}
	
	
	public static String postJsonObject(String urlStr,JSONObject jsonObject) {
		HttpRequestCore requestCore = new HttpRequestCore();
		requestCore.setSendData(jsonObject.toString());
		requestCore.setRequestUrl(urlStr);
		requestCore.setContentType("application/json");
		return requestCore.doPost().getResult();
	}
	
	public static boolean downLoadUrlFile(String downLoadFileUrl,String savePath) {
		HttpRequestCore requestCore = new HttpRequestCore();
		return requestCore.downLoadUrlFile(downLoadFileUrl, savePath);
	}
	
	
	private static Map<String, String> putCommonParams(Map<String, String> dataMap){
		if (dataMap == null) { 
			return null;
		}
		
		/*if (!dataMap.containsKey("sign")) {
			dataMap.put("sign", EfunResConfiguration.getLoginSign());
		}
		if (!dataMap.containsKey("loginTimestamp")) {
			dataMap.put("loginTimestamp", EfunResConfiguration.getLoginTimestamp());
		}*/
		
		if (!dataMap.containsKey("os_language")) {
			dataMap.put("os_language", EfunLocalUtil.getOsLanguage());
		}
		
		if (!dataMap.containsKey("eid")) {
			dataMap.put("eid", EfunLocalUtil.efun_uuid);
		}
		
		if (!TextUtils.isEmpty(EfunResConfiguration.getSDKLoginReturnData())) {
			dataMap.put("accessToken", EfunResConfiguration.getSDKLoginReturnData());
		}
		return dataMap;
	}
}
