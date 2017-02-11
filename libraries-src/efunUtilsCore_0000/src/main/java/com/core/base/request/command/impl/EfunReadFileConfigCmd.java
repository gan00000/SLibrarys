package com.core.base.request.command.impl;

import java.util.Date;

import com.core.base.http.HttpRequestCore;
import com.core.base.request.command.abstracts.EfunCommand;
import com.core.base.utils.EfunLogUtil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class EfunReadFileConfigCmd extends EfunCommand {

	private String fileUrl = null;
	private String saveFilePath = null;
	String result = null;
	Context context;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EfunReadFileConfigCmd(Context context) {
		this.context = context;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	@Override
	public String getResponse() {
		return result;
	}
	
	private long requestTime;
	
	/**
	 * @return the requestTime
	 */
	public long getRequestTime() {
		return requestTime;
	}

	@Override
	public void execute() throws Exception {
		if (fileUrl == null || "".equals(fileUrl.trim())) {
			Log.e("efun", "fileUrl is empty");
			return;
		}
		EfunLogUtil.logD("开始下载：" + fileUrl);
		requestTime = System.currentTimeMillis();
	/*	HttpGet request = new HttpGet(fileUrl);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
		client.getParams().setParameter("http.socket.timeout", Integer.valueOf(10000));
		HttpResponse response = client.execute(request);
		if (response == null) {
			return;
		}
		EfunLogUtil.logD("file result code:" + response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			EfunLogUtil.logD("file result:" + result);
			if (result == null || "".equals(result.trim())) {
				return;
			}
			
			try {
				Header[] h = response.getHeaders("Date");
				String serverTime = h[0].getValue();
				requestTime = new Date(serverTime).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		
		HttpRequestCore r = new HttpRequestCore();
		result = r.excuteGetRequest(fileUrl).getResult();
		String t = r.getHttpResponse().getServerTime();
		if (!TextUtils.isEmpty(t)) {
			requestTime = new Date(t).getTime();
		}
	}

}
