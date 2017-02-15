package com.core.base.utils.log;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;

import com.core.base.beans.EfunLogEntity;
import com.core.base.http.HttpRequest;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.FileUtil;
import com.core.base.utils.PL;
import com.core.base.utils.PermissionUtil;
import com.core.base.utils.ResUtil;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.StorageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 文件日志，记录日志信息并且上传到服务器
 * Created by Efun on 2016/10/25.
 */

public class EfunLogFileUtil {

    /** 保存到储存卡上的文件名 */
    private static final String FILE_NAME_LOG = "efun_log.dat";
    /** 最少512b就上传 */
    public static final long MIN_UPLOAD_SIZE = 3 * 1024L;
    /** 移动数据上传最大1Mb */
    public static final long MAX_MOBILE_UPLOAD_SIZE = 1024 * 1024L;
    /** wifi环境上传最大3Mb */
    private static final long MAX_WIFI_UPLOAD_SIZE = 3 * 1024 * 1024L;
    /** 上传日志文件的首选地址 */
    private static final String KEY_LOG_UPLOAD_PREFERRED_URL = "efunLogUploadPreferredUrl";
    /** 上传日志文件的备用地址 */
    private static final String KEY_LOG_UPLOAD_SPARE_URL = "efunLogUploadSpareUrl";
    /** 上传到服务器的日志文件的文件名 */
    private static final String FILE_NAME_UPLOAD_LOG = "android_log.txt";
    /** 是否正在上传日志文件 */
    private static volatile boolean mIsUploadingLogFile = false;

    /**
     * 是否正在上传
     * @return 是否正在上传
     */
    public static synchronized boolean checkIsUploading(){
        return mIsUploadingLogFile;
    }

    /**
     * 写Log
     * @param cxt 上下文
     * @param logEntity 日志实体
     */
    protected static void writeLog(final Context cxt, EfunLogEntity logEntity){
        String logFilePath = getLogFilePath(cxt);
        if(!TextUtils.isEmpty(logFilePath)){
            FileUtil.writeFile(logFilePath, logEntity.toString(), true);
        }
    }

    /**
     * 获取日志文件的路径
     * @param cxt 上下文
     * @return 日志文件的路径
     */
    protected static String getLogFilePath(final Context cxt){
        String logFilePath = null;
        if(null != cxt && StorageUtil.isExternalStorageExist() && PermissionUtil.hasSelfPermission(cxt, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File cachePath = cxt.getExternalCacheDir();
            if(cachePath != null) {
                logFilePath = cachePath.getAbsolutePath() + "/" + FILE_NAME_LOG;
            }
        }
        return logFilePath;
    }

    /**
     * 上传日志文件
     * @param cxt 上下文
     */
    protected static void uploadLog(final Context cxt){
        //没有网络时候就退出
        if(!ApkInfoUtil.isNetworkAvaiable(cxt)){
            return;
        }
        boolean uploadThreadStarted = false;
        mIsUploadingLogFile = true;
        //首先检查文件大小
        String strLogFilePath = getLogFilePath(cxt);
        if(!TextUtils.isEmpty(strLogFilePath)) {
            File logFile = new File(strLogFilePath);
            if(logFile.exists() && EfunUploadLogAsync.checkThreadInstance()){
                if(logFile.length() > MIN_UPLOAD_SIZE && logFile.length() < MAX_MOBILE_UPLOAD_SIZE){
                    new EfunUploadLogAsync(cxt, logFile).start();
                } else if(logFile.length() > MAX_MOBILE_UPLOAD_SIZE && logFile.length() < MAX_WIFI_UPLOAD_SIZE){
                    //文件略大，只有在wifi的网络环境下才可以上传
                    if(ApkInfoUtil.isWifiAvailable(cxt)){
                        new EfunUploadLogAsync(cxt, logFile).start();
                        uploadThreadStarted = true;
                    }
                } else if(logFile.length() > MAX_WIFI_UPLOAD_SIZE){
                    //文件太大，只能删掉了
                    logFile.delete();
                }
            }
        }
        if(!uploadThreadStarted){
            mIsUploadingLogFile = false;
        }
    }

    /**
     * 内部类形式，保证不会随便被其他类调用
     */
    private static class EfunUploadLogAsync extends Thread {
        private static String TAG = EfunUploadLogAsync.class.getSimpleName();

        private WeakReference<Context> mContext = null;

        private final File mLogFile;

        private static EfunUploadLogAsync mInstance;

        EfunUploadLogAsync(final Context ctx, final File file){
            mContext = new WeakReference<>(ctx.getApplicationContext());
            mLogFile = file;
            mInstance = this;
        }

        @Override
        public void run() {
            if(mContext.get() == null){
                PL.d(TAG, "上传失败，上下文已经被销毁！！");
                return;
            }
            PL.i(TAG, "开始上传文件");
            String preferredUploadUrl = ResUtil.findStringByName(mContext.get(), KEY_LOG_UPLOAD_PREFERRED_URL);
            String spareUploadUrl = ResUtil.findStringByName(mContext.get(), KEY_LOG_UPLOAD_SPARE_URL);
            if(SStringUtil.isEmpty(preferredUploadUrl) && SStringUtil.isEmpty(preferredUploadUrl)){
                PL.d(TAG, "上传失败，没有找到上传地址，销毁日志文件！！");
                mLogFile.delete();
                return;
            }

            String response = doRequest(preferredUploadUrl, spareUploadUrl);
            if(!TextUtils.isEmpty(response)){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int responseCode = jsonObject.getInt("code");
                    String responseMsg = jsonObject.getString("message");
                    if(responseCode == 1000){
                        PL.d(TAG, "上传日志成功！！！");
                        mLogFile.delete();
                    } else {
                        PL.i("上传日志失败！！" + (responseMsg == null ? "" : responseMsg));
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                    PL.i("上传日志失败！！json解析失败。。。");
                }
            } else {
                PL.i("上传日志失败！！服务端请求失败。。。");
            }
            mIsUploadingLogFile = false;
            mInstance = null;
        }

        /**
         * 上传文件
         * @param preferredUrl 首选地址
         * @param sparedUrl 备用地址
         * @return 服务端返回消息
         */
        String doRequest(String preferredUrl, String sparedUrl) {
            String response = "";
            if(SStringUtil.isNotEmpty(preferredUrl)) {
                PL.d("upload log preferredUrl:" + preferredUrl);
                response = HttpRequest.uploadFile(null, mLogFile, FILE_NAME_UPLOAD_LOG, preferredUrl);
                PL.d("upload log preferredUrl response: " + (response == null ? "null" : response));
            }
            if(SStringUtil.isEmpty(response) && SStringUtil.isNotEmpty(sparedUrl)) {
                PL.d("upload log spareUrl Url: " + sparedUrl);
                response = HttpRequest.uploadFile(null, mLogFile, FILE_NAME_UPLOAD_LOG, sparedUrl);
                PL.d("upload log spareUrl response: " + (response == null ? "null" : response));
            }
            return response;
        }

        /**
         * 检查是否已经存在线程实例
         * @return true--没有实例
         */
        static boolean checkThreadInstance(){
            return mInstance == null;
        }
    }
}
