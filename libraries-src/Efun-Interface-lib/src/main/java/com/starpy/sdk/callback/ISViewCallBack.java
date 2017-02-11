package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

import android.os.Bundle;

/**
* <p>Title: ISViewCallBack</p>
* <p>Description: 监测页面回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface ISViewCallBack extends ISCallBack {
	
	void onViewCreate(Bundle bundle);
	
	void onViewDestory(Bundle bundle);
	
}
