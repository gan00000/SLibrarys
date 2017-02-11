package com.core.base.utils.log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.core.base.beans.EfunLogEntity;
import com.core.base.res.SConfig;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.EfunLogUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
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
     * 记录日志
     * @param event 事件
     * @param remark 备注
     */
    public void log(@NonNull final Context context, final String event, final String remark){
        synchronized (mLock) {
            Map<String, String> info = getInfoMap(context);
            EfunLogEntity logEntity = new EfunLogEntity(event, info, remark);
            EfunLogFileUtil.writeLog(context, logEntity);
            checkUpload(context);
        }
    }

    /**
     * 记录日志
     * @param event 事件
     * @param infoMap 信息
     * @param remark 备注
     */
    public void log(@NonNull final Context context, final String event, final Map<String, String> infoMap, final String remark){
        synchronized (mLock) {
            Map<String, String> info = getInfoMap(context);
            if(infoMap != null && infoMap.size() > 0){
                info.putAll(infoMap);
            }
            EfunLogEntity logEntity = new EfunLogEntity(event, info, remark);
            EfunLogFileUtil.writeLog(context, logEntity);
            checkUpload(context);
        }
    }

    /**
     * 记录日志
     * @param event 事件
     * @param infoMap 消息
     */
    public void log(@NonNull final Context context, final String event, final Map<String, String> infoMap){
        synchronized (mLock) {
            Map<String, String> info = getInfoMap(context);
            if(infoMap != null && infoMap.size() > 0){
                info.putAll(infoMap);
            }
            EfunLogEntity logEntity = new EfunLogEntity(event, info, null);
            EfunLogFileUtil.writeLog(context, logEntity);
            checkUpload(context);
        }
    }

    /**
     * 记录日志
     * @param event 事件
     */
    public void log(@NonNull final Context context, final String event){
        synchronized (mLock) {
            Map<String, String> info = getInfoMap(context);
            EfunLogEntity logEntity = new EfunLogEntity(event, info, null);
            EfunLogFileUtil.writeLog(context, logEntity);
            checkUpload(context);
        }
    }



    /**
     * 获取必要的信息，比如版本号，gameCode，包名
     * @return  info
     */
    private Map<String, String> getInfoMap(final Context context){
        Map<String, String> info = new HashMap<>();
        PackageInfo packageInfo = ApkInfoUtil.getPackageInfo(context);
        info.put("systemVersion", ApkInfoUtil.getOsVersion());
        try{
            info.put("userId", SConfig.getCurrentEfunUserId(context));
        } catch (Exception | Error  ex ){
            EfunLogUtil.logD(ex.getMessage());
        }
        if(packageInfo != null) {
            info.put("versionCode", packageInfo.versionCode + "");
            info.put("packageName", packageInfo.packageName);
        }

        if(null != context) {
            String gameCode = SConfig.getGameCode(context);
            if (!TextUtils.isEmpty(gameCode)) {
                info.put("gameCode", gameCode);
            }
            String efunUUid = ApkInfoUtil.getCustomizedUniqueId(context);
            if (!TextUtils.isEmpty(efunUUid)) {
                info.put("efunUUid", efunUUid);
            }
        }
        return info;
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
