package com.starpy.sdk.out;

import android.app.Activity;

import com.core.base.callback.IGameLifeCycle;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.pay.PayType;

/**
 * Created by Efun on 2017/2/13.
 */

public interface IStarpy extends IGameLifeCycle {

    public void initSDK(Activity activity);

    public void setGameLanguage(Activity activity,String gameLanguage);

    public void registerRoleInfo(Activity activity,String roleId,String roleName,String roleLevel,String severCode,String serverName);

    public void login(Activity activity,ILoginCallBack iLoginCallBack);

    public void pay(Activity activity, PayType payType, String cpOrderId, String productId, String roleLevel, String extra);

}
