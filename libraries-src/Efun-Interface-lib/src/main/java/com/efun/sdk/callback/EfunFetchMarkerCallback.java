package com.efun.sdk.callback;

import com.efun.core.callback.EfunCallBack;

public interface EfunFetchMarkerCallback extends EfunCallBack {
	public void onSuccess(String json);
	public void onError();
}
