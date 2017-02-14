/**
 * 
 */
package com.starpy.googlepay;

import java.util.List;
import java.util.Vector;

import com.starpy.base.cfg.SConfig;
import com.starpy.base.utils.SLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.bean.EfunPayError;
import com.starpy.googlepay.bean.EfunQueryInventoryState;
import com.starpy.googlepay.bean.EfunWalletBean;
import com.starpy.googlepay.bean.GoogleOrderBean;
import com.starpy.googlepay.bean.WebOrderBean;
import com.starpy.googlepay.callback.ISWalletListener;
import com.starpy.googlepay.callback.EfunWalletService;
import com.starpy.googlepay.callback.QueryItemListener;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.efuntask.EfunPayUtil;
import com.starpy.googlepay.efuntask.PayLaunchPurchaseDialog;
import com.starpy.googlepay.efuntask.PayPrompt;
import com.starpy.googlepay.util.EfunPayHelper;
import com.starpy.util.SkuDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * <p>Title: BasePayActivity</p>
 * <p>Description: 初始化基本的参数</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2014年2月24日
 */
public abstract class BasePayActivity extends Activity {

	public static final String TAG = "efun_googlepay";

	public static final String GOOGLE_PAY_VERSION = "v2 2.3";
	
	public static final String EFUN_GOOGLE_PAY_LIST_SKUS = "EFUN_GOOGLE_PAY_LIST_SKUS";
	
	protected PayPrompt payPrompt;

	/**
	 * _GoogleOrderBean Google储值购买订单参数封装
	 */
	protected volatile GoogleOrderBean _GoogleOrderBean = null;
	
	/**
	 * @return the _GoogleOrderBean
	 */
	public GoogleOrderBean get_GoogleOrderBean() {
		return _GoogleOrderBean;
	}


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
	private EfunQueryInventoryState queryInventoryState;
	
	
	protected String _gameCode;
	protected String _language;

	
	private SkuDetails skuDetails;
	
	/**
	 * payPreferredUrl 储值首选域名
	 */
	protected String payPreferredUrl; 
	/**
	 * paySpareUrl 储值备用域名
	 */
	protected String paySpareUrl; 
	
	/**
	 * 查询未消费商品完成后的回调
	 */
	protected QueryItemListener queryItemListener;
	
	/**
	 * activity頁面是否需要關閉
	 */
	private boolean isCloseActivity = false;
	
	/**
	 * 當前購買的商品id
	 */
	private String _currentPurchaseSku = "";
	
	/**
	 * 标识是否处于整个购买过程（从创建efun订单到回调完成）,若处于购买过程，按返回键不给销毁activity
	 */
	private boolean launching = false;
	/**
	 * 判断购买过程是否已经取消（从创建efun订单到启动Google支付这过程中，是否进行了取消，取消的话最后不启动Google支付）
	 */
	private boolean purchaseCancel = false;
	
	/**
	 * 判断是否已经完成Google付款操作,若完成付款，再次购买时候必须关闭activity再能进行购买
	 */
	private boolean googlePayOk = false;
	
	private PayLaunchPurchaseDialog launchPurchaseDialog;
	
	/**
	 * @return the isCloseActivity
	 */
	public boolean isCloseActivity() {
		return isCloseActivity;
	}

	/**
	 * @param isCloseActivity the isCloseActivity to set
	 */
	public void setCloseActivity(boolean isCloseActivity) {
		this.isCloseActivity = isCloseActivity;
	}

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
		
		payPrompt = new PayPrompt(this);
		launchPurchaseDialog = new PayLaunchPurchaseDialog(this);
		
		efunPayError = new EfunPayError(this);
		initEfunPayErrorMessage(efunPayError);
		
		_GoogleOrderBean = initGoogleOrderBean();
		
		initializeGoogleBean();
		initPay();
		
		walletListeners = EfunWalletService.getInstance().getWalletListeners();
		walletBean = new EfunWalletBean();
		
