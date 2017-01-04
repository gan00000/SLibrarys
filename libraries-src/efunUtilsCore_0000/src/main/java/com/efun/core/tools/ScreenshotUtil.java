package com.efun.core.tools;

import java.io.File;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

public class ScreenshotUtil {

	/**
	 * 把当前activity转换为bitmap === 截图
	 * @param act  当前需要截图的Acitivity
	 * @param view 当前需要截图的view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(Activity act, View view) {

		View mView = null;
		if (view != null) {
			mView = view;
		}else{
			mView = act.getWindow().getDecorView();
		}
		
		Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
				Bitmap.Config.RGB_565);
		// 利用bitmap生成画布
		Canvas canvas = new Canvas(bitmap);
		// 把view中的内容绘制在画布上
		if (mView != null) {
			mView.draw(canvas);	
		}
		return bitmap;
	}
	
	/**
	 * 保存图片到相册
	 * @param context  上下文
	 * @param bitmap   需要保存的bitmap
	 */
	public static void insertImage(Context context, Bitmap bitmap){
		if (bitmap == null) {
			return;
		}
		if ( !PermissionUtil.hasSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			return ;
		}
		
		String path = MediaStore.Images.Media.insertImage(context.getApplicationContext().getContentResolver(), bitmap, "", "");
		Log.e("efun", "insertImage==path:" + path);
		MediaScanner mediaScanner = new MediaScanner(context.getApplicationContext());
		mediaScanner.scanFile(new File(path), "image/jpeg");
	}
}
