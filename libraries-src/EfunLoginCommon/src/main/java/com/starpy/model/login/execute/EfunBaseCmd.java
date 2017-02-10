/**
 * 
 */
package com.starpy.model.login.execute;

import android.content.Context;
import android.text.TextUtils;

import com.starpy.base.http.HttpRequest;
import com.starpy.base.http.HttpResponse;
import com.starpy.base.res.EfunResConfiguration;
import com.starpy.base.task.command.abstracts.EfunCommand;
import com.starpy.base.utils.ApkInfoUtil;
import com.starpy.base.utils.EfunLogUtil;
import com.starpy.base.utils.GoogleAdUtil;
import com.starpy.base.utils.ResUtil;
import com.starpy.base.utils.SPUtil;
import com.starpy.base.utils.SStringUtil;
import com.starpy.model.login.bean.CmdResponse;
import com.starpy.model.login.bean.ListenerParameters;
import com.starpy.model.login.utils.EfunLoginHelper;

import java.util.Map;

/**
 * <p>Title: EfunBaseCmd</p>
 * <p>Description: 请求接口对象封装类</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月10日
 */
public abstract class EfunBaseCmd extends EfunCommand {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private static final String EFUN_LOGIN_SIGN = "EFUN_LOGIN_SIGN";

	private static final String EFUN_LOGIN_TIMESTAMP = "EFUN_LOGIN_TIMESTAMP";

	protected Context context;
	
	protected String preferredUrl;
	protected String sparedUrl;
	
	protected String gameCode;
	protected String appKey;
	protected String language = "";

	protected ListenerParameters listenerParameters;
	/**
	 * @return the listenerParameters
	 */
	public ListenerParameters getListenerParameters() {
		return listenerParameters;
	}

	protected String loginType = "";
	
	protected String userArea = "";
	
	protected String mResponse;
	
	@Override
	public String getResponse(){
		return this.mResponse;
	}
	
//	protected EfunPerson efunPerson;
	protected CmdResponse cmdResponse;
	
	
	

	/**
	 * @return the cmdResponse
	 */
	public CmdResponse getCmdResponse() {
		return cmdResponse;
	}


	/**
	 * @param cmdResponse the cmdResponse to set
	 */
	public void setCmdResponse(CmdResponse cmdResponse) {
		this.cmdResponse = cmdResponse;
	}


