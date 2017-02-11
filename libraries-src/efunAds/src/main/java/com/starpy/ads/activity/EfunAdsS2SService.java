package com.starpy.ads.activity;

import com.starpy.ads.bean.AdsHttpParams;
import com.starpy.ads.callback.S2SListener;
import com.starpy.ads.server.AdsRequest;
import com.starpy.ads.util.AdsHelper;
import com.starpy.ads.util.SPUtil;
import com.core.base.res.SConfig;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SStringUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;

public class EfunAdsS2SService extends Service {
	
	private S2SListener s2sListener = null;
	private Handler efunAdsHandler;
	private AdsHttpParams adsHttpParams;
	private boolean runS2SFlag = true;
	public static final String EFUN_S2S_RUN_FLAG = "EFUN_S2S_RUN_FLAG";
	public static final String EFUN_ADSHTTP_PARAMS = "EFUN_ADSHTTP_PARAMS";
	
	/**
	 * 判断线程是否正在运行
	 */
	public volatile static boolean threadWaitting = false; 
	
	private int id = -1;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onStart(final Intent intent ,int startId){
		// TODO Auto-generated method stub
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SPUtil.adsCurrentVersion();
		adsHttpParams = (AdsHttpParams) intent.getSerializableExtra(EfunAdsS2SService.EFUN_ADSHTTP_PARAMS);
		runS2SFlag = intent.getBooleanExtra(EfunAdsS2SService.EFUN_S2S_RUN_FLAG, true);
		if (adsHttpParams == null) {
			adsHttpParams = new AdsHttpParams();
		}
		 if (s2sListener == null) {
			//获取在应用<meta-data>元素。
			try {
				String s2slistener = SConfig.getS2SListenerName(this);
	
				EfunLogUtil.logI("s2slistener: " + s2slistener);
				if (SStringUtil.isNotEmpty(s2slistener) && s2slistener.startsWith("com.")) {
	
					@SuppressWarnings("unchecked")
					Class<S2SListener> clazz = (Class<S2SListener>) Class.forName(s2slistener);
					if (clazz != null) {
						EfunLogUtil.logI("实例化S2SListener的实现类...");
						s2sListener = clazz.newInstance();
					}
				}
	
			} catch (ClassNotFoundException e) {
				EfunLogUtil.logE("EfunAdsS2SService里面的ClassNotFoundException异常,全限定类名配置对了吗");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				EfunLogUtil.logE("EfunAdsS2SService里面的IllegalAccessException异常");
				e.printStackTrace();
			} catch (InstantiationException e) {
				EfunLogUtil.logE("EfunAdsS2SService里面的InstantiationException异常");
				e.printStackTrace();
			}
		}
		 
		if (s2sListener != null) {
			EfunLogUtil.logI( "执行添加在EfunAdsS2SService里面的广告,每次");
			s2sListener.s2sRunEvery(this,intent, startId);
		}
		//检测是否有线程在等待GA广播
		EfunLogUtil.logD("AdsRequest.threadWaitting is:" + threadWaitting);
		EfunLogUtil.logD("runS2SFlag:" + runS2SFlag);
	
		if (s2sListener != null && runS2SFlag && !threadWaitting && !AdsHelper.existLocalResponeCode(this)) {
			threadWaitting = true;
			if (null != adsHttpParams) {
//				AdsHelper.initParams(this, adsHttpParams);
				if (null == efunAdsHandler) {
					efunAdsHandler = new Handler();
				}
			//	adsHttpParams.setUserAgent(AdsHelper.getUserAgent(EfunAdsS2SService.this));
				
				AdsRequest.getInstance().setContext(this);
				AdsRequest.getInstance().setAdsHttpParams(adsHttpParams);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						AdsHelper.initParams(EfunAdsS2SService.this, adsHttpParams);
						AdsRequest.getInstance().setAdsHttpParams(adsHttpParams);
						Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);
						AdsRequest.getInstance().requestAds(s2sListener, efunAdsHandler);
					}
				}).start();
				
			}
		}
		
		if(id != -1 || intent == null){
			EfunLogUtil.logI("EfunAdsS2SService is running（已经被启动过）...");
			return Service.START_NOT_STICKY;
		}
		
		id = startId;
	    
	    if(exitsLocalAdsCode()){
			EfunLogUtil.logI("onlyOnceForADS already called");
			return Service.START_NOT_STICKY;
		} else {
			EfunLogUtil.logI( "start Advertisers.");
			if (s2sListener != null) {
				SPUtil.saveAdsOnlyFlag(this);
				EfunLogUtil.logI( "执行添加在EfunAdsS2SService里面的广告,仅仅一次");
				s2sListener.onlyOnceForADS(EfunAdsS2SService.this, intent, startId);
		   }
		}
		return Service.START_NOT_STICKY;
	}

	private boolean exitsLocalAdsCode() {
		SharedPreferences ads_settings_old = this.getSharedPreferences(SPUtil.ads_efun, Context.MODE_PRIVATE);
		String advertisersResult = ads_settings_old.getString(SPUtil.ADVERTISERS_S2S_KEY, null);
		if (null != advertisersResult && (advertisersResult.equals(SPUtil.ADVERTISERS_S2S_RESULT))) {
			EfunLogUtil.logD( "has old local data--ADVERTISERS_SUCCESS_200...Efun.ads");
			return true;
		}
		advertisersResult = this.getSharedPreferences(SPUtil.ads_efun_older, Context.MODE_PRIVATE).getString(SPUtil.ADVERTISERS_S2S_KEY, null);
		if (null != advertisersResult && (advertisersResult.equals(SPUtil.ADVERTISERS_S2S_RESULT))) {
			EfunLogUtil.logD( "has old local data--ADVERTISERS_SUCCESS_200...ads.efun");
			return true;
		}
		String advertisersResult_new = SPUtil.takeAdsOnlyFlag(this, "");
		if (SStringUtil.isNotEmpty(advertisersResult_new) && advertisersResult_new.equals(SPUtil.ADS_ONLYONCE_CODE)) {
			EfunLogUtil.logD( "has new local data--ADS_ONLYONCE_CODE");
			return true;
		}
		return false;
	}
	
	
	@Override
	public void onDestroy() {
		EfunLogUtil.logI("service's onDestroy");
		if (s2sListener != null) {
			s2sListener.s2sonDestroy(this);
		}
		super.onDestroy();

	}
	
	@Override
	public boolean stopService(Intent intent) {
		EfunLogUtil.logI("service's stopService");
		if (s2sListener != null) {
			s2sListener.s2sonStopServic(this,intent);
		}
	    return super.stopService(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	

}