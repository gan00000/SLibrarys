package com.starpy.googlepay;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.utils.SLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.bean.WebPayReqBean;
import com.starpy.googlepay.callback.ISWalletListener;
import com.starpy.googlepay.constants.EfunDomainSite;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.efuntask.PayUtil;
import com.starpy.googlepay.efuntask.EndFlag;
import com.starpy.googlepay.util.EfunPayHelper;

public abstract class BaseGoogleWebActivity extends BasePayActivity {
	
	public final static String EFUNANDROIDOBJECT = "EFUNANDROIDOBJECT";
	
	private EfunAndroidJS efunJS;
	private WebView webView;
	
	private WebPayReqBean webOrderBean;
	
	/**
	 * efunDomainPreferredUrl 最终决定的首选域名
	 */
	private String efunDomainPreferredUrl;

	/**
	 * efunDomainSpareUrl 最终决定的备用域名
	 */
	private String efunDomainSpareUrl;

	/**
	 * mPreferredUrl webview加载的首选url
	 */
	private String mPreferredUrl;

	/**
	 * mSpareUrl webview加载的备用url
	 */
	private String mSpareUrl;
	
	private String efunDomainSite;

	/**
	 * urlParams 域名后拼接的参数
	 */
	private String urlParams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		if (!ApkInfoUtil.isNetworkAvaiable(BaseGoogleWebActivity.this)) {
			prompt.complainCloseAct("Network is not avaiable");
			return;
		}
		
		prompt.dismissProgressDialog();
		prompt.showProgressDialog("Loading");
		
	/*	prompt.showProgressDialog(false, new OnKeyListener() {
			
			@Override
			public boolean onKey(final DialogInterface dialog, int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
					Log.d("efun", "onKey");
					AlertDialog.Builder ab = new Builder(BaseGoogleWebActivity.this);
					ab.setTitle("Tips");
					ab.setMessage("exit the page");
					ab.setPositiveButton("confirm", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							paramDialogInterface.dismiss();
							prompt.dismissProgressDialog();
							BaseGoogleWebActivity.this.finish();
						}
					});
					ab.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {

						}
					});
					ab.create().show();

				}
				return true;
			}
		});*/
		efunJS = new EfunAndroidJS();
		startShowWebview();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SLogUtil.logI("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		handlerActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// very important:
		if (mHelper != null){
			SLogUtil.logI("mHelper.dispose");
			mHelper.dispose();
		}
		mHelper = null;
		EndFlag.setCanPurchase(true);
		EndFlag.setEndFlag(true);

		if (null != this.prompt) {
			prompt.dismissProgressDialog();
			//prompt = null;
		}
		if (null != webView) {
			try {
				this.webView.clearCache(true);
				this.webView.clearHistory();
				this.deleteDatabase("webview.db");
				this.deleteDatabase("webviewCache.db");
				webView.destroy();
				//webView.freeMemory();
				webView = null;
				Log.d("efun","webView destory");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			if (null != walletListeners && !walletListeners.isEmpty()) {
				SLogUtil.logI("walletListeners size:" + walletListeners.size());
				if (walletBean != null && walletBean.getPurchaseState() != GooglePayContant.PURCHASESUCCESS) {
					walletBean.setPurchaseState(GooglePayContant.PURCHASEFAILURE);
				}
				for (ISWalletListener walletListener : walletListeners) {
					if (walletListener != null) {
						walletListener.efunWallet(walletBean);
					}
				}
			} else {
				SLogUtil.logI("不回调");
			}
		} catch (Exception e) {
			Log.i("efun", e.getMessage() + "");
			e.printStackTrace();
		} finally {
			if (null != walletListeners) {
				walletListeners.clear();
			}
		}
	}
	
	@Override
	protected List<String> initSku() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void handlerActivityResult(int requestCode, int resultCode,Intent data) {
		prompt.dismissProgressDialog();
		if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			SLogUtil.logI("onActivityResult handled by IABUtil. the result was related to a purchase flow and was handled");
		} else {
			SLogUtil.logI("onActivityResult handled by IABUtil.the result was not related to a purchase");
			EndFlag.setEndFlag(true);
			prompt.complainCloseAct(efunPayError.getGoogleBuyFailError());
		}
	}
	
	

	/**
	* <p>Title: startShowWebview</p>
	* <p>Description: 加载网页商品列表</p>
	*/
	public void startShowWebview() {
		
		webView = new WebView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		linearLayout.addView(webView, layoutParams);
		setContentView(linearLayout);
		
		WebSettings settings = webView.getSettings(); 
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true); 
	    settings.setLoadWithOverviewMode(true); 
	    settings.setBuiltInZoomControls(true);
	    settings.setSupportZoom(true);
	    webView.addJavascriptInterface(efunJS, EFUNANDROIDOBJECT);//JS对象
	    SLogUtil.logD("设置webView");
	    
	    webView.setWebChromeClient(new WebChromeClient());
	    webOrderBean = initWebOrderBean();
	    urlParams = PayUtil.buildGoogleGoodsUrl(this, webOrderBean);
	    SLogUtil.logD("urlParams:" + urlParams);
	    efunDomainPreferredUrl = EfunPayHelper.getPreferredUrl(BaseGoogleWebActivity.this);
	    efunDomainSpareUrl = EfunPayHelper.getSpareUrl(BaseGoogleWebActivity.this);
		
