package com.starpy.base.utils;

import android.content.Context;
import android.widget.Toast;

public class EfunToast {

	public static void showS(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showL(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showS(Context context,int resId){
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	public static void showL(Context context,int resId){
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}
	
}
