package com.starpy.sdk.callback;

import com.starpy.base.callback.EfunCallBack;

import android.os.Bundle;

/**
* <p>Title: EfunShareCallback</p>
* <p>Description: 分享回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface EfunShareCallback extends EfunCallBack {
	
	void onShareSuccess(Bundle bundle);

	void onShareFail(Bundle bundle);
	
}