		queryInventoryState = new EfunQueryInventoryState();
		
	}

	private void initializeGoogleBean() {
		
		if (_GoogleOrderBean == null) {
			throw new RuntimeException("请先初始化OrderBean");
		}
		
		if (SStringUtil.isEmpty(_GoogleOrderBean.getGameCode())) {
			_gameCode = SConfig.getGameCode(this);
			if (SStringUtil.isEmpty(_gameCode)) {
				throw new RuntimeException("请先配置好gamecode");
			}
			_GoogleOrderBean.setGameCode(_gameCode);
		}else{
			_gameCode = _GoogleOrderBean.getGameCode();
		}
		
		if (SStringUtil.isEmpty(_GoogleOrderBean.getLanguage())) {
			_language = SConfig.getGameLanguage(this);
			_GoogleOrderBean.setLanguage(_language);
		}else{
			_language = _GoogleOrderBean.getLanguage();
		}
		
		if (SStringUtil.isEmpty(_GoogleOrderBean.getMoneyType())) {
			_GoogleOrderBean.setMoneyType(GooglePayContant.MONEY_TYPE);
		}
		if (SStringUtil.isEmpty(_GoogleOrderBean.getPayFrom())) {
			_GoogleOrderBean.setPayFrom(GooglePayContant.PAY_FROM);
		}
	}
	
	/**
	* <p>Title: startPurchase</p>
	* <p>Description: 开始购买商品（储值）</p>
	* @param sku 商品编号（标识）
	*/
	protected synchronized void startPurchase(String sku) {
		if (EfunGooglePayService.getIabHelper() == null || EfunGooglePayService.getIabHelper().isAsyncInProgress()) {
			determineCloseActivity();
			return;
		}
		if (EfunGooglePayService.getIabHelper() == null || !EfunGooglePayService.getIabHelper().isSupported()) {
			payPrompt.complainCloseAct("initialize Google play server error!");
			return;
		}
		
		if (googlePayOk) {
			payPrompt.complainCloseAct("Please close the page and then buy again");
			return;
		}
		if (!TextUtils.isEmpty(sku) && _GoogleOrderBean != null) {
			this.setPurchaseCancel(false);
			this._currentPurchaseSku = sku;
			_GoogleOrderBean.setSku(sku);
			EfunGooglePay.startGooglePurchase(this, sku);
		} else {
			payPrompt.complainCloseAct("Please close the page and then buy again");
		}
	}
	
	/**
	 * <p>Description: 直接購買，不需要彈出提示框</p>
	 * @param sku 商品id
	 * @date 2015年11月10日
	 */
	protected synchronized void startPurchaseWithoutDialog(final String sku) {
		if (TextUtils.isEmpty(sku)) {
			this.finish();
			return;
		}
		this.isCloseActivity = true;
		this._currentPurchaseSku  = sku;
		setQueryItemListener(new QueryItemListener() {
			
			@Override
			public void onQueryFinish() {
				startPurchase(sku);
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.setLaunching(false);
		this.googlePayOk = false;
		queryItemListener = null;
		SLogUtil.logD("_GoogleOrderBean:" + _GoogleOrderBean.toString());
		_GoogleOrderBean = null;
		if (launching && EfunGooglePayService.getIabHelper() != null) {
			EfunGooglePayService.getIabHelper().setAsyncInProgress(false);
		}
		SLogUtil.logD("google pay onDestroy");
		this.payPrompt.dismissProgressDialog();
		this.launchPurchaseDialog.dismissProgressDialog();
		EfunGooglePayService.setPayActivity(null);
		
	}
	
	
	@Override
	public void onBackPressed() {
		if (launching) {
			Toast.makeText(this, "wait a minute", Toast.LENGTH_SHORT).show();
		}else{
			super.onBackPressed();
		}
		
	}
	
	protected void handlerActivityResult(int requestCode, int resultCode,Intent data) {
		launchPurchaseDialog.dismissProgressDialog();
		
		if (EfunGooglePay.handleActivityResult(this,requestCode, resultCode, data)) {
			setQueryItemListener(null);
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			SLogUtil.logI("onActivityResult handled by IABUtil. the result was related to a purchase flow and was handled");
		} else {
			SLogUtil.logI("onActivityResult handled by IABUtil.the result was not related to a purchase");
			processPayCallBack();
			payPrompt.complainCloseAct(efunPayError.getEfunGoogleBuyFailError());
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
	
	/**
	 * @return the payPrompt
	 */
	public PayPrompt getPayPrompt() {
		return payPrompt;
	}

	/**
	 * @param payPrompt the payPrompt to set
	 */
	public void setPayPrompt(PayPrompt payPrompt) {
		this.payPrompt = payPrompt;
	}
	

/*	public GoogleOrderBean get_orderBean() {
		return _GoogleOrderBean;
	}

	public void set_orderBean(GoogleOrderBean _orderBean) {
		this._GoogleOrderBean = _orderBean;
	}*/

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

	public void showGoogleStoreErrorMessage() {
		payPrompt.complain(efunPayError.getEfunGoogleStoreError());
	}
	
	public void showGoogleServiceErrorMessage() {
		payPrompt.complain(efunPayError.getEfunGoogleServerError());
	}
	public void showGoogleBuyFailErrorMessage() {
		payPrompt.complain(efunPayError.getEfunGoogleBuyFailError());
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
		this.finish();
	}
	
	protected void startWebClient(Intent intent){
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startOtherWallet(this, webOrderBean, intent);
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
		this.finish();
	}
	
	
	/**
	 * <p>Description: 根据实际情况决定是否需要关闭activity（isCloseActivity为true关闭）</p>
	 * @date 2016年1月6日
	 */
	public void determineCloseActivity(){
		if (isCloseActivity()) {
			this.finish();
		}
	}
	
	/**
	 * <p>Description: 儲值回調</p>
	 * @param googleRespone
	 * @date 2016年1月5日
	 */
	public void processPayCallBack(int googleRespone) {
		try{
			if (null != walletListeners && !walletListeners.isEmpty() && null != walletBean) {
				SLogUtil.logI("walletListeners size:" + walletListeners.size());
				if (1122 != googleRespone) {
					walletBean.setGoogleResponeCode(googleRespone);
				}
				if(walletBean.getPurchaseState() != GooglePayContant.PURCHASESUCCESS){
					walletBean.setPurchaseState(GooglePayContant.PURCHASEFAILURE);
				}
				for (ISWalletListener walletListener : walletListeners) {
					if (walletListener != null) {
						walletListener.efunWallet(walletBean);
					}
				}
			} else {
				SLogUtil.logI("无回调");
			}
		}catch(Exception e){
			Log.d(BasePayActivity.TAG, e.getMessage() + "");
			e.printStackTrace();
		}finally{
			if (null != walletListeners) {
				walletListeners.clear();
			}
		}
	}
	
	public void processPayCallBack() {
		processPayCallBack(1122);
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

/*	public boolean isLaunching() {
		return launching;
	}*/

	public void setLaunching(boolean launching) {
		this.launching = launching;
	}

	public boolean isPurchaseCancel() {
		return purchaseCancel;
	}

	public void setPurchaseCancel(boolean purchaseCancel) {
		this.purchaseCancel = purchaseCancel;
	}

	public PayLaunchPurchaseDialog getLaunchPurchaseDialog() {
		return launchPurchaseDialog;
	}

	public void setLaunchPurchaseDialog(PayLaunchPurchaseDialog launchPurchaseDialog) {
		this.launchPurchaseDialog = launchPurchaseDialog;
	}

	public boolean isGooglePayOk() {
		return googlePayOk;
	}

	public void setGooglePayOk(boolean googlePayOk) {
		this.googlePayOk = googlePayOk;
	}
	
	
}
