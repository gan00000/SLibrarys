/*package com.efun.ads.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import S2SListener;
import EfunDomainSite;
import EfunJsonUtil;
import SPUtil;
import EfunResConfiguration;
import EfunLocalUtil;
import EfunLogUtil;
import EfunResourceUtil;
import EfunStringUtil;

*//**
* <p>Title: AdvertService</p>
* <p>Description: 此接口已经不用</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年7月31日
*//*
@Deprecated
public class AdvertService {
	
	private boolean gaIsCome = false;
	private volatile String advertiser = "";
	private volatile String partner = "";
	private volatile String referrer = "";
	private volatile String efunThirdPlat = "";
	private Context context;
	private Lock lock = new ReentrantLock();// 实例化一个锁对象
	private Condition condition = lock.newCondition();
	
	private static AdvertService advertService = null;
	
	private AdvertService(){
		
	}
	
	public static AdvertService getInstance(){
		if (advertService == null) {
			synchronized (AdvertService.class) {
				if (advertService == null) {
					advertService = new AdvertService();
				}
			}
		}
		return advertService;
	}
	
	public  void s2sWaitForGAAndSendPost(final S2SListener mS2sListener,Handler handler, Map<String, String> params){
		
		if (params == null || params.isEmpty()) {
			Log.e("efunLog","广告参数设置有误");
			return;  
		}


		String localCode = SPUtil.takeResponeCode(this.context,"");
		if (SStringUtil.isNotEmpty(localCode)) {
			if ("1000".equals(localCode) || "1001".equals(localCode) || "1006".equals(localCode) || "1003".equals(localCode)) {
				Log.d("efunLog", "ads already  has local ads code:" + localCode);
				return;
			}
		}
		
		
		if(SStringUtil.isEmpty(advertiser) && SStringUtil.isEmpty(partner) && SStringUtil.isEmpty(referrer)){
			
			lock.lock();
			try {
				while (!gaIsCome) {
					Log.i("efunLog", "广告--进入线程等待");
					try {
						condition.await(15, TimeUnit.SECONDS);
						if (gaIsCome) {
							Log.i("efunLog", "广告--等待结束,GA广播到达");
						} else {
							Log.i("efunLog", "广告--等待结束,没有GA广播到达");
						}
						gaIsCome = true;
					} catch (InterruptedException e) {
						Log.w("efunLog", "线程等待InterruptedException" + e.getMessage());
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				lock.unlock();
			}
		}
		
		
		EfunLogUtil.logD("before send post.");
		params.put("advertiser", advertiser);
		params.put("partner", partner);
		params.put("referrer", referrer);
//		params.put("efunthirdplat", this.efunThirdPlat);
		
		Log.i("efunLog", "advertiser:" + advertiser +",partner:" + partner + ",referrer:" + referrer + ",efunThirdPlat:" + efunThirdPlat);
		String adsRespone = "";
		
		if (SStringUtil.isEmpty(this.efunThirdPlat)) {
			Log.i("efunLog","efunThirdPlat is empty");
			String adsPreferredUrl = EfunResConfiguration.getAdsPreferredUrl(this.context);
			String adsSpareUrl = EfunResConfiguration.getAdsSpareUrl(this.context);
			if (SStringUtil.isAllEmpty(adsPreferredUrl,adsSpareUrl)) {
				Log.e("efunLog","广告url设置有误");
				return;
			}
			if (SStringUtil.isNotEmpty(adsPreferredUrl)) {
				adsPreferredUrl = adsPreferredUrl + EfunDomainSite.EFUN_ADS;
			}
			if (SStringUtil.isNotEmpty(adsSpareUrl)) {
				adsSpareUrl = adsSpareUrl + EfunDomainSite.EFUN_ADS;
			}
			adsRespone = SendPostService.startSendPost(params,adsPreferredUrl,adsSpareUrl);
		} else {
			Log.i("efunLog","efunThirdPlat is not empty");
			String efunThirdPlatPreferredUrl = EfunResConfiguration.getAdsPreferredUrl(context);
			String efunThirdPlatSpareUrl = EfunResConfiguration.getAdsSpareUrl(context);
			if (SStringUtil.isAllEmpty(efunThirdPlatPreferredUrl,efunThirdPlatSpareUrl)) {
				Log.e("efunLog","广告url设置有误");
				return;
			}
			if (SStringUtil.isNotEmpty(efunThirdPlatPreferredUrl)) {
				efunThirdPlatPreferredUrl = efunThirdPlatPreferredUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
			}
			if (SStringUtil.isNotEmpty(efunThirdPlatSpareUrl)) {
				efunThirdPlatSpareUrl = efunThirdPlatSpareUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
			}
			adsRespone = SendPostService.startSendPost(params,efunThirdPlatPreferredUrl,efunThirdPlatSpareUrl);
		}
		
		final String adsCode = EfunJsonUtil.adsJsonCode(adsRespone);
		*//**
		 * 1003：空值异常，标识gameCode=null 
		 * 1000：激活成功
		 * 1001：缓存没有记录，表示没有匹配到 
		 * 1006:该设备已经安装过该游戏
		 *//*
		if (null != this.context && SStringUtil.isNotEmpty(adsCode)) {
			if ("1000".equals(adsCode) || "1001".equals(adsCode) || "1006".equals(adsCode) || "1003".equals(adsCode)) {
				if (null != context && null != mS2sListener && null != handler) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							EfunLogUtil.logD("ads save return code:" + adsCode);
							SPUtil.saveResponeCode(AdvertService.this.context, adsCode);
							mS2sListener.s2sResultRunOnlyOne(AdvertService.this.context);
						}
					});
				}
			} else {
				EfunLogUtil.logD("ads return code is not 1000,1001,1006,1003");
			}
		} else {
			EfunLogUtil.logD("ads return code save fail");
		}
		
	}
	
	public  void checkGACome(String advertiser,String partner,String referrer,String efunThirdPlat){
		lock.lock();
		try {
			gaIsCome = true;
			this.advertiser = advertiser;
			this.partner = partner;
			this.referrer = referrer;
			this.efunThirdPlat = efunThirdPlat;
			EfunLogUtil.logI("广告--GA广播到达");
			//通知线程
			condition.signal();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		
	}
	
	public  Map<String, String> initAdsPostParams(Context context) {
		
		String localAndroidId = (null == ApkInfoUtil.getLocalAndroidId(context)?"":ApkInfoUtil.getLocalAndroidId(context));
		String localMacAddress = (null == ApkInfoUtil.getLocalMacAddress(context)?"":ApkInfoUtil.getLocalMacAddress(context));
		String localImeiAddress = (null == ApkInfoUtil.getLocalImeiAddress(context)?"":ApkInfoUtil.getLocalImeiAddress(context));
		String localIpAddress = (null == ApkInfoUtil.getLocalIpAddress(context)?"":ApkInfoUtil.getLocalIpAddress(context));
		String appPlatform = context.getResources().getString(EfunResourceUtil.findStringIdByName(context, "efunAppPlatform"));
		if (SStringUtil.isEmpty(appPlatform)) {
			throw new RuntimeException("please configure the efunAppPlatform in xml file,must not be null or “”");
		}
		
		String gameCode = EfunResConfiguration.getGameCode(context);
		String appKey = EfunResConfiguration.getAppKey(context);
		
		if (SStringUtil.isEmpty(gameCode) || SStringUtil.isEmpty(appKey)) {
			throw new RuntimeException("请先AndroidManifest.xml配置好广告url、gameCode、appKey");
		}
		
		String timeStamp = System.currentTimeMillis()/1000 + "";
		String signature  = SStringUtil.toMd5(appKey+ timeStamp + localMacAddress + gameCode + localImeiAddress + localIpAddress + localAndroidId, false);
		Map<String, String> params = new HashMap<String, String>();
		params.put("timestamp", timeStamp);
		params.put("signature", signature);
		params.put("mac", localMacAddress);
		params.put("imei", localImeiAddress);
		params.put("ip", localIpAddress);
		params.put("androidid", localAndroidId);
		
		params.put("flage", "iSNew_20130130");
		params.put("osVersion", ApkInfoUtil.getOsVersion());
		params.put("phoneModel", ApkInfoUtil.getDeviceType());
		
		params.put("gameCode", gameCode);
		
		advertiser = SPUtil.takeAdvertisersName(context, "");
		partner = SPUtil.takePartnerName(context, "");
		referrer = SPUtil.takeReferrer(context, "");
		
		params.put("advertiser", advertiser);
		params.put("partner", partner);
		params.put("referrer", referrer);
		
		params.put("appPlatform", appPlatform);
		
		this.efunThirdPlat = SPUtil.takeEfunAdsThirdPlat(context, "");
		
		Log.i("efunLog", "mac:" + localMacAddress +
				",imei:" + localImeiAddress +
				",ip:" + localIpAddress +
				",gameCode:"+ gameCode +
				",androidid:" + localAndroidId +
				",osVersion:" + ApkInfoUtil.getOsVersion() +
				",phoneModel:" + ApkInfoUtil.getDeviceType());
		
		return params;
	}

	*//**
	 * @return the context
	 *//*
	public Context getContext() {
		return context;
	}

	*//**
	 * @param context the context to set
	 *//*
	public void setContext(Context context) {
		this.context = context;
	}
	
}

*/