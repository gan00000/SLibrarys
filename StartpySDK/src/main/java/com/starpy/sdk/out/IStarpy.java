package com.starpy.sdk.out;

import android.app.Activity;

import com.core.base.callback.IGameLifeCycle;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.bean.SPayType;
import com.starpy.data.login.ILoginCallBack;

/**
 * Created by Efun on 2017/2/13.
 */

public interface IStarpy extends IGameLifeCycle {

    public void initSDK(Activity activity);

    public void setGameLanguage(Activity activity,SGameLanguage gameLanguage);

    public void registerRoleInfo(Activity activity,String roleId,String roleName,String roleLevel,String severCode,String serverName);

    public void login(Activity activity,ILoginCallBack iLoginCallBack);

    public void pay(Activity activity, SPayType payType, String cpOrderId, String productId, String roleLevel, String extra);

    public void cs(Activity activity, String roleLevel, String roleVipLevel);
}
