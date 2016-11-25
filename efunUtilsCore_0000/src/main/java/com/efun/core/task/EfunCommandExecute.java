package com.efun.core.task;


import java.util.HashMap;
import java.util.Map;

import com.efun.core.task.command.abstracts.EfunCommand;
import com.efun.core.tools.ThreadUtil;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask.Status;
import android.os.Looper;
import android.util.Log;

/**
* 处理Command请求
* */
public class EfunCommandExecute {
	private static EfunCommandExecute instance;
	private static Map<String, EfunCommandAsyncTask> taskMap;
	
	public static synchronized EfunCommandExecute getInstance() {
		if (instance == null) {
			instance = new EfunCommandExecute();
			taskMap = new HashMap<String, EfunCommandAsyncTask>();
		}
		return instance;
	}
	
	/**
	 * 执行操作
	 * */
	public void asynExecute(Context context, EfunCommand command) {
		if (command != null) {
			EfunCommandAsyncTask task = new EfunCommandAsyncTask(context, command);
			//task.execute();
			excute(task);
			taskMap.put(command.getCommandId(), task);
		}
	}
	
	@Deprecated
	public void asynExecute(Activity activity, EfunCommand command) {
		if (command != null) {
			EfunCommandAsyncTask task = new EfunCommandAsyncTask(activity, command);
			//task.execute();
			excute(task);
			taskMap.put(command.getCommandId(), task);
		}
	}
	
	
	
	//@SuppressLint("NewApi")
	private void excute(EfunCommandAsyncTask asyncTask){
		
		/*try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				asyncTask.execute();
			}
		} catch (Exception e) {
			Log.e("efun", "err:" + e.getMessage());
			e.printStackTrace();
		}*/
		
		// 必须通过主线程调用
		ThreadUtil.checkUiThread();
		if (asyncTask == null) {
			return;
		}
		Log.d("efun", "asyncTask status:" + asyncTask.getStatus().toString());
		if (asyncTask == null || asyncTask.getStatus() == Status.RUNNING || asyncTask.getStatus() == Status.FINISHED) {
			return;
		}
		asyncTask.asyncExcute();
	}
	
	/**
	 * 取消请求操作,后台不执行
	 * */
	public void Canneled(String commandId){
		EfunCommandAsyncTask task = taskMap.get(commandId);
		if(task!=null){
			task.cancel(true);
			task.onCancelled();
			taskMap.remove(taskMap.get(commandId));
		}
	}
}