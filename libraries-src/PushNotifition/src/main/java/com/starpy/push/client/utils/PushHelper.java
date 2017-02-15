package com.starpy.push.client.utils;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.starpy.base.cfg.SConfig;
import com.core.base.utils.SPUtil;
import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.utils.SLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.push.client.PushConstant;
import com.starpy.push.client.bean.NotificationMessage;
import com.starpy.push.client.im.Message;
import com.starpy.push.client.receiver.EfunPushReceiver;
import com.starpy.push.client.service.EfunPushService;
import com.starpy.push.utils.PushSPUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

public class PushHelper {

	public static final String EfunPushServiceKey = "EfunPushServiceKey";
	public static final int COMMON = 1;
	public static final int BOOT_COMPLETED = 2;
	public static final int CONNECTIVITY_CHANGE = 3;
	public static final int TICK_ALARM = 4;
	
	public static final int DELETE_INTENT_NOTIFICATION = 5;
	public static final int CLICK_INTENT_NOTIFICATION = 6;

	private static final int maxNum = 50;
	private static final String TAG = "push_PushHelper";

	/**
	 * <p>
	 * Description: 判断推送服务service是否正在运行
	 * </p>
	 * 
	 * @author GanYuanrong
	 * @return
	 * @date 2014年10月27日
	 */
	private static boolean isRunningEfunPushService(Context context) {
		// 获得ActivityManager服务的对象
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServiceInfos = mActivityManager.getRunningServices(maxNum);
		// Log.d(TAG, "runningServiceInfos size:" + runningServiceInfos.size());
		int i = 0;
		//Log.d(TAG, "server name:" + EfunPushService.class.getCanonicalName());

		String serverName = EfunPushService.class.getCanonicalName();
		/*if (SConfig.getGameLanguage(context).equals("zh_HK")) {//港台
			serverName = EfunPushServiceTW.class.getCanonicalName();
		}*/
		for (RunningServiceInfo rsi : runningServiceInfos) {
			ComponentName componentName = rsi.service;
			String pkn = componentName.getPackageName();
			String className = componentName.getClassName();
			i++;
			Log.d(TAG, "i:" + i + ",packageName:" + pkn + ",className:" + className);
			if (className.equals(serverName)) {
				return true;
			}
		}
		return false;
	}

	public static void startPush(Context context) {
		startPush(COMMON, context);
	}

	public static void startPush(int startType, Context context) {
		if (isRunningEfunPushService(context)) {
			Log.d(TAG, "push service is running");
			return;
		}
		Intent pushIntentStart = new Intent(context, EfunPushService.class);
		switch (startType) {
		case COMMON:
			pushIntentStart.putExtra(EfunPushServiceKey, COMMON);
			break;
		case BOOT_COMPLETED:
			pushIntentStart.putExtra(EfunPushServiceKey, BOOT_COMPLETED);
			break;
		case CONNECTIVITY_CHANGE:
			pushIntentStart.putExtra(EfunPushServiceKey, CONNECTIVITY_CHANGE);
			break;

		default:
			pushIntentStart.putExtra(EfunPushServiceKey, COMMON);
		}
		Log.d(TAG, "start EfunPushService");
		// pushIntentStart.setPackage(context.getPackageName());
		context.startService(pushIntentStart);
	}

