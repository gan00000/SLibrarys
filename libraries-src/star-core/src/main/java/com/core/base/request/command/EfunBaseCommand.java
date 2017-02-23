package com.core.base.request.command;

import java.io.Serializable;
import java.util.Map;

import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.request.EfunCommandCallBack;

import android.text.TextUtils;


public class EfunBaseCommand implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String commandId;
	private String commandMsg;
	private EfunCommandCallBack callback;
	private boolean showProgress = true;
	
	public EfunBaseCommand(){	
		this.commandId = null;		
		this.callback = null;
		this.commandMsg = null;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getCommandMsg() {
		return commandMsg;
	}

	public void setCommandMsg(String commandMsg) {
		this.commandMsg = commandMsg;
	}

	public EfunCommandCallBack getCallback() {
		return callback;
	}
	public void setCallback(EfunCommandCallBack callback) {
		this.callback = callback;
	}

	public boolean isShowProgress() {
		return showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}
	
	
	protected String requestCompleteUrl;
	
	public String getRequestCompleteUrl() {
		return requestCompleteUrl;
	}



	HttpResponse httpResponse;
	
	/**
	 * @return the httpResponse
	 */
	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	/**
	 * <p>Description: </p>
	 * @param requestParams 请求参数	
	 * @param requestUrls  请求连接
	 * @return
	 * @date 2016年8月15日
	 */
	protected HttpResponse doRequest_Post(Map<String, String> requestParams,String... requestUrls){
		
		for (String url : requestUrls) {
			
			if (!TextUtils.isEmpty(url)) {
				HttpRequest httpRequest = new HttpRequest();
				httpResponse = httpRequest.postReuqest(url, requestParams);
				String efunResponse = httpResponse.getResult();
				if (!TextUtils.isEmpty(efunResponse)) {
					requestCompleteUrl = httpResponse.getRequestCompleteUrl();
					return httpResponse;
				}
			}
			
		}
		return null;
		
	}
}
