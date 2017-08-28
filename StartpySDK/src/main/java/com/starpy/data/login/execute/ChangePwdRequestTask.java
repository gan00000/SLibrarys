package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.data.login.request.ChangePwdRequestBean;

//1000成功
//1001註冊登入成功
public class ChangePwdRequestTask extends BaseRequestTask {

    private ChangePwdRequestBean pwdRequestBean;

    public ChangePwdRequestTask(Context context, String userName, String password, String newPwd) {
        super(context);

        userName = userName.toLowerCase();

        pwdRequestBean = new ChangePwdRequestBean(context);
        sdkBaseRequestBean = pwdRequestBean;

        pwdRequestBean.setName(userName);

        pwdRequestBean.setPwd(SStringUtil.toMd5(password.toLowerCase()));
        pwdRequestBean.setNewPwd(SStringUtil.toMd5(newPwd.toLowerCase()));

        pwdRequestBean.setRequestMethod("changePwd");


    }


    @Override
    public BaseReqeustBean createRequestBean() {
        super.createRequestBean();

        pwdRequestBean.setSignature(SStringUtil.toMd5(pwdRequestBean.getAppKey() + pwdRequestBean.getTimestamp() +
                pwdRequestBean.getName() + pwdRequestBean.getPwd() +
                pwdRequestBean.getNewPwd() + pwdRequestBean.getGameCode()));

        return pwdRequestBean;
    }
}
