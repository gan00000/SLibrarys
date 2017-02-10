package com.starpy.sdk.callback;

import com.core.base.callback.EfunCallBack;

public interface EfunFetchMarkerCallback extends EfunCallBack {
	public void onSuccess(String json);
	public void onError();
}
