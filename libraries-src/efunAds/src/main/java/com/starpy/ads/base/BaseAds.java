package com.starpy.ads.base;

import java.util.HashMap;
import java.util.Map;

import com.starpy.ads.bean.AdsHttpParams;
import com.starpy.ads.util.SPUtil;
import com.core.base.http.HttpRequest;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SStringUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public abstract class BaseAds implements AdsInterface {
	
	/**
	 * preferredUrl efun 首选域名
	 */
	protected String preferredUrl;
	
	/**
	 * spareUrl efun 备用域名
	 */
	protected String spareUrl;
	
	/**
	 * httpParams 请求所用的数据实体封装
	 */
	protected AdsHttpParams httpParams;
	
	protected Context context;
	
	/**
	* <p>Title: executePost</p>
	* <p>Description: 使用双域名执行网络请求，如果首选域名无数据返回，就转向调用备用域名</p>
	* @return 返回网络请求返回的数据
	*/
	protected String executePost() {
		Map<String, String> map = initAdsPostParams();
		Log.d("efun","map:" + map.toString());
		//List<NameValuePair> params = new ArrayList<NameValuePair>();
		String request = null;
		
//		for (Map.Entry<String, String> entry : map.entrySet()) {
//			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//		}		
		
		if (SStringUtil.isNotEmpty(preferredUrl)) {
			EfunLogUtil.logD("ads PreferredUrl:" + preferredUrl);
		//	request = EfunHttpUtil.efunExecutePostRequest(preferredUrl, params);
			request = HttpRequest.post(preferredUrl, map);
			Log.d("efunLog", "ads PreferredUrl respone:" + request);
		}
		if (SStringUtil.isEmpty(request) && SStringUtil.isNotEmpty(spareUrl)) {
			EfunLogUtil.logD("ads SpareUrl:" + spareUrl);
			request = HttpRequest.post(spareUrl, map);
			Log.d("efunLog", "ads SpareUrl respone:" + request);
		}
		return request;
	}

	/**
	* <p>Title: initAdsPostParams</p>
	* <p>Description: 初始化网络post请求参数</p>
	* @return 网络请求参数名与值对应的map实例对象
	*/
	private Map<String, String> initAdsPostParams() {

		Map<String, String> map = new HashMap<String, String>();
		map.put("timestamp", httpParams.getTimeStamp());
		map.put("signature", httpParams.getSignature());
		
		map.put("mac", httpParams.getLocalMacAddress());
		map.put("imei", httpParams.getLocalImeiAddress());
		map.put("ip", httpParams.getLocalIpAddress());
		map.put("androidid", httpParams.getLocalAndroidId());
		map.put("osVersion", httpParams.getOsVersion());
		map.put("phoneModel", httpParams.getPhoneModel());

		map.put("flage", httpParams.getFlage());

		map.put("gameCode", httpParams.getGameCode());
		if (!TextUtils.isEmpty(SPUtil.takeAdvertisersName(context, ""))) {
			Log.d("efun", "advertiser is not empty");
			map.put("advertiser", httpParams.getAdvertiser());
			map.put("partner", httpParams.getPartner());
		}
		map.put("referrer", httpParams.getReferrer());

		map.put("appPlatform", httpParams.getAppPlatform());
		
		map.put("adstartTime", httpParams.getAdstartTime());
		map.put("adendTime", httpParams.getAdendTime());
		
		map.put("region", httpParams.getRegion());
		map.put("packageName", httpParams.getPackageName());
		map.put("versionCode", httpParams.getVersionCode());
		map.put("gameVersion", httpParams.getVersionName());//游戏版本名称
		
		map.put("advertising_id", httpParams.getAdvertising_id());//google广告id

		map.put("wifi", httpParams.getWifi());
		map.put("userAgent", httpParams.getUserAgent());
		map.put("os_language", httpParams.getOs_language());
		
		return map;
	}


	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public AdsHttpParams getHttpParams() {
		return httpParams;
	}

	public void setHttpParams(AdsHttpParams httpParams) {
		this.httpParams = httpParams;
	}
	
	
}
