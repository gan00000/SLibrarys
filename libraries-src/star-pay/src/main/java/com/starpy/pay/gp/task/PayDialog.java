package com.starpy.pay.gp.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.widget.Toast;

import com.starpy.base.utils.SLog;

public class PayDialog {

	private ProgressDialog progressDialog = null;
	private Activity mActivity;
	private boolean isCancel = false;
	private boolean isCloseActivity;


	/**
	 * @param isCloseActivity the isCloseActivity to set
	 */
	public void setCloseActivity(boolean isCloseActivity) {
		this.isCloseActivity = isCloseActivity;
	}

	public PayDialog(Activity activity) {
		this.mActivity = activity;
	
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
		setCancel(false);
		if (mActivity == null) {
			return;
		}
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(mActivity);
		}

		progressDialog.setMessage(message);
		progressDialog.setIndeterminate(true);

		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				SLog.logI("pay ProgressDialog Cancel");
				setCancel(true);
				EndFlag.setCanPurchase(true);
				EndFlag.setEndFlag(true);
				dialog.dismiss();
				if (isCloseActivity && mActivity != null) {
					mActivity.finish();
				}
			}
		});
		progressDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				SLog.logI("pay progressDialog onDismiss");
				EndFlag.setCanPurchase(true);
				EndFlag.setEndFlag(true);			
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
		}
	}

	/**
	* <p>Title: complain</p>
	* <p>Description: 弹出对话框</p>
	* @param message
	*/
	public void complain(String message) {
		
		if (isCloseActivity) {
			complainCloseAct(message);
		}else{
			alert("Error: " + message, null);
		}
	}
	
	/**
	 * <p>Title: complain</p>
	 * <p>Description: 弹出对话框,点击OK按钮后关闭当前activity</p>
	 * @param message
	 */
	public void complainCloseAct(final String message) {

		alert(message,  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismissProgressDialog();
				mActivity.finish();
			}
		});
	
	}
	
	/**
	* <p>Title: complain</p>
	* <p>Description: 弹出一个对话框</p>
	* @param message
	* @param listener 点击对话框后的回调
	*/
	public void complain(String message,DialogInterface.OnClickListener listener) {
		alert(message, listener);
	}

	private void alert(final String message,final DialogInterface.OnClickListener listener) {
		try{
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					AlertDialog.Builder bld = new AlertDialog.Builder(mActivity);
					bld.setMessage(message);
					bld.setCancelable(false);
					bld.setNeutralButton("OK", listener);
					bld.create().show();
				}
			});
		}catch(Exception e){
			e.printStackTrace();
			SLog.logI("On progressDialog " + e.getStackTrace());
		}
	}

	public void toastMessage(String message){
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
}