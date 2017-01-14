package com.efun.sdk.callback;

import com.efun.core.callback.EfunCallBack;

public interface ProtocolCompleteListener extends EfunCallBack {
	
	void onCancel();
	
	void onAgree();
	
}
