package com.starpy.googlepay;

import java.util.ArrayList;
import java.util.List;

import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.googlepay.bean.WebOrderBean;
import com.starpy.googlepay.constants.EfunDomainSite;
import com.starpy.googlepay.efuntask.EfunPayUtil;
import com.starpy.googlepay.efuntask.PayPrompt;
import com.starpy.googlepay.util.EfunPayHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public abstract class BaseGoogleWebActivity extends BasePayActivity {
	
	public final static String EFUNANDROIDOBJECT = "EFUNANDROIDOBJECT";
	
	private EfunAndroidJS efunJS;
	private WebView webView;
	
	private WebOrderBean webOrderBean;
	
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
	
	PayPrompt prompt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		prompt = this.payPrompt;
		
		if (!ApkInfoUtil.isNetworkAvaiable(this)) {
			prompt.complainCloseAct("Network is not avaiable");
			return;
		}
	/*	if(!IabHelper.isSupported()){
			prompt.complainCloseAct("initialize Google play server error!");
			return;
		}*/
		prompt.dismissProgressDialog();
		prompt.showProgressDialog("Loading");
		
		efunJS = new EfunAndroidJS();
		startShowWebview();
		EfunGooglePay.setUpGooglePayByBasePayActivity(BaseGoogleWebActivity.this);
	
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EfunLogUtil.logI("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		handlerActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (null != this.prompt) {
			prompt.dismissProgressDialog();
		}
		
		try {
			// webView处理
			if (null != webView) {
				this.webView.clearCache(true);
				this.webView.clearHistory();
				this.deleteDatabase("webview.db");
				this.deleteDatabase("webviewCache.db");
				this.webView.destroy();
				Log.d(BasePayActivity.TAG, "webView destory");
				// webView.freeMemory();
				this.webView = null;
			}
		} catch (Exception e) {
			Log.d(BasePayActivity.TAG, e.getMessage() + "");
			e.printStackTrace();
		}
			// 回調
		/*	if (null != walletListeners && !walletListeners.isEmpty()) {
				EfunLogUtil.logI("walletListeners size:" + walletListeners.size());
				if (walletBean != null && walletBean.getPurchaseState() != GooglePayContant.PURCHASESUCCESS) {
					walletBean.setPurchaseState(GooglePayContant.PURCHASEFAILURE);
				}
				for (ISWalletListener walletListener : walletListeners) {
					if (walletListener != null) {
						walletListener.efunWallet(walletBean);
					}
				}
			} else {
				EfunLogUtil.logI("不回调");
			}*/
			processPayCallBack();
	}
	
	@Override
	protected List<String> initSku() {
		// TODO Auto-generated method stub
		return null;
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
	    EfunLogUtil.logD("设置webView");
	    
	    webView.setWebChromeClient(new WebChromeClient());
	    webOrderBean = initWebOrderBean();
	    urlParams = EfunPayUtil.buildGoogleGoodsUrl(this, webOrderBean);
	    EfunLogUtil.logD("urlParams:" + urlParams);
	    efunDomainPreferredUrl = EfunPayHelper.getPreferredUrl(BaseGoogleWebActivity.this);
	    efunDomainSpareUrl = EfunPayHelper.getSpareUrl(BaseGoogleWebActivity.this);
		
	    if (TextUtils.isEmpty(efunDomainSite)) {
	    	mPreferredUrl = efunDomainPreferredUrl + EfunDomainSite.EFUN_GOODSLIS_TO_PAYLIST + urlParams;
		}else{
			mPreferredUrl = efunDomainPreferredUrl + efunDomainSite + urlParams;
		}
	    
		EfunLogUtil.logD("preferredUrl: " + mPreferredUrl);
	    
	    webView.setWebViewClient(new WebViewClient() {
			
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				EfunLogUtil.logD("payurl:" + url);
				
				if (url.toLowerCase().startsWith("http") || url.toLowerCase().startsWith("https") || url.toLowerCase().startsWith("file")) {
					view.loadUrl(url);
				} else {
					try {
						EfunLogUtil.logI("url:" + url);
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					} catch (Exception e) {
						EfunLogUtil.logD("Webview shouldOverrideUrlLoading start activity error:" + e.getMessage());
					}
				}
				return true;
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				EfunLogUtil.logD("onPageStarted");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				EfunLogUtil.logD("onPageFinished");
				prompt.dismissProgressDialog();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				EfunLogUtil.logD("onReceivedError  errorCode:" + errorCode);
				EfunLogUtil.logD("failingUrl");
				//如果首选域名访问超时，则转向备用域名
				if ((WebViewClient.ERROR_TIMEOUT == errorCode || WebViewClient.ERROR_CONNECT == errorCode) && failingUrl.contains(BaseGoogleWebActivity.this.efunDomainPreferredUrl)) {
					if(TextUtils.isEmpty(efunDomainSite)){
						mSpareUrl = efunDomainSpareUrl + EfunDomainSite.EFUN_GOODSLIS_TO_PAYLIST + urlParams;
					}else{
						mPreferredUrl = efunDomainPreferredUrl + efunDomainSite + urlParams;
					}
					EfunLogUtil.logD("spareUrl: " + mSpareUrl);
					if (SStringUtil.isNotEmpty(BaseGoogleWebActivity.this.mSpareUrl)) {
						//备用域名访问
						view.loadUrl(BaseGoogleWebActivity.this.mSpareUrl);
					}else {
						view.loadData("Page not find ,server timeout, Please try again later", "text/html", "utf-8");
						prompt.dismissProgressDialog();
					}
				} else {
					view.loadData("Page not find ,server timeout, Please try again later", "text/html", "utf-8");
					prompt.dismissProgressDialog();
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
			EfunLogUtil.logD("google sku from js:" + sku);
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
			if (SStringUtil.isNotEmpty(skus_string)) {
				skus_string = skus_string.trim();
				EfunLogUtil.logD("skus from html:" + skus_string);
				String[] mSkus = skus_string.split(",");
				for (int i = 0; i < mSkus.length; i++) {
					if (SStringUtil.isNotEmpty(mSkus[i])) {
						EfunLogUtil.logD("sku id:" + mSkus[i]);
						list.add(mSkus[i]);
					}
				}
				//BaseGoogleWebActivity.this.set_skus(list);
			}else{
				EfunLogUtil.logD("skus is empty");
			}
		/*	BaseGoogleWebActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					prompt.showProgressDialog();
					//启动Google 内购服务
					EfunGooglePay.setUpGooglePayByBasePayActivity(BaseGoogleWebActivity.this);
				}
			});*/
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
