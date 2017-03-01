package com.starpy.google;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.starpy.google.bean.NotificationMessage;
import com.starpy.google.utils.MessageUtil;

/**
* <p>Title: SPushReceiver</p>
* <p>Description: 此广播作用：1.接收开机广播，用来启动推送服务；2.接收网络变化广播，用来启动推送服务；3.接收推送服务广播出来的推送信息
*                 4.接收本应用信息被清除事件；5.接收本应用信息被点击事件</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2016年5月5日
*/
public class SPushReceiver extends BroadcastReceiver {

	private static final String TAG = "push_EfunPushReceiver";
	public static final String NOTIFICATION_CLICK = "com.efun.notification.click";
	public static final String NOTIFICATION_DELETE = "com.efun.notification.delete";// 信息被清除action

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent == null) {
			return;
		}
		if(!TextUtils.isEmpty(intent.getPackage()) && !intent.getPackage().equals(context.getPackageName())){
			return;
		}
		int extrInt = intent.getIntExtra(MessageUtil.EFUN_PUSH_MESSAGE_ACTION_KEY, 0);
		Log.d(TAG, "extrInt:" + extrInt);
		switch (extrInt) {
		case MessageUtil.DELETE_INTENT_NOTIFICATION://信息被清除事件
			Log.d(TAG, "DELETE_INTENT_NOTIFICATION");
			SFirebaseMessagingService.getNotificationMessages().clear();
			return;

		case MessageUtil.CLICK_INTENT_NOTIFICATION://信息被点击事件
			Log.d(TAG, "CLICK_INTENT_NOTIFICATION");
			processClickNotificationIntent(context);
			return;

		default:
			break;
		}

	}

	private void processClickNotificationIntent(Context context) {
		
		Log.d(TAG, "efun_push_msg click");

		boolean isEmpty = SFirebaseMessagingService.getNotificationMessages().isEmpty();
		if (!isEmpty) {
			int size = SFirebaseMessagingService.getNotificationMessages().size();
			NotificationMessage lastN = SFirebaseMessagingService.getNotificationMessages().get(size - 1);

			//处理点击后的事件
			if (SFirebaseMessagingService.instanceMessageDispatcher(context) != null && lastN != null) {//额外处理
					//返回true表示不再传递处理，返回false表示继续传递事件给默认点击处理器处理
				if (SFirebaseMessagingService.instanceMessageDispatcher(context).onClickNotification(context, lastN.getContent(), lastN.getData())) {
					SFirebaseMessagingService.getNotificationMessages().clear();
					return;
				}
			}

			if (!TextUtils.isEmpty(lastN.getClickOpenUrl()) && lastN != null) {
				MessageUtil.openBrowser(context, lastN.getClickOpenUrl());
				SFirebaseMessagingService.getNotificationMessages().clear();
				return;
			}

		}

		MessageUtil.startAppLaunchActivity(context);
		SFirebaseMessagingService.getNotificationMessages().clear();
	}

}
