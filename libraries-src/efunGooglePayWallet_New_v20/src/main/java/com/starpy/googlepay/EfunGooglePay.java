package com.starpy.googlepay;

import com.starpy.base.SLogUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class EfunGooglePay {
	

	/**
	 * <p>Description: 启动应用调用</p>
	 * @param context
	 * @date 2015年8月11日
	 */
	/*public static void setUpGooglePay(Context context){
		Intent service = new Intent(context, EfunGooglePayService.class);
		service.putExtra(EfunGooglePayService.GooglePayFunctionType, EfunGooglePayService.GooglePaySetUp);
		service.putExtra(EfunGooglePayService.GooglePaySetUpTiming, EfunGooglePayService.GooglePaySetUpTiming_GameStart);
		context.startService(service );
	}*/
	

	/**
	 * <p>Description: 进入游戏获取到角色信息的时候进行未消费订单查询</p>
	 * @param context 上下文
	 * @param userId  efun user id
	 * @param gameCode gamecode
	 * @param serverCode 伺服器编号
	 * @param roleId 角色id
	 * @param roleName 角色名称
	 * @param roleLevel 角色等级
	 * @date 2016年1月19日
	 */
	public static void setUpGooglePay(Context context,String userId,String gameCode,String serverCode,String roleId,String roleName,String roleLevel){
		
		if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(serverCode) || TextUtils.isEmpty(roleId)) {
			SLogUtil.logE("userId、serverCode、roleId不能为空");
			return;
		}
		
		Intent service = new Intent(context, EfunGooglePayService.class);
		service.putExtra(EfunGooglePayService.GooglePayFunctionType, EfunGooglePayService.GooglePaySetUp);
		service.putExtra(EfunGooglePayService.GooglePaySetUpTiming, EfunGooglePayService.GooglePaySetUpTiming_GameStart);
		
		EfunGooglePayService.googleOrderBean.setUserId(userId);
		EfunGooglePayService.googleOrderBean.setGameCode(gameCode);
		
		EfunGooglePayService.googleOrderBean.setServerCode(serverCode);
		EfunGooglePayService.googleOrderBean.setRoleId(roleId);
		EfunGooglePayService.googleOrderBean.setEfunRole(roleName);
		EfunGooglePayService.googleOrderBean.setEfunLevel(roleLevel);
		
		context.startService(service);
	}
	
	
	/**
	 * <p>Description: 进入购买的时候进行查询</p>
	 * @param basePayActivity
	 * @date 2016年1月19日
	 */
	public static void setUpGooglePayByBasePayActivity(BasePayActivity basePayActivity){
		EfunGooglePayService.setPayActivity(basePayActivity);
		Intent service = new Intent(basePayActivity, EfunGooglePayService.class);
		service.putExtra(EfunGooglePayService.GooglePayFunctionType, EfunGooglePayService.GooglePaySetUp);
		service.putExtra(EfunGooglePayService.GooglePaySetUpTiming, EfunGooglePayService.GooglePaySetUpTiming_GamePurchase);
		basePayActivity.startService(service );
	}
	
	/**
	 * <p>Description: 开始购买</p>
	 * @param basePayActivity
	 * @param sku  商品id
	 * @date 2015年8月11日
	 */
	public static void startGooglePurchase(BasePayActivity basePayActivity,String sku){
		
		if (TextUtils.isEmpty(sku)) {
			SLogUtil.logD("sku is empty:" + sku);
			return;
		}
		EfunGooglePayService.setPayActivity(basePayActivity);
		
		Intent service = new Intent(basePayActivity, EfunGooglePayService.class);
		service.putExtra(EfunGooglePayService.GooglePayFunctionType, EfunGooglePayService.GooglePayStartPurchase);
		service.putExtra(EfunGooglePayService.GooglePaySku, sku);
		basePayActivity.startService(service );
	}
	
	/**
	 * <p>Description: google支付完成后的回调</p>
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return
	 * @date 2016年1月19日
	 */
	public static boolean handleActivityResult(BasePayActivity activity,int requestCode, int resultCode, Intent data){
		if (EfunGooglePayService.getIabHelper() != null) {
			boolean m = EfunGooglePayService.getIabHelper().handleActivityResult(requestCode, resultCode, data);
			if (activity != null) {
				activity.setLaunching(false);
			}
			return m;
		}else{
			SLogUtil.logD("EfunGooglePayService.getIabHelper() == null");
			if (activity != null) {
				activity.setLaunching(false);
			}
			return false;
		}
	}

}
