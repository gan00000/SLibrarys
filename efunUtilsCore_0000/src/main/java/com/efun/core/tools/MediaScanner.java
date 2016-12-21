package com.efun.core.tools;

import java.io.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

/**
 * 通知系统有文件更新
 * @author Efun
 *
 */
public class MediaScanner {

	 private static final String TAG = "efun";

	    private MediaScannerConnection mConn = null;
	    private SannerClient mClient = null;
	    private File mFile = null;
	    private String mMimeType = null;  //文件类型

	    public MediaScanner(Context context) {
	        if (mClient == null) {
	            mClient = new SannerClient();
	        }
	        if (mConn == null) {
	            mConn = new MediaScannerConnection(context, mClient);
	        }
	    }

	    class SannerClient implements
	            MediaScannerConnection.MediaScannerConnectionClient {

	        public void onMediaScannerConnected() {
	        	Log.d(TAG, "onMediaScannerConnected");
	            if (mFile == null) {
	                return;
	            }
	            scan(mFile, mMimeType);
	        }

	        public void onScanCompleted(String path, Uri uri) {
	        	Log.d(TAG, "onScanCompleted");
	            mConn.disconnect();
	        }

	        private void scan(File file, String type) {
	            Log.i(TAG, "scan " + file.getAbsolutePath());
	            if (file.isFile()) {
	                mConn.scanFile(file.getAbsolutePath(), null);
	                return;
	            }
	            File[] files = file.listFiles();
	            if (files == null) {
	                return;
	            }
	            for (File f : file.listFiles()) {
	                scan(f, type);
	            }
	        }
	    }

	    public void scanFile(File file, String mimeType) {
	        mFile = file;
	        mMimeType = mimeType;
	        mConn.connect();
	    }
}