	public EfunBaseCmd(Context context) {
		super();
		this.context = context;
		listenerParameters = new ListenerParameters();
	}

	
	@Override
	public void execute() throws Exception {
		EfunLoginHelper.logCurrentVersion();
		if (this.context == null) {
			throw new NullPointerException("execute context is null");
		}
		
		if (SStringUtil.isEmpty(preferredUrl)) {
			preferredUrl = EfunResConfiguration.getLoginPreferredUrl(context);
		}
		if (SStringUtil.isEmpty(sparedUrl)) {
			sparedUrl = EfunResConfiguration.getLoginSpareUrl(context);
		}
		if (SStringUtil.isEmpty(gameCode)) {
			gameCode = EfunResConfiguration.getGameCode(context);
		}
		if (SStringUtil.isEmpty(appKey)) {
			appKey = EfunResConfiguration.getAppKey(context);
		}


		listenerParameters.setGameCode(gameCode);
		listenerParameters.setAppKey(appKey);

		listenerParameters.setImei(null == ApkInfoUtil.getLocalImeiAddress(context)?"": ApkInfoUtil.getLocalImeiAddress(context));
		listenerParameters.setMac(null == ApkInfoUtil.getLocalMacAddress(context)?"": ApkInfoUtil.getLocalMacAddress(context));
		listenerParameters.setIp(null == ApkInfoUtil.getLocalIpAddress(context)?"": ApkInfoUtil.getLocalIpAddress(context));
		listenerParameters.setAndroidId(null == ApkInfoUtil.getLocalAndroidId(context)?"": ApkInfoUtil.getLocalAndroidId(context));
		listenerParameters.setSystemVersion(ApkInfoUtil.getOsVersion());
		listenerParameters.setDeviceType(ApkInfoUtil.getDeviceType());
	//	listenerParameters.setEid(ApkInfoUtil.getEfunUUid(context));
		
		if(SStringUtil.isEmpty(language)){
			language = EfunResConfiguration.getLanguage(context);
			if (SStringUtil.isEmpty(language)) {
				language = "";
			}
		}
		listenerParameters.setLanguage(language);
		
		listenerParameters.setReferrer(EfunLoginHelper.takeReferrer(context, ""));
		listenerParameters.setPackageName(context.getPackageName());
		listenerParameters.setVersionCode(EfunLoginHelper.getVersionCode(context));
		listenerParameters.setVersionName(ApkInfoUtil.getVersionName(context));
		
		if (TextUtils.isEmpty(listenerParameters.getPartner())) {
			listenerParameters.setPartner("star");
		}

		//获取广告
		String advertisingId = SPUtil.getSimpleString(context, SPUtil.EFUN_FILE, SPUtil.EFUN_GOOGLE_ADVERTISING_ID);
		try {
			if (TextUtils.isEmpty(advertisingId)) {
				if (EfunLoginHelper.existClass("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {//判断google-play-server.jar是否存在
					advertisingId = GoogleAdUtil.getAdvertisingId(context);
					if (!TextUtils.isEmpty(advertisingId)) {
						SPUtil.saveSimpleInfo(context, SPUtil.EFUN_FILE, SPUtil.EFUN_GOOGLE_ADVERTISING_ID, advertisingId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		listenerParameters.setAdvertisingId(advertisingId);
		
		this.userArea = ResUtil.findStringByName(context, "Region");


//		handleLoginResult();
		
		
	}

	protected void handleLoginResult() {
		this.cmdResponse  = EfunLoginHelper.parseResult(mResponse);
		//this.efunPerson = EfunLoginHelper.parserPersion(mResponse);

		if(cmdResponse == null){
			return;
		}
		this.cmdResponse.setRawResponse(mResponse);
		if (!TextUtils.isEmpty(this.cmdResponse.getUserId())) {
			//保存用戶uid
			SPUtil.saveSimpleInfo(context, SPUtil.EFUN_FILE, SPUtil.EFUN_LOGIN_USER_ID, this.cmdResponse.getUserId());
			//SPUtil.saveSimpleInfo(context, SPUtil.EFUN_FILE, SPUtil.EFUN_LOGIN_SERVER_RETURN_DATA, mResponse);

		}
		if (!TextUtils.isEmpty(this.cmdResponse.getSign())) {//保存sign,供其他接口需要使用
			SPUtil.saveSimpleInfo(context, SPUtil.EFUN_FILE, EFUN_LOGIN_SIGN, this.cmdResponse.getSign());
			SPUtil.saveSimpleInfo(context, SPUtil.EFUN_FILE, EFUN_LOGIN_TIMESTAMP, this.cmdResponse.getTimestamp());
		}
		if (TextUtils.isEmpty(loginType) && !TextUtils.isEmpty(listenerParameters.getThirdPlate())) {
			this.cmdResponse.setThirdPlateId(listenerParameters.getThirdPlateId());
			this.cmdResponse.setLoginType(listenerParameters.getThirdPlate());
		}else{
			this.cmdResponse.setLoginType(loginType);
		}
	}

	protected void saveLoginReponse(String reponse){
		EfunLoginHelper.saveLoginReponse(context, reponse);
	}

	public String getPreferredUrl() {
		return preferredUrl;
	}


	public void setPreferredUrl(String preferredUrl) {
		this.preferredUrl = preferredUrl;
	}


	public String getSparedUrl() {
		return sparedUrl;
	}


	public void setSparedUrl(String sparedUrl) {
		this.sparedUrl = sparedUrl;
	}


	public String getGameCode() {
		return gameCode;
	}


	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}


	public String getAppKey() {
		return appKey;
	}


	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
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


}
