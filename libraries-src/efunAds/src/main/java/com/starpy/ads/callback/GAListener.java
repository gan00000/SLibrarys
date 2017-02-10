package com.starpy.ads.callback;

import android.content.Context;
import android.content.Intent;

/**
* <p>Title: GAListener</p>
* <p>Description: google分析广播接收者的监听接口，用于在GABroadcact里面添加广告.实现此接口的类不要写其他构造方法，默认即可</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2013-7-17
*/
public interface GAListener {
	/**
	* <p>Title: GAonReceive</p>
	* <p>Description: 当GABroadcact广播执行nReceive方法时候该方法被调用</p>
	* @param ctx The Context in which the receiver is running
	* @param intent The Intent being received
	*/
	public void GAonReceive(Context ctx, Intent intent);
}
