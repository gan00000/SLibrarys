/*package com.efun.ads.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.efun.core.http.EfunHttpUtil;
import EfunLogUtil;
import EfunStringUtil;

*//**
* <p>Title: SendPostService</p>
* <p>Description: 此接口已经不用</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年7月31日
*//*
@Deprecated
public class SendPostService {

	public static String startSendPost(Map<String, String> httpParms, String preferredUrl, String spareUrl) {
		SLogUtil.logI("httpParms:" + httpParms.toString());
		List<NameValuePair> params = null;
		String request = null;

		if (null == httpParms || httpParms.isEmpty()) {
			return null;
		}
		params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : httpParms.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		if (SStringUtil.isNotEmpty(preferredUrl)) {
			SLogUtil.logD("ads PreferredUrl:" + preferredUrl);
			request = EfunHttpUtil.efunExecutePostRequest(preferredUrl, params);
			Log.i("efunLog", "ads PreferredUrl request:" + request);
		}
		if (SStringUtil.isEmpty(request) && SStringUtil.isNotEmpty(spareUrl)) {
			SLogUtil.logD("ads SpareUrl:" + spareUrl);
			request = EfunHttpUtil.efunExecutePostRequest(spareUrl, params);
			Log.i("efunLog", "ads SpareUrl request:" + request);
		}
		return request;
	}
}
*/