package com.starpy.push.client;

import android.content.Context;

public interface MessageDispatcher {
	
	public void onMessage(final Context context, final String message);
}