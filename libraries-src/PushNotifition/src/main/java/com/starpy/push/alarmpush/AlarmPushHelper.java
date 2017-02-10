package com.starpy.push.alarmpush;

import java.util.ArrayList;
import java.util.Calendar;

import com.core.base.utils.SPUtil;
import com.core.base.utils.EfunLogUtil;
import com.starpy.push.client.bean.NotificationMessage;
import com.starpy.push.client.receiver.EfunPushReceiver;
import com.starpy.push.client.utils.PushHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class AlarmPushHelper {
	

	private static String EFUN_ALARMPUSH_CONTENT_ARRAY = "efun_alarmpush_content_array";
	private static String EFUN_ALARMPUSH_CONTENT_ARRAY_YMD = "efun_alarmpush_content_array_ymd";
	private static String REGULAR = "###";
	private static String EFUN_ALARMPUSH_TIME_MINUTI = "efun_alarmpush_time_minute";
	private static String EFUN_ALARMPUSH_TIME_INTERVAL = "efun_alarmpush_time_interval";

	/**
	 *  设置定时推送 （注册系统广播）
	 * @param context
	 * @param minute 在几分触发
	 * @param interval 时间间隔
	 */
	public static void setAlarmPush(Context context, int minute, int interval) {
		
		if(interval <= 0 ) {
			EfunLogUtil.logE("定时推送的时间间隔不能小于0----->interval:"+interval);
			throw new IllegalArgumentException("参数interval间隔不能小于0");
		}
		
		if(minute < 0 || minute >= 60) {
			throw new IllegalArgumentException("參數minute超限 (0 - 60)");
		}
		
		//将时间设置保存到本地，系统重启时重新设置定时推送
		saveAlarmPushTime(context, minute, interval);
		
		//将时间间隔换成毫秒
		long intervaltoms = interval * 60 * 1000L;
		
//		SimpleDateFormat sdf = new SimpleDateFormat("mm", Locale.ROOT);
//		SimpleDateFormat s = new SimpleDateFormat("ss", Locale.ROOT);
//		String str = sdf.format(new Date());
//		String ss = s.format(new Date());
//		int cminute = Integer.parseInt(str); //当前分钟
//		int cs = Integer.parseInt(ss);
		Calendar mCalendar = Calendar.getInstance();
		int cminute = mCalendar.get(Calendar.MINUTE);
		int cs = mCalendar.get(Calendar.SECOND);
		
		EfunLogUtil.logI("efunAlarmPush", "cminute : " + cminute);
		EfunLogUtil.logI("efunAlarmPush", "minute : " + minute);
		EfunLogUtil.logI("efunAlarmPush", "secend : " + cs);
		long ext = 0;
		if(cminute >= minute) {  //当前分钟数超过推送分钟数，计算延迟下次发送的时间（毫秒）
			ext = (60 - cminute + minute) * 1000 * 60;
			if (ext>cs*1000) {
				ext = ext - cs*1000;
			}
		} else {
			ext = (minute - cminute) * 1000 * 60;
			if (ext>cs*1000) {
				ext = ext - cs*1000;
			}
		}
		EfunLogUtil.logI("efunAlarmPush", "ext : " + ext);
		Intent intent = new Intent(context, EfunPushReceiver.class);
		intent.setAction("com.efun.alarmpush");
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if(am != null) {
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ext, intervaltoms, sender);
		}
	}
	
	/** 发消息推送。
	 * @param context
	 * @param messageContent
	 */
	public static void pushNotifition(Context context, String messageContent) {

		String messageTitle = "" + context.getApplicationInfo().loadLabel(context.getPackageManager());
		NotificationMessage noti = new NotificationMessage();
		noti.setTitle(messageTitle);
		noti.setContent(messageContent);
		noti.setShowMessageContent(true);
		
		EfunLogUtil.logI("Push message : "+messageContent);
		PushHelper.notifyUserRange(context, noti);

	}
	
	
	/** 设置推送的内容
	 * @param context 上下文
	 * @param hour  推送触发的时间 小时
	 * @param minute  推送触发的时间 分钟
	 * @param content  推送的内容
	 */
	public static void setAlarmPushContent(Context context,int hour ,int minute,String content) {
		setAlarmPushContent(context, Constant.EVERYDAY, hour, minute, content);
	}
	
	public static void setAlarmPushContent(Context context,int[] weeks ,int hour ,int minute,String content){
		if (weeks == null || weeks.length < 1) {
			EfunLogUtil.logE("setAlarmPushContent, weeks is null" );
		}
		for (int i = 0; i < weeks.length; i++) {
			setAlarmPushContent(context, weeks[i], hour, minute, content);
		}
	}
	
	
	/**
	 * @param context
	 * @param week day of week，范围 1-7 。1 标示星期日，2 标示 星期一 ，详细参看  Constant
	 * @param hour hour of day  0-23 
	 * @param minute 0-59
	 * @param content 推送内容
	 */
	public static void setAlarmPushContent(Context context,int week ,int hour ,int minute,String content) {
		if (week < 1 || week > 7) {
			EfunLogUtil.logE("setAlarmPushContent,time is illegal : week = " + week);
			return;
		}
		if (hour < 0 || hour > 23) {
			EfunLogUtil.logE("setAlarmPushContent,time is illegal : hour = " + hour);
			return;
		}
		if (minute < 0 || minute > 59) {
			EfunLogUtil.logE("setAlarmPushContent,time is illegal : minute = " + minute);
			return;
		}
		if (TextUtils.isEmpty(content)) {
			EfunLogUtil.logE("setAlarmPushContent:contentis null");
			return;
		} 
		String newContent = week + "#" + hour + "#" + minute + "#" + content;
		
		String existingContents = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY );
		if (TextUtils.isEmpty(existingContents)) {
			EfunLogUtil.logI("setAlarmPushContent:" + newContent);
			SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY , newContent);
		}else {
			String[] alarmPushContent = getAlarmPushContent(context);
			for (int i = 0; i < alarmPushContent.length; i++) {
				if (newContent.equals(alarmPushContent[i])) {
					EfunLogUtil.logI("setAlarmPushContent:content is existed");
					return;
				}
			}
			EfunLogUtil.logI("setAlarmPushContent:" + newContent);
			SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY ,existingContents +REGULAR + newContent);
		}
		
	}
	
	/** 按年月日设置推送内容
	 * @param context
	 * @param year 年 
	 * @param month 月 
	 * @param day 日 
	 * @param hour 小时  0 - 23 
	 * @param minute 分钟  0 - 59 
	 * @param content 推送内容
	 */
	public static void setAlarmPushContent(Context context,int year ,int month ,int day ,int hour ,int minute,String content){
		//判断年月日的有效性
		if (checkTimePast(context, year, month, day)) {
			EfunLogUtil.logE("setAlarmPushContent,time is past : year " + year + " month " + month + " day " + day);
			return;
		}
		//判断时间的合法性
		if (hour < 0 || hour > 23) {
			EfunLogUtil.logE("setAlarmPushContent,time is illegal : hour = " + hour);
			return;
		}
		if (minute < 0 || minute > 59) {
			EfunLogUtil.logE("setAlarmPushContent,time is illegal : minute = " + minute);
			return;
		}
		if (TextUtils.isEmpty(content)) {
			EfunLogUtil.logE("setAlarmPushContent:contentis null");
			return;
		} 
		// 检查内容为空
		if (TextUtils.isEmpty(content)) {
			EfunLogUtil.logE("setAlarmPushContent:contentis null");
			return;
		} 
		
		String newContent = year + "#" + month + "#" +day + "#" + hour + "#" + minute + "#" + content; 
		
		ArrayList<String> ymdList = getAlarmPushContentYMD(context);
		
		if (ymdList == null) {
			ymdList = new ArrayList<String>();
		}
		//查重
		for (int i = 0; i < ymdList.size();) {
			if (newContent.equals(ymdList.get(i))) {
				EfunLogUtil.logD(ymdList.get(i) + "is repetitive");
				ymdList.remove(i);
			} else {
				i++;
			}
		}
		//查过期
		for (int i = 0; i < ymdList.size();) {
			String[] strings = ymdList.get(i).split("#");
			if (checkTimePast(context, Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
					Integer.parseInt(strings[2]))) {
				EfunLogUtil.logD(ymdList.get(i) + "is past");
				ymdList.remove(i);
			} else {
				i++;
			}
		}
		
		ymdList.add(newContent);
		
		String contents = "";
		for (int i = 0; i < ymdList.size(); i++) {
			if (!TextUtils.isEmpty(ymdList.get(i))) {
				if (TextUtils.isEmpty(contents)) {
					contents = ymdList.get(i);
				}else {
					contents = contents + REGULAR + ymdList.get(i);
				}
			}
		}
		
		EfunLogUtil.logI("setAlarmPushContent:" + contents);
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY_YMD ,contents);
				
	}
	
	/** 获取 推送的内容
	 * @param context
	 * @return
	 */
	public static String[] getAlarmPushContent(Context context) {
		String content = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY );
		if (TextUtils.isEmpty(content)) {
			return null;
		} else {
			String[] stringArray = content.split(REGULAR);
			return stringArray;
		}
	}
	
	public static ArrayList<String> getAlarmPushContentYMD(Context context) {
		String content = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY_YMD );
		ArrayList<String> mList = new ArrayList<String>();
		if (!TextUtils.isEmpty(content)) {
			String[] stringArray = content.split(REGULAR);
			for (int i = 0; i < stringArray.length; i++) {
				mList.add(stringArray[i]);
			}
		}
		return mList;
	}
	
	/** 清除 推送的内容
	 * @param context
	 */
	public static void clearAlarmPushContent(Context context) {
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY , "");
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_CONTENT_ARRAY_YMD , "");
	}
	
	
	
	/**  保存注册系统广播的时间
	 * @param context
	 * @param minute 分钟数
	 * @param interval 间隔、分
	 */
	public static void saveAlarmPushTime(Context context,int minute, int interval) {
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_TIME_MINUTI, minute+"");
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_TIME_INTERVAL, interval + "");
	}	
	
	public static String getAlarmPushTimeMinute(Context context) {
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_TIME_MINUTI);
	}
	
	public static String getAlarmPushTimeInterval(Context context) {
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, EFUN_ALARMPUSH_TIME_INTERVAL);
	}
	
	/**  判断日期是否已过期
	 * @param context
	 * @param year
	 * @param month
	 * @param day
	 * @return true 已过期 ，
	 */
	private static boolean checkTimePast(Context context,int year ,int month ,int day) {
		Calendar mCalendar = Calendar.getInstance();
		//判断年月日的有效性
		if (mCalendar.get(Calendar.YEAR) > year) {
			return true;
		}else if (mCalendar.get(Calendar.YEAR) == year) {
			if ((mCalendar.get(Calendar.MONTH) + 1) > month) {
				return true;
			}else if ((mCalendar.get(Calendar.MONTH) + 1) == month) {
				if (mCalendar.get(Calendar.DAY_OF_MONTH) > day) {
					return true;
				}
			}
		}
		
		return false;
	}

}