	    if (TextUtils.isEmpty(efunDomainSite)) {
	    	mPreferredUrl = efunDomainPreferredUrl + EfunDomainSite.EFUN_GOODSLIS_TO_PAYLIST + urlParams;
		}else{
			mPreferredUrl = efunDomainPreferredUrl + efunDomainSite + urlParams;
		}
	    
		SLogUtil.logD("preferredUrl: " + mPreferredUrl);
	    
	    webView.setWebViewClient(new WebViewClient() {
			
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				SLogUtil.logD("shouldOverrideUrlLoading:initEfunGoogleSkus()" + url);
				view.loadUrl(url);
				return true;
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				SLogUtil.logD("onPageStarted");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				SLogUtil.logD("onPageFinished url:" + url);
				/*if (googlePay || url.contains("google_efun.html")) {
				} else if(prompt != null){
					prompt.dismissProgressDialog();
				}*/
				if(prompt != null){
					prompt.dismissProgressDialog();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.d("efunLog", "onReceivedError  errorCode:" + errorCode);
				SLogUtil.logD("failingUrl:" + failingUrl);
				//如果首选域名访问超时，则转向备用域名
				if ((WebViewClient.ERROR_TIMEOUT == errorCode || WebViewClient.ERROR_CONNECT == errorCode) && failingUrl.contains(BaseGoogleWebActivity.this.efunDomainPreferredUrl)) {
					if(TextUtils.isEmpty(efunDomainSite)){
						mSpareUrl = efunDomainSpareUrl + EfunDomainSite.EFUN_GOODSLIS_TO_PAYLIST + urlParams;
					}else{
						mPreferredUrl = efunDomainPreferredUrl + efunDomainSite + urlParams;
					}
					SLogUtil.logD("spareUrl: " + mSpareUrl);
					if (SStringUtil.isNotEmpty(BaseGoogleWebActivity.this.mSpareUrl)) {
						//备用域名访问
						view.loadUrl(BaseGoogleWebActivity.this.mSpareUrl);
					}else {
						view.loadData("Page not find ,server timeout, Please try again later", "text/html", "utf-8");
						if (prompt != null) {
							prompt.dismissProgressDialog();
						}
					}
				} else {
					view.loadData("Page not find ,server timeout, Please try again later", "text/html", "utf-8");
					if (prompt != null) {
						prompt.dismissProgressDialog();
					}
				}
			}

		});
//		this.webView.loadUrl(BaseGoogleWebActivity.this.mPreferredUrl);
		loadWebView(this.webView, BaseGoogleWebActivity.this.mPreferredUrl);
	}
	
	//可以重新自定义webview和加载url
	protected void loadWebView(WebView webView,String url) {
		webView.loadUrl(url);
	}

	public String getEfunDomainSite() {
		return efunDomainSite;
	}


	public void setEfunDomainSite(String efunDomainSite) {
		this.efunDomainSite = efunDomainSite;
	}
	/**
	 * @return the webView
	 */
	public WebView getWebView() {
		return webView;
	}
	//此类要为public
	public final class EfunAndroidJS{
		
		/**
		* <p>Title: startGooglePay</p>
		* <p>Description: 通过js启动商品购买流程</p>
		* @param sku 商品编号
		*/
		//如果应用设置argetSdkVersion 17 或者更高需要加上@JavascriptInterface
		@JavascriptInterface   
		public void startGooglePay(String sku){
			SLogUtil.logD("google sku from js:" + sku);
			final String mSku = sku;
			BaseGoogleWebActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					BaseGoogleWebActivity.this.startPurchase(mSku);
				}
			});
		}
		
		/**
		* <p>Title: finishActivity</p>
		* <p>Description: 通过js关闭当前avtivity</p>
		*/
		@JavascriptInterface
		public void finishActivity() {
			BaseGoogleWebActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					BaseGoogleWebActivity.this.finish();
				}
			});
		}
		
		/**
		* <p>Title: initGoogleSkus</p>
		* <p>Description: 网页js返回商品所有的sku,并且链接Google play远程服务，启动查询流程</p>
		* @param skus_string
		*/
		@JavascriptInterface
		public void initGoogleSkus(String skus_string){
			
			List<String> list = new ArrayList<String>();
			//googlePay = true;
			if (SStringUtil.isNotEmpty(skus_string)) {
				skus_string = skus_string.trim();
				SLogUtil.logD("skus from html:" + skus_string);
				String[] mSkus = skus_string.split(",");
				for (int i = 0; i < mSkus.length; i++) {
					if (SStringUtil.isNotEmpty(mSkus[i])) {
						SLogUtil.logD("sku id:" + mSkus[i]);
						list.add(mSkus[i]);
					}
				}
				BaseGoogleWebActivity.this.set_skus(list);
			}else{
				SLogUtil.logD("skus is empty");
			}
			BaseGoogleWebActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (mHelper == null || prompt == null) {
						Log.d("efun","mHelper == null || prompt == null,may be activity is destory");
						return;
					}
					prompt.showProgressDialog();
					BaseGoogleWebActivity.this.efunHelperSetUp();
				}
			});
		}
		
		/**
		* <p>Title: cardDataToJS</p>
		* <p>Description: 传递月卡数据到网页内</p>
		* @return 返回月卡数据到网页内
		*/
		@JavascriptInterface
		public String cardDataToJS(){
			
			return cardData();
		}
	}
	

}
