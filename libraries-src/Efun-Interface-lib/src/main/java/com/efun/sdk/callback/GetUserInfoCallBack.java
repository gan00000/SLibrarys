package com.efun.sdk.callback;

import com.efun.core.callback.EfunCallBack;

import android.os.Bundle;

public interface GetUserInfoCallBack extends EfunCallBack {
	
	void onGetUserInfoSuceess(Bundle userInfo);
	
	void onGetUserInfoError(Bundle bundle);
}
