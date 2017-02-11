package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

public interface ISFetchMarkerCallback extends ISCallBack {
	public void onSuccess(String json);
	public void onError();
}
