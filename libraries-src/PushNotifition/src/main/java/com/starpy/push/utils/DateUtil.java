package com.starpy.push.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	
	/**
	* <p>Title: getTime</p>
	* <p>Description:获取日期，格式为 yyyy-MM-dd HH:mm:ss </p>
	* @param time
	* @return
	*/
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		return format.format(new Date(time));
	}
	
	public static String getDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		return format.format(new Date(time));
	}
}
