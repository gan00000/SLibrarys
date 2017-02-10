package com.starpy.sdk.entrance.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.starpy.sdk.entrance.EfunSDK;
import com.starpy.sdk.entrance.constant.EfunChannelType;
import com.starpy.sdk.entrance.constant.EfunShareType;
import com.starpy.sdk.entrance.entity.EfunCustomerServiceEntity;
import com.starpy.sdk.entrance.entity.EfunFormEntity;
import com.starpy.sdk.entrance.entity.EfunInvitationEntity;
import com.starpy.sdk.entrance.entity.EfunLoginEntity;
import com.starpy.sdk.entrance.entity.EfunLogoutEntity;
import com.starpy.sdk.entrance.entity.EfunPayEntity;
import com.starpy.sdk.entrance.entity.EfunPlatformEntity;
import com.starpy.sdk.entrance.entity.EfunProtocolEntity;
import com.starpy.sdk.entrance.entity.EfunRankingEntity;
import com.starpy.sdk.entrance.entity.EfunSettingEntity;
import com.starpy.sdk.entrance.entity.EfunShareEntity;
import com.starpy.sdk.entrance.entity.EfunUser;


/**
 * 
 *  此子类此类的类路径固定，请勿修改
 * 	SDK接口内部实现，不提供外部实例，为了对厂商接口统一，请勿随意添加接口方法，子类实现由SDK制作人员实现，父类对外提供接口使用
 * 	如需要添加新接口，请让管理人员添加
 *
 *@author gan
 */
public class EfunSDKImpl extends EfunSDK {
	
	
	/**
	 * 请勿移除此构造方法
	 */
	public EfunSDKImpl() {
		Log.d(SDK_TAG, "EfunSDKImpl constructor");
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onCreate(android.app.Activity, android.os.Bundle)
	 */
	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		super.onCreate(activity, savedInstanceState);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onStart(android.app.Activity)
	 */
	@Override
	public void onStart(Activity activity) {
		super.onStart(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onRestart(android.app.Activity)
	 */
	@Override
	public void onRestart(Activity activity) {
		super.onRestart(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onResume(android.app.Activity)
	 */
	@Override
	public void onResume(Activity activity) {
		super.onResume(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onActivityResult(android.app.Activity, int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		super.onActivityResult(activity, requestCode, resultCode, data);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onPause(android.app.Activity)
	 */
	@Override
	public void onPause(Activity activity) {
		super.onPause(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onStop(android.app.Activity)
	 */
	@Override
	public void onStop(Activity activity) {
		super.onStop(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#onDestroy(android.app.Activity)
	 */
	@Override
	public void onDestroy(Activity activity) {
		super.onDestroy(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunLogin(android.content.Context, EfunLoginEntity)
	 */
	@Override
	public void efunLogin(Context context, EfunLoginEntity entity) {
		super.efunLogin(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunLogout(android.content.Context, EfunLogoutEntity)
	 */
	@Override
	public void efunLogout(Context context, EfunLogoutEntity entity) {
		super.efunLogout(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunPay(android.content.Context, EfunPayEntity)
	 */
	@Override
	public void efunPay(Context context, EfunPayEntity entity) {
		super.efunPay(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunCustomerService(android.content.Context, EfunCustomerServiceEntity)
	 */
	@Override
	public void efunCustomerService(Context context, EfunCustomerServiceEntity entity) {
		super.efunCustomerService(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunAds(android.app.Activity)
	 */
	@Override
	public void efunAds(Activity activity) {
		super.efunAds(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunShowPlatform(android.content.Context, EfunPlatformEntity)
	 */
	@Override
	public void efunShowPlatform(Context context, EfunPlatformEntity entity) {
		super.efunShowPlatform(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunHiddenPlatform(android.content.Context)
	 */
	@Override
	public void efunHiddenPlatform(Context context) {
		super.efunHiddenPlatform(context);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunResumePlatform(android.content.Context)
	 */
	@Override
	public void efunResumePlatform(Context context) {
		super.efunResumePlatform(context);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunDestoryPlatform(android.content.Context)
	 */
	@Override
	public void efunDestoryPlatform(Context context) {
		super.efunDestoryPlatform(context);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunShare(android.content.Context, EfunShareEntity)
	 */
	@Override
	public void efunShare(Context context, EfunShareEntity entity) {
		super.efunShare(context, entity);
		
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunInvitation(android.content.Context, EfunInvitationEntity)
	 */
	@Override
	public void efunInvitation(Context context, EfunInvitationEntity entity) {
		super.efunInvitation(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunRanking(android.content.Context, EfunRankingEntity)
	 */
	@Override
	public void efunRanking(Context context, EfunRankingEntity entity) {
		super.efunRanking(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunChannelType(android.content.Context, EfunChannelType)
	 */
	@Override
	public void efunChannelType(Context context, EfunChannelType channelType) {
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunForum(android.content.Context, EfunFormEntity)
	 */
	@Override
	public void efunForum(Context context, EfunFormEntity entity) {
		super.efunForum(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunProtocol(android.content.Context, EfunProtocolEntity)
	 */
	@Override
	public void efunProtocol(Context context, EfunProtocolEntity entity) {
		super.efunProtocol(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see EfunSDK#getEfunUser(android.content.Context)
	 */
	@Override
	public EfunUser getEfunUser(Context context) {
		EfunUser efunUser = new EfunUser();
		//TODO 
		//efunUser.setXXX
		//
		return efunUser;
	}

	/* (non-Javadoc)
	 * @see EfunSDK#efunSystemSetting(android.content.Context, EfunSettingEntity)
	 */
	@Override
	public void efunSystemSetting(Context context, EfunSettingEntity entity) {
		super.efunSystemSetting(context, entity);
		
		//TODO ...
	}

	@Override
	public boolean shouldShareWithType(Context context, EfunShareType shareType) {
		return super.shouldShareWithType(context, shareType);
	}
	
	

}
