package com.starpy.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.core.base.utils.AppUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.starpy.base.utils.StarPyUtil;

/**
 * Created by gan on 2017/8/29.
 */

public class StarpyBroadcastReceiver extends BroadcastReceiver {

    public static final String BroadcastReceiver_PushKey = "BroadcastReceiver_PushKey";//intent data key

    public static final String FLAG_BOOT_COMPLETEDP = "FLAG_BOOT_COMPLETEDP";//开机
    public static final String FLAG_START_COMMON = "FLAG_START_COMMON";//正常启动
    public static final String FLAG_CONNECTIVITY_CHANGE = "FLAG_CONNECTIVITY_CHANGE";//网络状态改变

    public static final String FLAG_PUSH_CLICK_ACTION = "com.starpy.sdk.push.CLICK";//启动action
    public static final String FLAG_PUSH_DELETE_ACTION = "com.starpy.sdk.push.DEL";//启动action

    public static final String INSTALL_REFERRER_ACTION = "com.android.vending.INSTALL_REFERRER";

    public static final int PUSH_CLICK = 100;
    public static final int PUSH_DELETE = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        PL.i("StarpyBroadcastReceiver onReceive");

        if (intent == null){
            return;
        }
        String action = intent.getAction();
        if (SStringUtil.isEmpty(action)){
            return;
        }

        int extrInt = intent.getIntExtra(BroadcastReceiver_PushKey,0);
        PL.i("extrInt:" + extrInt);

        if (action.equals(FLAG_PUSH_CLICK_ACTION)){

            AppUtil.startMainActivitiy(context);

        }else if (action.equals(FLAG_PUSH_DELETE_ACTION)){


        }else if (INSTALL_REFERRER_ACTION.equals(intent.getAction())){
            String referrer = intent.getStringExtra("referrer");
            PL.i("referrer:" + referrer);
            if (SStringUtil.isNotEmpty(referrer)){
                StarPyUtil.saveReferrer(context,referrer);
            }
        }
    }
}
