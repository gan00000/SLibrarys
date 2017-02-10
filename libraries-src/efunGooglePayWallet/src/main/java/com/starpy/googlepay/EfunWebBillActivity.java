package com.starpy.googlepay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.starpy.base.res.EfunResConfiguration;
import com.starpy.base.utils.EfunLogUtil;
import com.starpy.base.utils.SStringUtil;
import com.starpy.googlepay.constants.EfunDomainSite;
import com.starpy.googlepay.constants.GooglePayContant;
import com.starpy.googlepay.efuntask.Prompt;

public abstract class EfunWebBillActivity extends Activity {

	private WebView _efunWebView = null;
	protected Prompt mPrompt;
	/**
	 * efunWebPayReferredUrl 最终加载的网页首选地址
	 */
	protected String efunWebPayReferredUrl;
	/**
	 * efunWebPaySpareUrl 最终加载的网页备用地址
	 */
	protected String efunWebPaySpareUrl;
	/**
	 * efunWebPayParams 储值域名后拼接的参数
	 */
	private String efunWebPayParams;
	
	/**
	 * payPreferredUrl 储值首选域名
	 */
	private String payPreferredUrl = "";
	/**
	 * paySpareUrl 储值备用
	 */
	private String paySpareUrl = "";
	
	
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

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPay();
	    efunWebPayParams =  getIntent().getStringExtra(GooglePayContant.ExtraGWKey);
		if (SStringUtil.isEmpty(payPreferredUrl)) {
			payPreferredUrl = EfunResConfiguration.getEfunPayPreferredUrl(this);
		}
		if (SStringUtil.isEmpty(paySpareUrl)) {
			paySpareUrl = EfunResConfiguration.getEfunPaySpareUrl(this);
		}
		
		if (SStringUtil.isNotEmpty(efunWebPayParams)) {
			//官网储值
			if (SStringUtil.isNotEmpty(payPreferredUrl)) {
				efunWebPayReferredUrl = payPreferredUrl + EfunDomainSite.EFUN_MORE_GW_PAY + efunWebPayParams;
			}
			if (SStringUtil.isNotEmpty(paySpareUrl)) {
				efunWebPaySpareUrl = paySpareUrl + EfunDomainSite.EFUN_MORE_GW_PAY + efunWebPayParams;
			}
		} else {
			//更多储值
			efunWebPayParams = getIntent().getStringExtra(GooglePayContant.ExtraOtherKey);
			if (SStringUtil.isNotEmpty(efunWebPayParams)) {
				if (SStringUtil.isNotEmpty(payPreferredUrl)) {
					efunWebPayReferredUrl = payPreferredUrl + EfunDomainSite.EFUN_MORE_OTHER_PAY + efunWebPayParams;
				}
				if (SStringUtil.isNotEmpty(paySpareUrl)) {
					efunWebPaySpareUrl = paySpareUrl + EfunDomainSite.EFUN_MORE_OTHER_PAY + efunWebPayParams;
				}
			}
		}
		EfunLogUtil.logD("efunWebPayReferredUrl:" + efunWebPayReferredUrl);
	    EfunLogUtil.logD("efunWebPaySpareUrl:" + efunWebPaySpareUrl);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		loadWebViewContent();
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		loadWebViewContent();
	}

	/**
	* <p>Title: loadWebView</p>
	* <p>Description: 開始加載網頁</p>
	*/
	public void loadWebViewContent() {
		
				_efunWebView = initEfunWebView();
				if ( null == _efunWebView) {
					EfunLogUtil.logE("efunWebView is null");
					return;
				}
				 LayoutParams params = _efunWebView.getLayoutParams();
				 if (params != null) {
					params.height = LayoutParams.FILL_PARENT;
					params.width = LayoutParams.FILL_PARENT;
				}
				if (SStringUtil.isEmpty(efunWebPayReferredUrl) && SStringUtil.isEmpty(efunWebPaySpareUrl)) {
					EfunLogUtil.logE("efunUrl is empty");
					return;
				}
				WebSettings settings = _efunWebView.getSettings(); 
				settings.setJavaScriptEnabled(true);
//				settings.setUseWideViewPort(true);
				_efunWebView.addJavascriptInterface(new EfunWebAndroidJS(), BaseGoogleWebActivity.EFUNANDROIDOBJECT);//JS对象
			    settings.setLoadWithOverviewMode(true); 
			    settings.setBuiltInZoomControls(true);
			    settings.setSupportZoom(true);
			    _efunWebView.setWebChromeClient(new WebChromeClient());
				mPrompt = new Prompt(this);
				
				mPrompt.showProgressDialog(false,new OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							dialog.dismiss();
							EfunWebBillActivity.this.finish();
						}
						return true;
					}
				});
				
				_efunWebView.setWebViewClient(new WebViewClient() {
		
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						EfunLogUtil.logD("shouldOverrideUrlLoading:" + url);
						view.loadUrl(url);
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
						EfunLogUtil.logD("onPageFinished url:" + url);
						mPrompt.dismissProgressDialog();
					}
		
					@Override
					public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
						super.onReceivedError(view, errorCode, description, failingUrl);
						Log.d("efunLog", "onReceivedError  errorCode:" + errorCode);
						EfunLogUtil.logD("failingUrl:" + failingUrl);
						//如果首选域名访问超时，则转向备用域名
						if ((WebViewClient.ERROR_TIMEOUT == errorCode || WebViewClient.ERROR_CONNECT == errorCode) && failingUrl.contains(EfunWebBillActivity.this.payPreferredUrl) && SStringUtil.isNotEmpty(EfunWebBillActivity.this.efunWebPaySpareUrl)) {
							view.loadUrl(EfunWebBillActivity.this.efunWebPaySpareUrl);
						} else {
							view.loadData("Page not find ,server timeout, Please try again later", "text/html", "utf-8");
							mPrompt.dismissProgressDialog();
						}
					}
		
				});
				
				//_efunWebView.loadUrl(efunWebPayReferredUrl);
				loadWebView(_efunWebView, efunWebPayReferredUrl);
				
	}

	
	protected abstract WebView initEfunWebView();
	protected void initPay(){}
	protected void loadWebView(WebView webView,String url) {
		webView.loadUrl(url);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mPrompt) {
			mPrompt.dismissProgressDialog();
		}
		if (null != _efunWebView) {
			try {
				this._efunWebView.clearCache(true);
				this._efunWebView.clearHistory();
				this.deleteDatabase("webview.db");
				this.deleteDatabase("webviewCache.db");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	//此类要为public
	public final class EfunWebAndroidJS{
			
		/**
		* <p>Title: finishActivity</p>
		* <p>Description: 通过js关闭当前avtivity</p>
		*/
		@JavascriptInterface
		public void finishActivity() {
			EfunWebBillActivity.this.finish();
		}
	}
}
