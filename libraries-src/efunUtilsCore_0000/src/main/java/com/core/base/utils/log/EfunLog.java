package com.core.base.utils.log;

import java.io.File;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class EfunLog {

    private static EfunLog instance;
    /**
     * 是否正在检查上传条件
     */
    private static volatile boolean mCheckUpload = false;

    /** 同步锁, 为保证所有操作都按照顺序执行，所有日志操作必须要上同步锁 */
    private static final Object mLock = new Object();

    private EfunLog(){

    }

    /**
     * 获取单例
     * @return EfunLog
     */
    public static EfunLog getInstance(){
        if(instance == null){
            instance = new EfunLog();
        }
        return instance;
    }



    /**
     * 获取类同步锁
     * @return 同步锁
     */
    public Object getmLock(){
        return mLock;
    }

    /**
     * 上传日志文件
     * @param cxt 上下文
     */
    public void uploadLog(@NonNull final Context cxt){
        synchronized (mLock){
            EfunLogFileUtil.uploadLog(cxt);
        }
    }

    /**
     * 检查是否符合自动上传的条件
     * @param cxt
     */
    private void checkUpload(final Context cxt){
        if(!mCheckUpload) {
            mCheckUpload = true;
            if (EfunLogFileUtil.checkIsUploading()) {
                return;
            }
            //检查文件大小
            String strLogFilePath = getLogFilePath(cxt);
            if (!TextUtils.isEmpty(strLogFilePath)) {
                File logFile = new File(strLogFilePath);
                if (logFile.exists()) {
                    if (logFile.length() > (EfunLogFileUtil.MIN_UPLOAD_SIZE)) {
                        EfunLogFileUtil.uploadLog(cxt);
                    }
                }
            }
            mCheckUpload = false;
        }
    }

    /**
     * 获取日志文件的路径
     * @param cxt 上下文
     * @return 日志文件的路径
     */
    public String getLogFilePath(final Context cxt){
        return EfunLogFileUtil.getLogFilePath(cxt);
    }
}
