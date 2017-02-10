package com.starpy.push.client.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.ApkInfoUtil.NetworkType;
import com.starpy.push.alarmpush.AlarmPushReceiver;
import com.starpy.push.client.service.EfunPushService;
import com.starpy.push.client.utils.PushHelper;
import com.starpy.pushnotification.task.EfunPushManager;

public class EfunPushReceiver extends BroadcastReceiver {
	//public static final String EfunPushReceiverKey = "EfunPushReceiverKey";
	public static final String FLAG_BOOT_COMPLETEDP = "FLAG_BOOT_COMPLETEDP";//开机
	public static final String FLAG_START_COMMON = "FLAG_START_COMMON";//正常启动
	public static final String FLAG_CONNECTIVITY_CHANGE = "FLAG_CONNECTIVITY_CHANGE";//网络状态改变
	
	public static final String FLAG_EFUNPUSRECEIVER_ACTION = "com.efun.push.client.Dispatcher";//启动action
	public static final String FLAG_EFUN_DELETE_NOTIFICATION_ACTION = "com.efun.delete.notification.action";//启动action
	
	public static final String FLAG_MESSAGE_CONTENT = "FLAG_MESSAGE_CONTENT";//推送消息内容
	
	private static final String TAG = "push_EfunPushReceiver";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		AlarmPushReceiver.onReceive(context,intent);
		
		int extrInt = intent.getIntExtra(PushHelper.EfunPushServiceKey,0);
		Log.d(TAG, "extrInt:" + extrInt);
		switch (extrInt) {
		case PushHelper.DELETE_INTENT_NOTIFICATION:
			Log.d(TAG, "DELETE_INTENT_NOTIFICATION");
			EfunPushService.getNotificationMessages().clear();
			return;
			
		case PushHelper.CLICK_INTENT_NOTIFICATION:
			Log.d(TAG, "CLICK_INTENT_NOTIFICATION");
			EfunPushService.getNotificationMessages().clear();
			processClickNotificationIntent(context);
			return;
			
		default:
			break;
		}
		
		
		String actionStr = intent.getAction();
		if (TextUtils.isEmpty(actionStr) || actionStr.equals(FLAG_EFUN_DELETE_NOTIFICATION_ACTION)) {
			return;
		}
		
		if (actionStr.equals("android.intent.action.BOOT_COMPLETED")) {//开机启动广播
			Log.d(TAG, FLAG_BOOT_COMPLETEDP);
			PushHelper.startPush(PushHelper.BOOT_COMPLETED,context);
		}else if (actionStr.equals("android.net.conn.CONNECTIVITY_CHANGE")) {//网络状态改变广播
			Log.d(TAG, FLAG_CONNECTIVITY_CHANGE);
			int netType = ApkInfoUtil.getNetworkType(context);
			if (netType == NetworkType.NET_TYPE_2G || netType == NetworkType.NET_TYPE_UNKNOW) {
				PushHelper.startPush(PushHelper.CONNECTIVITY_CHANGE,context);
			}
		}else if (actionStr.equals(FLAG_EFUNPUSRECEIVER_ACTION)) {
			Log.d(TAG, FLAG_MESSAGE_CONTENT);
			String message = intent.getStringExtra(FLAG_MESSAGE_CONTENT);
			if (!TextUtils.isEmpty(message)) {
				PushHelper.processMessage(context, message);
			}
		}else{
			PushHelper.startPush(PushHelper.COMMON,context);
		}
	}

	
	private void processClickNotificationIntent(Context context) {
		String pkName = context.getPackageName();

		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_LAUNCHER);

		Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(pkName);
		if (mainIntent != null && mainIntent.getComponent() != null) {
			String className = mainIntent.getComponent().getClassName();
			it.setClassName(context, className);
		} else if (mainIntent != null) {
			it = mainIntent;
		}
		it.putExtra(EfunPushManager.PUSH_NOTIFICATION_START, true);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		context.startActivity(it);
		
	}
}
