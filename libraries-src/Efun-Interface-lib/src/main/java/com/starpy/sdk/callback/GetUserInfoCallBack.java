package com.starpy.sdk.callback;

import com.core.base.callback.EfunCallBack;

import android.os.Bundle;

public interface GetUserInfoCallBack extends EfunCallBack {
	
	void onGetUserInfoSuceess(Bundle userInfo);
	
	void onGetUserInfoError(Bundle bundle);
}
