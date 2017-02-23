package com.starpy.pay.gp.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;

public class Prompt {

	private ProgressDialog progressDialog = null;
	private Activity mActivity;

	public Prompt(Activity activity) {
		this.mActivity = activity;
	
	}
	
	/**
	* <p>Title: showProgressDialog</p>
	* <p>Description: 显示一个进度条，message为 "Loading..."</p>
	*/
	public void showProgressDialog() {
		showProgressDialog("Loading...");
	}
	
	public void showProgressDialog(boolean cancel,OnKeyListener onKeyListener) {
		showProgressDialog("Loading...",cancel,onKeyListener);
	}

	/**
	* <p>Title: showProgressDialog</p>
	* <p>Description: 显示一个进度条</p>
	* @param message
	*/
	public void showProgressDialog(String message) {
		dismissProgressDialog();
		if (mActivity == null) {
			Log.d("efun","prompt activity == null");
			return;
		}
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(mActivity);
		}
		
		progressDialog.setMessage(message);
		progressDialog.setTitle("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.d("efun pay", "efun pay ProgressDialog Cancel");
				dialog.dismiss();
				if (mActivity != null) {
					mActivity.finish();
				}
			}
		});
		progressDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Log.d("efun pay", "progressDialog onDismiss");
			}
		});
		progressDialog.show();
	}
	
	/**
	 * <p>Title: showProgressDialog</p>
	 * <p>Description: 显示一个进度条</p>
	 * @param message
	 * @param onKeyListener 
	 */
	public void showProgressDialog(String message, boolean cancle, OnKeyListener onKeyListener) {
		dismissProgressDialog();
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(mActivity);
		}
		progressDialog.setMessage(message);
		progressDialog.setTitle("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(cancle);
		progressDialog.setOnKeyListener(onKeyListener);
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
