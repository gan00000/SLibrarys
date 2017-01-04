package com.efun.platform.login.comm.callback;



import android.content.Context;

/**
 * Class Description：Callback server Singleton save params.
 * @author Joe
 * @date 2013-4-16
 * @version 1.0
 */
public class CallBackServer {
	
	private static CallBackServer callback;
	private static Context mContext;
	
	private static OnEfunLoginListener onEfunLoginListener;
	
	public static CallBackServer getInstance(){
		if(callback==null){
			callback = new CallBackServer();
		}
		return callback;
	}
	
	public static CallBackServer getInstance(Context context){
		mContext = context;
		if(callback==null){
			callback = new CallBackServer();
		}
		return callback;
	}
	
	/**
	 * 获取上下文环境
	 * @return
	 */
	public static Context getmContext() {
		return mContext;
	}
	
	public static void setmContext(Context mContext) {
		CallBackServer.mContext = mContext;
	}
	
	public OnEfunLoginListener getOnEfunLoginListener() {
		return onEfunLoginListener;
	}
	
	public void setOnEfunLoginListener(OnEfunLoginListener onEfunLoginListener) {
		CallBackServer.onEfunLoginListener = onEfunLoginListener;
	}
}
