package com.efun.googlepay.efuntask;

import com.efun.core.tools.EfunLogUtil;
import com.efun.googlepay.BasePayActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.widget.Toast;

public class Prompt {

	private ProgressDialog progressDialog = null;
	private Activity mActivity;
	private boolean isCancel = false;
	private boolean isCloseActivity;
	
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
		setCancel(false);
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
				Log.d("efun pay", "progressDialog onDismiss");
				EndFlag.setCanPurchase(true);
				EndFlag.setEndFlag(true);			
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
			EfunLogUtil.logI("On progressDialog " + e.getStackTrace());
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
