package com.starpy.googlepay;

import com.starpy.base.utils.SLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.bean.GoogleOrderBean;
import com.starpy.googlepay.task.SAsyncPurchaseTask;
import com.starpy.googlepay.task.QueryInventoryFinishedListenerImpl;
import com.starpy.util.IabHelper;
import com.starpy.util.IabResult;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;

public class EfunGooglePayService extends Service {
	
	/**
	 * 打开service要执行的功能类别Extra
	 */
	public static final String GooglePayFunctionType = "GOOGLE_PAY_FUNCTION_TYPE";
	/**
	 * 标识要进行set up google pay远程服务
	 */
	public static final String GooglePaySetUp = "GOOGLE_PAY_SETUP";
	/**
	 *  set up google pay远程服务的时间Extra （两个地方set up服务，一个是游戏启动的时候，一个是进入商品界面的时候）
	 */
	public static final String GooglePaySetUpTiming = "GOOGLE_PAY_SETUP_TIMING";
	/**
	 * 表示游戏启动的时候set up服务
	 */
	public static final int GooglePaySetUpTiming_GameStart = 100;
	/**
	 * 表示进入商品界面购买的时候set up
	 */
	public static final int GooglePaySetUpTiming_GamePurchase = 200;
	/**
	 * 标识要进行google pay购买流程
	 */
	public static final String GooglePayStartPurchase = "GOOGLE_PAY_START_PURCHASE";
	
	/**
	 * 标识要进行google pay付款后的回调处理
	 */
	public static final String GooglePayHandleActivityResult = "GOOGLE_PAY_HANDLE_ACTIVITY_RESULT";
	
	public static final String GooglePaySku = "GOOGLE_PAY_SKU";

	private static IabHelper mHelper;
	/**
	 * @return the mHelper
	 */
	public static IabHelper getIabHelper() {
		return mHelper;
	}

	private int setUpTiming = 0;
	
//	private boolean supportGooglePay = true;
	
	private static BasePayActivity payActivity;

	/**
	 * @return the payActivity
	 */
	public static BasePayActivity getPayActivity() {
		return payActivity;
	}


	/**
	 * @param payActivity the payActivity to set
	 */
	public static void setPayActivity(BasePayActivity payActivity) {
		EfunGooglePayService.payActivity = payActivity;
	}
	
	public static GoogleOrderBean googleOrderBean = new GoogleOrderBean();
	
