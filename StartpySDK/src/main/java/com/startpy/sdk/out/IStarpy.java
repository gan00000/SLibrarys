package com.startpy.sdk.out;

import android.app.Activity;

import com.startpy.sdk.login.ILoginCallBack;

/**
 * Created by Efun on 2017/2/13.
 */

public interface IStarpy extends IGameLifeCycle{

    public void initSDK(Activity activity);

    public void setGameLanguage(Activity activity,String gameLanguage);

    public void registerRoleInfo(Activity activity,String roleId,String roleName,String roleLevel,String severCode,String serverName);

    public void login(Activity activity,ILoginCallBack iLoginCallBack);

    public void pay(Activity activity, String cpOrderId, String productId, String roleLevel,String customize);

}
