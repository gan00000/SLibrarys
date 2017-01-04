package com.efun.googlepay.efuntask;

import com.efun.googlepay.BasePayActivity;
import com.efun.googlepay.EfunGooglePayService;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;

public class PayLaunchPurchaseDialog {

	private ProgressDialog progressDialog = null;
	private BasePayActivity bpActivity;

	public PayLaunchPurchaseDialog(BasePayActivity bpActivity) {
		this.bpActivity = bpActivity;
	}

	/**
	* <p>Title: showProgressDialog</p>
	* <p>Description: 显示一个进度条，message为 "Loading..."</p>
	*/
	public void showProgressDialog() {
		showProgressDialog("Loading...");
	}
	

	/**
	* <p>Title: showProgressDialog</p>
	* <p>Description: 显示一个进度条</p>
	* @param message
	*/
	public void showProgressDialog(String message) {
		dismissProgressDialog();
		//setCancel(false);
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(bpActivity);
		}
		progressDialog.setMessage(message);
		progressDialog.setTitle("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.d(BasePayActivity.TAG, "efun pay ProgressDialog Cancel");
				bpActivity.setPurchaseCancel(true);
				bpActivity.setLaunching(false);
				if (EfunGooglePayService.getIabHelper() != null) {
					EfunGooglePayService.getIabHelper().setAsyncInProgress(false);
				}
				dialog.dismiss();
				bpActivity.determineCloseActivity();
			}
		});
		progressDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Log.d(BasePayActivity.TAG, "progressDialog onDismiss");
				
			}
		});
		progressDialog.show();
	}
	

	/**
	* <p>Title: dismissProgressDialog</p>
	* <p>Description: 进度条消失</p>
	*/
	public synchronized void dismissProgressDialog() {
		
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			progressDialog = null;
		}
	}
	

}