	public static NotificationMessage parseMessage(String message) {
		NotificationMessage notificationMessage = null;
		if (TextUtils.isEmpty(message)) {
			return null;
		}

		try {
			JSONObject messageJson = new JSONObject(message);
			notificationMessage = new NotificationMessage();
			notificationMessage.setContent(messageJson.optString("content", ""));
			notificationMessage.setGameCode(messageJson.optString("gameCode", ""));
			notificationMessage.setMessageId(messageJson.optString("messageId", ""));
			notificationMessage.setPackageName(messageJson.optString("packageName", ""));
			notificationMessage.setTitle(messageJson.optString("title", ""));
			notificationMessage.setType(messageJson.optString("type", ""));
			notificationMessage.setAdvertiser(messageJson.optString("advertiser", ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notificationMessage;
	}

	/**
	 * <p>
	 * Description: 通过广告receiver分发消息
	 * </p>
	 * 
	 * @author GanYuanrong
	 * @param message
	 * @date 2014年12月8日
	 */
	public static void dispatcherMessageToApp(Context context, Message message) {
		if (message == null || message.getData() == null) {
			return;
		}
		String messageContent = null;
		try {
			messageContent = new String(message.getData(), 5, message.getContentLength(), "UTF-8");
		} catch (Exception e) {
			messageContent = UdpPushUtil.convert(message.getData(), 5, message.getContentLength());
		}
		if (TextUtils.isEmpty(messageContent)) {
			return;
		}
		Log.d(TAG, "message:" + messageContent);
		try {
			JSONObject objectTemp = new JSONObject(messageContent);
			objectTemp = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.d(TAG, "message not json");
			return;
		}
		Intent broIntent = new Intent(EfunPushReceiver.FLAG_EFUNPUSRECEIVER_ACTION);
		broIntent.putExtra(EfunPushReceiver.FLAG_MESSAGE_CONTENT, messageContent);
		context.sendBroadcast(broIntent);
	}

	/**
	 * <p>
	 * Description: 处理消息
	 * </p>
	 * 
	 * @author GanYuanrong
	 * @param context
	 * @param message
	 * @date 2014年12月8日
	 */
	public static void processMessage(Context context, String message) {

		NotificationMessage notificationMessage = PushHelper.parseMessage(message);
		String messageId = SPUtil.getSimpleString(context, StarPyUtil.STAR_PY_SP_FILE, EFUN_PUSH_MESSAGE_ID);
		//出现相同messageid不显示消息
		if (!TextUtils.isEmpty(messageId) && messageId.equals(notificationMessage.getMessageId())) {
			return;
		}
		try {
			EfunPushService.setNotificationId(Integer.parseInt(notificationMessage.getMessageId()));
		} catch (Exception e) {
			EfunPushService.setNotificationId(EfunPushService.getNotificationId() + 1);
		}
		if (notificationMessage == null) {
			return;
		}
		
		if ((!TextUtils.isEmpty(notificationMessage.getAdvertiser())) && notificationMessage.getAdvertiser().equals(PushHelper.getAdvertisers(context))
				&& notificationMessage.getGameCode().equals(PushHelper.getGameCode(context))) {

			if (EfunPushService.instanceMessageDispatcher(context) != null) {
				EfunPushService.instanceMessageDispatcher(context).onMessage(context,message);
				return;
			}
			//notifyUser(context, notificationMessage);
			notifyUserRange(context, notificationMessage);
		
		}else if (!TextUtils.isEmpty(notificationMessage.getPackageName()) && !TextUtils.isEmpty(notificationMessage.getPackageName().trim()) &&
			notificationMessage.getPackageName().equals(context.getPackageName()) && notificationMessage.getGameCode().equals(PushHelper.getGameCode(context))) {
				if (EfunPushService.instanceMessageDispatcher(context) != null) {
					EfunPushService.instanceMessageDispatcher(context).onMessage(context,message);
					return;
				}
				//notifyUser(context, notificationMessage);
				notifyUserRange(context, notificationMessage);
			
		} else if (notificationMessage.getGameCode().equals(PushHelper.getGameCode(context))) {
			if (EfunPushService.instanceMessageDispatcher(context) != null) {
				EfunPushService.instanceMessageDispatcher(context).onMessage(context,message);
				return;
			}
			//notifyUser(context, notificationMessage);
			notifyUserRange(context, notificationMessage);
		}
	}

	/**
	 * <p>
	 * Description: 任务栏显示消息
	 * </p>
	 * 
	 * @author GanYuanrong
	 * @param context
	 * @param notificationMessage
	 * @date 2014年12月8日
	 */
	/*public static void notifyUser(Context context, NotificationMessage notificationMessage) {

		String messageInfo = notificationMessage.getContent();
		String title = notificationMessage.getTitle();

		if (TextUtils.isEmpty(title) && TextUtils.isEmpty(messageInfo)) {
			return;
		}

		Notification notification = new Notification();

		String pkName = context.getPackageName();
		
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_LAUNCHER);
		
		Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(pkName);
		if (mainIntent != null && mainIntent.getComponent() != null) {
			String className = mainIntent.getComponent().getClassName();
			it.setClassName(context, className);
		}else if(mainIntent != null){
			it = mainIntent;
		}
			
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		// SLogUtil.logI("包名 ： " + pkName + "  className :" + className);

		int mIcon = PushSPUtil.takePullIcon(context, -1);
		SLogUtil.logI("mIcon id值为：" + mIcon);
		if (mIcon != -1 && mIcon != 0) {
			notification.icon = mIcon;
		} else {
			SLogUtil.logW("PushManager没有设置notifitionIcon，采用默认ic_launcher图标");
			notification.icon = context.getApplicationInfo().icon;
		}
		notification.when = System.currentTimeMillis() + 100;

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		// * 或者可以自己的LED提醒模式:
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300; // 亮的时间
		notification.ledOffMS = 1000; // 灭的时间
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		PendingIntent pendIntent = PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = pendIntent;
		notification.setLatestEventInfo(context, title, messageInfo, pendIntent);
		// n.tickerText = messageInfo;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(EfunPushService.getNotificationId(), notification);
		//保存上一次的message id,下次出现相同messageid不显示消息
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_PUSH_MESSAGE_ID, notificationMessage.getMessageId());
		notificationManager.notify(100, notification);

	}*/
	
	public static void notifyUserRange(Context context, NotificationMessage notificationMessage) {
		
		String messageInfo = notificationMessage.getContent();
		String title = notificationMessage.getTitle();

		if (TextUtils.isEmpty(title) && TextUtils.isEmpty(messageInfo)) {
			return;
		}
		
		EfunPushService.getNotificationMessages().add(notificationMessage);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setContentTitle(notificationMessage.getTitle())
				.setContentText(notificationMessage.getContent());
		
		
		int mIcon = PushSPUtil.takePullIcon(context, -1);
		SLogUtil.logI("mIcon id值为：" + mIcon);
		if (mIcon != -1 && mIcon != 0) {
			mBuilder.setSmallIcon(mIcon);
		} else {
			SLogUtil.logW("PushManager没有设置notifitionIcon，采用默认ic_launcher图标");
			mBuilder.setSmallIcon(context.getApplicationInfo().icon);
		}
		
		if (EfunPushService.getNotificationMessages() != null && EfunPushService.getNotificationMessages().size() == 1) {
			NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
			bigTextStyle.bigText(notificationMessage.getContent());
			bigTextStyle.setBigContentTitle(notificationMessage.getTitle());
			mBuilder.setStyle(bigTextStyle);
		}else{

			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			// Sets a title for the Inbox in expanded layout
			inboxStyle.setBigContentTitle(EfunPushService.getNotificationMessages().size() + " new messages:");
			// Moves events into the expanded layout
			for (int i = 0; i < EfunPushService.getNotificationMessages().size(); i++) {
				if (EfunPushService.getNotificationMessages().get(i).isShowMessageContent()) {
					inboxStyle.addLine(EfunPushService.getNotificationMessages().get(i).getContent());
					// inboxStyle.setSummaryText(EfunPushService.getNotificationMessages().get(i).getContent());
				} else {
					inboxStyle.addLine(EfunPushService.getNotificationMessages().get(i).getTitle());
				}
			}
			// Moves the expanded layout object into the notification object.
			mBuilder.setStyle(inboxStyle);
		
		}
		mBuilder.setAutoCancel(true);
		//mBuilder.setGroupSummary(true);
		mBuilder.setLights(0xff00ff00, 300, 1000);
		
		mBuilder.setNumber(EfunPushService.getNotificationMessages().size());

		//设置清除消息的intent 
		Intent deleteIntent = new Intent(EfunPushReceiver.FLAG_EFUN_DELETE_NOTIFICATION_ACTION);
		deleteIntent.setPackage(context.getPackageName());
		deleteIntent.putExtra(EfunPushServiceKey, DELETE_INTENT_NOTIFICATION);
		PendingIntent deletePendIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setDeleteIntent(deletePendIntent);
		
		Intent clickIntent = new Intent(context, EfunPushReceiver.class); 
		clickIntent.putExtra(EfunPushServiceKey, CLICK_INTENT_NOTIFICATION);
		PendingIntent clickPendIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(clickPendIntent);

		SPUtil.saveSimpleInfo(context, StarPyUtil.STAR_PY_SP_FILE, EFUN_PUSH_MESSAGE_ID, notificationMessage.getMessageId());
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(100, mBuilder.build());
		
	}

	public static final String EFUN_PUSH_MESSAGE_ID = "EFUN_PUSH_MESSAGE_ID";
	
	public static void setPushConfig(Context context, String serverIp, String serverPort) {
		if (TextUtils.isEmpty(serverIp)) {
			throw new IllegalArgumentException("serverIp must not be null");
		}
		SPUtil.saveSimpleInfo(context, StarPyUtil.STAR_PY_SP_FILE, PushConstant.PUSH_SERVER_IP, serverIp);
		SPUtil.saveSimpleInfo(context, StarPyUtil.STAR_PY_SP_FILE, PushConstant.PUSH_SERVER_PORT, serverPort);
	}

	public static String getGameCode(Context context) {
		String gameCode = PushSPUtil.takeGameCode(context, "");
		if (TextUtils.isEmpty(gameCode)) {
			gameCode = SConfig.getGameCode(context);
		}
		return gameCode;
	}

	public static String getAppPlatform(Context context) {
		String appPlatform = PushSPUtil.takeAppPlatform(context);
		if (TextUtils.isEmpty(appPlatform)) {
			appPlatform = SConfig.getAppPlatform(context);
		}
		return appPlatform;
	}

	public static String generateUUID(Context context) {
		return SStringUtil.toMd5(ApkInfoUtil.getDeviceType() + "_" + ApkInfoUtil.getAndroidId(context), true);
	}
	
	public static String getAdvertisers(Context context) {
		return SPUtil.getSimpleString(context, StarPyUtil.STAR_PY_SP_FILE, StarPyUtil.ADS_ADVERTISERS_NAME);
	}
	
	
}
