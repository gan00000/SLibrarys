/**
 * 
 */
package com.starpy.pay.gp;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.SLog;
import com.starpy.pay.gp.bean.EfunPayError;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.bean.QueryInventoryState;
import com.starpy.pay.gp.callback.QueryItemListener;
import com.starpy.pay.gp.constants.GooglePayContant;
import com.starpy.pay.gp.task.EndFlag;
import com.starpy.pay.gp.task.PayDialog;
import com.starpy.pay.gp.task.QueryInventoryFinished;
import com.starpy.pay.gp.task.SAsyncPurchaseTask;
import com.starpy.pay.gp.util.IabHelper;
import com.starpy.pay.gp.util.IabResult;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.pay.gp.util.SkuDetails;

/**
 * <p>Title: BasePayActivity</p>
 * <p>Description: 初始化基本的参数</p>
 * @author GanYuanrong
 * @date 2014年2月24日
 */
public abstract class BasePayActivity extends Activity {
	
	public static final String GOOGLE_PAY_VERSION = "1.0";

	public static final String GOOGLE_PAY_VERSION_CHAGE_LOG = GOOGLE_PAY_VERSION + ":修复log日志打印msg为null的时候崩溃";

	// The helper object
	protected IabHelper mHelper;
	protected PayDialog payDialog;
	/**
	 * googleOrderBean Google储值购买订单参数封装
	 */
	protected volatile GooglePayCreateOrderIdReqBean googleOrderBean = null;

	/**
	 * efunPayError 错误提示语封装
	 */
	protected EfunPayError efunPayError;
	/**
	 * queryInventoryState 查询状态封装
	 */
	private QueryInventoryState queryInventoryState;


	protected boolean supportGooglePlay = true;
	protected String _gameCode;
	protected String _language;

	protected String _currentPurchaseSku;

	private SkuDetails skuDetails;

	/**
	 * payPreferredUrl 储值首选域名
	 */
	protected String payPreferredUrl;
	/**
	 * paySpareUrl 储值备用域名
	 */
	protected String paySpareUrl;

	private boolean isCloseActivityUserCancel = false;

	protected QueryItemListener queryItemListener;


	/**
	 * @return the queryItemListener
	 */
	public QueryItemListener getQueryItemListener() {
		return queryItemListener;
	}

	/**
	 * @param queryItemListener the queryItemListener to set
	 */
	public void setQueryItemListener(QueryItemListener queryItemListener) {
		this.queryItemListener = queryItemListener;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PayHelper.logCurrentVersion();
		efunPayError = new EfunPayError(this);
		initEfunPayErrorMessage(efunPayError);
		
		payDialog = new PayDialog(this);

		if (googleOrderBean !=null) {
			googleOrderBean.clear();
			googleOrderBean = null;
		}
		googleOrderBean = initGoogleOrderBean();
		
		initializeGoogleBean();

		mHelper = new IabHelper(this);
		queryInventoryState = new QueryInventoryState();
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);
		mHelper.enableDebugLogging(true);
		

