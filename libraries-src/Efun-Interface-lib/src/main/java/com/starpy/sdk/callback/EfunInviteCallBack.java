package com.starpy.sdk.callback;

import com.starpy.base.callback.EfunCallBack;

import android.os.Bundle;

/**
* <p>Title: EfunInviteCallBack</p>
* <p>Description: 邀请回调接口</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface EfunInviteCallBack extends EfunCallBack {
	
	void onCloseInvite(Bundle bundle);
}
