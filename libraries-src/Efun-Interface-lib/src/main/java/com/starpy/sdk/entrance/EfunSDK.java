package com.starpy.sdk.entrance;

import java.util.List;

import com.core.base.callback.ISCallBack;
import com.core.base.utils.SPUtil;
import com.core.base.utils.PermissionUtil;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.entrance.constant.EfunChannelType;
import com.starpy.sdk.entrance.constant.EfunPayType;
import com.starpy.sdk.entrance.constant.EfunShareType;
import com.starpy.sdk.entrance.entity.EfunAdsWallEntity;
import com.starpy.sdk.entrance.entity.EfunBindPhoneEntity;
import com.starpy.sdk.entrance.entity.EfunCafeEntity;
import com.starpy.sdk.entrance.entity.EfunCustomerServiceEntity;
import com.starpy.sdk.entrance.entity.EfunFormEntity;
import com.starpy.sdk.entrance.entity.EfunGetUserEntity;
import com.starpy.sdk.entrance.entity.EfunInvitationEntity;
import com.starpy.sdk.entrance.entity.EfunLoginEntity;
import com.starpy.sdk.entrance.entity.EfunLogoutEntity;
import com.starpy.sdk.entrance.entity.EfunNoteEntity;
import com.starpy.sdk.entrance.entity.EfunNotificationEntity;
import com.starpy.sdk.entrance.entity.EfunPayEntity;
import com.starpy.sdk.entrance.entity.EfunPlatformEntity;
import com.starpy.sdk.entrance.entity.EfunProtocolEntity;
import com.starpy.sdk.entrance.entity.EfunRankingEntity;
import com.starpy.sdk.entrance.entity.EfunSettingEntity;
import com.starpy.sdk.entrance.entity.EfunShareEntity;
import com.starpy.sdk.entrance.entity.EfunTrackingEventEntity;
import com.starpy.sdk.entrance.entity.EfunUser;
import com.starpy.sdk.entrance.entity.EfunUserCenterEntity;
import com.starpy.sdk.entrance.entity.EfunWebPageEntity;
import com.starpy.sdk.entrance.impl.EfunSDKImpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * <p>
 * Title: EfunSDK
 * </p>
 * <p>
 * Description: EFUN SDK统一接口类
 * </p>
 * <p>
 * Company: EFun
 * </p>
 * 
 * @author GanYuanrong
 */
public abstract class EfunSDK {

	private static EfunSDK efunSDK = null;
	public static final String SDK_TAG = "EfunSDK";

	public static final int EFUN_REQUEST_PERMISSIONS_STATE_CODE = 13;

	protected EfunSDK() {
		Log.d(SDK_TAG, "EfunSDK constructor");
	}

	/**
	 * <p>
	 * Description: 获取实例对象
	 * </p>
	 * 
	 * @return 返回实例对象
	 */
	public static final synchronized EfunSDK getInstance() {
		if (efunSDK == null) {
			// Log.d(SDK_TAG, "new EfunSDKImpl");
			Log.d(SDK_TAG, "efun-interface-3.6");
			efunSDK = new EfunSDKImpl();
		}
		return efunSDK;
	}

	public void onCreate(Activity activity, Bundle savedInstanceState) {
	}

	public void onStart(Activity activity) {
	}

	public void onRestart(Activity activity) {
	}

