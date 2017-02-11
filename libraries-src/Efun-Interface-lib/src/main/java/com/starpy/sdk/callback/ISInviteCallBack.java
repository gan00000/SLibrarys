package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

import android.os.Bundle;

/**
* <p>Title: ISInviteCallBack</p>
* <p>Description: 邀请回调接口</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface ISInviteCallBack extends ISCallBack {
	
	void onCloseInvite(Bundle bundle);
}
