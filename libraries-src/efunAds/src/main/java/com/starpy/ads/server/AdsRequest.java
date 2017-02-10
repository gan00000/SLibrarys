package com.starpy.ads.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.starpy.ads.activity.EfunAdsS2SService;
import com.starpy.ads.base.BaseAds;
import com.starpy.ads.bean.AdsHttpParams;
import com.starpy.ads.callback.S2SListener;
import com.starpy.ads.impl.AdsImpl;
import com.starpy.ads.util.AdsHelper;
import com.starpy.ads.util.EfunJsonUtil;
import com.starpy.ads.util.SPUtil;
import com.starpy.base.utils.EfunLogUtil;
import com.starpy.base.utils.SStringUtil;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


public class AdsRequest {
	
	private boolean gaIsCome = false;
	
	private volatile String efunThirdPlat = "";
	
	private volatile static AdsHttpParams adsHttpParams;
	
	private BaseAds baseAds = new AdsImpl();
	
	private Context context;
	
	
	private Lock lock = new ReentrantLock();// 实例化一个锁对象
	private Condition condition = lock.newCondition();
	
	private static AdsRequest adsRequest = null;
	
	private AdsRequest(){
		
	}
	
	/**
	* <p>Title: getInstance</p>
	* <p>Description: 获取AdsRequest唯一实例对象</p>
	* @return 返回AdsRequest唯一实例对象
	*/
	public static AdsRequest getInstance(){
		if (adsRequest == null) {
			synchronized (AdsRequest.class) {
				if (adsRequest == null) {
					adsRequest = new AdsRequest();
				}
			}
		}
		return adsRequest;
	}
	
	public  void requestAds(final S2SListener mS2sListener,Handler handler){
		Log.d("efunLog","requestAds");
		
		if(context != null && AdsHelper.existLocalResponeCode(context)){
			return;
		}
		if(isNeedToWaitGA()){
			waitingGA();
		}
		
		if (adsHttpParams == null) {
			Log.w("efunLog","adsHttpParams is nul");
			return;
			//throw new NullPointerException("adsHttpParams is null");
		}
		
		AdsHelper.initParams(context, adsHttpParams);
		EfunLogUtil.logD("before send post.");
//		params.put("efunthirdplat", this.efunThirdPlat);
		
		efunThirdPlat = SPUtil.takeEfunAdsThirdPlat(context, "");
		adsHttpParams.setAdendTime(System.currentTimeMillis() + "");
		Log.d("efunLog", "advertiser:" + adsHttpParams.getAdvertiser() +",partner:" + adsHttpParams.getPartner() + ",referrer:" + adsHttpParams.getReferrer() + ",efunThirdPlat:" + efunThirdPlat);
		Log.d("efunLog", "adstartTime:" + adsHttpParams.getAdstartTime() +",adendTime:" + adsHttpParams.getAdendTime());
		String adsRespone = "";
		baseAds.setContext(context);
		baseAds.setHttpParams(adsHttpParams);
//		Log.d("efunLog", "ads params:" + adsHttpParams.toString());
		if (SStringUtil.isNotEmpty(this.efunThirdPlat)) {
			Log.d("efunLog","efunThirdPlat is not empty");
			adsRespone = baseAds.installThirdplatStatistics();
		} else {
			Log.d("efunLog","efunThirdPlat is empty");
			adsRespone = baseAds.adsStatistics();
		}
		EfunAdsS2SService.threadWaitting = false;
		final String adsCode = EfunJsonUtil.adsJsonCode(adsRespone);
		checkResponeCodeAndSave(mS2sListener, handler, adsCode);
	}

	private void checkResponeCodeAndSave(final S2SListener mS2sListener, Handler handler, final String adsCode) {
		/**
		 * 1003：空值异常，标识gameCode=null 
		 * 1000：激活成功
		 * 1001：缓存没有记录，表示没有匹配到 
		 * 1006:该设备已经安装过该游戏
		 */
		
		//==========================修改判断为不管code为任何值，只要不为null,"",空就认为上报成功=====================
		if (null != this.context && SStringUtil.isNotEmpty(adsCode) && SStringUtil.isNotEmpty(adsCode.trim()) && !"null".equals(adsCode)) {
//			if ("1000".equals(adsCode) || "1001".equals(adsCode) || "1006".equals(adsCode) || "1003".equals(adsCode)) {
			if (null != context && null != mS2sListener && null != handler) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						EfunLogUtil.logD("ads save return code:" + adsCode);
						SPUtil.saveResponeCode(AdsRequest.this.context, adsCode);
						mS2sListener.s2sResultRunOnlyOne(AdsRequest.this.context);
					}
				});
			}
//			} else {
//				EfunLogUtil.logD("ads return code is not 1000,1001,1006,1003");
//			}
		} else {
			EfunLogUtil.logD("ads return code save fail");
		}
	}

	private void waitingGA() {
		lock.lock();
		try {
			gaIsCome = false;
	//		threadWaitting = true;
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

	private boolean isNeedToWaitGA() {
		if (SStringUtil.isEmpty(adsHttpParams.getAdvertiser())) {
			adsHttpParams.setAdvertiser(SPUtil.takeAdvertisersName(context, ""));
		}
		if (SStringUtil.isEmpty(adsHttpParams.getPartner())) {
			adsHttpParams.setPartner(SPUtil.takePartnerName(context, ""));
		}
		if (SStringUtil.isEmpty(adsHttpParams.getReferrer())) {
			adsHttpParams.setReferrer(SPUtil.takeReferrer(context, ""));
		}
		if (TextUtils.isEmpty(SPUtil.takeAdvertisersName(context, ""))
				&&TextUtils.isEmpty(SPUtil.takeReferrer(context, ""))
				&& SStringUtil.isEmpty(adsHttpParams.getAdvertiser())
				//&& SStringUtil.isEmpty(adsHttpParams.getPartner())
				&& SStringUtil.isEmpty(adsHttpParams.getReferrer())
				) {
			return true;
		}
		return false;
	}


	
	public  void signalGACome(String advertiser,String partner,String referrer,String efunThirdPlat){
		lock.lock();
		try {
			gaIsCome = true;
			if (AdsRequest.adsHttpParams != null) {
//				AdsRequest.adsHttpParams.setAdvertiser(advertiser);
//				AdsRequest.adsHttpParams.setPartner(partner);
				AdsRequest.adsHttpParams.setReferrer(referrer);
			}else{
				EfunLogUtil.logW("adsHttpParams is null");
			}
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
	
	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	public AdsHttpParams getAdsHttpParams() {
		return adsHttpParams;
	}

	public void setAdsHttpParams(AdsHttpParams adsHttpParams) {
		AdsRequest.adsHttpParams = adsHttpParams;
	}

}

