package com.starpy.googlepay.efuntask;

import com.starpy.base.utils.EfunLogUtil;
import com.starpy.googlepay.BasePayActivity;
import com.starpy.googlepay.EfunGooglePayService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.widget.Toast;

public class PayPrompt {

	private ProgressDialog progressDialog = null;
	private BasePayActivity bpActivity;
	private static final String TAG = "efun_PayPrompt"; 
	
	private boolean cancelable = true;
	
	
	public PayPrompt(BasePayActivity bpActivity) {
		this.bpActivity = bpActivity;
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
		//setCancel(false);
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(bpActivity);
		}
		progressDialog.setMessage(message);
		progressDialog.setTitle("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(cancelable);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.d(BasePayActivity.TAG, "efun pay ProgressDialog Cancel");
				dialog.dismiss();
				cancelable = true;
			}
		});
		progressDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Log.d(BasePayActivity.TAG, "progressDialog onDismiss");
				cancelable = true;
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
			progressDialog = new ProgressDialog(bpActivity);
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
	
	public synchronized void dismissProgressDialogWithFinishActivity(int when) {
			
			try {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					if (bpActivity != null) {
						this.bpActivity.finish();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				progressDialog = null;
			}
		}
	

	/**
	* <p>Title: complain</p>
	* <p>Description: 弹出对话框,根据dismissCloseActivity来确定是否需要关闭activity</p>
	* @param message
	*/
	public void complain(String message) {
		
		if (EfunGooglePayService.getPayActivity() != null && EfunGooglePayService.getPayActivity().isCloseActivity()) {
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
	public void complainCloseAct(String message) {
		alert(message,  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismissProgressDialog();
				if (bpActivity != null) {
					bpActivity.finish();
				}
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
			bpActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					AlertDialog.Builder bld = new AlertDialog.Builder(bpActivity);
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
		Toast.makeText(bpActivity, message, Toast.LENGTH_SHORT).show();
	}


	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}
}
