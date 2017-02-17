package com.starpy.google;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.ResUtil;
import com.starpy.google.bean.EfunFirebaseKey;
import com.starpy.google.bean.NotificationMessage;
import com.starpy.google.utils.GoogleUtil;
import com.starpy.google.utils.MessageUtil;
import com.starpy.google.utils.PushSPUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Efun on 2016/6/27.
 */
public class EfunGoogleProxy {
	
	public static final int GOOGLE_SHARE_CODE = 9002;
	public static final int GOOGLE_SERVICES_ERROR_CODE = 21;
	private static final String TAG = "EfunGoogleProxy";

	public static void setMessageIcon(Context context,String name){
        setMessageIcon(context, ResUtil.findDrawableIdByName(context,name));
    }

    public static void setMessageIcon(Context context,int resId){
        PushSPUtil.savePullIcon(context,resId);
    }

	public static void initPush(Context context){
		FirebaseMessaging.getInstance().subscribeToTopic(ApkInfoUtil.getVersionName(context));
	}

    //===========================================================================================
    //=====================================Google 分析============================================
    //===========================================================================================

	public static class EfunGoogleAnalytics{
		//新版google分析
		private static Tracker mTracker;

		public synchronized static Tracker initDefaultTracker(Context context, String trackingId) {
			if (mTracker == null) {
				GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
				mTracker = analytics.newTracker(trackingId);
			}
			return mTracker;
		}

		public static void trackEvent(String category, String action, String label) {

			if (null != mTracker) {
				mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
			}
		}

		public static void stopSession(){

		}
	}

    //===========================================================================================
    //=====================================Google 分析  end============================================
    //===========================================================================================


	//=====================================Google getAdvertisingId ============================================

	public static String getAdvertisingId(Context mContext){
		return GoogleUtil.getAdvertisingId(mContext);
	}
	//=====================================Google getAdvertisingId end============================================

	public static void setMessageDispather(Context context,Class< ? extends MessageDispatcher> messageDispatherClazz){
		PushSPUtil.saveDispatherClassName(context, messageDispatherClazz.getCanonicalName());
	}

	public static Map onCreateMainActivity(Activity activity){
		return onCreateMainActivity(activity,true);
	}

	public static int isGooglePlayServicesAvailable(Context context){
		return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
	}

	public static boolean showGoogleServicesErrorDialog(Activity activity){
		int googleServicesCode = isGooglePlayServicesAvailable(activity.getApplicationContext());
		if (googleServicesCode == ConnectionResult.SUCCESS){
			return true;
		}
		Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(activity,googleServicesCode,GOOGLE_SERVICES_ERROR_CODE);
		errorDialog.show();
		return false;
	}

	public static Map onCreateMainActivity(Activity activity,boolean callOnClickNotification){
		Intent i = activity.getIntent();
		if (i != null){
			Bundle b = i.getExtras();
			if (b != null && !b.isEmpty() && b.containsKey(EfunFirebaseKey.efun_firebase_message)){
				Map<String,String> data = new HashMap<String,String>();
				NotificationMessage nm = new NotificationMessage();
				Set<String> s = b.keySet();
				for (String bKey: s) {
					if (!TextUtils.isEmpty(bKey)) {
						data.put(bKey,b.getString(bKey));
					}
				}
				if (callOnClickNotification) {
					nm.setData(data);
					nm.setClickOpenUrl(data.get(EfunFirebaseKey.efun_click_open_url));
					EfunFirebaseMessagingService.getNotificationMessages().add(nm);

					Intent intent = new Intent(EfunPushReceiver.NOTIFICATION_CLICK);
					intent.setClass(activity,EfunPushReceiver.class);
					intent.putExtra(MessageUtil.EFUN_PUSH_MESSAGE_ACTION_KEY, MessageUtil.CLICK_INTENT_NOTIFICATION);
					intent.setPackage(activity.getPackageName());
					Log.d(TAG,"fire message on main activity sendBroadcast");
					activity.sendBroadcast(intent);
				}
//				Bdown.excuteSpecialThing(activity, data);
				return data;
			}
		}
		return null;
	}
	
	public void share(Activity activity,String text,String url){
		// Launch the Google+ share dialog with attribution to your app.
	      Intent shareIntent = new PlusShare.Builder(activity)
	          .setType("text/plain")
	          .setText(text)
	          .setContentUrl(Uri.parse(url))
	          .getIntent();
	      activity.startActivityForResult(shareIntent, GOOGLE_SHARE_CODE);
	}

	public static class EfunFirebaseAnalytics{

		private static FirebaseAnalytics mFirebaseAnalytics;

		public static void logEvent(Context context,String eventName){
			if (mFirebaseAnalytics == null){
				mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
			}
			Bundle bundle = new Bundle();

			mFirebaseAnalytics.logEvent(eventName, bundle);

		}

		public static void logEvent(Context context,String eventName,Bundle bundle){
			if (mFirebaseAnalytics == null){
				mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
			}
			mFirebaseAnalytics.logEvent(eventName, bundle);
		}

	}

}
