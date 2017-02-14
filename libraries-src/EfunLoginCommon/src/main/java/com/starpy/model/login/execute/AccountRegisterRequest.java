package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.AccountRegRequest;

//1000成功
//1001註冊登入成功
public class AccountRegisterRequest extends BaseRequest {

    private AccountRegRequest regRequest;

    public AccountRegisterRequest(Context context, String userName, String password) {
        super(context);
        regRequest = new AccountRegRequest(context);
        baseRequest = regRequest;

        regRequest.setName(userName);

        password = SStringUtil.toMd5(password);
        regRequest.setPwd(password);

        regRequest.setCompleteUrl("http://10.10.10.110:8080/login/register");
        regRequest.setAppKey("test123");
        regRequest.setGameCode("test");
        regRequest.setGameLanguage("tw");

        regRequest.setSignature(SStringUtil.toMd5(regRequest.getAppKey() + regRequest.getTimestamp() +
                regRequest.getName() + regRequest.getPwd() + regRequest.getGameCode()));
    }

}