	public void onResume(Activity activity) {
	}

	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
	}

	public void onPause(Activity activity) {
	}

	public void onStop(Activity activity) {
	}

	public void onDestroy(Activity activity) {
	}

	/**
	 * <p>
	 * Description: 判断是否有相应的权限
	 * </p>
	 * 
	 * @param activity
	 * @param permission
	 *            权限常量
	 * @date 2015年10月27日
	 */
	public boolean efunHasSelfPermission(Activity activity, String permission) {
		return PermissionUtil.hasSelfPermission(activity, permission);
	}

	public boolean efunHasSelfPermissions(Activity activity, String[] permissions) {
		return PermissionUtil.hasSelfPermission(activity, permissions);
	}

	public void efunRequestPermission(Activity activity, String permission, int requestCode) {
		PermissionUtil.requestPermission(activity, permission, requestCode);
	}

	public void efunRequestPermissions(Activity activity, String[] permissions, int requestCode) {
		PermissionUtil.requestPermissions(activity, permissions, requestCode);
	}

	/**
	 * <p>
	 * Description: 权限请求(6.0以上系统并且targetVersionCode >= 23的时候需要)
	 * </p>
	 * 
	 * @param activity
	 * @date 2015年10月17日
	 */
	public void efunRequestPermission(Activity activity) {
		PermissionUtil.requestPermissions_PHONE_STORAGE(activity, EFUN_REQUEST_PERMISSIONS_STATE_CODE);
	}

	/**
	 * <p>
	 * Description: 权限请求结果返回。当权限请求结果在游戏activity回调之后，游戏调用此方法可以传回相关参数给SDK
	 * </p>
	 * 
	 * @param activity
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 * @date 2015年10月17日
	 */
	public void efunRequestPermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {

	}

	/**
	 * <p>
	 * Description: 登陆
	 * </p>
	 * 
	 * @param context
	 *            上下文
	 * @param entity
	 *            登陆 参数包装对象EfunLoginEntity
	 */
	
	public void efunLogin(Context context,EfunLoginEntity entity){
		if (entity.getISCallBack() == null) {
			Log.e(SDK_TAG, "login call back is null");
		}
	}

	/**
	 * <p>
	 * Description: 登出/注销
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunLogout(Context context, EfunLogoutEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 储值接口
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunPay(Context context, EfunPayEntity entity) {
		checkContext(context);
		checkEfunPayEntity(entity);
	}

	/**
	 * <p>
	 * Description: 客服
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunCustomerService(Context context, EfunCustomerServiceEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 广告
	 * </p>
	 * 
	 * @param activity
	 */
	public void efunAds(Activity activity) {
		checkContext(activity);
	}

	public void efunPlatformByStatu(Context context, EfunPlatformEntity entity) {
		checkContext(context);
	}

	/**
	 * 创建并显示悬浮按钮
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunShowPlatform(Context context, EfunPlatformEntity entity) {
		checkContext(context);
	}

	/**
	 * Pause And Stop生命周期调用，隐藏内嵌平台
	 * 
	 * @param context
	 */
	public void efunHiddenPlatform(Context context) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 恢复悬浮按钮
	 * </p>
	 * 
	 * @param context
	 */
	public void efunResumePlatform(Context context) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 销毁悬浮按钮
	 * </p>
	 * 
	 * @param context
	 */
	public void efunDestoryPlatform(Context context) {
		checkContext(context);
	}

	/**
	 * 社交分享
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunShare(Context context, EfunShareEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 邀请
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunInvitation(Context context, EfunInvitationEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 好友排行
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunRanking(Context context, EfunRankingEntity entity) {
		checkContext(context);
	};

	public final static String EFUN_SDK_LANGUAGE = "EFUN_SDK_LANGUAGE";

	/**
	 * <p>
	 * Description: 设置SDK语言
	 * </p>
	 * 
	 * @param context
	 *            上下文
	 * @param language
	 *            语言 具体语言值参看EfunLanguage类常量
	 * @date 2016年3月10日
	 */
	public void efunSetLanguage(Context context, String language) {
		Log.d(SDK_TAG, "setScrollDuration gameLanguage:" + language);
		SPUtil.saveSimpleInfo(context, StarPyUtil.STAR_PY_SP_FILE, EFUN_SDK_LANGUAGE, language);
	}

	// ===========================================================================================================================
	// ===========================================================================================================================
	// 以下接口为与IOS不相同的接口（或者Android平台特有接口）
	// ===========================================================================================================================
	// ===========================================================================================================================

	/**
	 * 设置渠道或者平台标识，用于判断改包为哪个渠道或者平台的包
	 * 
	 * @param context
	 */
	public void efunChannelType(Context context, EfunChannelType channelType) {
		checkContext(context);
	}
	
	/**
	 * 打開平台的绑定手机網頁(韩国)
	 * @param context
	 * @param entity
	 */
	public void efunBindPhone(Context context, EfunBindPhoneEntity entity) {
		checkContext(context);
	}
	
	/**
	 * 打开个人中心页面（韩国）
	 * @param context
	 * @param entity
	 */
	public void efunUserCenter(Context context, EfunUserCenterEntity entity) {
		checkContext(context);
	}
	
	/**
	 * 打开cafe主页（韩国）
	 * @param context
	 * @param entity：需要传角色ID
	 */
	public void efunCafeHome(Context context, EfunCafeEntity entity) {
		checkContext(context);
	}
	
	/**
	 * <p>
	 * Description: 打开论坛
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunForum(Context context, EfunFormEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 协议
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunProtocol(Context context, EfunProtocolEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 * @return 返回实例对象
	 */
	public EfunUser getEfunUser(Context context) {
		checkContext(context);
		return new EfunUser();
	}

	public void getEfunUserAsync(Context context, EfunGetUserEntity userEntity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description: 系统设置
	 * </p>
	 * 
	 * @param context
	 * @param entity
	 */
	public void efunSystemSetting(Context context, EfunSettingEntity entity) {
		checkContext(context);
	}

	/**
	 * <p>
	 * Description:广告墙
	 * </p>
	 * 
	 * @param context
	 */
	public void efunAdsWall(Context context, EfunAdsWallEntity adsWallEntity) {
		checkContext(context);

	}

	/**
	 * <p>
	 * Description: 展示公告
	 * </p>
	 * 
	 * @param context
	 */
	public void efunShowNote(Context context, EfunNoteEntity efunNoteEntity) {
		checkContext(context);
	}

	public void efunOpenWebPage(Context context, EfunWebPageEntity efunWebPageEntity) {

	}

	public void efunTrackingEvent(Context context, EfunTrackingEventEntity trackingEventEntity) {

	}

	public void efunFuc(Context context, ISCallBack callBack, String... s) {

	}
	
	/** 消息通知
	 * @param context
	 * @param efunNotificationEntity
	 */
	public void efunNotification(Context context,EfunNotificationEntity efunNotificationEntity) {
		
	}
	
	/**
	 * 提交玩家数据到排行榜
	 * 
	 * @param act
	 * @param leaderboaderId
	 *            排行榜
	 * @param score
	 */
	public void submitGoogleGameScore(Activity act, String leaderboaderId, long score) {
	}

	/**
	 * 显示指定排行榜接口
	 * 
	 * @param act
	 * @param leaderboaderId
	 *            排行榜id
	 */
	public void showGoogleGameLeaderboader(Activity act, String leaderboaderId) {

	}

	/**
	 * 显示所有排行榜接口
	 * 
	 * @param act
	 */
	public void showGoogleGameAllLeaderboaders(Activity act) {

	}

	/**
	 * 显示玩家个人成就
	 * 
	 * @param act
	 */
	public void showGoogleGameAchievement(Activity act) {

	}

	/**
	 * 解锁玩家成就（该成就可以分为多个步骤完成）
	 * 
	 * @param act
	 * @param achievementId
	 */
	public void unlockGoogleGameAchievementYesStep(Activity act, String achievementId) {

	}

	/**
	 * 解锁玩家成就（该成就无多个阶段）
	 * 
	 * @param act
	 * @param achievementId
	 */
	public void unlockGoogleGameAchievementNoStep(Activity act, String achievementId) {

	}

	public EfunPayType getPayType(Context context) {

		return null;
	}

	public List<EfunPayType> getPayTypes(Context context) {

		return null;
	}

	public EfunChannelType getEfunChannelType(Context context) {
		return null;
	}

	public String getEfunLoginType(Context context) {
		return null;
	}

	public String getEfunChannel(Context context) {
		return null;
	}

	public void onBackPressed() {
	}

	/**
	 * <p>
	 * Description: 检查上下文
	 * </p>
	 * 
	 * @param context
	 */
	void checkContext(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("login context is null");
		}
	}

	/**
	 * <p>
	 * Description: 检查储值参数
	 * </p>
	 * 
	 * @param entity
	 */
	void checkEfunPayEntity(EfunPayEntity entity) {
		if (TextUtils.isEmpty(entity.getUserId())) {
			throw new IllegalArgumentException("userid is empty,must be not empty");
		} else if (TextUtils.isEmpty(entity.getServerCode())) {
			throw new IllegalArgumentException("servercode is empty,must be not empty");
		} else if (TextUtils.isEmpty(entity.getCreditId())) {
			throw new IllegalArgumentException("creditid is empty,must be not empty");
		}
	}

	public boolean shouldShareWithType(Context context, EfunShareType shareType) {
		return false;
	}

}
