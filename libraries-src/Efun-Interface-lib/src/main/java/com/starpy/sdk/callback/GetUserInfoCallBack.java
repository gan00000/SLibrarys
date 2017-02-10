package com.starpy.sdk.callback;

import com.starpy.base.callback.EfunCallBack;

import android.os.Bundle;

public interface GetUserInfoCallBack extends EfunCallBack {
	
	void onGetUserInfoSuceess(Bundle userInfo);
	
	void onGetUserInfoError(Bundle bundle);
}
