/*package com.efun.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.efun.core.http.EfunHttpUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunStringUtil;

public class TaskRequest {
	
	private Map<String, String> map;
	private String preferredUrl;
	private String spareUrl;
	private RequestCallBack requestCallBack;
	private String urlSuffix = "";
	
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getPreferredUrl() {
		return preferredUrl;
	}

	public void setPreferredUrl(String preferredUrl) {
		this.preferredUrl = preferredUrl;
	}

	public String getSpareUrl() {
		return spareUrl;
	}

	public void setSpareUrl(String spareUrl) {
		this.spareUrl = spareUrl;
	}

	public String getUrlSuffix() {
		return urlSuffix;
	}

	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	public RequestCallBack getRequestCallBack() {
		return requestCallBack;
	}

	public void setRequestCallBack(RequestCallBack requestCallBack) {
		this.requestCallBack = requestCallBack;
	}

	
	public void asyncExecute(){
		EfunAsyncRequest asyncRequest = new EfunAsyncRequest();
		asyncRequest.execute();
	}
	
	private final class EfunAsyncRequest extends AsyncTask<Void, Void, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (null != requestCallBack) {
				requestCallBack.onPreExecute();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null != requestCallBack) {
				requestCallBack.onRequestFinish(result);
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> basicNameValuePairs = new ArrayList<NameValuePair>();
			String efunResponse = "";
			if (map != null) {
				if (map != null && map.size() > 0) {
					for (Map.Entry<String, String> entry : map.entrySet()) {
						basicNameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					EfunLogUtil.logD("map:" + map.toString());
					if (EfunStringUtil.isNotEmpty(preferredUrl)) {
						efunResponse  = EfunHttpUtil.efunExecutePostRequest(preferredUrl + urlSuffix, basicNameValuePairs);
					}
					if (EfunStringUtil.isEmpty(efunResponse) && EfunStringUtil.isNotEmpty(spareUrl)) {
						efunResponse = EfunHttpUtil.efunExecutePostRequest(spareUrl + urlSuffix, basicNameValuePairs);
					}
				}
			}
			return efunResponse;
		}
		
	}
	
	public interface RequestCallBack{
		
		void onPreExecute();
		
		void onRequestFinish(String result);
	}
}
*/