/**
 * 
 */
package com.efun.googlepay;

import java.util.List;
import java.util.Vector;

import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.core.tools.PermissionUtil;
import com.efun.googlepay.bean.EfunPayError;
import com.efun.googlepay.bean.EfunQueryInventoryState;
import com.efun.googlepay.bean.EfunWalletBean;
import com.efun.googlepay.bean.GoogleOrderBean;
import com.efun.googlepay.bean.WebOrderBean;
import com.efun.googlepay.callback.EfunWalletListener;
import com.efun.googlepay.callback.EfunWalletService;
import com.efun.googlepay.callback.QueryItemListener;
import com.efun.googlepay.constants.GooglePayContant;
import com.efun.googlepay.efuntask.EfunPayUtil;
import com.efun.googlepay.efuntask.EndFlag;
import com.efun.googlepay.efuntask.Prompt;
import com.efun.googlepay.efuntask.PurchaseFlow;
import com.efun.googlepay.efuntask.QueryInventoryFinished;
import com.efun.googlepay.util.EfunPayHelper;
import com.efun.util.IabHelper;
import com.efun.util.IabResult;
import com.efun.util.SkuDetails;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

/**
 * <p>Title: BasePayActivity</p>
 * <p>Description: 初始化基本的参数</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2014年2月24日
 */
public abstract class BasePayActivity extends Activity /*implements ActivityCompat.OnRequestPermissionsResultCallback*/{
	
	public static final String GOOGLE_PAY_VERSION = "3.9.2";

	public static final String GOOGLE_PAY_VERSION_CHAGE_LOG = GOOGLE_PAY_VERSION + ":修复log日志打印msg为null的时候崩溃";
	
	// The helper object
	protected IabHelper mHelper;
	protected Prompt prompt;
	/**
	 * _GoogleOrderBean Google储值购买订单参数封装
	 */
	protected volatile GoogleOrderBean _GoogleOrderBean = null;
	
	/**
	 * walletBean 储值页面关闭回调结果封装
	 */
	protected EfunWalletBean walletBean;
	/**
	 * walletListeners 储值页面关闭回调对象
	 */
	protected Vector<EfunWalletListener> walletListeners;
	/**
	 * efunPayError 错误提示语封装
	 */
	protected EfunPayError efunPayError;
	/**
	 * queryInventoryState 查询状态封装
	 */
	private EfunQueryInventoryState queryInventoryState;
	
	
	protected boolean supportGooglePlay = true;
	protected String _gameCode;
	protected String _language;
	
	/**
	 * _skus 商品id
	 */
	protected List<String> _skus;
	
	protected String _currentPurchaseSku;
	
	private SkuDetails skuDetails;
	
	/**
	 * openGW 判断本次是否打开了更多储值，或者官网
	 */
	protected boolean openGW = false;
	/**
	 * googlePay 用于网页版Google储值，判断是否进入当前的页面是否属于Google储值商品界面，主要用于是否需要把进度条关掉
	 */
	//protected boolean googlePay =  false;
	
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
		EfunPayHelper.logCurrentVersion();
		efunPayError = new EfunPayError(this);
		initEfunPayErrorMessage(efunPayError);
		
		prompt = new Prompt(this);
		prompt.showProgressDialog();
		
		if (_GoogleOrderBean!=null) {
			_GoogleOrderBean.clear();
			_GoogleOrderBean = null;
		}
		_GoogleOrderBean = initGoogleOrderBean();
		
		initializeGoogleBean();
		initPay();
		
		walletListeners = EfunWalletService.getInstance().getWalletListeners();
		walletBean = new EfunWalletBean();
		
