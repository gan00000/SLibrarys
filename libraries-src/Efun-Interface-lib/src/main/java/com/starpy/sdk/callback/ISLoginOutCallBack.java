package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

import android.os.Bundle;

/**
* <p>Title: ISLoginOutCallBack</p>
* <p>Description: 注销回调接口</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface ISLoginOutCallBack extends ISCallBack {
	
	void onLoginOutSuccess(Bundle bundle);
	
	void onLoginOutFail(Bundle bundle);
	
}
