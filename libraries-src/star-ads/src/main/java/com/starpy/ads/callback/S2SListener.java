package com.starpy.sdk.ads.callback;

import android.content.Context;
import android.content.Intent;

/**
* <p>Title: S2SListener</p>
* <p>Description: S2S服务监听接口，用于在S2S服务里面添加各种广告.实现此接口的类不要写其他构造方法，默认即可</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013-7-17
*/
public interface S2SListener {
	
	/**
	* <p>Title: onlyOneForADS</p>
	* <p>Description: 服务执行onStartCommand方法的时候被调用，此方法用于接入仅仅调用一次的广告</p>
	* @param intent The Intent supplied to startService(Intent), as given. This may be null if the service is being restarted after its process has gone away, and it had previously returned anything except START_STICKY_COMPATIBILITY
	* @param startId A unique integer representing this specific request to start. Use with stopSelfResult(int).
	*/
	public void onlyOnceForADS(Context context,Intent intent, int startId);
	
	/**
	* <p>Title: s2sResultRunOnlyOne</p>
	* <p>Description: This method has been invoked  the server returns success </p>
	* @param context
	*/
	public void s2sResultRunOnlyOne(Context context);
	
	/**
	* <p>Title: s2sRunEvery</p>
	* <p>Description: 服务执行onStartCommand方法的时候被调用，此方法用于接入每次启动服务都需要调用的广告</p>
	* @param intent  The Intent supplied to startService(Intent), as given. This may be null if the service is being restarted after its process has gone away, and it had previously returned anything except START_STICKY_COMPATIBILITY
	* @param startId A unique integer representing this specific request to start. Use with stopSelfResult(int).
	*/
	public void s2sRunEvery(Context context, Intent intent, int startId);
	
	/**
	* <p>Title: s2sonDestroy</p>
	* <p>Description: 服务被摧毁的时候执行该方法</p>
	*/
	public void s2sonDestroy(Context context);
	
	/**
	* <p>Title: s2sonStopServic</p>
	* <p>Description: 服务调用onStopServic的时候被调用</p>
	* @param intent 
	*/
	public void s2sonStopServic(Context context, Intent intent);
	
	/**
	* <p>Title: onPCLAds</p>
	* <p>Description: </p>
	* @param context
	* @param intent
	* @param startId
	* @param level
	*/
//	public void onAdsForPCL(Context context,Intent intent, int startId, String level);

}
