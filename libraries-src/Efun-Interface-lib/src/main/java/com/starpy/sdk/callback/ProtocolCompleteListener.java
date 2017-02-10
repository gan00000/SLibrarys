package com.starpy.sdk.callback;

import com.starpy.base.callback.EfunCallBack;

public interface ProtocolCompleteListener extends EfunCallBack {
	
	void onCancel();
	
	void onAgree();
	
}