		payDialog.setCloseActivity(isCloseActivityUserCancel);//设置弹框关闭activity
	}

	private void initializeGoogleBean() {
		
		if (googleOrderBean == null) {
			SLog.logE("googleOrderBean is null");
		}
		
		if (SStringUtil.isEmpty(googleOrderBean.getGameCode())) {
			_gameCode = ResConfig.getGameCode(this);
			if (SStringUtil.isEmpty(_gameCode)) {
				throw new RuntimeException("请先配置好gamecode");
			}
			googleOrderBean.setGameCode(_gameCode);
		}else{
			_gameCode = googleOrderBean.getGameCode();
		}
		
		if (SStringUtil.isEmpty(googleOrderBean.getGameLanguage())) {
			_language = ResConfig.getGameLanguage(this);
			googleOrderBean.setGameLanguage(_language);
		}else{
			_language = googleOrderBean.getGameLanguage();
		}
		
		if (SStringUtil.isEmpty(googleOrderBean.getPayFrom())) {
			googleOrderBean.setPayFrom(GooglePayContant.PAY_FROM);
		}
	}
	
	/**
	 * <p>Description: 直接購買，不需要彈出提示框</p>
	 * @param sku 商品id
	 * @date 2015年11月10日
	 */
	protected synchronized void startPurchaseWithoutDialog(String sku) {
		if (TextUtils.isEmpty(sku)) {
			this.finish();
			return;
		}
		this.isCloseActivityUserCancel = true;
		payDialog.setCloseActivity(isCloseActivityUserCancel);
		this._currentPurchaseSku = sku;
		setQueryItemListener(new QueryItemListener() {
			
			@Override
			public void onQueryFinish() {
				startPurchase(_currentPurchaseSku);
			}
		});
	}
	
	/**
	* <p>Title: startPurchase</p>
	* <p>Description: 开始购买商品（储值）</p>
	* @param sku 商品编号（标识）
	*/
	protected synchronized void startPurchase(String sku) {
		SLog.logI("EndFlag: " + EndFlag.isEndFlag());
		this._currentPurchaseSku = sku;
		if (EndFlag.isEndFlag()) {
			EndFlag.setEndFlag(false);
			if (EndFlag.isCanPurchase()) {
				/*if (!PermissionUtil.requestPermissions_PHONE_STORAGE(this, 11)) {
					return;
				}*/
				getGoogleOrderBean().setProductId(sku);
				new SAsyncPurchaseTask(BasePayActivity.this).asyncExcute();

			} else {
				payDialog.complainCloseAct("Please close the page and then buy again");
			}
		}
	}
	
	/**
	* <p>Title: googlePaySetUp</p>
	* <p>Description: 启动远程服务</p>
	*/
	protected void googlePaySetUp(){
		payDialog.showProgressDialog();
		if (null != mHelper && !mHelper.isSetupDone()) {
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result) {
					SLog.logI("startSetup onIabSetupFinished.");
					if (!result.isSuccess()) {
						payDialog.dismissProgressDialog();
						supportGooglePlay = false;
						showGoogleStoreErrorMessage();
						return;
					}
					mHelper.queryInventoryAsync(new QueryInventoryFinished(BasePayActivity.this));

				}

				@Override
				public void onError(String message) {//发生错误时提示
					SLog.logI("message:" + message);
					supportGooglePlay = false;
					payDialog.dismissProgressDialog();
					payDialog.complainCloseAct(message);
				}
			});

		}else{
			payDialog.dismissProgressDialog();
			if (isCloseActivityUserCancel) {
				this.finish();
			}
		}

	}
	
	protected abstract GooglePayCreateOrderIdReqBean initGoogleOrderBean();
	protected void initEfunPayErrorMessage(EfunPayError efunPayError){}


	public IabHelper getHelper() {
		return mHelper;
	}

	public void setHelper(IabHelper mHelper) {
		this.mHelper = mHelper;
	}

	public PayDialog getPayDialog() {
		return payDialog;
	}

	public void setPayDialog(PayDialog payDialog) {
		this.payDialog = payDialog;
	}


	public GooglePayCreateOrderIdReqBean getGoogleOrderBean() {
		return googleOrderBean;
	}


	public void showGoogleStoreErrorMessage() {
		if (isCloseActivityUserCancel) {
			payDialog.complainCloseAct(efunPayError.getGoogleStoreError());
		}else{
			payDialog.complain(efunPayError.getGoogleStoreError());
		}
	}

	public EfunPayError getEfunPayError() {
		return efunPayError;
	}

	public void setEfunPayError(EfunPayError efunPayError) {
		this.efunPayError = efunPayError;
	}

	public QueryInventoryState getQueryInventoryState() {
		return queryInventoryState;
	}

	public void setQueryInventoryState(QueryInventoryState queryInventoryState) {
		this.queryInventoryState = queryInventoryState;
	}


	public String getPayPreferredUrl() {
		return payPreferredUrl;
	}

	public void setPayPreferredUrl(String payPreferredUrl) {
		this.payPreferredUrl = payPreferredUrl;
	}

	public String getPaySpareUrl() {
		return paySpareUrl;
	}

	public void setPaySpareUrl(String paySpareUrl) {
		this.paySpareUrl = paySpareUrl;
	}

	public SkuDetails getSkuDetails() {
		return skuDetails;
	}

	public void setSkuDetails(SkuDetails skuDetails) {
		this.skuDetails = skuDetails;
	}
	

	public boolean isCloseActivityUserCancel() {
		return isCloseActivityUserCancel;
	}

	public void setCloseActivityUserCancel(boolean isCloseActivityUserCancel) {
		this.isCloseActivityUserCancel = isCloseActivityUserCancel;
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (googleOrderBean != null) {
			googleOrderBean.clear();
			googleOrderBean = null;
		}
		
		this._currentPurchaseSku = "";
	}
}
