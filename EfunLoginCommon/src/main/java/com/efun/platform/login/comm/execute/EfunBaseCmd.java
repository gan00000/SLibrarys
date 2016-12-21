/**
 * 
 */
package com.efun.platform.login.comm.execute;

import com.efun.core.db.EfunDatabase;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.task.command.abstracts.EfunCommand;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunResourceUtil;
import com.efun.core.tools.GoogleAdUtil;
import com.efun.platform.login.comm.bean.CmdResponse;
import com.efun.platform.login.comm.bean.ListenerParameters;
import com.efun.platform.login.comm.dao.impl.EfunBaseLoginDao;
import com.efun.platform.login.comm.utils.EfunLoginHelper;
import com.efun.platform.login.comm.utils.LoinStringUtil;

import android.content.Context;
import android.text.TextUtils;

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
	protected String gameShortName;
	
	protected String appPlatform;
	protected String language = "";
	protected String region = "";
	
	protected ListenerParameters listenerParameters;
	/**
	 * @return the listenerParameters
	 */
	public ListenerParameters getListenerParameters() {
		return listenerParameters;
	}

	protected String loginType = "";
	
	protected String userArea = "";
	
	protected EfunBaseLoginDao dao;
	
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


	public EfunBaseCmd(Context context,EfunBaseLoginDao dao) {
		super();
		this.context = context;
		this.dao = dao;
		listenerParameters = new ListenerParameters();
	}

	
	@Override
	public void execute() throws Exception {
		EfunLoginHelper.logCurrentVersion();
		if (this.context == null) {
			throw new NullPointerException("com.efun.platform.execute.BaseThirdCmd.execute() > context is null");
		}
		
		if (LoinStringUtil.isEmpty(preferredUrl)) {
			preferredUrl = EfunResConfiguration.getLoginPreferredUrl(context);
		}
		if (LoinStringUtil.isEmpty(sparedUrl)) {
			sparedUrl = EfunResConfiguration.getLoginSpareUrl(context);
		}
		if (LoinStringUtil.isEmpty(gameCode)) {
			gameCode = EfunResConfiguration.getGameCode(context);
		}
		if (LoinStringUtil.isEmpty(appKey)) {
			appKey = EfunResConfiguration.getAppKey(context);
		}
		if (LoinStringUtil.isEmpty(gameShortName)) {
			gameShortName = EfunResConfiguration.getGameShortName(context);
		}
		
		if (LoinStringUtil.isEmpty(appPlatform)) {
			appPlatform = context.getResources().getString(EfunResourceUtil.findStringIdByName(context, "efunAppPlatform"));
			if (LoinStringUtil.isEmpty(appPlatform)) {
				throw new NullPointerException("please configure the efunAppPlatform in xml file,must not be null or “”");
			}
		}
		
		listenerParameters.setAppPlatform(appPlatform);
		
		listenerParameters.setGameCode(gameCode);
		listenerParameters.setAppKey(appKey);
		listenerParameters.setGameShortName(gameShortName);
		
		listenerParameters.setImei(null == EfunLocalUtil.getLocalImeiAddress(context)?"":EfunLocalUtil.getLocalImeiAddress(context));
		listenerParameters.setMac(null == EfunLocalUtil.getLocalMacAddress(context)?"":EfunLocalUtil.getLocalMacAddress(context));
		listenerParameters.setIp(null == EfunLocalUtil.getLocalIpAddress(context)?"":EfunLocalUtil.getLocalIpAddress(context));
		listenerParameters.setAndroidId(null == EfunLocalUtil.getLocalAndroidId(context)?"":EfunLocalUtil.getLocalAndroidId(context));
		listenerParameters.setSystemVersion(EfunLocalUtil.getOsVersion());
		listenerParameters.setDeviceType(EfunLocalUtil.getDeviceType());
		listenerParameters.setEid(EfunLocalUtil.getEfunUUid(context));
		
		if(LoinStringUtil.isEmpty(language)){
			language = EfunResConfiguration.getLanguage(context);
			if (LoinStringUtil.isEmpty(language)) {
				language = "";
			}
		}
		listenerParameters.setLanguage(language);
		
		listenerParameters.setReferrer(EfunLoginHelper.takeReferrer(context, ""));
		listenerParameters.setRegion(region);
		listenerParameters.setPackageName(context.getPackageName());
		listenerParameters.setVersionCode(EfunLoginHelper.getVersionCode(context));
		listenerParameters.setVersionName(EfunLocalUtil.getVersionName(context));
		
		if (TextUtils.isEmpty(listenerParameters.getPartner())) {
			listenerParameters.setPartner("efun");
		}
		
		//获取广告
		String advertisingId = EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_GOOGLE_ADVERTISING_ID);
		try {
			if (TextUtils.isEmpty(advertisingId)) {
				if (EfunLoginHelper.existClass("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {//判断google-play-server.jar是否存在
					advertisingId = GoogleAdUtil.getAdvertisingId(context);
					if (!TextUtils.isEmpty(advertisingId)) {
						EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_GOOGLE_ADVERTISING_ID, advertisingId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		listenerParameters.setAdvertisingId(advertisingId);
		
		this.userArea = EfunResourceUtil.findStringByName(context, "Region");
		/*if (TextUtils.isEmpty(userArea)) {
			Log.e("efun", "请在xml中配置EfunUserArea标签");
		}*/
		listenerParameters.setUserArea(this.userArea);
		if (dao != null) {
			dao.setPreferredUrl(preferredUrl);
			dao.setSparedUrl(sparedUrl);
			dao.setParameters(listenerParameters);
		}

		this.mResponse = dao.efunRequestServer();
		setRequestCompleteUrl(dao.getRequestCompleteUrl());
		this.cmdResponse  = EfunLoginHelper.parseResult(mResponse);
		//this.efunPerson = EfunLoginHelper.parserPersion(mResponse);
		
		if(cmdResponse == null){
			return;
		}
		this.cmdResponse.setRawResponse(mResponse);
		if (!TextUtils.isEmpty(this.cmdResponse.getUserId())) {
			//保存用戶uid
			EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_LOGIN_USER_ID, this.cmdResponse.getUserId());
			//EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_LOGIN_SERVER_RETURN_DATA, mResponse);
			
		}
		if (!TextUtils.isEmpty(this.cmdResponse.getSign())) {//保存sign,供其他接口需要使用
			EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, EFUN_LOGIN_SIGN, this.cmdResponse.getSign());
			EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, EFUN_LOGIN_TIMESTAMP, this.cmdResponse.getTimestamp());
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


	public String getGameShortName() {
		return gameShortName;
	}


	public void setGameShortName(String gameShortName) {
		this.gameShortName = gameShortName;
	}


	public String getAppPlatform() {
		return appPlatform;
	}


	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


/*	public ListenerParameters getListenerParameters() {
		return listenerParameters;
	}


	public void setListenerParameters(ListenerParameters listenerParameters) {
		this.listenerParameters = listenerParameters;
	}*/
	

}
