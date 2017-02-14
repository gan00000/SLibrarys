package com.starpy.push.alarmpush;

import java.util.ArrayList;
import java.util.Calendar;

import com.starpy.base.utils.SLogUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class AlarmPushReceiver  {
	
	
	public static void onReceive(Context context, Intent intent) {
		
		if ("com.efun.alarmpush".equals(intent.getAction())) {
			SLogUtil.logI("com.efun.alarmpush action");
			
//			SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss", Locale.ROOT);
//			String str = sdf.format(new Date());
//			String[] d = str.split(":");
//			int hour = Integer.parseInt(d[0]);
//			int minute = Integer.parseInt(d[1]);
//			int second = Integer.parseInt(d[2]);
			
			Calendar mCalendar = Calendar.getInstance();
			
			int year = mCalendar.get(Calendar.YEAR);
			int month = mCalendar.get(Calendar.MONTH) + 1;
			int day = mCalendar.get(Calendar.DAY_OF_MONTH);
			
			int week = mCalendar.get(Calendar.DAY_OF_WEEK);
			int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
			int minute = mCalendar.get(Calendar.MINUTE);
			int second = mCalendar.get(Calendar.SECOND);
			
			SLogUtil.logI(" week: "+week+" hour: "+hour+" minute: "+minute+" second: "+second);
			
			long current = hour*60*60*1000l + minute*60*1000l + second*1000l; 
			
			//星期循环的推送
			String[] stringArray = AlarmPushHelper.getAlarmPushContent(context);
			
			if (stringArray != null) {
				for (int i = 0; i < stringArray.length; i++) {
					String stringItem = stringArray[i];
					SLogUtil.logI("推送内容时间---->"+stringItem);
					
					String[] split = stringItem.split("#");
					int pushweek = Integer.parseInt(split[0]);
					int pushhour = Integer.parseInt(split[1]);
					int pushminute = Integer.parseInt(split[2]);
					String messageContent = split[3];
					if (week == pushweek) {
						SLogUtil.logI("推送内容时间---->"+"pushhour:"+pushhour+"  pushminute:"+pushminute+"  messageContent:"+messageContent);
						long pushtimemin = pushhour * 60 * 60 * 1000l + pushminute * 60 *1000l - 1 * 60 * 1000l;
						long pushtimemax = pushhour * 60 * 60 * 1000l + pushminute * 60 *1000l + 2 * 60 * 1000l;
						if (current >= pushtimemin && current <= pushtimemax) {
							AlarmPushHelper.pushNotifition(context, messageContent);
						}
					}
					
				}
			}
			
			
			//年 月日 的推送
			ArrayList<String> ymdList = AlarmPushHelper.getAlarmPushContentYMD(context);
			if (ymdList != null) {
				 for (int i = 0; i < ymdList.size(); i++) {
					SLogUtil.logI("推送内容时间---->"+ymdList.get(i));
					
					String[] split = ymdList.get(i).split("#");
					int pyuer = Integer.parseInt(split[0]);
					int pmonth = Integer.parseInt(split[1]);
					int pday = Integer.parseInt(split[2]);
					int phour = Integer.parseInt(split[3]);
					int pminute = Integer.parseInt(split[4]);
					String messageContent = split[5];
					
					if (year == pyuer && month == pmonth && day == pday) {
						SLogUtil.logI("推送内容时间---->"+"phour:"+phour+"  pminute:"+pminute+"  messageContent:"+messageContent);
						long pushtimemin = phour * 60 * 60 * 1000l + pminute * 60 *1000l - 1 * 60 * 1000l;
						long pushtimemax = phour * 60 * 60 * 1000l + pminute * 60 *1000l + 2 * 60 * 1000l;
						if (current >= pushtimemin && current <= pushtimemax) {
							AlarmPushHelper.pushNotifition(context, messageContent);
						}
					}
				}
			}
			
			
			
			
		}else if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			String munite = AlarmPushHelper.getAlarmPushTimeMinute(context);
			String cycle = AlarmPushHelper.getAlarmPushTimeInterval(context);
			
			if (!TextUtils.isEmpty(munite) && !TextUtils.isEmpty(cycle)) {
				int m = Integer.parseInt(munite);
				int c = Integer.parseInt(cycle);

				SLogUtil.logI("定時推送广播开始分鐘 --> " + m);
				SLogUtil.logI("定時推送广播周期 --> " + c);
				AlarmPushHelper.setAlarmPush(context, m, c);
				SLogUtil.logI("AlarmPushReceiver had run when system start!");
			}
		}

	}

}
