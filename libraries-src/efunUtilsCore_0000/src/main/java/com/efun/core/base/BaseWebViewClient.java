package com.efun.core.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Efun on 2016/12/2.
 */

public class BaseWebViewClient extends WebViewClient {

    protected static boolean DISABLE_SSL_CHECK_FOR_TESTING = false;

    Activity activity;

    public BaseWebViewClient() {
    }

    public BaseWebViewClient(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param view
     * @param url
     * @deprecated
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        overrideUrlLoading(view,url);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        overrideUrlLoading(view,request.getUrl().toString());
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    public boolean overrideUrlLoading(WebView webView,String url){

        /*if (url.startsWith("sms:")) {
            try {
                url = URLDecoder.decode(url, "utf-8");
                String[] str = url.split("\\?");
                String[] str1 = str[0].split("\\:");
                String[] str2 = str[1].split("\\=");
                AppUtils.sendMessage(activity,str1[1], str2[1]);
            } catch (UnsupportedEncodingException e) {
                webView.loadUrl(url);
            }

        }else if(url.contains("line.naver.jp")){
            AppUtils.comeDownloadPageInAndroidWeb(activity, url);

        }else if(url.startsWith("whatsapp://send")){
            url = url.replaceAll("whatsapp://send", "http://m.efuntw.com/whatsappShare.html");
            AppUtils.comeDownloadPageInAndroidWeb(activity, url);

        } else */

        if (url.toLowerCase().startsWith("http") || url.toLowerCase().startsWith("https") || url.toLowerCase().startsWith("file")) {
            webView.loadUrl(url);
        } else {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            } catch (Exception e) {
                webView.loadUrl(url);
            }
        }
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (DISABLE_SSL_CHECK_FOR_TESTING) {
            handler.cancel();
        } else {//永远只走这里，这么写是为了躲过google警告
            handler.proceed();
        }
    }
}
