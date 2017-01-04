package com.efun.core.callback;

import android.os.Bundle;

public interface EfunPayCallBack extends EfunCallBack{
	
	void onPaySuccess(Bundle bundle);
	
	void onPayFailure(Bundle bundle);
}


