package com.starpy.pushnotification.task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.res.SConfig;
import com.core.base.utils.ResUtil;
import com.starpy.push.client.MessageDispatcher;
import com.starpy.push.client.PushRequest;
import com.starpy.push.client.utils.PushHelper;
import com.starpy.push.utils.PushSPUtil;

public class EfunPushManager {

	//private static final long triggerAtMillis = 5000;
	//private long intervalMillis = 2 * 60 * 60 * 1000;
	
	//private static final int requestCode = 0;
	
	//efunPushServerUrl
	//efunPushServerPort 动态域名使用获取IP和端口号的key
	
	public static final String PUSH_NOTIFICATION_START = "PUSH_NOTIFICATION_START";
	private static EfunPushManager mPushManager;
	
	private EfunPushManager (){
		
	}
	
	public static EfunPushManager getInstance() {
		
		if (mPushManager == null) {
			synchronized (EfunPushManager.class) {
				if (mPushManager == null) {
					mPushManager = new EfunPushManager();
				}
			}
		}
		return mPushManager;
	}
	
	/**
	 * <p>Description: 开始推送</p>
	 * @author GanYuanrong
	 * @param mContext
	 * @return
	 * @date 2014年12月8日
	 */
	public synchronized EfunPushManager startPush(Context mContext){
		//initPullTimer(mContext);
		Log.d("efun", "push-notifition 3.5");
		sendPushRequest(mContext);
		PushHelper.startPush(mContext);
		return getInstance();
	}

	public void sendPushRequest(Context mContext) {
		PushRequest pushRequest = new PushRequest();
		pushRequest.setGameCode(PushHelper.getGameCode(mContext));
		pushRequest.setAppPlatform(PushHelper.getAppPlatform(mContext));
		pushRequest.setAdvertiser(PushHelper.getAdvertisers(mContext));
		String gamePreferredDomainUrl = PushSPUtil.takePreUrl(mContext, "");
		String gameSpareDomainUrl = PushSPUtil.takeSpareUrl(mContext, "");
		if (TextUtils.isEmpty(gamePreferredDomainUrl)) {
			try {
//				gamePreferredDomainUrl = ResUtil.findStringByName(mContext, "efunGamePreferredDomainUrl");
//				gameSpareDomainUrl = ResUtil.findStringByName(mContext, "efunGameSpareDomainUrl");
				gamePreferredDomainUrl = SConfig.getGamePreferredUrl(mContext);
				gameSpareDomainUrl = SConfig.getGameSpareUrl(mContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		pushRequest.setPreUrl(gamePreferredDomainUrl);
		pushRequest.setSpaUrl(gameSpareDomainUrl);
		pushRequest.sendUUIDToServer(mContext);
	}

	/*private void initPullTimer(Context mContext) {
		Log.d("efun", "push-notifition 1.1");
		EfunLogUtil.logI("设置推送定时器....");
		PushSPUtil.savePullTime(mContext, System.currentTimeMillis());// 记录设置推送的日期
		AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent mIntent = new Intent(mContext, ScheduleService.class);
		PendingIntent mPendingIntent = PendingIntent.getService(mContext, requestCode, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + triggerAtMillis, intervalMillis, mPendingIntent);
		
	}*/
	
	
	/*public EfunPushManager setIntervalMillis(long intervalMillis) {
		this.intervalMillis = intervalMillis;
		return getInstance();
	}
	*/
	
	public EfunPushManager setNotifitionIcon(Context context,String notifitionIconName){
		PushSPUtil.savePullIcon(context, ResUtil.findDrawableIdByName(context, notifitionIconName));
		return getInstance();
	}
	
	public EfunPushManager setEfunNotifitionIcon(Context context,String notifitionIconName){
		PushSPUtil.savePullIcon(context, ResUtil.findDrawableIdByName(context, notifitionIconName));
		return getInstance();
	}
	

	public EfunPushManager setGameCode(Context context,String gameCode){
		PushSPUtil.saveGameCode(context, gameCode);
		return getInstance();
	}
	public EfunPushManager setPreUrl(Context context,String preUrl){
		PushSPUtil.savePreUrl(context, preUrl);
		return getInstance();
	}
	public EfunPushManager setSpareUrl(Context context,String spareUrl){
		PushSPUtil.saveSpareUrl(context, spareUrl);
		return getInstance();
	}
	
/*	public  EfunPushManager saveInstallTime(Context context, Long installTime) {
		PushSPUtil.saveInstallTime(context, installTime);
		return getInstance();
	}
	
	public  EfunPushManager saveLastLoginTime(Context context, Long mLastLoginTime) {
		PushSPUtil.saveLastLoginTime(context, mLastLoginTime);
		return getInstance();
	}
*/
	public EfunPushManager setAppPlatform(Context context,String appPlatform) {
		PushSPUtil.saveAppPlatform(context, appPlatform);
		return getInstance();
	}
	
	public EfunPushManager setPushConfig(Context context,String serverIp,String serverPort){
		PushHelper.setPushConfig(context, serverIp, serverPort);
		return getInstance();
	}
	
	public EfunPushManager setMessageDispather(Context context,Class< ? extends MessageDispatcher> messageDispatherClazz){
		PushSPUtil.saveDispatherClassName(context, messageDispatherClazz.getCanonicalName());
		return getInstance();
		
	}
}
