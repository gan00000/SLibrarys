package com.starpy.base.utils;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.utils.SPUtil;

import org.json.JSONObject;

/**
 * Created by Efun on 2017/2/7.
 */

public class StarPyUtil {

    public static final String STAR_PY_SP_FILE = "star_py_sp_file.xml";
    public static final String STARPY_SDK_LOGIN_TERMS_FILE = "starpy_sdk_login_terms_file.xml";

    public static final String ADS_ADVERTISERS_NAME = "ADS_ADVERTISERS_NAME";

    public static final String EFUN_SDK_LANGUAGE = "EFUN_SDK_LANGUAGE";//SDK设置的语言

    public static final String EFUN_LOGIN_SIGN = "EFUN_LOGIN_SIGN";//SDK保存的登陆返回sign
    public static final String EFUN_LOGIN_TIMESTAMP = "EFUN_LOGIN_TIMESTAMP";//SDK保存的登陆返回timestamp

    public static final String STARPY_LOGIN_USERNAME = "STARPY_LOGIN_USERNAME";//保存用户的账号
    public static final String STARPY_LOGIN_PASSWORD = "STARPY_LOGIN_PASSWORD";//保存用户的密码
    public static final String STARPY_LOGIN_USER_ID = "STARPY_LOGIN_USER_ID";//保存用户的用戶id
    public static final String STARPY_LOGIN_SERVER_RETURN_DATA = "STARPY_LOGIN_SERVER_RETURN_DATA";//保存服务端返回的数据

    public static final String STARPY_SDK_CFG = "STARPY_SDK_CFG";//保存sdk配置
    public static final String STARPY_SDK_LOGIN_TERMS = "STARPY_SDK_LOGIN_TERMS";


    public static void saveSdkCfg(Context context,String cfg){
        SPUtil.saveSimpleInfo(context,STAR_PY_SP_FILE,STARPY_SDK_CFG,cfg);
    }

    public static String getSdkCfg(Context context){
        return SPUtil.getSimpleString(context,STAR_PY_SP_FILE,STARPY_SDK_CFG);
    }

    public static void saveAccount(Context context,String account){
        SPUtil.saveSimpleInfo(context,STAR_PY_SP_FILE, STARPY_LOGIN_USERNAME, account);
    }

    public static String getAccount(Context context){
        return SPUtil.getSimpleString(context,STAR_PY_SP_FILE, STARPY_LOGIN_USERNAME);
    }

    public static void savePassword(Context context,String password){
        SPUtil.saveSimpleInfo(context,STAR_PY_SP_FILE, STARPY_LOGIN_PASSWORD, password);
    }

    public static String getPassword(Context context){
        return SPUtil.getSimpleString(context,STAR_PY_SP_FILE, STARPY_LOGIN_PASSWORD);
    }

    public static void saveUid(Context context,String uid){
        SPUtil.saveSimpleInfo(context,STAR_PY_SP_FILE, STARPY_LOGIN_USER_ID, uid);
    }

    public static String getUid(Context context){
        return SPUtil.getSimpleString(context,STAR_PY_SP_FILE, STARPY_LOGIN_USER_ID);
    }


    public static void saveSdkLoginData(Context context,String data){
        SPUtil.saveSimpleInfo(context,STAR_PY_SP_FILE,STARPY_LOGIN_SERVER_RETURN_DATA,data);
    }

    public static String getSdkLoginData(Context context){
        return SPUtil.getSimpleString(context,STAR_PY_SP_FILE,STARPY_LOGIN_SERVER_RETURN_DATA);
    }

    public static void saveSdkLoginTerms(Context context,String terms){
        SPUtil.saveSimpleInfo(context,STARPY_SDK_LOGIN_TERMS_FILE,STARPY_SDK_LOGIN_TERMS,terms);
    }

    public static String getSdkLoginTerms(Context context){
        return SPUtil.getSimpleString(context,STARPY_SDK_LOGIN_TERMS_FILE,STARPY_SDK_LOGIN_TERMS);
    }


    public static String getCfgValueByKey(Context context, String key, String defaultValue) {

        String sdkCfg = StarPyUtil.getSdkCfg(context);
        if (!TextUtils.isEmpty(sdkCfg)) {
            try {
                JSONObject jsonObject = new JSONObject(sdkCfg);
                if (jsonObject != null && jsonObject.has(key)) {
                    String value = jsonObject.optString(key, defaultValue);
                    if (!TextUtils.isEmpty(value)) {
                        return value;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static boolean checkAccount(String account){
        if (TextUtils.isEmpty(account)){
            return false;
        }
        if (account.matches("^[A-Za-z0-9]{6,18}$")){
            return true;
        }
        return false;
    }

    public static boolean checkPassword(String password){
        if (TextUtils.isEmpty(password)){
            return false;
        }
        if (password.matches("^[A-Za-z0-9]{6,18}$")){
            return true;
        }
        return false;
    }


}
