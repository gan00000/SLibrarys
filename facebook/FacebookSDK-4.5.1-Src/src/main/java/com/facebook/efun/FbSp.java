package com.facebook.efun;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Efun on 2016/12/21.
 */

public class FbSp {


    public static final String EFUN_FILE = "Efun.db";

    public static final String FB_TOKEN_FOR_BUSINESS = "FB_TOKEN_FOR_BUSINESS";
    public static final String EFUN_APP_BUSINESS_IDS = "EFUN_APP_BUSINESS_IDS";

    public static void saveTokenForBusiness(Context context, String token_for_business){
        SharedPreferences sharedPreferences = context.getSharedPreferences(EFUN_FILE,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(FB_TOKEN_FOR_BUSINESS,token_for_business).commit();
    }

    public static String getTokenForBusiness(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(EFUN_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(FB_TOKEN_FOR_BUSINESS,"");
    }
}
