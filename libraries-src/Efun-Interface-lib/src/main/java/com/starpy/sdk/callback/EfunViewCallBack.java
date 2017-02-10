package com.starpy.sdk.callback;

import com.starpy.base.callback.EfunCallBack;

import android.os.Bundle;

/**
* <p>Title: EfunViewCallBack</p>
* <p>Description: 监测页面回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface EfunViewCallBack extends EfunCallBack {
	
	void onViewCreate(Bundle bundle);
	
	void onViewDestory(Bundle bundle);
	
}
