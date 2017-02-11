package com.core.base.callback;

import android.os.Bundle;

public interface ISPayCallBack extends ISCallBack {
	
	void onPaySuccess(Bundle bundle);
	
	void onPayFailure(Bundle bundle);
}


