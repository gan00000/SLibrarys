/**
 * 
 */
package com.efun.platform.login.comm.dao.impl;

import java.util.Map;

import com.efun.core.exception.EfunException;
import com.efun.core.http.HttpRequest;
import com.efun.core.http.HttpResponse;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.bean.ListenerParameters;
import com.efun.platform.login.comm.dao.IEfunLoginDao;
import com.efun.platform.login.comm.utils.LoinStringUtil;

/**
 * <p>Title: EfunBaseLoginDao</p>
 * <p>Description: </p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */
public abstract class EfunBaseLoginDao implements IEfunLoginDao {

	protected ListenerParameters parameters = null;
	protected String preferredUrl = null;
	protected String sparedUrl = null;
	
	String requestCompleteUrl;
	
	public String getRequestCompleteUrl() {
		return requestCompleteUrl;
	}
	
	/* (non-Javadoc)
	 * @see com.efun.platform.login.comm.dao.IEfunLoginDao#efunLogin()
	 */
	@Override
	public String efunRequestServer() throws EfunException {
		// TODO Auto-generated method stub
		return null;
	}


	protected String checkUserAndPwd(ListenerParameters parameters){
		
		if (LoinStringUtil.isAllEmpty(parameters.getUserName(), parameters.getPassword())){
			return emptyReturn();
		}
		return null;
	}
	
	protected String checkEfunUserId(ListenerParameters parameters){
		
		if (LoinStringUtil.isEmpty(parameters.getEfunUserId())){
			return emptyReturn();
		}
		return null;
	}
	
	protected String checkUrl(String url){
		
		if (LoinStringUtil.isEmpty(url)){
			return null;
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		return url;
	}
	
	
	protected String emptyReturn() {
		EfunLogUtil.logI("Method efunLogin params userName or userPwd is null");
		return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
	}

	/**
	* <p>Title: doRequest</p>
	* <p>Description: 实际网络请求</p>
	* @param payActivity
	* @param efunDomainSite
	* @param requestParams
	* @return
	*/
	public String doRequest(String efunDomainSite,Map<String, String> requestParams){
		String efunResponse = "";
		if (EfunStringUtil.isNotEmpty(preferredUrl)) {
			preferredUrl = preferredUrl + efunDomainSite;
			EfunLogUtil.logD("preferredUrl:" + preferredUrl);
//			efunResponse = HttpRequest.post(preferredUrl, requestParams);
			HttpRequest httpRequest = new HttpRequest();
			HttpResponse hr = httpRequest.postReuqest(preferredUrl, requestParams);
			efunResponse = hr.getResult();
			requestCompleteUrl = hr.getRequestCompleteUrl();
			EfunLogUtil.logD("preferredUrl response: " + efunResponse);
		}
		if (EfunStringUtil.isEmpty(efunResponse) && EfunStringUtil.isNotEmpty(sparedUrl)) {
			sparedUrl = sparedUrl + efunDomainSite;
			EfunLogUtil.logD("spareUrl Url: " + sparedUrl);
//			efunResponse = HttpRequest.post(sparedUrl, requestParams);
			
			HttpRequest httpRequest = new HttpRequest();
			HttpResponse hr = httpRequest.postReuqest(sparedUrl, requestParams);
			efunResponse = hr.getResult();
			requestCompleteUrl = hr.getRequestCompleteUrl();
			
			EfunLogUtil.logD("spareUrl response: " + efunResponse);
		}
		return efunResponse;
	}
	
	/**
	 * @return the parameters
	 */
	public ListenerParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ListenerParameters parameters) {
		this.parameters = parameters;
	}

	public String getPreferredUrl() {
		return preferredUrl;
	}

	public void setPreferredUrl(String preferredUrl) {
		preferredUrl = checkUrl(preferredUrl);
		this.preferredUrl = preferredUrl;
	}

	public String getSparedUrl() {
		return sparedUrl;
	}

	public void setSparedUrl(String sparedUrl) {
		sparedUrl = checkUrl(sparedUrl);
		this.sparedUrl = sparedUrl;
	}
}
