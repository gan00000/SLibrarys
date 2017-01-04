package com.efun.core.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
/**
 * 屏幕帮助类
 * @author 李小健
 *
 */
public class EfunScreenUtil {
	DisplayMetrics dm = null;
	Display display = null;
	static EfunScreenUtil util = null;
	private EfunScreenUtil(Activity activity){
	    dm = new DisplayMetrics();        
	    display = activity.getWindowManager().getDefaultDisplay();
	    display.getMetrics(dm);   
	}
	
	public static EfunScreenUtil getInStance(Activity activity){
		if(util == null){
			util = new EfunScreenUtil(activity);
		}
		return util;
	}
	
	/**
	 * 得到手机屏幕宽度
	 * @return
	 */
	public int getPxWidth(){
		return display.getWidth();
	}
	
	/**
	 * 获得手机屏幕宽度，单位dip
	 * @return
	 */
	public int getDipWidth(){
		return display.getWidth()/(int)dm.density;
	}
	
	/**
	 * 得到手机屏幕高度
	 * @return
	 */
	public int getPxHeight(){
		return display.getHeight();
	}
	

	
	/**
	 * 将dp转换成px
	 * @param dp_value
	 * @return
	 */
	public int getPxValue(int dp_value){
		return (int) (dm.density * dp_value);
	}
	/**
	 * 将px转换成dp
	 * @param px_value
	 * @return
	 */
	public int getDpValue(int px_value){
		return (int)(px_value/dm.density);
	}

	/**
	 * 获取状态栏高度
	 * @param activity
	 * @return
	 */
	public int getStatuHeight(Activity activity){
		Rect frame = new Rect();  
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}
	/**
	 * 获取横屏还是竖屏
	 * @param activity
	 * @return true为竖屏，false为横屏
	 */
	public boolean isPortrait(Activity activity)
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double mWidth = dm.widthPixels;
		double mHeight = dm.heightPixels;
		if(mHeight>mWidth){
			return true;
		}else if(mHeight<mWidth){
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是平板
	 * 
	 * @param context
	 * @return  true是手机，false 是平板
	 */
	public boolean isPhone(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		// 平板
		/*if (telManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
			return false;
		}*/
		if (TextUtils.isEmpty(EfunLocalUtil.getLocalImeiAddress(context))) {
			return false;
		}
		// 手机，利用分辨率防止一些可以打电话的平板
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenX = dm.widthPixels;
		int screenY = dm.heightPixels;
		int dpi = dm.densityDpi;
		float a = screenX / dpi;
		float b = screenY / dpi;
		double screenIn = Math.sqrt((a * a) + (b * b));
		if (screenIn > 5) {
			return false;
		}
		return true;
	}
}