	EfunPayBroadcastReceiver epbr;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Notification notification = new Notification();
		startForeground(0, notification);//设置服务为前台服务，防止server被回收，0不显示任务栏通知
		epbr = new EfunPayBroadcastReceiver();
		IntentFilter filter = new IntentFilter("com.android.vending.billing.PURCHASES_UPDATED");
		this.registerReceiver(epbr, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SLogUtil.logD("startId:" + startId);
		
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		
		
		if (mHelper == null) {
			mHelper = new IabHelper(this);
		}else{
			if (!mHelper.isSetupDone() || !mHelper.isSupported() || !mHelper.inappServiceOK()) {
				mHelper.dispose();
				mHelper = null;
				mHelper = new IabHelper(this);
			}
		}
		mHelper.enableDebugLogging(true);
		
		String payFucType = intent.getStringExtra(GooglePayFunctionType);//获取打开server的作用功能
		SLogUtil.logD("payFucType:" + payFucType);
		if (!TextUtils.isEmpty(payFucType)) {
			if (payFucType.equals(GooglePaySetUp)) {//启动储值原厂服务
				setUpTiming = intent.getIntExtra(GooglePaySetUpTiming, 0);
				if (setUpTiming == EfunGooglePayService.GooglePaySetUpTiming_GamePurchase) {//打开储值页面的时候setup
					efunHelperSetUp(true);
				}else{
					efunHelperSetUp(false);
				}
				
			}else if (payFucType.equals(GooglePayStartPurchase) && startId != 1) {//开始储值流程
				String _skus = intent.getStringExtra(GooglePaySku);
				startPurchase(_skus);
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) { 
			mHelper.dispose();
		}
		if (epbr != null) {
			this.unregisterReceiver(epbr);
		}
	}
	
	

	/**
	 * <p>Description: 启动远程服务</p>
	 * @param isInView  是否是在页面储值的时候启动
	 * @date 2016年1月5日
	 */
	protected void efunHelperSetUp(final boolean isInView){
		if (isInView && payActivity != null) {
			showProgressDialog();
		}
		if (null != mHelper) {
				SLogUtil.logD("startSetup iab.");
				if (!mHelper.isSetupDone() || !mHelper.inappServiceOK()) {
				mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

					public void onIabSetupFinished(IabResult result) {
						SLogUtil.logD("startSetup onIabSetupFinished.");
						if (!result.isSuccess()) {
							SLogUtil.logD("start set up not success:" + result.getMessage());
						//	supportGooglePay = false;
							if (isInView && payActivity != null) {//弹框提示
								dismissProgressDialog();
								payActivity.showGoogleStoreErrorMessage();
							}
							return;
						}
						SLogUtil.logD("开始检查有没有消费的商品");
						startQueryInventory();
						//mHelper.getPuchase();
					}
				});
			} else {
				startQueryInventory();
			}
		}else{
			SLogUtil.logD("mHelper is null");
			complainCloseAct("error");
		}

	}
	
	private void startQueryInventory(){
		SLogUtil.logD("set up timing:" + setUpTiming);
		if (EfunGooglePayService.mHelper.isAsyncInProgress()) {
			SLogUtil.logD("startQueryInventory isAsyncInProgress true");
			complain("error,try again");
			return;
		}
		if (payActivity == null) {
			mHelper.queryInventoryAsync(new QueryInventoryFinishedListenerImpl(EfunGooglePayService.this,setUpTiming));
		}else{
			mHelper.queryInventoryAsync(new QueryInventoryFinishedListenerImpl(payActivity,setUpTiming));
		}
	}
	
	public synchronized void startPurchase(String sku){
		
		if (SStringUtil.isEmpty(sku)) {
			SLogUtil.logD("sku is empty");
			return;
		}
		
		if (payActivity != null && !EfunGooglePayService.mHelper.isAsyncInProgress() &&  payActivity.get_GoogleOrderBean() != null) {
			GoogleOrderBean gob = payActivity.get_GoogleOrderBean();
			GoogleOrderBean gobClone = (GoogleOrderBean)gob.cloneGoogleOrderBean();
			if (gob != gobClone) {
				SLogUtil.logD("gob != gobClone");
				new SAsyncPurchaseTask(payActivity, EfunGooglePayService.mHelper,gobClone).asyncExcute();
			}else{
				SLogUtil.logD("gob == gobClone");
				new SAsyncPurchaseTask(payActivity, EfunGooglePayService.mHelper,gobClone).asyncExcute();
			}
		}else{
			SLogUtil.logE("payActivity is null || isAsyncInProgress");
		}
	}
	
	public static void complain(String message){
		if (payActivity != null && payActivity.getPayPrompt() != null && !TextUtils.isEmpty(message)) {
			payActivity.getPayPrompt().complain(message);
		}
	}
	public static void complainCloseAct(String message){
		if (payActivity != null && payActivity.getPayPrompt() != null && !TextUtils.isEmpty(message)) {
			payActivity.getPayPrompt().complainCloseAct(message);
		}
	}
	public static void dismissProgressDialog(){
		if (payActivity != null && payActivity.getPayPrompt() != null) {
			payActivity.getPayPrompt().dismissProgressDialog();
		}
	}
	public static void showProgressDialog(){
		if (payActivity != null && payActivity.getPayPrompt() != null) {
			payActivity.getPayPrompt().showProgressDialog();
		}
	}

}