		mHelper = new IabHelper(this);
		queryInventoryState = new EfunQueryInventoryState();
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);
		mHelper.enableDebugLogging(false);
		
		_skus = initSku();
		
		prompt.setCloseActivity(isCloseActivityUserCancel);//设置弹框关闭activity
	}

	private void initializeGoogleBean() {
		
		if (_GoogleOrderBean == null) {
			throw new RuntimeException("请先初始化OrderBean");
		}
		
		if (EfunStringUtil.isEmpty(_GoogleOrderBean.getGameCode())) {
			_gameCode = EfunResConfiguration.getGameCode(this);
			if (EfunStringUtil.isEmpty(_gameCode)) {
				throw new RuntimeException("请先配置好gamecode");
			}
			_GoogleOrderBean.setGameCode(_gameCode);
		}else{
			_gameCode = _GoogleOrderBean.getGameCode();
		}
		
		if (EfunStringUtil.isEmpty(_GoogleOrderBean.getLanguage())) {
			_language = EfunResConfiguration.getLanguage(this);
			_GoogleOrderBean.setLanguage(_language);
		}else{
			_language = _GoogleOrderBean.getLanguage();
		}
		
		if (EfunStringUtil.isEmpty(_GoogleOrderBean.getMoneyType())) {
			_GoogleOrderBean.setMoneyType(GooglePayContant.MONEY_TYPE);
		}
		if (EfunStringUtil.isEmpty(_GoogleOrderBean.getPayFrom())) {
			_GoogleOrderBean.setPayFrom(GooglePayContant.PAY_FROM);
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
		EfunLogUtil.logI("EndFlag: " + EndFlag.isEndFlag());
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
	* <p>Title: efunHelperSetUp</p>
	* <p>Description: 启动远程服务</p>
	*/
	protected void efunHelperSetUp(){
		if (null != mHelper && !mHelper.ismSetupDone()) {
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result) {
					EfunLogUtil.logI("startSetup onIabSetupFinished.");
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
					EfunLogUtil.logI("message:" + message);
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
	
	protected abstract List<String> initSku();
	protected abstract GoogleOrderBean initGoogleOrderBean();
	protected abstract WebOrderBean initWebOrderBean();
	protected void initEfunPayErrorMessage(EfunPayError efunPayError){}
	protected void initPay(){}
	
	protected String cardData(){
		return "";
	}
	
	
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


	public GoogleOrderBean get_orderBean() {
		return _GoogleOrderBean;
	}

	public void set_orderBean(GoogleOrderBean _orderBean) {
		this._GoogleOrderBean = _orderBean;
	}

	public EfunWalletBean getWalletBean() {
		return walletBean;
	}

	public void setWalletBean(EfunWalletBean walletBean) {
		this.walletBean = walletBean;
	}

	public Vector<EfunWalletListener> getWalletListeners() {
		return walletListeners;
	}

	public void setWalletListeners(Vector<EfunWalletListener> walletListeners) {
		this.walletListeners = walletListeners;
	}

	public boolean isSupportGooglePlay() {
		return supportGooglePlay;
	}

	public void setSupportGooglePlay(boolean supportGooglePlay) {
		this.supportGooglePlay = supportGooglePlay;
	}

	public List<String> get_skus() {
		return _skus;
	}

	public void set_skus(List<String> _skus) {
		this._skus = _skus;
	}

	public void showGoogleStoreErrorMessage() {
		if (isCloseActivityUserCancel) {
			prompt.complainCloseAct(efunPayError.getEfunGoogleStoreError());
		}else{
			prompt.complain(efunPayError.getEfunGoogleStoreError());
		}
	}
	
	public void showGoogleServiceErrorMessage() {
		if (isCloseActivityUserCancel) {
			prompt.complainCloseAct(efunPayError.getEfunGoogleServerError());
		}else{
			prompt.complain(efunPayError.getEfunGoogleServerError());
		}
	}
	public void showGoogleBuyFailErrorMessage() {
		if (isCloseActivityUserCancel) {
			prompt.complainCloseAct(efunPayError.getEfunGoogleBuyFailError());
		}else{
			prompt.complain(efunPayError.getEfunGoogleBuyFailError());
		}
	}

	public EfunPayError getEfunPayError() {
		return efunPayError;
	}

	public void setEfunPayError(EfunPayError efunPayError) {
		this.efunPayError = efunPayError;
	}

	public EfunQueryInventoryState getQueryInventoryState() {
		return queryInventoryState;
	}

	public void setQueryInventoryState(EfunQueryInventoryState queryInventoryState) {
		this.queryInventoryState = queryInventoryState;
	}
	
	protected void startWebClient(String intentAction){
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startOtherWallet(this, webOrderBean, intentAction);
		openGW = true;
		this.finish();
	}
	
	protected void startWebClient(Intent intent){
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startOtherWallet(this, webOrderBean, intent);
		openGW = true;
		this.finish();
	}
	
	protected void startWebGW(String intentAction){
		/*Intent GWPayIntent = new Intent(action);
		startGW(GWPayIntent);*/
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startGWWallet(this, webOrderBean, intentAction);
		openGW = true;
		this.finish();
	}
	
	protected void startWebGW(Intent intent){
		/*Intent GWPayIntent = new Intent(action);
		startGW(GWPayIntent);*/
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startGWWallet(this, webOrderBean, intent);
		openGW = true;
		this.finish();
	}

	public boolean isOpenGW() {
		return openGW;
	}

	public void setOpenGW(boolean openGW) {
		this.openGW = openGW;
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
	
/*	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		if (requestCode == 11) {
			PurchaseFlow.startPurchase(this,_currentPurchaseSku);
		}else{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}*/

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
		
		if (_GoogleOrderBean != null) {
			_GoogleOrderBean.clear();
			_GoogleOrderBean = null;
		}
		
		this._currentPurchaseSku = "";
	}
}
