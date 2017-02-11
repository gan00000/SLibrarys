package com.starpy.sdk.callback;

import com.core.base.callback.ISCallBack;

import android.os.Bundle;

/**
* <p>Title: ISShareCallback</p>
* <p>Description: 分享回调</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年10月17日
*/
public interface ISShareCallback extends ISCallBack {
	
	void onShareSuccess(Bundle bundle);

	void onShareFail(Bundle bundle);
	
}
