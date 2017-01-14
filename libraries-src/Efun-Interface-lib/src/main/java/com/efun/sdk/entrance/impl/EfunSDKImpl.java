package com.efun.sdk.entrance.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.efun.sdk.entrance.EfunSDK;
import com.efun.sdk.entrance.constant.EfunChannelType;
import com.efun.sdk.entrance.constant.EfunShareType;
import com.efun.sdk.entrance.entity.EfunCustomerServiceEntity;
import com.efun.sdk.entrance.entity.EfunFormEntity;
import com.efun.sdk.entrance.entity.EfunInvitationEntity;
import com.efun.sdk.entrance.entity.EfunLoginEntity;
import com.efun.sdk.entrance.entity.EfunLogoutEntity;
import com.efun.sdk.entrance.entity.EfunPayEntity;
import com.efun.sdk.entrance.entity.EfunPlatformEntity;
import com.efun.sdk.entrance.entity.EfunProtocolEntity;
import com.efun.sdk.entrance.entity.EfunRankingEntity;
import com.efun.sdk.entrance.entity.EfunSettingEntity;
import com.efun.sdk.entrance.entity.EfunShareEntity;
import com.efun.sdk.entrance.entity.EfunUser;


/**
 * 
 *  此子类此类的类路径固定，请勿修改
 * 	SDK接口内部实现，不提供外部实例，为了对厂商接口统一，请勿随意添加接口方法，子类实现由SDK制作人员实现，父类对外提供接口使用
 * 	如需要添加新接口，请让管理人员添加
 *
 *@author gan
 */
public class EfunSDKImpl extends EfunSDK{
	
	
	/**
	 * 请勿移除此构造方法
	 */
	public EfunSDKImpl() {
		Log.d(SDK_TAG, "EfunSDKImpl constructor");
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onCreate(android.app.Activity, android.os.Bundle)
	 */
	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		super.onCreate(activity, savedInstanceState);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onStart(android.app.Activity)
	 */
	@Override
	public void onStart(Activity activity) {
		super.onStart(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onRestart(android.app.Activity)
	 */
	@Override
	public void onRestart(Activity activity) {
		super.onRestart(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onResume(android.app.Activity)
	 */
	@Override
	public void onResume(Activity activity) {
		super.onResume(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onActivityResult(android.app.Activity, int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		super.onActivityResult(activity, requestCode, resultCode, data);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onPause(android.app.Activity)
	 */
	@Override
	public void onPause(Activity activity) {
		super.onPause(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onStop(android.app.Activity)
	 */
	@Override
	public void onStop(Activity activity) {
		super.onStop(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#onDestroy(android.app.Activity)
	 */
	@Override
	public void onDestroy(Activity activity) {
		super.onDestroy(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunLogin(android.content.Context, com.efun.sdk.entrance.entity.EfunLoginEntity)
	 */
	@Override
	public void efunLogin(Context context, EfunLoginEntity entity) {
		super.efunLogin(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunLogout(android.content.Context, com.efun.sdk.entrance.entity.EfunLogoutEntity)
	 */
	@Override
	public void efunLogout(Context context, EfunLogoutEntity entity) {
		super.efunLogout(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunPay(android.content.Context, com.efun.sdk.entrance.entity.EfunPayEntity)
	 */
	@Override
	public void efunPay(Context context, EfunPayEntity entity) {
		super.efunPay(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunCustomerService(android.content.Context, com.efun.sdk.entrance.entity.EfunCustomerServiceEntity)
	 */
	@Override
	public void efunCustomerService(Context context, EfunCustomerServiceEntity entity) {
		super.efunCustomerService(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunAds(android.app.Activity)
	 */
	@Override
	public void efunAds(Activity activity) {
		super.efunAds(activity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunShowPlatform(android.content.Context, com.efun.sdk.entrance.entity.EfunPlatformEntity)
	 */
	@Override
	public void efunShowPlatform(Context context, EfunPlatformEntity entity) {
		super.efunShowPlatform(context, entity);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunHiddenPlatform(android.content.Context)
	 */
	@Override
	public void efunHiddenPlatform(Context context) {
		super.efunHiddenPlatform(context);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunResumePlatform(android.content.Context)
	 */
	@Override
	public void efunResumePlatform(Context context) {
		super.efunResumePlatform(context);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunDestoryPlatform(android.content.Context)
	 */
	@Override
	public void efunDestoryPlatform(Context context) {
		super.efunDestoryPlatform(context);
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunShare(android.content.Context, com.efun.sdk.entrance.entity.EfunShareEntity)
	 */
	@Override
	public void efunShare(Context context, EfunShareEntity entity) {
		super.efunShare(context, entity);
		
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunInvitation(android.content.Context, com.efun.sdk.entrance.entity.EfunInvitationEntity)
	 */
	@Override
	public void efunInvitation(Context context, EfunInvitationEntity entity) {
		super.efunInvitation(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunRanking(android.content.Context, com.efun.sdk.entrance.entity.EfunRankingEntity)
	 */
	@Override
	public void efunRanking(Context context, EfunRankingEntity entity) {
		super.efunRanking(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunChannelType(android.content.Context, com.efun.sdk.entrance.constant.EfunChannelType)
	 */
	@Override
	public void efunChannelType(Context context, EfunChannelType channelType) {
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunForum(android.content.Context, com.efun.sdk.entrance.entity.EfunFormEntity)
	 */
	@Override
	public void efunForum(Context context, EfunFormEntity entity) {
		super.efunForum(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#efunProtocol(android.content.Context, com.efun.sdk.entrance.entity.EfunProtocolEntity)
	 */
	@Override
	public void efunProtocol(Context context, EfunProtocolEntity entity) {
		super.efunProtocol(context, entity);
		
		//TODO  ...
	}

	/* (non-Javadoc)
	 * @see com.efun.sdk.entrance.EfunSDK#getEfunUser(android.content.Context)
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
	 * @see com.efun.sdk.entrance.EfunSDK#efunSystemSetting(android.content.Context, com.efun.sdk.entrance.entity.EfunSettingEntity)
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
