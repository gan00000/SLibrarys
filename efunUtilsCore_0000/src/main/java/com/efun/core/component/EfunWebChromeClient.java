package com.efun.core.component;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.efun.core.js.UploadHandler;

public class EfunWebChromeClient extends WebChromeClient {

	private static final String TAG = "EfunWebChromeClient";
	Activity activity;
	UploadHandler handler;
	public EfunWebChromeClient(Activity activity) {
		this.activity = activity;
		handler = new UploadHandler(activity);
	}

	
	public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
	//	handler.openFileChooser(filePathCallback, fileChooserParams.getAcceptTypes()[0], "");
		Log.d(TAG, "onShowFileChooser");
		handler.onShowFileChooser(filePathCallback,fileChooserParams);
		return true;
	}
	
	
//	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
	public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture){
		Log.d(TAG, "openFileChooser");
		handler.openFileChooser(uploadFile, acceptType, capture);
	}

	// Android 2.x
	public void openFileChooser(ValueCallback<Uri> uploadFile){
		Log.d(TAG, "openFileChooser  openFileChooser(ValueCallback<Uri> uploadFile)");
		handler.openFileChooser(uploadFile, null, null);
	}
	

    // Android 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
    	Log.d(TAG, "openFileChooser ValueCallback<Uri> uploadMsg, String acceptType");
    }

    
	public void onActivityResult(int requestCode,int resultCode, Intent intent){
		handler.onResult(requestCode,resultCode, intent);
	}
	
	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		return super.onJsAlert(view, url, message, result);
		/*if (!TextUtils.isEmpty(message)) {
			Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		result.confirm();
		return true;*/
	}
	
	 @Override
     public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
      
         String message = "Console: " + consoleMessage.message() + " "
                 + consoleMessage.sourceId() +  ":"
                 + consoleMessage.lineNumber();

         switch (consoleMessage.messageLevel()) {
             case TIP:
                 //Log.v(TAG, message);
                 break;
             case LOG:
                // Log.i(TAG, message);
                 break;
             case WARNING:
                 Log.w(TAG, message);
                 break;
             case ERROR:
                 Log.e(TAG, message);
                 break;
             case DEBUG:
                 //Log.d(TAG, message);
                 break;
         }

         return true;
     }
	 
	 
}
