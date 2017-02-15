package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.SConfig;
import com.starpy.model.login.bean.request.AccountRegRequest;

//1000成功
//1001註冊登入成功
public class AccountRegisterRequestTask extends BaseRequestTask {

    private AccountRegRequest regRequest;

    public AccountRegisterRequestTask(Context context, String userName, String password) {
        super(context);
        regRequest = new AccountRegRequest(context);
        baseRequest = regRequest;

        regRequest.setName(userName);

        password = SStringUtil.toMd5(password);
        regRequest.setPwd(password);

        regRequest.setRequestMethod("login/register");


    }


    @Override
    public BaseReqeustBean onHttpRequest() {
        super.onHttpRequest();

        regRequest.setSignature(SStringUtil.toMd5(regRequest.getAppKey() + regRequest.getTimestamp() +
                regRequest.getName() + regRequest.getPwd() + regRequest.getGameCode()));

        return regRequest;
    }
}
