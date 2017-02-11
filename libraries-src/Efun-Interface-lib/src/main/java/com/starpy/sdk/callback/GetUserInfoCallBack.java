package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

import android.os.Bundle;

public interface GetUserInfoCallBack extends ISCallBack {
	
	void onGetUserInfoSuceess(Bundle userInfo);
	
	void onGetUserInfoError(Bundle bundle);
}
