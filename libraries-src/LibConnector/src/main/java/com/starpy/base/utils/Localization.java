package com.starpy.base.utils;

import android.app.Activity;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.cfg.ResConfig;

import java.util.Locale;

/**
 * Created by gan on 2017/4/10.
 */

public class Localization {

    public static void gameLanguage(Activity activity, SGameLanguage gameLanguage){
        if (gameLanguage == null){
            gameLanguage = SGameLanguage.zh_TW;
        }
        if (!gameLanguage.getLanguage().equals(ResConfig.getGameLanguage(activity))){//如果语言改变，从新更新服务条款
            StarPyUtil.saveSdkLoginTerms(activity,"");
        }
        ResConfig.saveGameLanguage(activity,gameLanguage.getLanguage());

        if (gameLanguage == SGameLanguage.zh_CH){

            ApkInfoUtil.updateConfigurationLocale(activity, Locale.SIMPLIFIED_CHINESE);//简体

        }else if(gameLanguage == SGameLanguage.en_US){

            ApkInfoUtil.updateConfigurationLocale(activity, Locale.US);//英文（美国）

        }else{

            ApkInfoUtil.updateConfigurationLocale(activity, Locale.TRADITIONAL_CHINESE);//繁体

        }
    }
}
