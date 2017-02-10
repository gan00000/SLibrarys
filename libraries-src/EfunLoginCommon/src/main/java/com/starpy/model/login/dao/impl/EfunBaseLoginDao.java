/**
 * 
 */
package com.starpy.model.login.dao.impl;

import java.util.Map;

import com.core.base.exception.EfunException;
import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.LoginBaseRequest;
import com.starpy.model.login.dao.IEfunLoginDao;

/**
 * <p>Title: EfunBaseLoginDao</p>
 * <p>Description: </p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */
public abstract class EfunBaseLoginDao implements IEfunLoginDao {

	protected LoginBaseRequest parameters = null;
	protected String preferredUrl = null;
	protected String sparedUrl = null;
	
	String requestCompleteUrl;
	
	public String getRequestCompleteUrl() {
		return requestCompleteUrl;
	}
	
	/* (non-Javadoc)
	 * @see IEfunLoginDao#efunLogin()
	 */
	@Override
	public String efunRequestServer() throws EfunException {
		// TODO Auto-generated method stub
		return null;
	}


	protected String checkUserAndPwd(LoginBaseRequest parameters){
		
		if (SStringUtil.isAllEmpty(parameters.getUserName(), parameters.getPassword())){
			return emptyReturn();
		}
		return null;
	}
	

	protected String checkUrl(String url){
		
		if (SStringUtil.isEmpty(url)){
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
	* @param efunDomainSite
	* @param requestParams
	* @return
	*/
	public String doRequest(String efunDomainSite,Map<String, String> requestParams){
		String efunResponse = "";
		if (SStringUtil.isNotEmpty(preferredUrl)) {
			preferredUrl = preferredUrl + efunDomainSite;
			EfunLogUtil.logD("preferredUrl:" + preferredUrl);
//			efunResponse = HttpRequest.post(preferredUrl, requestParams);
			HttpRequest httpRequest = new HttpRequest();
			HttpResponse hr = httpRequest.postReuqest(preferredUrl, requestParams);
			efunResponse = hr.getResult();
			requestCompleteUrl = hr.getRequestCompleteUrl();
			EfunLogUtil.logD("preferredUrl response: " + efunResponse);
		}
		if (SStringUtil.isEmpty(efunResponse) && SStringUtil.isNotEmpty(sparedUrl)) {
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
	public LoginBaseRequest getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(LoginBaseRequest parameters) {
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
