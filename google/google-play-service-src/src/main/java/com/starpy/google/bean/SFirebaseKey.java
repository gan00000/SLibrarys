package com.starpy.google.bean;

public class SFirebaseKey {

	/**
	 * 若需要配置键值对，并且sdk点击通知的时候需要获取，则需要配置这一个键，值为true,否则应用处于后台情况下点击通知无法获取配置的键值
	 */
	public static final String efun_firebase_message = "efun_firebase_message";
	/**
	 * 点击通知信息的时候，如果有此值，则打开该链接，否则打开应用
	 */
	public static final String efun_click_open_url = "efun_click_open_url";
	/**
	 * 若应用在前台，有次键的话，不显示消息；若应用处于后台，此值不起作用
	 */
	public static final String efun_not_show_message = "efun_not_show_message";
	/**
	 * 特殊的执行下载地址
	 */
	public static final String e_special_call_download = "e_special_call_download";
}
