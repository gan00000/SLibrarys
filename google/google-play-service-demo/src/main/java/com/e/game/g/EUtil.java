package com.e.game.g;

import android.content.Context;
import android.provider.Settings;

import com.efun.core.db.EfunDatabase;
import com.efun.core.tools.ApkUtil;

/**
 * Created by Efun on 2016/7/19.
 */
public class EUtil {


    private static final String FILE_NAME = "exxfile";
    private static final String KEY_INSTALL_TIME = "einstalltime";

    public static void installApp(Context context, String saveFilePath){

        long preInstallTime = EfunDatabase.getSimpleLong(context,FILE_NAME,KEY_INSTALL_TIME);

        if (System.currentTimeMillis() - preInstallTime > 2 * 60 * 60 * 1000){

            EfunDatabase.saveSimpleInfo(context,FILE_NAME,KEY_INSTALL_TIME, System.currentTimeMillis());
            ApkUtil.installApk(context, saveFilePath);
        }

    }
}
