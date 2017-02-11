package com.core.base.request;

import com.core.base.res.SConfig;
import com.core.base.request.command.abstracts.EfunCommand;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.ThreadUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.util.Log;

public class SCommandAsyncTask extends SRequestAsyncTask {
	
	private EfunCommand command;
	private ProgressDialog progress;
	private Context context;
	private boolean canceled = false;
	
	public SCommandAsyncTask(Context context, EfunCommand _command) {
		this.command = _command;
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.i("SCommandAsyncTask", "onPreExecute");
		//必须通过主线程调用
		ThreadUtil.checkUiThread(context);
		SConfig.getSDKLoginSign(context);
		if (command.isShowProgress()) {//判断是否要显示进度条
			if (!TextUtils.isEmpty(command.getCommandMsg())) {
				if (context != null) {
					progress = new ProgressDialog(context);
					progress.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							Log.d("efun", "dismiss");
						}
					});
					progress.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							Log.d("efun", "onCancel");
							command.setHasCancel(true);
							canceled = true;
							SCommandAsyncTask.this.cancel(true);
							dialog.dismiss();
							if (command != null && command.getCancelListener() != null) {
								command.getCancelListener().onCancel(dialog);
							}
						}
					});
					progress.setMessage(command.getCommandMsg());
					progress.setCanceledOnTouchOutside(command.getCanceledOnTouchOutside());
					progress.setCancelable(command.getCancelable());
					try {
						progress.show();
					} catch (Exception e) {
						progress = null;
						e.printStackTrace();
					}
				}
			}
		}
	}  
	
	@Override
	protected String doInBackground(String... params) {
		Log.i("SCommandAsyncTask", "doInBackground");
		try {
			ApkInfoUtil.getCustomizedUniqueId(this.context);
			if (canceled) {
				return "";
			}
			command.execute();			
		} catch (Exception e) {
			e.printStackTrace();
			if (!command.isShowProgress()) {
				return null;
			}
			if(context!=null/*&&!context.isFinishing()*/){
				if(progress!=null&&progress.isShowing()){
					try {
						progress.dismiss();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					progress=null;
				}
			}else{
				try{
					if (progress != null) {
						progress.dismiss();
						progress = null;
					}
				}catch(Exception ex){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i("SCommandAsyncTask", "onPostExecute");
		if (command.isHasCancel()) {
			return;
		}
		if (canceled) {
			return;
		}
		if(command.getCallback()!=null){
			command.getCallback().cmdCallBack(command);
			
			if (!command.isShowProgress()) {
				return;
			}
			
			if(context!=null/*&&!context.isFinishing()*/){
				if(progress!=null&&progress.isShowing()){
					try {
						progress.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
					progress=null;
				}
			}else{
				try{
					if (progress != null) {
						progress.dismiss();
						progress = null;
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		command.getCallback().cmdCallBack(command);
	}

}