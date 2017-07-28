package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.data.login.request.AccountInjectionRequest;

//1000成功
//1001註冊登入成功
public class AccountInjectionRequestTask extends BaseRequestTask {

    private AccountInjectionRequest injectionRequest;

    public AccountInjectionRequestTask(Context context, String userName, String password,String uid) {
        super(context);

        userName = userName.toLowerCase();
        password = password.toLowerCase();

        injectionRequest = new AccountInjectionRequest(context);
        baseRequest = injectionRequest;

        injectionRequest.setName(userName);

        password = SStringUtil.toMd5(password);
        injectionRequest.setPwd(password);
        injectionRequest.setUserId(uid);

        injectionRequest.setRequestMethod("dynamic_injection");


    }


    @Override
    public BaseReqeustBean createRequestBean() {
        super.createRequestBean();

        injectionRequest.setSignature(SStringUtil.toMd5(injectionRequest.getAppKey() + injectionRequest.getTimestamp() +
                injectionRequest.getName() + injectionRequest.getPwd() + injectionRequest.getGameCode()));

        return injectionRequest;
    }
}
