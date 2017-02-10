package com.starpy.base.utils;

import android.util.Log;

/**
 * Created by Efun on 2016/11/28.
 */

public class PL {

    public final static String PL_LOG = "PL_LOG";

    public static void d(String msg){
        d(PL_LOG,msg + "");
    }

    public static void d(String tag, String msg){
        Log.d(tag,msg + "");
    }
}
