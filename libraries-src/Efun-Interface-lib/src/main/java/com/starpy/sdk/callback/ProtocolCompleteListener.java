package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

public interface ProtocolCompleteListener extends ISCallBack {
	
	void onCancel();
	
	void onAgree();
	
}
