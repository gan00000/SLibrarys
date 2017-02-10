package com.starpy.push.client.service;

import java.util.ArrayList;

import com.starpy.base.utils.ApkInfoUtil;
import com.starpy.base.utils.SPUtil;
import com.starpy.base.utils.ApkInfoUtil.NetworkType;
import com.starpy.push.client.MessageDispatcher;
import com.starpy.push.client.PushConstant;
import com.starpy.push.client.bean.NotificationMessage;
import com.starpy.push.client.im.Message;
import com.starpy.push.client.im.UDPClientBase;
import com.starpy.push.client.utils.PushHelper;
import com.starpy.push.client.utils.UdpPushUtil;
import com.starpy.push.utils.PushSPUtil;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;

public class EfunPushService extends Service {

	private static final String LOG_TAG = "push_EfunPushService";
	private static final String EFUN_SERVER_PORT = "57114";
	protected PendingIntent tickPendIntent;
	private WakeLock wakeLock;
	private static MyUdpClient myUdpClient;
	private Handler handle;
	private static int notificationId = 1;
	
	private static MessageDispatcher dispatcher;
	
	private static ArrayList<NotificationMessage> notificationMessages;
	
	
	/**
	 * @return the notificationMessages
	 */
	public static ArrayList<NotificationMessage> getNotificationMessages() {
		if (notificationMessages == null) {
			notificationMessages = new ArrayList<NotificationMessage>();
		}
		return notificationMessages;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Notification notification = new Notification();
		
		startForeground(0, notification);//设置服务为前台服务，防止server被回收，0不显示任务栏通知
		setTickAlarm();//设置定时触发
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		/*PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
		SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
		SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
		FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
		ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
		ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间*/
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EfunPushService");
	//	resetClient();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand startId:" + startId);
		Log.d(LOG_TAG, "efun push version: 3.3,删除旧推送代码；推送通知按照Google推荐要求显示优化；每次启动游戏上报uuid");
		Log.d(LOG_TAG, "efun push version: 3.3.1,修改生成uuid规则");
		
		if (notificationMessages == null) {
			notificationMessages = new ArrayList<NotificationMessage>();
		}
		
		if (handle == null) {
			handle = new Handler();
		}
		
/*		if (startId > 1) {
			return super.onStartCommand(intent, flags, startId);
		}
		*/
		if (wakeLock != null && wakeLock.isHeld() == false) {// 当前是否持有唤醒锁
			wakeLock.acquire();// 获取唤醒锁
		}
		if (intent != null) {
			int extrInt = intent.getIntExtra(PushHelper.EfunPushServiceKey,0);
			Log.d(LOG_TAG, "onStartCommand extrInt:" + extrInt);
			switch (extrInt) {
			case PushHelper.COMMON:
				break;
			case PushHelper.BOOT_COMPLETED:
				break;
			case PushHelper.CONNECTIVITY_CHANGE:
				break;
			case PushHelper.TICK_ALARM:
				break;

			default:
			}
		}
		instanceMessageDispatcher(this);
		resetClient();
	//	setPkgsInfo();// 保存发送和接收的网络包
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		this.cancelTickAlarm();
		//cancelNotifyRunning();
		this.tryReleaseWakeLock();
		if (this.myUdpClient != null) {
			try {
				myUdpClient.stopUdpThread();
				myUdpClient = null;
				PushHelper.startPush(PushHelper.COMMON,this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*protected void setPkgsInfo() {
		if (this.myUdpClient == null) {
			return;
		}
		long sent = myUdpClient.getSentPackets();
		long received = myUdpClient.getReceivedPackets();
		SharedPreferences account = this.getSharedPreferences(PushParams.DEFAULT_PRE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = account.edit();
		editor.putString(PushParams.SENT_PKGS, "" + sent);
		editor.putString(PushParams.RECEIVE_PKGS, "" + received);
		editor.commit();
	}*/

	protected void resetClient() {
		//SharedPreferences account = this.getSharedPreferences(PushParams.DEFAULT_PRE_NAME, Context.MODE_PRIVATE);
		String serverIp = SPUtil.getSimpleString(this, SPUtil.EFUN_FILE, PushConstant.PUSH_SERVER_IP);//account.getString(PushParams.SERVER_IP, "");
		String serverPort = SPUtil.getSimpleString(this, SPUtil.EFUN_FILE, PushConstant.PUSH_SERVER_PORT);// account.getString(PushParams.SERVER_PORT, "");
	//	String pushPort = "57112";//account.getString(PushParams.PUSH_PORT, "");
	//	String userName = account.getString(PushParams.USER_NAME, "");
		if (TextUtils.isEmpty(serverPort) || TextUtils.isEmpty(serverPort.trim())) {
			serverPort = EFUN_SERVER_PORT;
		}
		if (serverIp == null || serverIp.trim().length() == 0) {
			return;
		}
		if (this.myUdpClient != null) {
			try {
				myUdpClient.stopUdpThread();
				myUdpClient = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			String uuidStr = PushHelper.generateUUID(this);
			//EfunPushManager.getInstance().sendPushRequest(this);
			Log.d(LOG_TAG, "uuidStr:" + uuidStr);
			myUdpClient = new MyUdpClient(UdpPushUtil.md5Byte(uuidStr), 1, serverIp, Integer.parseInt(serverPort));
			myUdpClient.setHeartbeatInterval(50);//50秒一次心跳，可由您动态调节 ，设置心跳时间
			myUdpClient.startUdpThread();//发送心跳
			//SharedPreferences.Editor editor = account.edit();
//			editor.putString(PushParams.SENT_PKGS, "0");
//			editor.putString(PushParams.RECEIVE_PKGS, "0");
//			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "终端重置成功...");
	}

	/**
	 * <p>Description: 尝试释放wakeLock</p>
	 * @author GanYuanrong
	 * @date 2014年10月24日
	 */
	protected void tryReleaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld() == true) {
			wakeLock.release();
		}
	}

	/**
	 * <p>Description: 取消定时广播</p>
	 * @author GanYuanrong
	 * @date 2014年10月24日
	 */
	protected void cancelTickAlarm() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(tickPendIntent);
	}


	
	/**
	 * <p>Description: 定时发送广播到TickAlarmReceiver，TickAlarmReceiver收到广告启动OnlineService</p>
	 * @author GanYuanrong
	 * @date 2014年10月24日
	 */
	protected void setTickAlarm() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, EfunPushService.class);
		intent.putExtra(PushHelper.EfunPushServiceKey, PushHelper.TICK_ALARM);
		int requestCode = 0;
		tickPendIntent = PendingIntent.getService(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 小米2s的MIUI操作系统，目前最短广播间隔为5分钟，少于5分钟的alarm会等到5分钟再触发！2014-04-28
		long triggerAtTime = System.currentTimeMillis() + 20 * 1000;
		int interval = 10 * 60 * 1000;
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval, tickPendIntent);
	}
	
	public static int getNotificationId() {
		return notificationId;
	}

	public static void setNotificationId(int notificationId) {
		EfunPushService.notificationId = notificationId;
	}
	
	public static MessageDispatcher  instanceMessageDispatcher(Context context){
		if (dispatcher == null) {
			String className = PushSPUtil.getDispatherClassName(context);
			if (!TextUtils.isEmpty(className)) {
				try {
					Class<MessageDispatcher> dispatcherClazz = (Class<MessageDispatcher>) Class.forName(className);
					dispatcher = dispatcherClazz.newInstance();
					return dispatcher;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return dispatcher;
	}

	public class MyUdpClient extends UDPClientBase {

		public MyUdpClient(byte[] uuid, int appid, String serverAddr, int serverPort) throws Exception {
			super(uuid, appid, serverAddr, serverPort);

		}
		
		//判断是否有网络连接
		@Override
		public boolean hasNetworkConnection() {
			int netType = ApkInfoUtil.getNetworkType(EfunPushService.this);
			if (netType == NetworkType.NET_TYPE_2G || netType == NetworkType.NET_TYPE_UNKNOW) {
				return false;
			}
			return UdpPushUtil.hasNetwork(EfunPushService.this);
		}

		//该函数同样主要用于智能终端，在客户端不需要发送心跳包，并且服务端无推送信息的时候，尝试系统休眠，达到省电的目的。PC版软件可以直接将该函数留空，
		//不做任何处理。Android则可能要考虑释放WakeLock以进入系统休眠状态。
		@Override
		public void trySystemSleep() {
			tryReleaseWakeLock();
		}

		
		/* 
		 * 该函数是客户端处理推送信息的业务入口函数。当客户端收到服务端的推送命令，将自动发送确认包，然后调用该函数。所以该函数内或其调用的其他函数，
		 * 应该确保信息的正确处理，因为正常情况下该推送信息已经确认，极可能不会再次收到通知。
		 * DDPush现成的客户端基类，会在该函数返回后，再调用trySystemSleep()函数。因此该函数内不用考虑保有WakeLock等防止系统休眠的操作。
		 */
		@Override
		public void onPushMessage(final Message message) {
			if (message == null) {
				return;
			}
			if (message.getData() == null || message.getData().length == 0) {
				return;
			}
		
			if (message.getCmd() == 16) {// 0x10 通用推送信息
				//notifyUser(16, "DDPush通用推送信息", "时间：" + DateTimeUtil.getCurDateTime(), "收到通用推送信息");
			}
			if (message.getCmd() == 17) {// 0x11 分组推送信息
				//long msg = ByteBuffer.wrap(message.getData(), 5, 8).getLong();
				//notifyUser(17, "DDPush分组推送信息", "" + msg, "收到通用推送信息");
			}
			if (message.getCmd() == 32) {// 0x20 自定义推送信息
				if (handle != null) {
					handle.post(new Runnable() {
						
						@Override
						public void run() {
							Log.d(LOG_TAG, "dispatcherMessage");
							
							PushHelper.dispatcherMessageToApp(EfunPushService.this,message);
						}
					});
				}
			}
		//	setPkgsInfo();
		}

	}
	
}
