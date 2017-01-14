package com.efun.sdk.callback;

import com.efun.core.callback.EfunCallBack;

import android.os.Bundle;

/**
* <p>Title: EfunLoginOutCallBack</p>
* <p>Description: 注销回调接口</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface EfunLoginOutCallBack extends EfunCallBack {
	
	void onLoginOutSuccess(Bundle bundle);
	
	void onLoginOutFail(Bundle bundle);
	
}
