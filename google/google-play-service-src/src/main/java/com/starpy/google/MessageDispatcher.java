package com.starpy.google;

import android.content.Context;

import java.util.Map;

public interface MessageDispatcher {
	
	public boolean onMessage(Context context, String message, Map<String,String> data);

	public boolean onClickNotification(Context context, String message,Map<String,String> data);

	public void onNotShowMessage(Context context, String message,Map<String,String> data);
}
