/**
 * 
 */
package com.starpy.googlepay;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.SLogUtil;
import com.starpy.googlepay.bean.EfunPayError;
import com.starpy.googlepay.bean.EfunWalletBean;
import com.starpy.googlepay.bean.GooglePayReqBean;
import com.starpy.googlepay.bean.QueryInventoryState;
import com.starpy.googlepay.callback.EfunWalletService;
import com.starpy.googlepay.callback.ISWalletListener;
import com.starpy.googlepay.callback.QueryItemListener;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.task.EndFlag;
import com.starpy.googlepay.task.Prompt;
import com.starpy.googlepay.task.PurchaseFlow;
import com.starpy.googlepay.task.QueryInventoryFinished;
import com.starpy.googlepay.util.PayHelper;
import com.starpy.googlepay.util.IabHelper;
import com.starpy.googlepay.util.IabResult;
import com.starpy.googlepay.util.SkuDetails;

import java.util.Vector;

/**
 * <p>Title: BasePayActivity</p>
 * <p>Description: 初始化基本的参数</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2014年2月24日
 */
public abstract class BasePayActivity extends Activity {
	
	public static final String GOOGLE_PAY_VERSION = "3.9.2";

	public static final String GOOGLE_PAY_VERSION_CHAGE_LOG = GOOGLE_PAY_VERSION + ":修复log日志打印msg为null的时候崩溃";

	// The helper object
	protected IabHelper mHelper;
	protected Prompt prompt;
	/**
	 * googleOrderBean Google储值购买订单参数封装
	 */
	protected volatile GooglePayReqBean googleOrderBean = null;

	/**
	 * walletBean 储值页面关闭回调结果封装
	 */
	protected EfunWalletBean walletBean;
	/**
	 * walletListeners 储值页面关闭回调对象
	 */
	protected Vector<ISWalletListener> walletListeners;
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
		
		prompt = new Prompt(this);

		if (googleOrderBean !=null) {
			googleOrderBean.clear();
			googleOrderBean = null;
		}
		googleOrderBean = initGoogleOrderBean();
		
		initializeGoogleBean();

		walletListeners = EfunWalletService.getInstance().getWalletListeners();
		walletBean = new EfunWalletBean();
		
		mHelper = new IabHelper(this);
		queryInventoryState = new QueryInventoryState();
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);
		mHelper.enableDebugLogging(true);
		

		prompt.setCloseActivity(isCloseActivityUserCancel);//设置弹框关闭activity
	}

	private void initializeGoogleBean() {
		
		if (googleOrderBean == null) {
			SLogUtil.logE("googleOrderBean is null");
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
		prompt.setCloseActivity(isCloseActivityUserCancel);
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
		SLogUtil.logI("EndFlag: " + EndFlag.isEndFlag());
		this._currentPurchaseSku = sku;
		if (EndFlag.isEndFlag()) {
			EndFlag.setEndFlag(false);
			if (EndFlag.isCanPurchase()) {
				/*if (!PermissionUtil.requestPermissions_PHONE_STORAGE(this, 11)) {
					return;
				}*/
				PurchaseFlow.startPurchase(this,_currentPurchaseSku);
			} else {
				prompt.complainCloseAct("Please close the page and then buy again");
			}
		}
	}
	
	/**
	* <p>Title: googlePaySetUp</p>
	* <p>Description: 启动远程服务</p>
	*/
	protected void googlePaySetUp(){
		prompt.showProgressDialog();
		if (null != mHelper && !mHelper.ismSetupDone()) {
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result) {
					SLogUtil.logI("startSetup onIabSetupFinished.");
					if (!result.isSuccess()) {
						prompt.dismissProgressDialog();
						supportGooglePlay = false;
						showGoogleStoreErrorMessage();
						return;
					}
					mHelper.queryInventoryAsync(new QueryInventoryFinished(BasePayActivity.this));
					
				}

				@Override
				public void onError(String message) {//发生错误时提示
					SLogUtil.logI("message:" + message);
					supportGooglePlay = false;
					prompt.dismissProgressDialog();
					prompt.complainCloseAct(message);
				}
			});
			
		}else{
			prompt.dismissProgressDialog();
			if (isCloseActivityUserCancel) {
				this.finish();
			}
		}

	}
	
	protected abstract GooglePayReqBean initGoogleOrderBean();
	protected void initEfunPayErrorMessage(EfunPayError efunPayError){}


	public IabHelper getHelper() {
		return mHelper;
	}

	public void setHelper(IabHelper mHelper) {
		this.mHelper = mHelper;
	}

	public Prompt getPrompt() {
		return prompt;
	}

	public void setPrompt(Prompt prompt) {
		this.prompt = prompt;
	}


	public GooglePayReqBean getGoogleOrderBean() {
		return googleOrderBean;
	}


	public EfunWalletBean getWalletBean() {
		return walletBean;
	}

	public void setWalletBean(EfunWalletBean walletBean) {
		this.walletBean = walletBean;
	}

	public Vector<ISWalletListener> getWalletListeners() {
		return walletListeners;
	}

	public void setWalletListeners(Vector<ISWalletListener> walletListeners) {
		this.walletListeners = walletListeners;
	}

	public boolean isSupportGooglePlay() {
		return supportGooglePlay;
	}

	public void setSupportGooglePlay(boolean supportGooglePlay) {
		this.supportGooglePlay = supportGooglePlay;
	}


	public void showGoogleStoreErrorMessage() {
		if (isCloseActivityUserCancel) {
			prompt.complainCloseAct(efunPayError.getGoogleStoreError());
		}else{
			prompt.complain(efunPayError.getGoogleStoreError());
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
