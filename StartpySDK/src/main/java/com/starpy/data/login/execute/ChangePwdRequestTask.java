package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.data.login.request.ChangePwdRequest;

//1000成功
//1001註冊登入成功
public class ChangePwdRequestTask extends BaseRequestTask {

    private ChangePwdRequest changePwdRequest;

    public ChangePwdRequestTask(Context context, String userName, String password, String newPwd) {
        super(context);

        userName = userName.toLowerCase();

        changePwdRequest = new ChangePwdRequest(context);
        baseRequest = changePwdRequest;

        changePwdRequest.setName(userName);

        changePwdRequest.setPwd(SStringUtil.toMd5(password.toLowerCase()));
        changePwdRequest.setNewPwd(SStringUtil.toMd5(newPwd.toLowerCase()));

        changePwdRequest.setRequestMethod("changePwd");


    }


    @Override
    public BaseReqeustBean createRequestBean() {
        super.createRequestBean();

        changePwdRequest.setSignature(SStringUtil.toMd5(changePwdRequest.getAppKey() + changePwdRequest.getTimestamp() +
                changePwdRequest.getName() + changePwdRequest.getPwd() +
                changePwdRequest.getNewPwd() + changePwdRequest.getGameCode()));

        return changePwdRequest;
    }
}
