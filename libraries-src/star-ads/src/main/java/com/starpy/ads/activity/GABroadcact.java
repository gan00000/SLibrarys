package com.starpy.ads.activity;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.starpy.base.cfg.ResConfig;
import com.starpy.ads.analytics.GoogleAnalytics;
import com.starpy.ads.callback.GAListener;
import com.starpy.ads.server.AdsRequest;
import com.starpy.ads.util.SPUtil;
import com.starpy.base.utils.SLog;
import com.core.base.utils.SStringUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class GABroadcact extends BroadcastReceiver {
	
	public static final String VENDING_INSTALL = "com.android.vending.INSTALL_REFERRER";
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		if (intent == null || intent.getAction() == null) {
			return ;
		}
		
		String referrer = intent.getStringExtra("referrer");

		if (!VENDING_INSTALL.equals(intent.getAction()) || referrer == null) {
			SLog.logI( "referrer is null");
			return;
		} else {
			SLog.logI( "referrer = " + referrer);
		}
		
		notifyS2S(ctx,referrer);
		
		try {
			
			String galistener = ResConfig.getGAListenerName(ctx);
			
			if (SStringUtil.isNotEmpty(galistener) && galistener.startsWith("com.")) {
				
				Class<GAListener> clazz = (Class<GAListener>) Class.forName(galistener);
				if (clazz != null) {
					SLog.logI( "执行google分析里面的广告");
					clazz.newInstance().GAonReceive(ctx, intent);
				}
			}
		} catch (ClassNotFoundException e) {
			SLog.logE("google分析GABroadcact，ClassNotFoundException异常");
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			SLog.logE("google分析GABroadcact，IllegalAccessException异常");
			e.printStackTrace();
		} catch (InstantiationException e) {
			SLog.logE("google分析GABroadcact，InstantiationException异常");
			e.printStackTrace();
		} 
		
		SLog.logI( "GABroadcact BroadcastReceiver");

		if (!TextUtils.isEmpty(referrer) && !TextUtils.isEmpty(ResConfig.getGoogleAnalyticsTrackingId(ctx))) {
			try {
				GoogleAnalytics.tarckerEvent(ctx, referrer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void notifyS2S(final Context ctx, final String referrer) {
		if (null != ctx) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try{
						Log.d("efun", "start to notify s2s thread,referrer:" + referrer);
						SLog.logI("EfunGAService start...");
						Map<String, String> map = new HashMap<String, String>();
						String referrers[] = (URLDecoder.decode(referrer)).split("&");
						for (String referrerValue : referrers) {
							String keyValue[] = referrerValue.split("=");
							if (keyValue.length < 2) {
								map.put(keyValue[0], "");
							} else {
								map.put(keyValue[0], keyValue[1]);
							}
						}
	
//						String advertisersName = (null == map.get("efun_advertisers") ? "" : map.get("efun_advertisers"));
//						String advertisersName = (null == map.get("utm_source") ? "" : map.get("utm_source"));
//						String partner = (null == map.get("efun_partner") ? "" : map.get("efun_partner"));
						String efunThirdPlat = (null == map.get("efun_thirdplat") ? "" : map.get("efun_thirdplat"));
						String efunRegion = (null == map.get("efun_region") ? "" : map.get("efun_region"));
	
//						SPUtil.saveAdvertisersName(ctx, advertisersName);
//						SPUtil.savePartnerName(ctx, partner);
						SPUtil.saveEfunAdsThirdPlat(ctx, efunThirdPlat);
						SPUtil.saveReferrer(ctx, referrer);
						SPUtil.saveRegion(ctx, efunRegion);
//						AdvertService.getInstance().checkGACome(advertisersName, partner, referrer, efunThirdPlat);
						AdsRequest.getInstance().signalGACome("", "", referrer, efunThirdPlat);
					}catch(Exception ex){
						Log.i("efun", ex.getMessage()+"");
						ex.printStackTrace();
					}
				}
			}).start();
		}
		
	}
	
}
