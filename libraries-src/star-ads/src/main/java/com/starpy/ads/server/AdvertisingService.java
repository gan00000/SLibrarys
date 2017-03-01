/*package com.efun.ads.server;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import EfunLocalUtil;
import EfunLogUtil;
import EfunStringUtil;

*//**
* <p>Title: AdvertisingService</p>
* <p>Description: 此接口已经不用</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年7月31日
*//*
@Deprecated
public class AdvertisingService implements Runnable {

	private  String adsPreferredUrl = "";
	private  String adsSpareUrl = "";
	private  Map<String, String> params = null;
	
	private String S2S_RESULT_KEY = "S2S_RESULT";
	private String GA_RESULT_KEY = "GA_RESULT";
	private String S2S_RESULT_VALUE = "S2S_SUCCESS_200";
	private String GA_RESULT_VALUE = "GA_SUCCESS_200";
	
	private Context ctx = null;
	private String GAME_CODE = null;
	private String APP_KEY = null;
	private SharedPreferences settings = null;
	private SharedPreferences settingsS2S = null;
	private SharedPreferences settingsGA = null;
	private String ADVERTISERS_NAME = "";
	private String PARTNER_NAME = "";

	public static AdvertisingService getInstance() {
		return new AdvertisingService();
	}

	private AdvertisingService() {}
	
	public void egActivationAdvertising(Context ctx, String adsPreferredUrl,String adsSpareUrl, Map<String, String> params,Object... objects) {
		this.adsPreferredUrl = adsPreferredUrl;
		this.adsSpareUrl = adsSpareUrl;
		this.params = params;
		
		if (EfunStringUtil.isAllEmpty(adsPreferredUrl,adsSpareUrl)) {
			Log.e("efunLog","广告url设置有误");
			return;
		}
		SLog.logI("adsPreferredUrl:" + adsPreferredUrl);
		SLog.logI("adsSpareUrl:" + adsSpareUrl);
		this.ctx = ctx;
		this.settings = this.ctx.getSharedPreferences("ads.efun", Context.MODE_APPEND);
		this.settingsS2S = this.ctx.getSharedPreferences("ads.s2s.efun", this.ctx.MODE_PRIVATE);
		this.settingsGA = this.ctx.getSharedPreferences("ads.ga.efun", this.ctx.MODE_PRIVATE);
		int objectsLength = objects.length;
		SLog.logI( "init objectsLength with " + objectsLength + ".");
		if(objectsLength < 2){
			SLog.logI("Class ActivationAdvertisingService does not match the number of parameters.At least two parameters.");
			throw new RuntimeException("Class ActivationAdvertisingService does not match the number of parameters.At least two parameters.");
		}
		this.GAME_CODE = objects[0].toString();
		this.APP_KEY = objects[1].toString();
		if(objectsLength == 4){
			SLog.logI( "objectsLength is 4 add params.");
			this.ADVERTISERS_NAME = objects[2].toString();
			this.PARTNER_NAME = objects[3].toString();
		}
		String s2sIsSuccess = settings.getString(this.S2S_RESULT_KEY , null);
		String googleIsSuccess = settings.getString(this.GA_RESULT_KEY , null);
		String s2sIsSuccess = settingsS2S.getString(this.S2S_RESULT_KEY , null);
		String googleIsSuccess = settingsGA.getString(this.GA_RESULT_KEY , null);
		
		if(objectsLength == 2 && null != s2sIsSuccess && s2sIsSuccess.equals(S2S_RESULT_VALUE)){
			SLog.logI(s2sIsSuccess);
			return;
		}else if(objectsLength == 4 && null !=googleIsSuccess && googleIsSuccess.equals(GA_RESULT_VALUE)){
			SLog.logI( googleIsSuccess);
			return;
		}else if(null != this.ctx && EfunLocalUtil.isNetworkAvaiable(this.ctx)){
			Editor editor = settings.edit();
			if(objectsLength == 2){
				SLog.logI( "start Thread with objectsLength is 2.");
				//editor = settingsS2S.edit();
				editor.putString(this.S2S_RESULT_KEY, S2S_RESULT_VALUE);
			} else if(objectsLength == 4){
				SLog.logI( "start Thread with objectsLength is 4.");
				//editor = settingsGA.edit();
				editor.putString(this.GA_RESULT_KEY, GA_RESULT_VALUE);
			}
			editor.commit();
			
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		SendPostService.startSendPost(this.params,this.adsPreferredUrl,this.adsSpareUrl);
	}
}
*/